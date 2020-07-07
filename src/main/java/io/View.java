package io;

import java.io.InputStream;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.logging.Logger;

import data.dto.PeriodDTo;
import data.dto.TransactionDto;

public class View implements Observer {

	Logger logger = Logger.getLogger("View");

	public void update(Observable o, Object arg) {
		System.out.println("\n => person updated to " + arg);
	}

	public List<TransactionDto> readTransaction(String fileName, PeriodDTo periode) {
		// TODO utilité de tester un passe plat
		System.out.println(" Lecture du fichier: " + fileName);

		CSVReader csvReader = new CSVReader();

		return csvReader.readFile(fileName, periode);
	}

	public double readInitialBalance(InputStream in) {

		Scanner sc = new Scanner(in);
		System.out.println("Entrez le solde initial:");
		String solde = sc.nextLine();

		return Double.parseDouble(solde);
	}

	public String readFilePath(InputStream in) {
		Scanner sc = new Scanner(in);
		System.out.println("Entrez le chemin du fichier:");
		return sc.nextLine();
	}

	public String readPeriod(InputStream in) {
		Scanner sc = new Scanner(in);
		System.out.println("Entrez l'année concernée (nombre):");
		String period = sc.nextLine();
		System.out.println("Entrez le mois concernée (nombre):");
		return sc.nextLine() + period;
	}

	public void printValue(String prompt, String value) {
		System.out.println(prompt + value);
	}

}
