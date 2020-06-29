package ebudget.data.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CategoryDtoTest {

	private static final CategoryDto SALAIRE = new CategoryDto("salaire", true);
	private static final CategoryDto SALAIRE2 = new CategoryDto("salaire", true);
	private static final CategoryDto SALAIRE_FALSE = new CategoryDto("salaire");
	private static final CategoryDto LOYER = new CategoryDto("Loyer");

	@Test
	void testCompareToEquals() {
		assertEquals(0, SALAIRE.compareTo(SALAIRE2));
		// a negative integer, zero, or a positive integer as this objectis less
		// than, equal to, or greater than the specified object.
	}

	@Test
	void testCompareGeater() {
		assertTrue(SALAIRE.compareTo(SALAIRE_FALSE) > 0);

	}

	@Test
	void testCompareLess() {
		assertTrue(SALAIRE_FALSE.compareTo(SALAIRE) < 0);

	}

	@Test
	void testCompareToGeaterDifferent() {
		assertTrue(SALAIRE.compareTo(LOYER) > 0);

	}

}
