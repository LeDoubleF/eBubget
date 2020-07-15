package ebudget;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import calculation.Calculator;
import data.Repository;
import data.dao.AccountEntity;
import data.dao.PeriodEntity;
import data.dao.TransactionEntity;
import data.dto.AccountDto;
import data.dto.AccountType;
import data.dto.PeriodDTo;
import data.dto.TransactionDto;
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
		TransactionEntity.sumCash();

	}// end main

	public Ebudget() {
		calculator = new Calculator();
		view = new View();
	}

	public void run() {
		Repository.initCategories();
		String fileName = view.readFilePath(System.in);
		PeriodDTo periode = view.readPeriod(System.in);

		List<TransactionDto> fileContentList = view.readTransaction(fileName, periode);
		PeriodEntity.save(periode);

		fileContentList.forEach(item -> TransactionEntity.save(item));
		double transactionSum = TransactionEntity.sumAccount();

		double initialBalance = view.readInitialBalance(System.in);
		double finalBalance = calculator.calculateFinalBalance(initialBalance, transactionSum);
		view.printValue("Solde final : ", Double.toString(finalBalance));

		AccountDto cpp = new AccountDto("cpp", AccountType.CPP, "compte courant", initialBalance, finalBalance);
		AccountEntity.save(cpp);

		logger.log(Level.INFO, "fin du programme");

	}

}
