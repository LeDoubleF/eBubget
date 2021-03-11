package integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ebudget.calculation.AnnualBudget;
import ebudget.calculation.BaseBudget;
import ebudget.calculation.RecurringItem;
import ebudget.data.Categories;
import ebudget.data.Common;
import ebudget.data.dto.CategoryDto;
import ebudget.io.View;

class CreateBudgetTest {

	private static final double PRECISION = 0.01;
	private static final int YEAR = 2020;
	private static final Integer NBCategories = 39;
	private static final CategoryDto SALAIRE = new CategoryDto("salaire", true);
	private static final CategoryDto IMPOT = new CategoryDto("impôt sur revenu");

	private ClassLoader classLoader = getClass().getClassLoader();

	@BeforeEach
	public void clean() {
		Common.clearDataBase();
	}

	@Test
	void createBalancedBudget() {
		// inititalisation des catégories
		Categories.initCategories();
		assertEquals(NBCategories, Categories.countCategory());
		assertTrue(Categories.isCategory(SALAIRE), "this categry should existe");
		assertTrue(Categories.isIncome(SALAIRE), "this category should be an income");

		// creation du budget de reference
		View view = new View();
		File BaseBudgetItemFile = new File(classLoader.getResource("budgetReferenceIntegrationTest.csv")
			.getFile());
		Map<CategoryDto, Double> budgetItemList = view.readBudgetItem(BaseBudgetItemFile.getAbsolutePath());

		BaseBudget baseBudget = new BaseBudget(budgetItemList);
		assertEquals(2000.0, baseBudget.getAmount(SALAIRE));
		baseBudget.print();
		System.out.println("\n\t" + baseBudget.getBalance());
		assertEquals(250.0, baseBudget.getBalance(), PRECISION);

		// ajout des dépenses reccurrente depuis un fichier
		File recurringItemFile = new File(classLoader.getResource("recurringIntegrationTest.csv")
			.getFile());
		List<RecurringItem> reccuringItemList = view.readRecurringItem(recurringItemFile.getAbsolutePath());

		// creation du budget annuel
		AnnualBudget annualBudget = new AnnualBudget(YEAR, 0.0, baseBudget, reccuringItemList);
		annualBudget.print();
		System.out.println("\n\t" + annualBudget.getBalance());

		// verification des montants
		for (int i = 1; i < 13; i++) {
			assertEquals(0.0, annualBudget.getAmountToFitPerMonthList(i), PRECISION);
		}

		for (int i = 1; i < 9; i++) {

			assertEquals(1311.0, annualBudget.getBalanceByMonth(i), 0.001);
		}
		assertEquals(1324.31, annualBudget.getBalanceByMonth(9), PRECISION);
		assertEquals(1311.0, annualBudget.getBalanceByMonth(10), PRECISION);
		assertEquals(571.0, annualBudget.getBalanceByMonth(11), PRECISION);
		assertEquals(-806.0, annualBudget.getBalanceByMonth(12), PRECISION);

	}

	@Test
	// TODO scenario de base
	void createBudgetToFit() {
		// inititalisation des catégories
		Categories.initCategories();
		assertEquals(NBCategories, Categories.countCategory());
		assertTrue(Categories.isCategory(SALAIRE), "this categry should existe");
		assertTrue(Categories.isIncome(SALAIRE), "this category should be an income");

		// creation du budget de reference
		View view = new View();
		File BaseBudgetItemFile = new File(classLoader.getResource("budgetReferenceIntegrationTest.csv")
			.getFile());
		Map<CategoryDto, Double> budgetItemList = view.readBudgetItem(BaseBudgetItemFile.getAbsolutePath());

		BaseBudget baseBudget = new BaseBudget(budgetItemList);
		assertEquals(2000.0, baseBudget.getAmount(SALAIRE));
		baseBudget.print();
		System.out.println("\n\t" + baseBudget.getBalance());
		assertEquals(250.0, baseBudget.getBalance(), PRECISION);

		// ajout des dépenses reccurrente depuis un fichier
		File recurringItemFile = new File(classLoader.getResource("recurringIntegrationTest.csv")
			.getFile());
		List<RecurringItem> reccuringItemList = view.readRecurringItem(recurringItemFile.getAbsolutePath());
		RecurringItem impot = new RecurringItem(IMPOT, "retard", 10000.0, false, true, Arrays.asList(false, false, false, false, false, true, false,
				false, false, false, false, false));
		reccuringItemList.add(impot);

		// creation du budget annuel
		AnnualBudget annualBudget = new AnnualBudget(YEAR, 0.0, baseBudget, reccuringItemList);
		annualBudget.print();
		System.out.println("\n\t" + annualBudget.getBalance());

		// verification des montants
		for (int i = 1; i < 6; i++) {
			assertEquals(1311.0, annualBudget.getBalanceByMonth(i), PRECISION);
		}
		assertEquals(-8689.0, annualBudget.getBalanceByMonth(6), PRECISION);
		for (int i = 7; i < 9; i++) {
			assertEquals(1311.0, annualBudget.getBalanceByMonth(i), PRECISION);
		}
		assertEquals(1324.31, annualBudget.getBalanceByMonth(9), PRECISION);
		assertEquals(1311.0, annualBudget.getBalanceByMonth(10), PRECISION);
		assertEquals(571.0, annualBudget.getBalanceByMonth(11), PRECISION);
		assertEquals(-806.0, annualBudget.getBalanceByMonth(12), PRECISION);

		for (int i = 1; i < 7; i++) {
			assertEquals(-355.67, annualBudget.getAmountToFitPerMonthList(i), PRECISION);
		}
		for (int i = 7; i < 13; i++) {
			assertEquals(0.0, annualBudget.getAmountToFitPerMonthList(i), PRECISION);
		}

	}

}