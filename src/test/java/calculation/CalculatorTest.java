package calculation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CalculatorTest {

	Calculator calculator = new Calculator();

	@Test
	public final void testCalculateFinalBalance() {
		assertEquals(32.7, calculator.calculateFinalBalance(10.2, 22.5), 0);
	}

}