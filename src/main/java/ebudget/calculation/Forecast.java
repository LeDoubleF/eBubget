package ebudget.calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe servant � caluler les futurs budgets d'une ann�e donn�e
 * 
 * @author ffazer
 *
 */
public class Forecast {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	/**
	 * Solde d'un budget (les budgets sont mensuels)
	 */
	private ArrayList<Double> balanceByMonthList = new ArrayList<>();
	/**
	 * �volution du solde g�n�ral chaque mois
	 */
	private ArrayList<Double> globalBalanceList = new ArrayList<>();
	/**
	 * Montant des ajustement � faire par mois
	 */
	private ArrayList<Double> amountToFitPerMonthList = new ArrayList<>();
	/**
	 * poste de d�pense pour ajustement des bugets par mois
	 */
	private ArrayList<BudgetItem> forcastPerMonthList = new ArrayList<>();

	public Forecast() {
		super();
		for (int i = 0; i < 12; i++) {
			amountToFitPerMonthList.add(0.0);
		}
	}

	public void analyseForecats(double initialBalance) {
		if (isForecastNeedfit()) {

			int from = 1;
			int to = 0;
			double balance;
			double sumBalance = initialBalance;

			for (int i = 0; i < 12; i++) {
				to++;
				balance = getBalanceByMonth().get(to - 1);
				double globalBalance = sumBalance + balance;
				sumBalance = globalBalance;
				globalBalanceList.add(globalBalance);
				if ((balance < 0) && (globalBalance < 0)) {
					double amountToFit = globalBalance / (to - from);
					LOGGER.log(Level.INFO, "Ajustement � partir du mois: {0} jusqu''au mois : {1} de {2}", new Object[]{from, to, globalBalance});
					for (int month = from; month < to; month++) {
						amountToFitPerMonthList.set(month - 1, amountToFit);
						fitForecast(month, amountToFit);
					}

					from = to + 1;
				}

			}
		}
	}

	public boolean isForecastNeedfit() {
		forcastPerMonthList = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			if (getBalanceByMonth().get(i) < 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �volution du solde g�n�ral chaque mois
	 */
	public List<Double> getGlobalBalanceList() {
		return globalBalanceList;
	}

	/**
	 * Solde d'un budget (les budgets sont mensuels)
	 */
	public List<Double> getBalanceByMonth() {
		return balanceByMonthList;
	}

	public void setBalanceByMonth(List<Double> balanceByMonth) {
		balanceByMonthList = (ArrayList<Double>) balanceByMonth;
	}

	/**
	 * mois dont le budget pr�visionnel dopit �tre ajust� <br/>
	 * janvier=1, d�cembre=12
	 * 
	 * @param month
	 * @param d
	 */
	public void fitForecast(int month, double amount) {
		// TODO fitForecast
		LOGGER.log(Level.INFO, "fitForecast mois :{0} montant :{1}", new Object[]{month, amount});
		forcastPerMonthList.add(new BudgetItem());
	}

	/**
	 * poste de d�pense pour ajustement des bugets par mois
	 */
	public List<BudgetItem> getForcastPerMonth() {
		return forcastPerMonthList;
	}

	public List<Double> getAmountToFitPerMonthList() {
		return amountToFitPerMonthList;
	}

}
