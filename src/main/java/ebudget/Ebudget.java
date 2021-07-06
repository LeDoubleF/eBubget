package ebudget;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import ebudget.calculation.AnnualBudget;
import ebudget.calculation.BaseBudget;
import ebudget.calculation.Calculator;
import ebudget.calculation.RecurringItem;
import ebudget.calculation.TransactionSummary;
import ebudget.data.Accounts;
import ebudget.data.Categories;
import ebudget.data.dao.AccountEntity;
import ebudget.data.dao.PeriodEntity;
import ebudget.data.dao.TransactionEntity;
import ebudget.data.dto.AccountDto;
import ebudget.data.dto.AccountType;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.PeriodDTo;
import ebudget.data.dto.TransactionDto;
import ebudget.io.View;

public class Ebudget {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private ClassLoader classLoader = getClass().getClassLoader();
	private Calculator calculator;
	private View view;

	public static void main(String[] args) {
		String curDir = System.getProperty("user.dir");
		System.out.println(Ebudget.class.getName() + "Le répertoire courant est: " + curDir);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate nowDate = LocalDate.now();
		LOGGER.log(Level.INFO, "Il est maintenant : {0}", nowDate.format(formatter));
		try {
			EBudgetLogger.setup();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "erreur lors de  la création du fichier de log {0} :", e.toString());
		}

		Ebudget budget = new Ebudget();

		budget.run();
		////////////
		// Common.loadForecats(".\\src\\main\\resources\\forecast.sql");
		//
		// System.out.println("Somme total du compte principal : " +
		// TransactionEntity.sumAccount());
		// System.out.println(TransactionEntity.sumCash());

	}// end main

	public Ebudget() {
		calculator = new Calculator();
		view = new View();
	}

	public void run() {

		// inititalisation des catégories
		Categories.initCategories();

		AccountEntity.save(new AccountDto("poste", AccountType.CPP, false, 0.0));
		AccountEntity.save(new AccountDto("portefeuille", AccountType.CPP, false, 0.0));
		AccountEntity.save(new AccountDto("joint", AccountType.CPP, false, 0.0));
		AccountEntity.save(new AccountDto("principal", AccountType.CPP, true, 0.0));
		Accounts.initAccounts();

		// //InputStream inForFileName = System.in;
		// ByteArrayInputStream inForFileName = new
		// ByteArrayInputStream("C:\\Users\\ffazer\\Documents\\ebudget
		// exe\\janvier.csv".getBytes());
		// String fileName = view.readFilePath(inForFileName);

		// InputStream inForPeriod = System.in;
		// PeriodDTo periode = view.readPeriod(inForPeriod);
		PeriodDTo periode = new PeriodDTo(2021, 1);
		PeriodEntity.save(periode);

		// charger un budget de référence
		File baseBudgetItemFile = new File(classLoader.getResource("budgetReference.csv")
			.getFile());
		Map<CategoryDto, Double> budgetItemList = view.readBudgetItem(baseBudgetItemFile.getAbsolutePath());

		BaseBudget baseBudget = new BaseBudget(budgetItemList);
		baseBudget.print();
		System.out.println("\n\t" + baseBudget.getBalance());

		// charger le fichier des dépenses récurrentes
		File recurringItemFile = new File(classLoader.getResource("recurring.csv")
			.getFile());
		List<RecurringItem> reccuringItemList = view.readRecurringItem(recurringItemFile.getAbsolutePath());

		// récupérer solde initiale
		// double initialBalance =view.readInitialBalance(System.in);
		double initialBalance = 100.0;

		// creation du budget annuel
		AnnualBudget annualBudget = new AnnualBudget(periode.getYear(), 0.0, baseBudget, reccuringItemList);
		annualBudget.print();
		System.out.println("\n\t" + annualBudget.getBalance());

		// charger les transactions
		File transactionFileName = new File(classLoader.getResource("transaction.csv")
			.getFile());
		List<TransactionDto> fileContentList = view.readTransaction(transactionFileName.getAbsolutePath(), periode);

		// TransactionSummary transactionSummary = new
		// TransactionSummary(PERIOD);
		//
		// transactionSummary.addTransaction(TAXI, date, "description",
		// "payment", 100.0);
		//
		// assertEquals(100, transactionSummary.getBalance(), PRECISION);
		//
		// annualBudget.print();
		//
		// annualBudget.setTransaction(MONTH, TAXI, date, "facture perdue",
		// "CB", 180.0);
		// annualBudget.setTransaction(MONTH, LOYER, date, "facture perdue",
		// "CB", 800.0);
		// annualBudget.setTransaction(MONTH, DIVERS, date, "facture perdue",
		// "CB", 40.0);
		// annualBudget.setTransaction(MONTH, SALAIRE, date, "facture perdue",
		// "CB", 1000.0);
		//
		// annualBudget.closeBudget(1);
		//
		// annualBudget.print();

		fileContentList.forEach(TransactionEntity::save);
		// double transactionSum = TransactionEntity.sumAccount();

		// ByteArrayInputStream in = new ByteArrayInputStream("100".getBytes());
		//
		// double finalBalance =
		// calculator.calculateFinalBalance(initialBalance, transactionSum);
		// view.printValue("Solde final : ", Double.toString(finalBalance));

		LOGGER.log(Level.INFO, "fin du programme");

	}

}
