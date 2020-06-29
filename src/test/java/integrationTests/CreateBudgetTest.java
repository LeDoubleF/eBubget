package integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ebudget.calculation.AnnualBudget;
import ebudget.calculation.BaseBudget;
import ebudget.calculation.RecurringItem;
import ebudget.data.Common;
import ebudget.data.Categories;
import ebudget.data.Repository;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.TransactionDto;
import ebudget.io.View;

class CreateBudgetTest {

	private static final int YEAR = 2020;

	private static final CategoryDto CPT = new CategoryDto("formule compte");
	private static final CategoryDto BANQUE = new CategoryDto("frais bancaire");
	private static final CategoryDto SALAIRE = new CategoryDto("salaire fanny", true);
	private static final CategoryDto OUMAR = new CategoryDto("salaire oumar");
	private static final CategoryDto DIVERS = new CategoryDto("Divers");
	private static final CategoryDto LOYER = new CategoryDto("Loyer");
	private static final CategoryDto CAF = new CategoryDto("caf", true);
	private static final CategoryDto EMPRUNT = new CategoryDto("emprunt");
	private static final CategoryDto EPARGNE = new CategoryDto("epargne");
	private static final CategoryDto AUTRE = new CategoryDto("autre revenu", true);
	private static final CategoryDto PSY = new CategoryDto("psy");

	private static final CategoryDto ALIMENTATION = new CategoryDto("alimentation");
	private static final CategoryDto ASSURANCE = new CategoryDto("assurance habitation");

	private static final CategoryDto CADEAUX = new CategoryDto("cadeaux");

	private static final CategoryDto COIFFEUR = new CategoryDto("coiffure");
	private static final CategoryDto CPAM = new CategoryDto("cpam", true);
	private static final CategoryDto CREDIT = new CategoryDto("credit");
	private static final CategoryDto EDF = new CategoryDto("edf");
	private static final CategoryDto ELECTROMENAGER = new CategoryDto("electromenager");

	private static final CategoryDto GARDE = new CategoryDto("frais de garde");
	private static final CategoryDto HYGIENE = new CategoryDto("hygiène");
	private static final CategoryDto IMPOT = new CategoryDto("impôt sur revenu");
	private static final CategoryDto IDEMNITE = new CategoryDto("indemnite");
	private static final CategoryDto JEUX = new CategoryDto("jeux");
	private static final CategoryDto LABORATOIRE = new CategoryDto("laboratoire");
	private static final CategoryDto LOISIR = new CategoryDto("loisirs");

	private static final CategoryDto MEDECIN = new CategoryDto("medecin");
	private static final CategoryDto MOBILE = new CategoryDto("mobile");
	private static final CategoryDto MUTUELLE = new CategoryDto("mutuelle");
	private static final CategoryDto NAVIGO = new CategoryDto("navigo");
	private static final CategoryDto NET = new CategoryDto("net");
	private static final CategoryDto PHARMACIE = new CategoryDto("pharmacie");

	private static final CategoryDto RELIQUAT = new CategoryDto("reliquat");

	private static final CategoryDto TAXI = new CategoryDto("Taxi");
	private static final CategoryDto TICKET = new CategoryDto("ticket");
	private static final CategoryDto VACANCE = new CategoryDto("vacances");
	private static final CategoryDto VETEMENT = new CategoryDto("vêtements");

	@BeforeEach
	public void clean() {
		Repository.clearDataBase();
	}

	@Test
	// TODO scenario de base
	void createBalancedBudget() {
		// inititalisation des catégories

		Categories.initCategories();
		assertEquals(41, Categories.countCategory());
		assertTrue(Categories.isCategory(SALAIRE), "this categry should existe");
		assertFalse(Categories.isCategory(OUMAR), "this category should be an income");

		// creation du budget de reference
		View view = new View();
		String fileNameBaseBudget = "C:\\Users\\ffazer\\projets\\eBubget\\budgetReference.csv";
		Map<CategoryDto, Double> budgetItemList = view.readBudgetItem(fileNameBaseBudget);

		BaseBudget baseBudget = new BaseBudget(budgetItemList);
		assertEquals(3130.0, baseBudget.getAmount(SALAIRE));
		assertTrue(baseBudget.getBalance() > 0.0);
		baseBudget.print();

		// ajout des dépenses reccurrente depuis un fichier
		String fileNameRecurringItem = "C:\\Users\\ffazer\\projets\\eBubget\\src\\test\\resources\\recurring.csv";
		List<RecurringItem> reccuringItemList = view.readRecurringItem(fileNameRecurringItem);

		// creation du budget annuel
		AnnualBudget annualBudget = new AnnualBudget(YEAR, 0.0, baseBudget, reccuringItemList);
		annualBudget.print();
		// verification des montants un test
		assertEquals(0.0, annualBudget.getAmountToFitPerMonthList(1), 0.0);
		assertEquals(0.0, annualBudget.getAmountToFitPerMonthList(5), 0.0);

		assertEquals(101.92, annualBudget.getBalanceByMonth(1), 0.001);
		assertEquals(68.35, annualBudget.getBalanceByMonth(5), 0.001);

	}

	@Test
	// TODO scenario de base
	void createBudgetToFit() {
		Categories.initCategories();
		assertEquals(41, Categories.countCategory());
	}

}