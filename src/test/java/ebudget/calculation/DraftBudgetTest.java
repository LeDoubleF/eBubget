package ebudget.calculation;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import ebudget.data.Categories;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.PeriodDTo;

public class DraftBudgetTest {

	private static final CategoryDto DIVERS = new CategoryDto("Divers");
	private static final CategoryDto LOYER = new CategoryDto("Loyer");
	private static final CategoryDto SALAIRE = new CategoryDto("Salaire", true);
	private static final CategoryDto TAXI = new CategoryDto("Taxi");

	@Before
	public void setUp() {

		Categories.addCategory(SALAIRE);
		Categories.addCategory(LOYER);
		Categories.addCategory(TAXI);
		Categories.addCategory(DIVERS);
		Categories.setDefaultCategory(DIVERS);
	}

	@Test
	public void testNewDraftBudgetwithoutRecurringExpense() {
		// Given
		BaseBudget baseBudget = new BaseBudget();
		baseBudget.add(TAXI, 10.0);
		PeriodDTo period = new PeriodDTo(2020, 1);

		// When
		DraftBudget draftBudget = new DraftBudget(period, baseBudget);

		// Then
		assertEquals(0.0, draftBudget.getAmount(DIVERS), 0);
		assertEquals(10.0, draftBudget.getAmount(TAXI), 0);
		assertEquals(-10.0, draftBudget.getBalance(), 0);
	}

	@Test
	public void testNewDraftBudgetwithRecurringExpense() {
		// Given
		List<BudgetItem> recurringbudgetItemList = new ArrayList<>();
		recurringbudgetItemList.add(new BudgetItem(SALAIRE, 2000.0));
		recurringbudgetItemList.add(new BudgetItem(LOYER, 100.0));
		recurringbudgetItemList.add(new BudgetItem(TAXI, 50.0));

		BaseBudget baseBudget = new BaseBudget();
		baseBudget.add(TAXI, 10.0);
		baseBudget.add(LOYER, 800.0);

		PeriodDTo period = new PeriodDTo(2020, 1);

		// When

		DraftBudget draftBudget = new DraftBudget(period, baseBudget, recurringbudgetItemList);

		// Then
		assertEquals(0.0, draftBudget.getAmount(DIVERS), 0);
		assertEquals(50.0, draftBudget.getAmount(TAXI), 0);
		assertEquals(2000.0, draftBudget.getAmount(SALAIRE), 0);
		assertEquals(800.0, draftBudget.getAmount(LOYER), 0);
		assertEquals(1150.0, draftBudget.getBalance(), 0);

	}
}
