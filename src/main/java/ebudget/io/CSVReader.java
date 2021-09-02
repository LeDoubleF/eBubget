package ebudget.io;

import java.text.Normalizer;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import ebudget.calculation.RecurringItem;
import ebudget.data.Categories;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.PaymentType;
import ebudget.data.dto.PeriodDTo;
import ebudget.data.dto.TransactionDto;
import ebudget.exception.FileReaderException;
import ebudget.exception.Message;

public class CSVReader {

	private static final String AMOUNT = "amount";
	private static final String CATEGORY = "category";

	private static final String LE_FICHIER_N_EXISTE_PAS = "le fichier {0} nexiste pas";
	private static final String COMMA_DELIMITER = ";"; // Delimiter used in CSV
														// file

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private ResourceBundle bundle = ResourceBundle.getBundle("ebudget.properties.config");

	public CSVReader() {

		super();
	}

	public List<RecurringItem> readRecurringItemFile(String filePath) {
		List<RecurringItem> recurringItemList = new ArrayList<>();
		verifyFile(filePath);

		Map<Integer, ColumnDescription> columnDescription = getRecurringItemFileDescription();

		List<Object> fileContentList = readFile(filePath, columnDescription);

		for (Object filContent : fileContentList) {
			@SuppressWarnings("unchecked")
			List<Object> valueList = (List<Object>) filContent;

			String categoryName = (String) valueList.get(0);
			CategoryDto categoryDto = Categories.getCategory(categoryName);
			Double amount = (Double) valueList.get(4);
			Boolean mandatory = (Boolean) valueList.get(2);
			Boolean variable = (Boolean) valueList.get(3);
			List<Boolean> frequenceList = new ArrayList<>();
			String description = (String) valueList.get(1);

			for (int i = 5; i < 17; i++) {
				frequenceList.add((Boolean) valueList.get(i));
			}

			if (categoryDto == null) {
				LOGGER.log(Level.WARNING, "La categorie {0} n existe pas", categoryName);
			} else {
				RecurringItem recurringItem = new RecurringItem(categoryDto, description, amount, mandatory, variable, frequenceList);

				recurringItemList.add(recurringItem);
			}
		}

		return recurringItemList;
	}

	public List<CategoryDto> readCategoriesFile(String filePath) {
		List<CategoryDto> categoryDtoList = new ArrayList<>();

		verifyFile(filePath);

		Map<Integer, ColumnDescription> columnDescription = new HashMap<>();
		columnDescription.put(0, new ColumnDescription(CVSParameter.STRING, "name"));
		columnDescription.put(1, new ColumnDescription(CVSParameter.BOOLEAN, "income"));
		List<Object> fileContentList = readFile(filePath, columnDescription);

		for (Object filContent : fileContentList) {
			@SuppressWarnings("unchecked")
			List<Object> valueList = (List<Object>) filContent;

			String categoryName = (String) valueList.get(0);
			Boolean income = (Boolean) valueList.get(1);
			CategoryDto category = new CategoryDto(categoryName, income);
			categoryDtoList.add(category);
		}

		return categoryDtoList;
	}

	private List<Object> readFile(String filePath, Map<Integer, ColumnDescription> fileDescription) {
		List<Object> fileContentList = new ArrayList<>();
		verifyFile(filePath);

		int lineNumber = 1;
		try (BufferedReader fichierSource = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.ISO_8859_1))) {

			String line = fichierSource.readLine();// lecture de l'entête
			while ((line = fichierSource.readLine()) != null) {
				lineNumber++;
				line = line.trim();
				if (line.length() != 0) {// gestion ligne vide
					String[] tabValue = line.split(COMMA_DELIMITER);

					if (tabValue.length < 2)
						throw new FileReaderException(Message.FILE_CONTENT_KO);
					List<Object> valueList = new ArrayList<>();
					for (int i = 0; i < tabValue.length; i++) {
						ColumnDescription columnType = fileDescription.get(i);
						valueList.add(columnType.convert(tabValue[i]));
					}
					fileContentList.add(valueList);
				}
			}
		} catch (FileNotFoundException fileNotFoundException) {
			LOGGER.log(Level.SEVERE, LE_FICHIER_N_EXISTE_PAS, filePath);
		} catch (IOException ioException) {
			LOGGER.log(Level.SEVERE, ioException.getMessage());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Message.FILE_CONTENT_KO_ARGUMENT, lineNumber);
			throw new FileReaderException(Message.FILE_CONTENT_KO);
		}
		return fileContentList;
	}

	protected void verifyFile(String filePath) {
		File file = new File(filePath);
		if (file.exists() && !file.isDirectory()) {
			String[] findExtension = filePath.split("\\.");
			if (!(findExtension.length == 2 && findExtension[1].equalsIgnoreCase("csv")))
				throw new FileReaderException(Message.FILE_EXTENSION_KO);
		} else {
			LOGGER.log(Level.SEVERE, LE_FICHIER_N_EXISTE_PAS, filePath);
			throw new FileReaderException(Message.FILE_PATH_KO);
		}
	}

	/**
	 * description de l'emplacement des données dans le fichier selon fichier de
	 * configuration ebudget.properties.config
	 * 
	 * @return Map<Integer, ColumnDescription>
	 */
	public Map<Integer, ColumnDescription> getBudgetFileDescription() {

		Integer amountColumn = getColumnNumber("app.budgetFile.amount");
		Integer categoryColumn = getColumnNumber("app.budgetFile.category");
		Map<Integer, ColumnDescription> columnDescription = new HashMap<>();
		columnDescription.put(amountColumn, new ColumnDescription(CVSParameter.DOUBLE, AMOUNT));
		columnDescription.put(categoryColumn, new ColumnDescription(CVSParameter.STRING, CATEGORY));
		return columnDescription;
	}

	public Map<CategoryDto, Double> readBudgetFile(String filePath) {
		Map<CategoryDto, Double> budgetItemList = new HashMap<>();
		verifyFile(filePath);

		Map<Integer, ColumnDescription> columnDescription = getBudgetFileDescription();

		List<Object> fileContentList = readFile(filePath, columnDescription);

		for (Object filContent : fileContentList) {
			@SuppressWarnings("unchecked")
			List<Object> valueList = (List<Object>) filContent;
			String categoryName = (String) valueList.get(0);
			CategoryDto categoryDto = Categories.getCategory(categoryName);
			Double amount = (Double) valueList.get(1);
			if (categoryDto == null) {
				LOGGER.log(Level.WARNING, "La categorie {0} n existe pas", categoryName);
			} else
				budgetItemList.put(categoryDto, amount);
		}

		return budgetItemList;
	}

	/**
	 * description de l'emplacement des données dans le fichier selon fichier de
	 * configuration ebudget.properties.config
	 * 
	 * @return Map<Integer, ColumnDescription>
	 */
	public Map<Integer, ColumnDescription> getTransactionFileDescription() {
		Map<Integer, ColumnDescription> transactionFileColumn = new HashMap<>();

		transactionFileColumn.put(getColumnNumber("app.transactionFile.amount"), new ColumnDescription(CVSParameter.DOUBLE, AMOUNT));
		transactionFileColumn.put(getColumnNumber("app.transactionFile.date"), new ColumnDescription(CVSParameter.DATE, "date"));
		transactionFileColumn.put(getColumnNumber("app.transactionFile.category"), new ColumnDescription(CVSParameter.STRING, CATEGORY));
		transactionFileColumn.put(getColumnNumber("app.transactionFile.description"), new ColumnDescription(CVSParameter.STRING, "description"));
		transactionFileColumn.put(getColumnNumber("app.transactionFile.payment"), new ColumnDescription(CVSParameter.STRING, "payment"));
		transactionFileColumn.put(getColumnNumber("app.transactionFile.account"), new ColumnDescription(CVSParameter.STRING, "account"));

		return transactionFileColumn;
	}

	protected Integer getColumnNumber(String parameter) {
		try {
			return Integer.valueOf(bundle.getString(parameter));
		} catch (MissingResourceException e) {
			// TODO test LOGGER
			LOGGER.log(Level.SEVERE, "La parametre {0} n existe pas", parameter);
			throw (MissingResourceException) e;
		}

	}

	public List<TransactionDto> readTransactionFile(String filePath, PeriodDTo periode) {
		Map<Integer, ColumnDescription> transactionFileColumn = getTransactionFileDescription();

		List<TransactionDto> transactionList = new ArrayList<>();
		List<Object> fileContentList = readFile(filePath, transactionFileColumn);
		for (Object filContent : fileContentList) {
			@SuppressWarnings("unchecked")
			List<Object> valueList = (List<Object>) filContent;
			TransactionDto transaction;
			PaymentType payment = convertStringToPayment((String) valueList.get(3));
			if (valueList.size() >= 6) {
				transaction = new TransactionDto((LocalDate) valueList.get(0), (String) valueList.get(1), (String) valueList
					.get(2), payment, (Double) valueList.get(4), periode, (String) valueList.get(5));
			} else {
				transaction = new TransactionDto((LocalDate) valueList.get(0), (String) valueList.get(1), (String) valueList
					.get(2), payment, (Double) valueList.get(4), periode);
			}
			transactionList.add(transaction);
		}
		return transactionList;
	}

	protected PaymentType convertStringToPayment(String stringToConvert) {
		try {
			String normalizeString = Normalizer.normalize(stringToConvert, Normalizer.Form.NFD);
			Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
			stringToConvert = pattern.matcher(normalizeString)
				.replaceAll("")
				.toUpperCase();
			return PaymentType.valueOf(stringToConvert);
		} catch (IllegalArgumentException e) {
			return PaymentType.INCONNU;
		}

	}

	public Map<Integer, ColumnDescription> getRecurringItemFileDescription() {

		Map<Integer, ColumnDescription> columnDescription = new HashMap<>();
		columnDescription.put(getColumnNumber("app.recurringItemFile.category"), new ColumnDescription(CVSParameter.STRING, CATEGORY));
		columnDescription.put(getColumnNumber("app.recurringItemFile.description"), new ColumnDescription(CVSParameter.STRING, "description"));
		columnDescription.put(getColumnNumber("app.recurringItemFile.amount"), new ColumnDescription(CVSParameter.DOUBLE, AMOUNT));

		List<String> parameterList = Arrays.asList("mandatory", "variable", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
		for (String parameter : parameterList) {
			columnDescription.put(getColumnNumber("app.recurringItemFile." + parameter), new ColumnDescription(CVSParameter.BOOLEAN, parameter));
		}

		return columnDescription;
	}

}
