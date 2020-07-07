package ebudget;

import java.util.logging.Level;
import java.util.logging.Logger;

import calculation.Calculator;
import data.dao.TransactionEntity;
import io.View;

public class Ebudget {

	static Logger logger = Logger.getLogger("Ebudget");

	private Calculator calculator;
	private View view;

	public static void main(String[] args) {
		String curDir = System.getProperty("user.dir");
		System.out.println("Le répertoire courant est: " + curDir);

		Ebudget budget = new Ebudget();

		budget.run();
		////////////

		TransactionEntity.sumByCategory();

	}// end main

	public Ebudget() {
		calculator = new Calculator();
		view = new View();
	}

	public void run() {
		System.out.println(view.readPeriod(System.in));
		// Repository.initCategories();
		// String fileName = view.readFilePath(System.in);
		// PeriodDTo periode = new PeriodDTo(2020, 2, 1);
		// List<TransactionDto> fileContentList = view.readTransaction(fileName,
		// periode);
		// PeriodeEntity.save(periode);
		// fileContentList.forEach(item -> TransactionEntity.save(item));
		// double transactionSum = TransactionEntity.sum();
		//
		// double initialBalance = view.readInitialBalance(System.in);
		//
		// double finalBalance = calculator.calculateFinalBalance(initialBalance,
		// transactionSum);
		// view.printValue("Solde final : ", Double.toString(finalBalance));
		logger.log(Level.INFO, "fin du programme");

	}

}
