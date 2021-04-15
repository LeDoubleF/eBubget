package ebudget.calculation;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ebudget.data.Categories;
import ebudget.data.dto.CategoryDto;

class RecurringItemTest {

	private final CategoryDto LOYER = new CategoryDto("Loyer");
	private final CategoryDto SALAIRE = new CategoryDto("Salaire", true);
	private final CategoryDto TAXI = new CategoryDto("Taxi");
	private final CategoryDto IMPOT = new CategoryDto("Impot");
	public static List<Boolean> monthly = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true);

	@BeforeEach
	public void setUp() {
		Categories.addCategory(SALAIRE);
		Categories.addCategory(TAXI);
		Categories.addCategory(IMPOT);
		Categories.setDefaultCategory(TAXI);
	}

	@Test
	void testWithExistingCategory() {
		RecurringItem salaire = new RecurringItem(SALAIRE, "SALAIRE", 20.0, false, true, monthly);
		RecurringItem impot = new RecurringItem(IMPOT, "impot", 1070.0, true, true, monthly);

		assertEquals(IMPOT, impot.getCategory());
		assertEquals(SALAIRE, salaire.getCategory());
	}

	@Test
	void testWithNotExistingCategory() {
		RecurringItem loyer = new RecurringItem(LOYER, "LOYER", 20.0, false, true, monthly);

		assertEquals(LOYER, loyer.getCategory());
	}

	@Test
	void ExceptionOUTOfBound12() {
		RecurringItem salaire = new RecurringItem(SALAIRE, "SALAIRE", 20.0, false, true, monthly);
		try {
			salaire.isForThisMonth(13);
			fail("Should throw exception out of bound");
		} catch (ArrayIndexOutOfBoundsException aExp) {
			assertTrue(aExp.getMessage()
				.contains("12"));
		}
	}

	@Test
	void ExceptionOUTOfBoundMinus1() {

		RecurringItem salaire = new RecurringItem(SALAIRE, "SALAIRE", 20.0, false, true, monthly);
		try {
			salaire.isForThisMonth(0);
			fail("Expected an IndexOutOfBoundsException to be thrown");
		} catch (ArrayIndexOutOfBoundsException aExp) {
			System.out.println(aExp.getMessage());
			assertTrue(aExp.getMessage()
				.contains("-1"));
		}
	}

	@Test
	void testIisForThisMonth() {
		RecurringItem salaire = new RecurringItem(SALAIRE, "SALAIRE", 20.0, false, true, monthly);
		assertTrue(salaire.isForThisMonth(2));

	}
}
