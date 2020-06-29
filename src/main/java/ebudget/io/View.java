package ebudget.io;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import ebudget.calculation.RecurringItem;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.PeriodDTo;
import ebudget.data.dto.TransactionDto;
import ebudget.exception.ComplianceException;

public class View implements Observer {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public void update(Observable o, Object arg) {
		try {
			System.out.println("\n => person updated to " + arg);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "notification", e);
		}
	}

	public List<TransactionDto> readTransaction(String fileName, PeriodDTo periode) {
		try {
			LOGGER.log(Level.INFO, "Lecture du fichier: {0}", fileName);

			CSVReader csvReader = new CSVReader();

			return csvReader.readTransactionFile(fileName, periode);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "exit when read transaction", e);
			System.exit(1);
		}
		return new ArrayList<>();
	}

	public List<RecurringItem> readRecurringItem(String fileName) {
		try {
			LOGGER.log(Level.INFO, "Lecture du fichier: {0}", fileName);

			CSVReader csvReader = new CSVReader();

			return csvReader.readRecurringItemFile(fileName);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "exit when read transaction", e);
			System.exit(1);
		}
		return new ArrayList<>();
	}

	public double readInitialBalance(InputStream in) {
		try {
			Scanner sc = new Scanner(in);
			System.out.println("Entrez le solde initial:");
			String solde = sc.nextLine();
			sc.close();

			return Double.parseDouble(solde);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "lecture du solde initiale", e);
		}
		return 0;
	}

	public String readFilePath(InputStream in) {
		try {
			Scanner sc = new Scanner(in);
			System.out.println("Entrez le chemin du fichier:");
			return sc.nextLine();

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "lecture du chemin du fichier", e);
		}
		return null;
	}

	public PeriodDTo readPeriod(InputStream in) {
		try {
			Scanner sc = new Scanner(in);
			System.out.println("Entrez l'année concernée (nombre):");
			int year = Integer.parseInt(sc.nextLine());
			System.out.println("Entrez le mois concernée (nombre):");
			int month = Integer.parseInt(sc.nextLine());
			return new PeriodDTo(year, month);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "lecture de la période", e);
			throw new ComplianceException(e.getMessage());
		}
	}

	public void printValue(String prompt, String value) {
		try {
			System.out.println(prompt + value);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "affichage console", e);
		}
	}

	public Map<CategoryDto, Double> readBudgetItem(String fileName) {
		try {
			LOGGER.log(Level.INFO, "Lecture du fichier: {0}", fileName);

			CSVReader csvReader = new CSVReader();

			return csvReader.readBudgetFile(fileName);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "exit when read transaction", e);
			System.exit(1);
		}
		return new HashMap<>();
	}

}
