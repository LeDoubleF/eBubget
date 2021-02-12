package integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ebudget.calculation.AnnualBudget;
import ebudget.calculation.BaseBudget;
import ebudget.calculation.RecurringItem;
import ebudget.common.CommonTest;
import ebudget.data.Categories;
import ebudget.data.Repository;
import ebudget.data.dto.CategoryDto;
import ebudget.io.View;

class CreateBudgetTest {

	private static final int YEAR = 2020;
	private ClassLoader classLoader = getClass().getClassLoader();

	private static final CategoryDto SALAIRE = new CategoryDto("salaire", true);

	@BeforeEach
	public void clean() {
		Repository.clearDataBase();
	}

	@Test
	void createBalancedBudget() {
		// inititalisation des catégories

		Categories.initCategories();
		assertEquals(CommonTest.NBCategories, Categories.countCategory());
		assertTrue(Categories.isCategory(SALAIRE), "this categry should existe");
		assertTrue(Categories.isIncome(SALAIRE), "this category should be an income");

		// creation du budget de reference
		View view = new View();
		File BaseBudgetItemFile = new File(classLoader.getResource("budgetReferenceIntegrationTest.csv").getFile());
		Map<CategoryDto, Double> budgetItemList = view.readBudgetItem(BaseBudgetItemFile.getAbsolutePath());

		BaseBudget baseBudget = new BaseBudget(budgetItemList);
		assertEquals(2000.0, baseBudget.getAmount(SALAIRE));
		baseBudget.print();
		System.out.println("\n\t" + baseBudget.getBalance());
		assertEquals(250.0, baseBudget.getBalance(), 0.0);

		// ajout des dépenses reccurrente depuis un fichier
		File recurringItemFile = new File(classLoader.getResource("recurringIntegrationTest.csv").getFile());
		List<RecurringItem> reccuringItemList = view.readRecurringItem(recurringItemFile.getAbsolutePath());

		// creation du budget annuel
		AnnualBudget annualBudget = new AnnualBudget(YEAR, 0.0, baseBudget, reccuringItemList);
		annualBudget.print();
		System.out.println("\n\t" + annualBudget.getBalance());
		// verification des montants un test
		for (int i = 1; i < 13; i++) {
			assertEquals(0.0, annualBudget.getAmountToFitPerMonthList(i), 0.0);
		}

		for (int i = 1; i < 9; i++) {

			assertEquals(178.0, annualBudget.getBalanceByMonth(i), 0.001);
		}
		assertEquals(191.3, annualBudget.getBalanceByMonth(9), 0.01);
		assertEquals(178.0, annualBudget.getBalanceByMonth(10), 0.0);
		assertEquals(-562, annualBudget.getBalanceByMonth(11), 0.0);
		assertEquals(-39, annualBudget.getBalanceByMonth(12), 0.0);

	}

	@Test
	// TODO scenario de base
	void createBudgetToFit() {
		Categories.initCategories();
		assertEquals(CommonTest.NBCategories, Categories.countCategory());
	}

}