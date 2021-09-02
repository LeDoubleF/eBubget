package ebudget;

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
import ebudget.data.Accounts;
import ebudget.data.Categories;
import ebudget.data.Common;
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
		// Common.clearDataBase();
		// inititalisation des catégories
		Categories.initCategories();

		// TODO initialisation des comptes
		AccountDto cptJoint = new AccountDto("joint", AccountType.CPP, false, 123.02);
		AccountDto cpp = new AccountDto("cpp", AccountType.CPP, true, 2332.41);
		//
		// AccountEntity.save(new AccountDto("poste", AccountType.CPP, false,
		// 0.0));
		// AccountEntity.save(new AccountDto("portefeuille", AccountType.ESPECE,
		// false, 0.0));
		// AccountEntity.save(new AccountDto("attente", AccountType.CPP, false,
		// 0.0));
		// AccountEntity.save(cptJoint);
		// AccountEntity.save(cpp);
		// AccountEntity.save(new AccountDto("carte", AccountType.CPP, true,
		// 0.0));
		Accounts.initAccounts();

		// //InputStream inForFileName = System.in;
		// ByteArrayInputStream inForFileName = new
		// ByteArrayInputStream("C:\\Users\\ffazer\\Documents\\ebudget
		// exe\\janvier.csv".getBytes());
		// String fileName = view.readFilePath(inForFileName);

		// InputStream inForPeriod = System.in;
		// PeriodDTo periode = view.readPeriod(inForPeriod);
		PeriodDTo periode = new PeriodDTo(2021, 1);
		// PeriodEntity.save(periode);

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

		// creation du budget annuel
		AnnualBudget annualBudget = new AnnualBudget(periode.getYear(), 0.0, baseBudget, reccuringItemList);
		annualBudget.print();
		System.out.println("\n\t" + annualBudget.getBalance());

		// charger les transactions
		File transactionFileName = new File("C:\\Donnees\\Perso\\projet\\budget\\juillet.csv");
		List<TransactionDto> fileContentList = view.readTransaction(transactionFileName.getAbsolutePath(), periode);

		fileContentList.forEach(TransactionEntity::save);

		double transactionSum = TransactionEntity.sum("cpp");

		double finalBalance = calculator.calculateFinalBalance(cpp.getInitialBalance(), transactionSum);

		Accounts.setFinalBalance("poste", TransactionEntity.sum("poste") + Accounts.getInitialBalance("poste"));

		Accounts.setFinalBalance("portefeuille", TransactionEntity.sum("portefeuille") + Accounts.getInitialBalance("portefeuille"));

		Accounts.setFinalBalance("joint", TransactionEntity.sum("joint") + Accounts.getInitialBalance("joint"));
		Accounts.saveOrUpdate("joint");

		Accounts.setFinalBalance("carte", TransactionEntity.sum("carte") + Accounts.getInitialBalance("carte"));

		view.printValue("Solde initial cpp: ", Double.toString(cpp.getInitialBalance()));
		view.printValue("Solde final cpp: ", Double.toString(finalBalance));
		view.printValue("Solde final poste: ", Accounts.getFinalBalance("poste")
			.toString());

		view.printValue("Solde final portefeuille: ", Accounts.getFinalBalance("portefeuille")
			.toString());
		view.printValue("Solde initial : ", Double.toString(Accounts.getInitialBalance("joint")));
		view.printValue("Solde joint: ", Double.toString(Accounts.getFinalBalance("joint")));
		view.printValue("Solde final carte: ", Accounts.getFinalBalance("carte")
			.toString());

		LOGGER.log(Level.INFO, "fin du programme");

	}

}
