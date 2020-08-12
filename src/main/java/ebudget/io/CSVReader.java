package ebudget.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ebudget.data.dto.PeriodDTo;
import ebudget.data.dto.TransactionDto;
import ebudget.exception.FileReaderException;
import ebudget.exception.Message;

public class CSVReader {
	private static final String COMMA_DELIMITER = ";"; // Delimiter used in CSV file

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public CSVReader() {
		super();
	}

	/**
	 * description de l'emplacement des donn�es dans le fichier
	 * 
	 * 0: amount, 1: Date ,2: category ,3: description ,4: payment
	 * 
	 * @return int FileDescription[]
	 */
	private int[] describeFile() {
		// TODO description du fichier par utilisateur ou fichier de configuration

		return new int[] { 4, 0, 1, 2, 3 };
	}

	public List<TransactionDto> readFile(String filePath, PeriodDTo periode) {

		List<TransactionDto> fileContentList = new ArrayList<>();
		verifyFile(filePath);
		// TODO rendre la lecture de la description du fichier plus jolie
		int[] fileDescription = describeFile();
		int amountPlace = fileDescription[0];
		int datePlace = fileDescription[1];
		int categoryPlace = fileDescription[2];
		int descriptionPlace = fileDescription[3];
		int paymentPlace = fileDescription[4];

		int lineNumber = 1;

		try (BufferedReader fichierSource = new BufferedReader(
				new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {

			String line;

			line = fichierSource.readLine();// lecture de l'ent�te

			while ((line = fichierSource.readLine()) != null) {
				lineNumber++;
				line = line.trim();
				String[] tabValue = line.split(COMMA_DELIMITER);
				if (tabValue.length < 2)
					throw new FileReaderException(Message.FILE_CONTENT_KO);
				String date = tabValue[datePlace].trim();
				String category = tabValue[categoryPlace].trim();
				String description = tabValue[descriptionPlace].trim();
				String payement = tabValue[paymentPlace].trim();
				String amountString = tabValue[amountPlace].trim();
				Double amount = Double.parseDouble(amountString.replaceAll(",", "."));
				TransactionDto transaction = new TransactionDto(date, category.toLowerCase(), description, payement,
						amount, periode);
				fileContentList.add(transaction);

			}

		} catch (

		FileNotFoundException fileNotFoundException) {
			LOGGER.log(Level.SEVERE, "le fichier {0} nexiste pas", filePath);
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
			LOGGER.log(Level.SEVERE, "le fichier {0} nexiste pas", filePath);
			throw new FileReaderException(Message.FILE_PATH_KO);
		}
	}

}