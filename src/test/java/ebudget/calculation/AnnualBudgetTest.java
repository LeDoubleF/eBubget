
package ebudget.calculation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import org.junit.Before;
import org.junit.Test;
import ebudget.data.Categories;
import ebudget.data.dao.CategoryEntity;
import ebudget.data.dao.PeriodEntity;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.PeriodDTo;

public class AnnualBudgetTest {

	private final double BALANCE = 170.0;
	private final int YEAR = 2020;
	private final int MONTH = 1;
	private static final double PRECISION = 0.01;
	public static List<Boolean> monthly = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true);

	private final CategoryDto DIVERS = new CategoryDto("Divers");
	private final CategoryDto LOYER = new CategoryDto("Loyer");
	private final CategoryDto SALAIRE = new CategoryDto("Salaire", true);
	private final CategoryDto TAXI = new CategoryDto("Taxi");
	private final CategoryDto IMPOT = new CategoryDto("Impot");

	private BaseBudget baseBudget = new BaseBudget();

	@Before
	public void setUp() {

		Categories.addCategory(SALAIRE);
		Categories.addCategory(LOYER);
		Categories.addCategory(TAXI);
		Categories.addCategory(DIVERS);
		Categories.addCategory(IMPOT);
		Categories.setDefaultCategory(DIVERS);

		baseBudget.add(SALAIRE, 1000.0);
		baseBudget.add(TAXI, 10.0);
		baseBudget.add(LOYER, 800.0);
		baseBudget.add(DIVERS, 20.0);
	}

	@Test
	public void bugetWithoutReccuringItemShouldEqualsBaseBudget() {

		// GIVEN
		List<RecurringItem> reccuringItemList = new ArrayList<RecurringItem>();

		// WHEN
		AnnualBudget annualBudget = new AnnualBudget(YEAR, 0.0, baseBudget, reccuringItemList);

		// THEN
		for (int i = 1; i < 13; i++) {
			assertEquals(BALANCE, annualBudget.getBalanceByMonth(i), PRECISION);
		}

	}

	@Test
	public void budgetWithReccuringItemIfItMoreThanBaseBudget() {

		// GIVEN
		RecurringItem taxi = new RecurringItem(TAXI, "perso", 5.0, true, true, monthly);
		RecurringItem taxi2 = new RecurringItem(TAXI, "pro", 6.0, true, true, Arrays.asList(true, false, false, false, false, false, false, false,
				false, false, false, true));
		List<RecurringItem> reccuringItemList = new ArrayList<RecurringItem>();
		reccuringItemList.add(taxi);
		reccuringItemList.add(taxi2);

		// WHEN
		AnnualBudget annualBudget = new AnnualBudget(YEAR, 0.0, baseBudget, reccuringItemList);

		// THEN
		assertEquals(11.0, annualBudget.getAmount(TAXI, 1), 0);
		assertEquals(10.0, annualBudget.getAmount(TAXI, 2), 0.0);
		assertEquals(11.0, annualBudget.getAmount(TAXI, 12), 0.0);

	}

	@Test
	public void whenUpdateInitialeBalanceComputeBudget() {

		// GIVEN
		List<RecurringItem> reccuringItemList = new ArrayList<RecurringItem>();

		RecurringItem taxi = new RecurringItem(TAXI, "perso", 5.0, true, true, monthly);
		RecurringItem taxe = new RecurringItem(IMPOT, "impotrevenue", 50.0, true, true, Arrays.asList(false, true, false, false, false, false, false,
				false, true, false, false, false));
		reccuringItemList.add(taxi);
		reccuringItemList.add(taxe);

		// WHEN
		AnnualBudget annualBudget = new AnnualBudget(YEAR, 0.0, baseBudget, reccuringItemList);

		annualBudget.setInitialeBalance(-180.0);

		// THEN
		assertEquals(1760, annualBudget.getBalance(), PRECISION);
		assertEquals(-10.0, annualBudget.getAmountToFitPerMonthList(1), PRECISION);
		for (int i = 2; i < 13; i++) {
			assertEquals(0.0, annualBudget.getAmountToFitPerMonthList(i), PRECISION);
		}
	}

	@Test
	public void computeBudgetWithNewBaseBudgetTest() {

		// GIVEN
		List<RecurringItem> reccuringItemList = new ArrayList<RecurringItem>();

		RecurringItem taxi = new RecurringItem(TAXI, "perso", 5.0, true, true, monthly);
		RecurringItem taxi2 = new RecurringItem(TAXI, "pro", 6.0, true, true, Arrays.asList(true, false, false, false, false, false, false, false,
				false, false, false, true));
		reccuringItemList.add(taxi);
		reccuringItemList.add(taxi2);

		BaseBudget newBaseBudget = new BaseBudget();
		newBaseBudget.add(SALAIRE, 1000.0);
		newBaseBudget.add(TAXI, 11.0);
		newBaseBudget.add(LOYER, 800.0);
		newBaseBudget.add(DIVERS, 20.0);

		// WHEN
		AnnualBudget annualBudget = new AnnualBudget(YEAR, 0.0, baseBudget, reccuringItemList);
		assertEquals(11.0, annualBudget.getAmount(TAXI, 1), 0);
		assertEquals(10.0, annualBudget.getAmount(TAXI, 2), 0.0);
		assertEquals(11.0, annualBudget.getAmount(TAXI, 12), 0.0);
		annualBudget.print();
		annualBudget.setBaseBudget(newBaseBudget);
		annualBudget.computeBudget();

		// THEN
		annualBudget.print();
		assertEquals(11.0, annualBudget.getAmount(TAXI, 1), 0);
		assertEquals(11.0, annualBudget.getAmount(TAXI, 2), 0.0);
		assertEquals(11.0, annualBudget.getAmount(TAXI, 12), 0.0);

	}

	@Test
	public void computeBudgetWithNothingChangeTest() {

		// GIVEN
		List<RecurringItem> reccuringItemList = new ArrayList<RecurringItem>();

		RecurringItem taxi = new RecurringItem(TAXI, "perso", 5.0, true, true, monthly);
		RecurringItem taxi2 = new RecurringItem(TAXI, "pro", 6.0, true, true, Arrays.asList(true, false, false, false, false, false, false, false,
				false, false, false, true));
		reccuringItemList.add(taxi);
		reccuringItemList.add(taxi2);

		// WHEN
		AnnualBudget annualBudget = new AnnualBudget(YEAR, 0.0, baseBudget, reccuringItemList);
		assertEquals(11.0, annualBudget.getAmount(TAXI, 1), 0);
		assertEquals(10.0, annualBudget.getAmount(TAXI, 2), 0.0);
		assertEquals(11.0, annualBudget.getAmount(TAXI, 12), 0.0);

		annualBudget.computeBudget();
		assertEquals(11.0, annualBudget.getAmount(TAXI, 1), 0);
		assertEquals(10.0, annualBudget.getAmount(TAXI, 2), 0.0);
		assertEquals(11.0, annualBudget.getAmount(TAXI, 12), 0.0);

	}

	@Test
	public void whenCloseBugetRecomputeBudgetTest() {

		// GIVEN
		List<RecurringItem> reccuringItemList = new ArrayList<RecurringItem>();

		RecurringItem taxi = new RecurringItem(TAXI, "perso", 5.0, true, true, monthly);
		RecurringItem taxi2 = new RecurringItem(TAXI, "pro", 6.0, true, true, Arrays.asList(true, false, false, false, false, false, false, false,
				false, false, false, true));
		reccuringItemList.add(taxi);
		reccuringItemList.add(taxi2);

		// WHEN
		AnnualBudget annualBudget = new AnnualBudget(YEAR, 0.0, baseBudget, reccuringItemList);

		annualBudget.print();

		annualBudget.setTransaction(MONTH, TAXI, "25/06/2005", "facture perdue", "CB", 180.0);
		annualBudget.setTransaction(MONTH, LOYER, "25/06/2005", "facture perdue", "CB", 800.0);
		annualBudget.setTransaction(MONTH, DIVERS, "25/06/2005", "facture perdue", "CB", 40.0);
		annualBudget.setTransaction(MONTH, SALAIRE, "25/06/2005", "facture perdue", "CB", 1000.0);

		annualBudget.closeBudget(1);

		annualBudget.print();

		// THEN
		assertEquals(1849.0, annualBudget.getBalance(), PRECISION);
		assertEquals(-20.0, annualBudget.getAmountToFitPerMonthList(1), PRECISION);
		for (int i = 2; i < 13; i++) {
			assertEquals(0.0, annualBudget.getAmountToFitPerMonthList(i), PRECISION);
		}

	}
}