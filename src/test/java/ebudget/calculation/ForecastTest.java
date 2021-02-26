package ebudget.calculation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class ForecastTest {

	@Test
	public void testForecastWithNoInitialisation() {

		Forecast forecast = new Forecast();
		forecast.analyseForecats(0.0);

		for (int i = 1; i < 13; i++) {
			assertEquals(0.0, forecast.getAmountToFitPerMonthList(i), 0.0);
		}

		for (int i = 1; i < 13; i++) {
			assertEquals(0.0, forecast.getGlobalBalanceList(i), 0.0);
		}
	}

	@Test
	public void testAnalyseForecastWhenNothingToFit() {
		ArrayList<Double> balanceByMonth = new ArrayList<>();
		balanceByMonth.add(10.0);// 1
		balanceByMonth.add(10.0);
		balanceByMonth.add(20.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(100.0);// 5
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 10
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);

		Forecast forecast = new Forecast(balanceByMonth);
		forecast.analyseForecats(0.0);

		for (int i = 1; i < 13; i++) {
			assertEquals(0.0, forecast.getAmountToFitPerMonthList(i), 0.0);
		}

		List<Double> expectedGlobalBalanceList = Arrays
				.asList(new Double[]{10.0, 20.0, 40.0, 50.0, 150.0, 160.0, 170.0, 180.0, 190.0, 200.0, 210.0, 220.0});
		Assertions.assertEquals(expectedGlobalBalanceList, forecast.getGlobalBalanceList());
	}

	@Test
	public void testAnalyseForecastWhenNothingToFitAndInitialBalance() {
		ArrayList<Double> balanceByMonth = new ArrayList<>();
		balanceByMonth.add(10.0);// 1
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 5
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 10
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);

		Forecast forecast = new Forecast(balanceByMonth);
		forecast.analyseForecats(-10.0);

		for (int i = 1; i < 13; i++) {
			assertEquals(0.0, forecast.getAmountToFitPerMonthList(i), 0.0);
		}

		List<Double> expectedGlobalBalanceList = Arrays.asList(new Double[]{0.0, 10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0, 110.0});
		Assertions.assertEquals(expectedGlobalBalanceList, forecast.getGlobalBalanceList());
	}

	@Test
	public void testAnalyseForecastWithNegativeBalanceButNothingToFitAndInitialBalance() {

		ArrayList<Double> balanceByMonth = new ArrayList<>();
		balanceByMonth.add(10.0);// 1
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(-30.0);// 7
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 10
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);

		Forecast forecast = new Forecast(balanceByMonth);
		forecast.analyseForecats(-10.0);

		for (int i = 1; i < 13; i++) {
			assertEquals(0.0, forecast.getAmountToFitPerMonthList(i), 0.0);
		}

		List<Double> expectedGlobalBalanceList = Arrays.asList(new Double[]{0.0, 10.0, 20.0, 30.0, 40.0, 50.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0});
		Assertions.assertEquals(expectedGlobalBalanceList, forecast.getGlobalBalanceList());
	}

	@Test
	public void testAnalyseForecastWithNegativeBalanceButNothingToFit() {

		ArrayList<Double> balanceByMonth = new ArrayList<>();
		balanceByMonth.add(10.0);// 1
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(-30.0);// 7
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 10
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);

		Forecast forecast = new Forecast(balanceByMonth);
		forecast.analyseForecats(0.0);

		for (int i = 1; i < 13; i++) {
			assertEquals(0.0, forecast.getAmountToFitPerMonthList(i), 0.0);
		}

		List<Double> expectedGlobalBalanceList = Arrays.asList(new Double[]{10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0});
		Assertions.assertEquals(expectedGlobalBalanceList, forecast.getGlobalBalanceList());
	}

	@Test
	public final void testAnalyseForecastWithOneForecast() {
		ArrayList<Double> balanceByMonth = new ArrayList<>();
		balanceByMonth.add(10.0);// 1
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(-80.0);
		balanceByMonth.add(10.0);// 7
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 10
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);

		Forecast forecast = new Forecast(balanceByMonth);
		forecast.analyseForecats(0.0);

		assertEquals(-5.0, forecast.getAmountToFitPerMonthList(1), 0.0);
		assertEquals(-5.0, forecast.getAmountToFitPerMonthList(2), 0.0);
		assertEquals(-5.0, forecast.getAmountToFitPerMonthList(3), 0.0);
		assertEquals(-5.0, forecast.getAmountToFitPerMonthList(4), 0.0);
		assertEquals(-5.0, forecast.getAmountToFitPerMonthList(5), 0.0);
		assertEquals(-5.0, forecast.getAmountToFitPerMonthList(6), 0.0);

		List<Double> expectedGlobalBalanceList = Arrays
				.asList(new Double[]{10.0, 20.0, 30.0, 40.0, 50.0, -30.0, -20.0, -10.0, 0.0, 10.0, 20.0, 30.0});
		Assertions.assertEquals(expectedGlobalBalanceList, forecast.getGlobalBalanceList());

		for (int i = 7; i < 13; i++) {
			assertEquals(0.0, forecast.getAmountToFitPerMonthList(i), 0.0);
		}
	}

	@Test
	public final void testAnalyseForecastWithOneForecastAndInitialBalance() {
		ArrayList<Double> balanceByMonth = new ArrayList<>();
		balanceByMonth.add(10.0);// 1
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(-80.0);
		balanceByMonth.add(10.0);// 7
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 10
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);

		Forecast forecast = new Forecast(balanceByMonth);
		forecast.analyseForecats(6.0);

		assertEquals(-4.0, forecast.getAmountToFitPerMonthList(1), 0.0);
		assertEquals(-4.0, forecast.getAmountToFitPerMonthList(2), 0.0);
		assertEquals(-4.0, forecast.getAmountToFitPerMonthList(3), 0.0);
		assertEquals(-4.0, forecast.getAmountToFitPerMonthList(4), 0.0);
		assertEquals(-4.0, forecast.getAmountToFitPerMonthList(5), 0.0);
		assertEquals(-4.0, forecast.getAmountToFitPerMonthList(6), 0.0);

		List<Double> expectedGlobalBalanceList = Arrays.asList(new Double[]{16.0, 26.0, 36.0, 46.0, 56.0, -24.0, -14.0, -4.0, 6.0, 16.0, 26.0, 36.0});
		Assertions.assertEquals(expectedGlobalBalanceList, forecast.getGlobalBalanceList());

		for (int i = 7; i < 13; i++) {
			assertEquals(0.0, forecast.getAmountToFitPerMonthList(i), 0.0);
		}
	}

	@Test
	public final void testAnalyseForecastWithTwoNegativeBalanceButOneForecast() {
		ArrayList<Double> balanceByMonth = new ArrayList<>();
		balanceByMonth.add(10.0);// 1
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 5
		balanceByMonth.add(-80.0);
		balanceByMonth.add(10.0);// 7
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 10
		balanceByMonth.add(-40.0);// 11
		balanceByMonth.add(10.0);

		Forecast forecast = new Forecast(balanceByMonth);
		forecast.analyseForecats(0.0);;

		assertEquals(-5.0, forecast.getAmountToFitPerMonthList(1), 0.00001);
		assertEquals(-5.0, forecast.getAmountToFitPerMonthList(2), 0.00001);
		assertEquals(-5.0, forecast.getAmountToFitPerMonthList(3), 0.00001);
		assertEquals(-5.0, forecast.getAmountToFitPerMonthList(4), 0.00001);
		assertEquals(-5.0, forecast.getAmountToFitPerMonthList(5), 0.00001);;
		assertEquals(-5.0, forecast.getAmountToFitPerMonthList(6), 0.00001);

		for (int i = 7; i < 13; i++) {
			assertEquals(0.0, forecast.getAmountToFitPerMonthList(i), 0.0);
		}
		List<Double> expectedGlobalBalanceList = Arrays
				.asList(new Double[]{10.0, 20.0, 30.0, 40.0, 50.0, -30.0, -20.0, -10.0, 0.0, 10.0, -30.0, -20.0});
		Assertions.assertEquals(expectedGlobalBalanceList, forecast.getGlobalBalanceList());
	}

	@Test
	public final void testAnalyseForecastInFirstMonth() {
		ArrayList<Double> balanceByMonth = new ArrayList<>();
		balanceByMonth.add(-10.0);// 1
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 7
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 10
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);

		Forecast forecast = new Forecast(balanceByMonth);
		forecast.analyseForecats(0.0);

		assertEquals(-10.0, forecast.getAmountToFitPerMonthList(1), 0.0);

		List<Double> expectedGlobalBalanceList = Arrays
				.asList(new Double[]{-10.0, 0.0, 10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0,});
		Assertions.assertEquals(expectedGlobalBalanceList, forecast.getGlobalBalanceList());

		for (int i = 2; i < 13; i++) {
			assertEquals(0.0, forecast.getAmountToFitPerMonthList(i), 0.0);
		}

	}

	@Test
	public final void testAnalyseForecastWithtwoForecastInOne() {
		ArrayList<Double> balanceByMonth = new ArrayList<>();
		balanceByMonth.add(10.0);// 1
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(-110.0);
		balanceByMonth.add(10.0);// 7
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 10
		balanceByMonth.add(10.0);
		balanceByMonth.add(-230.0);

		Forecast forecast = new Forecast(balanceByMonth);
		forecast.analyseForecats(0.0);

		for (int i = 1; i < 13; i++) {
			assertEquals(-20.0, forecast.getAmountToFitPerMonthList(i), 0.0);
		}

		List<Double> expectedGlobalBalanceList = Arrays
				.asList(new Double[]{10.0, 20.0, 30.0, 40.0, 50.0, -60.0, -50.0, -40.0, -30.0, -20.0, -10.0, -240.0});
		Assertions.assertEquals(expectedGlobalBalanceList, forecast.getGlobalBalanceList());

	}
}
