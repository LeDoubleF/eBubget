package ebudget.calculation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

public class CalculatorTest {

	Calculator calculator = new Calculator();

	@Test
	public final void testCalculateFinalBalance() {
		assertEquals(32.7, calculator.calculateFinalBalance(10.2, 22.5), 0.0);
	}

	@Test
	public final void testAnalyseForecast() {
		ArrayList<Double> balanceByMonth = new ArrayList<>();
		balanceByMonth.add(10.0);// 1
		balanceByMonth.add(10.0);
		balanceByMonth.add(-20.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(-100.0);// 5
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 10
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		Calculator.AnalyseForecats(balanceByMonth);
	}
}