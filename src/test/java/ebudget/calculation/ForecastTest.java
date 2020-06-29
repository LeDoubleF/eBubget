package ebudget.calculation;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ebudget.data.Repository;
import ebudget.data.dao.CategoryEntity;
import ebudget.data.dto.ForecastDto;

public class ForecastTest {

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	@Ignore("test à déplacer")

	// TODO deplacer
	public void testBalanceByMonth() {
		// given
		Forecast forecast = new Forecast();
		Repository.clearDataBase();
		CategoryEntity.save("salaire");
		CategoryEntity.save("jeux");
		CategoryEntity.save("taxi");
		CategoryEntity.save("divers");
		ForecastDto forecast1 = new ForecastDto("salaire", "docapost", 1000.0, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
		ForecastDto forecast2 = new ForecastDto("jeux", "ippudo", 50.0, true, false, true, true, true, false, true, false, true, false, true, false, true, false, true);
		ForecastDto forecast3 = new ForecastDto("taxi", "g7", 300.0, false, true, false, true, true, false, false, false, true, false, false, false, true, false, false);
		ForecastDto forecast4 = new ForecastDto("divers", "meuble", 350.0, false, false, false, true, false, false, false, false, true, false, false, false, false, false, false);
		ArrayList<ForecastDto> forecastList = new ArrayList<>();
		forecastList.add(forecast1);
		forecastList.add(forecast2);
		forecastList.add(forecast3);
		forecastList.add(forecast4);

		Repository.addForecats(forecastList);

		ArrayList<Double> balanceByMonth = new ArrayList<>();
		balanceByMonth.add(400.0);// 1
		balanceByMonth.add(750.0);
		balanceByMonth.add(1000.0);
		balanceByMonth.add(1050.0);
		balanceByMonth.add(1000.0);// 5
		balanceByMonth.add(400.0);
		balanceByMonth.add(1000.0);
		balanceByMonth.add(1050.0);
		balanceByMonth.add(1000.0);;
		balanceByMonth.add(750.0);// 10
		balanceByMonth.add(1000.0);
		balanceByMonth.add(1050.0);

		assertEquals(balanceByMonth, forecast.getBalanceByMonth());
	}

	@Test
	public void testAnalyseForecastWhenNeverCallfitForecastWithAllPositiveBalances() {
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

		Forecast forecastMock = new Forecast();
		forecastMock = Mockito.spy(forecastMock);
		Mockito.when(forecastMock.getBalanceByMonth()).thenReturn(balanceByMonth);
		forecastMock.analyseForecats(0.0);

		Mockito.verify(forecastMock, Mockito.never()).fitForecast(Mockito.anyInt(), Mockito.anyDouble());
		assertEquals(new ArrayList<>(), forecastMock.getForcastPerMonth());
	}

	@Test
	public void testAnalyseForecastWhenNeverCallfitForecastWithNegativeBalances() {

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

		Forecast forecastMock = new Forecast();
		forecastMock = Mockito.spy(forecastMock);
		Mockito.when(forecastMock.getBalanceByMonth()).thenReturn(balanceByMonth);

		forecastMock.analyseForecats(0.0);

		Mockito.verify(forecastMock, Mockito.never()).fitForecast(Mockito.anyInt(), Mockito.anyDouble());
		assertEquals(new ArrayList<>(), forecastMock.getForcastPerMonth());
	}

	@Test
	public final void testAnalyseForecastWithOnForecast() {
		ArrayList<Double> balanceByMonth = new ArrayList<>();
		balanceByMonth.add(10.0);// 1
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(-90.0);// 7
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 10
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);

		Forecast forecastMock = new Forecast();
		forecastMock = Mockito.spy(forecastMock);
		Mockito.when(forecastMock.getBalanceByMonth()).thenReturn(balanceByMonth);

		forecastMock.analyseForecats(0.0);

		assertEquals(6, forecastMock.getForcastPerMonth().size());
		Mockito.verify(forecastMock).fitForecast(1, -5.0);
		Mockito.verify(forecastMock).fitForecast(2, -5.0);
		Mockito.verify(forecastMock).fitForecast(3, -5.0);
		Mockito.verify(forecastMock).fitForecast(4, -5.0);
		Mockito.verify(forecastMock).fitForecast(5, -5.0);
		Mockito.verify(forecastMock).fitForecast(6, -5.0);

		List<Double> expectedGlobalBalanceList = Arrays
				.asList(new Double[]{10.0, 20.0, 30.0, 40.0, 50.0, 60.0, -30.0, -20.0, -10.0, 0.0, 10.0, 20.0});
		Assert.assertEquals(expectedGlobalBalanceList, forecastMock.getGlobalBalanceList());
		// TODO assertEquals(new ArrayList<>(),
		// forecastMock.getForcastPerMonth());
	}

	@Test
	public final void testAnalyseForecastWithtwoForecast() {
		ArrayList<Double> balanceByMonth = new ArrayList<>();
		balanceByMonth.add(10.0);// 1
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(-48.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 7
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);
		balanceByMonth.add(10.0);// 10
		balanceByMonth.add(-60.0);
		balanceByMonth.add(10.0);

		Forecast forecastMock = new Forecast();
		forecastMock = Mockito.spy(forecastMock);
		Mockito.when(forecastMock.getBalanceByMonth()).thenReturn(balanceByMonth);

		forecastMock.analyseForecats(0.0);

		assertEquals(9, forecastMock.getForcastPerMonth().size());
		Mockito.verify(forecastMock).fitForecast(1, -6.0);
		Mockito.verify(forecastMock).fitForecast(2, -6.0);
		Mockito.verify(forecastMock).fitForecast(3, -6.0);

		Mockito.verify(forecastMock).fitForecast(5, -3.0);
		Mockito.verify(forecastMock).fitForecast(6, -3.0);
		Mockito.verify(forecastMock).fitForecast(7, -3.0);
		Mockito.verify(forecastMock).fitForecast(8, -3.0);
		Mockito.verify(forecastMock).fitForecast(9, -3.0);
		Mockito.verify(forecastMock).fitForecast(10, -3.0);

		List<Double> expectedGlobalBalanceList = Arrays.asList(new Double[]{10.0, 20.0, 30.0, -18.0, -8.0, 2.0, 12.0, 22.0, 32.0, 42.0, -18.0, -8.0});
		System.out.println(forecastMock.getGlobalBalanceList());
		Assert.assertEquals(expectedGlobalBalanceList, forecastMock.getGlobalBalanceList());

		// TODO assertEquals(new ArrayList<>(),
		// forecastMock.getForcastPerMonth());
	}

}
