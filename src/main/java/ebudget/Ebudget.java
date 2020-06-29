package ebudget;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import calculation.Calculator;
import data.dao.PeriodeEntity;
import data.dao.TransactionEntity;
import data.dto.PeriodDTo;
import data.dto.TransactionDto;
import io.View;

public class Ebudget {

	static Logger logger = Logger.getLogger("Ebudget");

	private Calculator calculator;
	private View view;

	public static void main(String[] args) {
		// CategoryEntity.deleteAllCategory();
		// CategoryEntity
		// .createCategories("C:\\Donnees\\Perso\\projet\\budget\\eBudget\\src\\main\\resources\\category.sql");
		Ebudget budget = new Ebudget();

		budget.run();
		////////////

		// TransactionEntity.sumByCategory();

	}// end main

	public Ebudget() {
		calculator = new Calculator();
		view = new View();
	}

	public void run() {

		String fileName = view.readFilePath(System.in);
		PeriodDTo periode = new PeriodDTo(2020, 1, 1);
		List<TransactionDto> fileContentList = view.readTransaction(fileName, periode);
		PeriodeEntity.save(periode);
		fileContentList.forEach(item -> TransactionEntity.save(item));
		double transactionSum = TransactionEntity.sum();

		double initialBalance = view.readInitialBalance(System.in);

		double finalBalance = calculator.calculateFinalBalance(initialBalance, transactionSum);
		view.printValue("Solde final : ", Double.toString(finalBalance));
		logger.log(Level.INFO, "fin du programme");

	}

}
