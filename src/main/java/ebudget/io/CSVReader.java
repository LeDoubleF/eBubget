package ebudget.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import ebudget.calculation.RecurringItem;
import ebudget.data.Categories;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.PeriodDTo;
import ebudget.data.dto.TransactionDto;
import ebudget.exception.FileReaderException;
import ebudget.exception.Message;

public class CSVReader {

	private static final String DOUBLE = "Double";
	private static final String STRING = "String";
	private static final String BOOLEAN = "Boolean";
	private static final String LE_FICHIER_N_EXISTE_PAS = "le fichier {0} nexiste pas";
	private static final String COMMA_DELIMITER = ";"; // Delimiter used in CSV
														// file

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public CSVReader() {
		super();
	}

	/**
	 * description de l'emplacement des données dans le fichier
	 * 
	 * 0: amount, 1: Date ,2: category ,3: description ,4: payment
	 * 
	 * @return int FileDescription[]
	 */
	private Map<Integer, String> describeFile() {
		// TODO description du fichier par utilisateur ou fichier de
		// configuration

		return null;

	}

	@SuppressWarnings("unchecked")
	private <T> T checkType(String description, String value) {
		// TODO date
		if (description.equals(DOUBLE))
			return (T) new Double(value.replace(",", "."));
		else if (description.equals(BOOLEAN))
			return (T) new Boolean(value);
		// Date date1 = new Date();
		else
			return (T) value.trim();
	}

	public List<RecurringItem> readRecurringItemFile(String filePath) {
		List<RecurringItem> recurringItemList = new ArrayList<>();
		verifyFile(filePath);

		Map<Integer, Column> columnDescription = new HashMap<>();
		columnDescription.put(0, new Column(CVSParameter.STRING, "category"));
		columnDescription.put(1, new Column(CVSParameter.STRING, "description"));
		columnDescription.put(2, new Column(CVSParameter.BOOLEAN, "mandatory"));
		columnDescription.put(3, new Column(CVSParameter.BOOLEAN, "variable"));
		columnDescription.put(4, new Column(CVSParameter.DOUBLE, "amount"));
		// Month
		columnDescription.put(5, new Column(CVSParameter.BOOLEAN, "1"));
		columnDescription.put(6, new Column(CVSParameter.BOOLEAN, "2"));
		columnDescription.put(7, new Column(CVSParameter.BOOLEAN, "3"));
		columnDescription.put(8, new Column(CVSParameter.BOOLEAN, "4"));
		columnDescription.put(9, new Column(CVSParameter.BOOLEAN, "5"));
		columnDescription.put(10, new Column(CVSParameter.BOOLEAN, "6"));
		columnDescription.put(11, new Column(CVSParameter.BOOLEAN, "7"));
		columnDescription.put(12, new Column(CVSParameter.BOOLEAN, "8"));
		columnDescription.put(13, new Column(CVSParameter.BOOLEAN, "9"));
		columnDescription.put(14, new Column(CVSParameter.BOOLEAN, "10"));
		columnDescription.put(15, new Column(CVSParameter.BOOLEAN, "11"));
		columnDescription.put(16, new Column(CVSParameter.BOOLEAN, "12"));

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

	private List<Object> readFile(String filePath, Map<Integer, Column> fileDescription) {
		List<Object> fileContentList = new ArrayList<>();
		verifyFile(filePath);
		// TODO rendre la lecture de la description du fichier plus jolie

		int lineNumber = 1;
		try (BufferedReader fichierSource = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

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
						Column columnType = fileDescription.get(i);
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

	public Map<CategoryDto, Double> readBudgetFile(String filePath) {
		// TODO mutualiser avec code lecture fichier transaction

		Map<CategoryDto, Double> budgetItemList = new HashMap<>();
		verifyFile(filePath);

		Map<Integer, Column> columnDescription = new HashMap<>();
		columnDescription.put(1, new Column(CVSParameter.DOUBLE, "amount"));
		columnDescription.put(0, new Column(CVSParameter.STRING, "category"));

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

	public List<TransactionDto> readTransactionFile(String filePath, PeriodDTo periode) {
		Map<Integer, Column> transactionFileColumn = new HashMap<>();
		transactionFileColumn.put(4, new Column(CVSParameter.DOUBLE, "amount"));
		transactionFileColumn.put(1, new Column(CVSParameter.STRING, "date"));
		transactionFileColumn.put(2, new Column(CVSParameter.STRING, "category"));
		transactionFileColumn.put(3, new Column(CVSParameter.STRING, "description"));
		transactionFileColumn.put(0, new Column(CVSParameter.STRING, "payment"));

		List<TransactionDto> transactionList = new ArrayList<>();
		List<Object> fileContentList = readFile(filePath, transactionFileColumn);
		for (Object filContent : fileContentList) {
			@SuppressWarnings("unchecked")
			List<Object> valueList = (List<Object>) filContent;
			TransactionDto transaction = new TransactionDto((String) valueList.get(0), (String) valueList.get(1), (String) valueList
					.get(2), (String) valueList.get(3), (Double) valueList.get(4), periode);
			transactionList.add(transaction);
		}
		return transactionList;
	}
}
