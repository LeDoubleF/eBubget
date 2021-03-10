package ebudget.io;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ColumnDescriptionTest {

	private static final double PRECISION = 0.00;

	@Test
	void constructorTest() {
		Column description = new Column(CVSParameter.DOUBLE, "amount");

		assertEquals("amount", description.getName());
		assertEquals(CVSParameter.DOUBLE, description.getType());
	}

	@Test
	void convertDoubleTest() {

		Column column = new Column(CVSParameter.DOUBLE, "amount");
		Double Value = column.convert("55.6");

		assertEquals(55.6, Value, PRECISION);
	}

	@Test
	void convertDoubleWithComma() {

		Column column = new Column(CVSParameter.DOUBLE, "amount");
		Double Value = column.convert("55,6");

		assertEquals(55.6, Value, PRECISION);
	}
}
