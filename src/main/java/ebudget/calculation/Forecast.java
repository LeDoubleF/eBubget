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
	private ArrayList<Double> balanceByMonthList = new ArrayList<>();
	/**
	 * évolution du solde général chaque mois
	 */
	private ArrayList<Double> globalBalanceList = new ArrayList<>();
	/**
	 * Montant des ajustement à faire par mois
	 */
	private ArrayList<Double> amountToFitPerMonthList = new ArrayList<>();
	/**
	 * poste de dépense pour ajustement des bugets par mois
	 */
	private ArrayList<BudgetItem> forcastPerMonthList = new ArrayList<>();

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

		double amountToFit = 0.0;
		double sumBalance = initialBalance;
		ArrayList<Double> getBalanceByMonthClone = (ArrayList<Double>) balanceByMonthList.clone();
		for (int i = 0; i < 12; i++) {

			double globalBalance = sumBalance + balanceByMonthList.get(i);
			sumBalance = globalBalance;
			globalBalanceList.set(i, globalBalance);

		}
		ArrayList<Double> balanceByMonthClone = (ArrayList<Double>) balanceByMonthList.clone();
		ArrayList<Double> globalBalanceListClone = (ArrayList<Double>) globalBalanceList.clone();
		System.out.println("amountToFitPerMonthList");
		System.out.println(amountToFitPerMonthList);
		System.out.println("balanceByMonthClone");
		System.out.println(balanceByMonthClone);
		System.out.println("globalBalanceListClone");
		System.out.println(globalBalanceListClone);

		int from = 0;
		int to = 13;
		for (int i = 11; i > 0; i--) {
			System.out.println("boucle " + i);
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
				System.out.println("\n\t" + intermediare + "\n");
				if (intermediare < 0) {
					amountToFit = globalBalance / to;
					System.out.println("\t amountToFit " + amountToFit + " globalBalance " + globalBalance + " to " + to);
					for (int j = from; j < to; j++) {
						double value = amountToFitPerMonthList.get(j) + amountToFit;
						System.out.println("ToFit " + value + " j " + j + " from " + from);
						amountToFitPerMonthList.set(j, value);
						value = balanceByMonthClone.get(j) - amountToFit;
						balanceByMonthClone.set(j, value);
					}
					sumBalance = 0.0;
					for (int k = 0; k < 12; k++) {

						sumBalance = sumBalance + balanceByMonthClone.get(k);
						globalBalanceListClone.set(k, sumBalance);

					}
				}

				System.out.println("amountToFitPerMonthList");
				System.out.println(amountToFitPerMonthList);
				System.out.println("balanceByMonthClone");
				System.out.println(balanceByMonthClone);
				System.out.println("globalBalanceListClone");
				System.out.println(globalBalanceListClone);

			}
		}
		if (globalBalanceListClone.get(0) < 0) {
			double value = amountToFitPerMonthList.get(0) + globalBalanceListClone.get(0);
			System.out.println("Tfisrt  " + value);
			amountToFitPerMonthList.set(0, value);
		}
		System.out.println(globalBalanceList);
		System.out.println(balanceByMonthList);
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
