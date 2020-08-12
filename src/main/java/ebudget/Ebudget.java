package ebudget;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ebudget.calculation.Calculator;
import ebudget.data.Repository;
import ebudget.data.dao.AccountEntity;
import ebudget.data.dao.PeriodEntity;
import ebudget.data.dao.TransactionEntity;
import ebudget.data.dto.AccountDto;
import ebudget.data.dto.AccountType;
import ebudget.data.dto.PeriodDTo;
import ebudget.data.dto.TransactionDto;
import ebudget.io.View;

public class Ebudget {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private Calculator calculator;
	private View view;

	public static void main(String[] args) {
		String curDir = System.getProperty("user.dir");
		System.out.println(Ebudget.class.getName() + "Le répertoire courant est: " + curDir);

		try {
			EBudgetLogger.setup();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "erreur lors du main :", e);
			throw new RuntimeException("Problems with creating the log files");
		}

		Ebudget budget = new Ebudget();

		budget.run();
		////////////
		Repository.loadForecats(".\\src\\main\\resources\\forecast.sql");

		System.out.println("Somme total du compte principal : " + TransactionEntity.sumAccount());
		System.out.println(TransactionEntity.sumCash());

	}// end main

	public Ebudget() {
		calculator = new Calculator();
		view = new View();
	}

	public void run() {
		Repository.initCategories();
		// String fileName = view.readFilePath(System.in);
		// PeriodDTo periode = view.readPeriod(System.in);
		ByteArrayInputStream in = new ByteArrayInputStream(
				"C:\\Users\\ffazer\\Documents\\ebudget exe\\janvier.csv".getBytes());
		String fileName = view.readFilePath(in);
		in = new ByteArrayInputStream("2020\n1".getBytes());
		PeriodDTo periode = view.readPeriod(in);

		List<TransactionDto> fileContentList = view.readTransaction(fileName, periode);
		PeriodEntity.save(periode);

		fileContentList.forEach(item -> TransactionEntity.save(item));
		double transactionSum = TransactionEntity.sumAccount();

		in = new ByteArrayInputStream("100".getBytes());
		// double initialBalance = view.readInitialBalance(System.in);
		double initialBalance = view.readInitialBalance(in);
		double finalBalance = calculator.calculateFinalBalance(initialBalance, transactionSum);
		view.printValue("Solde final : ", Double.toString(finalBalance));

		AccountDto cpp = new AccountDto("cpp", AccountType.CPP, "compte courant", initialBalance, finalBalance);
		AccountEntity.save(cpp);

		LOGGER.log(Level.INFO, "fin du programme");

	}

}
