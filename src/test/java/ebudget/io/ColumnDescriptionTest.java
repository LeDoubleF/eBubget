package ebudget.io;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ColumnDescriptionTest {

	private static final double PRECISION = 0.00;

	@Test
	void constructorTest() {
		ColumnDescription description = new ColumnDescription(CVSParameter.DOUBLE, "amount");

		assertEquals("amount", description.getName());
		assertEquals(CVSParameter.DOUBLE, description.getType());
	}

	@Test
	void convertDoubleTest() {

		ColumnDescription column = new ColumnDescription(CVSParameter.DOUBLE, "amount");
		Double Value = column.convert("55.6");

		assertEquals(55.6, Value, PRECISION);
	}

	@Test
	void convertDoubleWithComma() {

		ColumnDescription column = new ColumnDescription(CVSParameter.DOUBLE, "amount");
		Double Value = column.convert("55,6");

		assertEquals(55.6, Value, PRECISION);
	}
}
