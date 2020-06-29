
package ebudget.calculation;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import ebudget.common.CommonTest;
import ebudget.data.Categories;
import ebudget.data.dto.CategoryDto;

public class AnnualBudgetTest {

	private final double BALANCE = 170.0;
	private final int YEAR = 2020;

	private final CategoryDto DIVERS = new CategoryDto("Divers");
	private final CategoryDto LOYER = new CategoryDto("Loyer");
	private final CategoryDto SALAIRE = new CategoryDto("Salaire", true);
	private final CategoryDto TAXI = new CategoryDto("Taxi");
	private final CategoryDto IMPOT = new CategoryDto("Impot");

	@Before
	public void setUp() {

		Categories.addCategory(SALAIRE);
		Categories.addCategory(LOYER);
		Categories.addCategory(TAXI);
		Categories.addCategory(DIVERS);
		Categories.addCategory(IMPOT);
		Categories.setDefaultCategory(DIVERS);
	}

	@Test
	public void bugetWtihoutReccuringItemShouldEqualsBaseBudget() {

		// GIVEN
		BaseBudget baseBudget = new BaseBudget();
		baseBudget.add(SALAIRE, 1000.0);
		baseBudget.add(TAXI, 10.0);
		baseBudget.add(LOYER, 800.0);
		baseBudget.add(DIVERS, 20.0);

		List<RecurringItem> reccuringItemList = new ArrayList<RecurringItem>();

		// WHEN
		AnnualBudget annualBudget = new AnnualBudget(YEAR, 0.0, baseBudget, reccuringItemList);

		// THEN
		assertEquals(BALANCE, annualBudget.getBalanceByMonth(1), 0.0);
		assertEquals(BALANCE, annualBudget.getBalanceByMonth(5), 0.0);
		assertEquals(BALANCE, annualBudget.getBalanceByMonth(11), 0.0);
	}

	@Test
	public void bugetIsReccuringItemIfItMoreThanBaseBudget() {

		// GIVEN
		BaseBudget baseBudget = new BaseBudget();
		baseBudget.add(SALAIRE, 1000.0);
		baseBudget.add(TAXI, 10.0);
		baseBudget.add(LOYER, 800.0);
		baseBudget.add(DIVERS, 20.0);

		RecurringItem taxi = new RecurringItem(TAXI, "perso", 5.0, true, true, CommonTest.monthly);
		RecurringItem taxi2 = new RecurringItem(TAXI, "pro", 6.0, true, true, Arrays.asList(true, false, false, false, false, false, false, false,
				false, false, false, true));
		List<RecurringItem> reccuringItemList = new ArrayList<RecurringItem>();
		reccuringItemList.add(taxi);
		reccuringItemList.add(taxi2);

		// WHEN
		AnnualBudget annualBudget = new AnnualBudget(YEAR, 0.0, baseBudget, reccuringItemList);

		annualBudget.print();
		// THEN
		assertEquals(11.0, annualBudget.getAmount(TAXI, 1), 0.0);
		assertEquals(10.0, annualBudget.getAmount(TAXI, 2), 0.0);
		assertEquals(11.0, annualBudget.getAmount(TAXI, 12), 0.0);

	}
}