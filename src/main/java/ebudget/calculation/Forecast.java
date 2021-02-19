package ebudget.calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe servant à caluler les futurs budgets d'une année donnée
 * 
 * @author ffazer
 *
 */
public class Forecast {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	/**
	 * Solde d'un budget (les budgets sont mensuels)
	 */
	private List<Double> balanceByMonthList = new ArrayList<>();
	/**
	 * évolution du solde général chaque mois
	 */
	private List<Double> globalBalanceList = new ArrayList<>();
	/**
	 * Montant des ajustement à faire par mois
	 */
	private List<Double> amountToFitPerMonthList = new ArrayList<>();
	/**
	 * poste de dépense pour ajustement des bugets par mois
	 */
	private List<BudgetItem> forcastPerMonthList = new ArrayList<>();

	public Forecast() {
		super();
		for (int i = 0; i < 12; i++) {
			amountToFitPerMonthList.add(0.0);
			globalBalanceList.add(0.0);
			balanceByMonthList.add(0.0);
		}
	}

	public Forecast(List<Double> balanceByMonthList) {
		this();
		setBalanceByMonth(balanceByMonthList);

	}

	public void analyseForecats(double initialBalance) {

		globalBalanceList = arithmeticSum(initialBalance, balanceByMonthList);
		List<Double> balanceByMonthClone = new ArrayList<>(balanceByMonthList);
		List<Double> globalBalanceListClone = new ArrayList<>(globalBalanceList);

		int from = 0;
		int to = 13;
		for (int i = 11; i > 0; i--) {
			to--;
			double balance = balanceByMonthClone.get(i);
			double globalBalance = globalBalanceListClone.get(i);
			if ((balance < 0) && globalBalance < 0) {
				double intermediare = 0.0;
				int searchPrevious = i;
				do {
					intermediare = intermediare + balanceByMonthClone.get(searchPrevious);
					searchPrevious--;
				} while (balanceByMonthClone.get(searchPrevious) > 0 && searchPrevious > 0);
				if (intermediare < 0) {
					double amountToFit = globalBalance / to;
					updateDoubleList(amountToFitPerMonthList, amountToFit, from, to);
					updateDoubleList(balanceByMonthClone, -amountToFit, from, to);

					globalBalanceListClone = arithmeticSum(initialBalance, balanceByMonthClone);
				}
			}
		}
		if (globalBalanceListClone.get(0) < 0) {
			double value = amountToFitPerMonthList.get(0) + globalBalanceListClone.get(0);
			amountToFitPerMonthList.set(0, value);
		}
	}

	private List<Double> arithmeticSum(double initialisationValue, List<Double> listToSum) {
		double sumBalance = initialisationValue;
		ArrayList<Double> arithmeticSum = new ArrayList<>();
		for (int i = 0; i < listToSum.size(); i++) {

			sumBalance = sumBalance + listToSum.get(i);
			arithmeticSum.add(sumBalance);

		}
		return arithmeticSum;
	}

	private void updateDoubleList(List<Double> doubleList, double amountToAdd, int from, int to) {
		for (int i = from; i < to; i++) {
			double value = doubleList.get(i) + amountToAdd;
			doubleList.set(i, value);
		}
	}

	/**
	 * Solde d'un budget (les budgets sont mensuels)
	 */
	public List<Double> getBalanceByMonth() {
		return balanceByMonthList;
	}

	public void setBalanceByMonth(List<Double> balanceByMonth) {
		balanceByMonthList = balanceByMonth;
	}

	/**
	 * mois dont le budget prévisionnel doit être ajusté <br/>
	 * janvier=1, décembre=12
	 * 
	 * @param month
	 *            (1 to 12)
	 * @param amount
	 */
	public void fitForecast(int month, double amount) {
		// TODO fitForecast
		LOGGER.log(Level.INFO, "fitForecast mois :{0} montant :{1}", new Object[]{month, amount});
		forcastPerMonthList.add(new BudgetItem());
	}

	/**
	 * poste de dépense pour ajustement des bugets par mois
	 */
	public List<BudgetItem> getForcastPerMonth() {
		return forcastPerMonthList;
	}

	public List<Double> getAmountToFitPerMonthList() {
		return amountToFitPerMonthList;
	}

	public double getAmountToFitPerMonthList(int month) {
		return amountToFitPerMonthList.get(month - 1);
	}

	/**
	 * évolution du solde général chaque mois
	 * 
	 * @param month
	 *            (1 to 12)
	 */
	public double getGlobalBalanceList(int month) {
		return globalBalanceList.get(month - 1);
	}

	public List<Double> getGlobalBalanceList() {
		return globalBalanceList;
	}

}
