package io;

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

import data.dto.PeriodDTo;
import data.dto.TransactionDto;
import exception.FileReaderException;
import exception.Message;

public class CSVReader {
	private static final String COMMA_DELIMITER = ";"; // Delimiter used in CSV file
	static Logger logger = Logger.getLogger("CSVReader");

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

		try (BufferedReader fichierSource = new BufferedReader(
				new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {

			String line;
			line = fichierSource.readLine();// lecture de l'entête
			while ((line = fichierSource.readLine()) != null) {
				line = line.trim();
				String[] tabValue = line.split(COMMA_DELIMITER);
				if (tabValue.length < 2)
					throw new FileReaderException(Message.FILE_CONTENT_KO);
				String date = tabValue[datePlace].trim();
				String category = tabValue[categoryPlace].trim();// TODO supprimer les accents ou les gérer
				String description = tabValue[descriptionPlace].trim();
				String payement = tabValue[paymentPlace].trim();
				Double amount = Double.parseDouble(tabValue[amountPlace].replaceAll(",", "."));
				// TODO situer ligne d'une erreur
				TransactionDto transaction = new TransactionDto(date, category.toLowerCase(), description, payement,
						amount, periode);
				fileContentList.add(transaction);

			}

		} catch (

		FileNotFoundException fileNotFoundException) {
			throw new FileReaderException(Message.FILE_PATH_KO);
		} catch (IOException ioException) {
			logger.log(Level.SEVERE, ioException.getMessage());
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
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
			throw new FileReaderException(Message.FILE_PATH_KO);
		}
	}

}
