package ebudget.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import ebudget.data.Categories;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.PeriodDTo;

public class AnnualBudget {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private static DecimalFormat df2 = new DecimalFormat("#.##");
	/**
	 * Solde d'un budget (les budgets sont mensuels), pour un budget créditeur
	 * ,balanceByMonthList>0
	 */
	private List<Double> balanceByMonthList = new ArrayList<>();
	private List<DraftBudget> draftBudgetList = new ArrayList<>();
	private Map<CategoryDto, Double> sumByCategory = new HashMap<>();
	private Double balance;
	private Forecast forecast;

	public Double getBalance() {
		return balance;
	}

	/**
	 * Montant des ajustement à faire par mois
	 */
	private List<Double> amountToFitPerMonthList = new ArrayList<>();

	public AnnualBudget(int year, double initialBalance, BaseBudget baseBudget, List<RecurringItem> recurringbudgetItemList) {
		df2.setRoundingMode(RoundingMode.UP);
		balance = 0.0;
		for (int month = 1; month < 13; month++) {
			PeriodDTo period = new PeriodDTo(year, month);
			List<BudgetItem> recurringBudgetItemList = new ArrayList<>();
			Map<CategoryDto, Double> budgetItemMap = new HashMap<>();
			for (RecurringItem recurringItem : recurringbudgetItemList) {
				LOGGER.log(Level.INFO, "recurringItem: {0} {1}  {2}",
						new Object[]{recurringItem.isForThisMonth(month), recurringItem.getCategory(), recurringItem.getAmount()});
				if (recurringItem.isForThisMonth(month)) {
					CategoryDto category = recurringItem.getCategory();
					double amount = 0.0;
					if (budgetItemMap.containsKey(category)) {
						amount = budgetItemMap.get(category);
					}
					budgetItemMap.put(category, amount + recurringItem.getAmount());
				}
			}
			for (Map.Entry<CategoryDto, Double> map : budgetItemMap.entrySet()) {
				LOGGER.log(Level.INFO, "création de la ligne de budget pour la catégory {0} et le montant {1}",
						new Object[]{map.getKey(), map.getValue()});
				BudgetItem budgetItem = new BudgetItem(map.getKey(), map.getValue());
				recurringBudgetItemList.add(budgetItem);
			}

			DraftBudget draftBudget = new DraftBudget(period, baseBudget, recurringBudgetItemList);
			draftBudgetList.add(draftBudget);
			balanceByMonthList.add(draftBudget.getBalance());
			balance = balance + draftBudget.getBalance();
		}
		forecast = new Forecast(balanceByMonthList);
		forecast.analyseForecats(initialBalance);
		amountToFitPerMonthList = forecast.getAmountToFitPerMonthList();

		for (CategoryDto category : Categories.getAllCategory()) {
			double sumforThisCategory = 0.0;
			for (DraftBudget draftBudget : draftBudgetList) {
				sumforThisCategory = sumforThisCategory + draftBudget.getAmount(category);

			}
			sumByCategory.put(category, sumforThisCategory);
		}
		System.out.print("\n" + sumByCategory + "\n");
	}

	public double getBalanceByMonth(int month) {
		return balanceByMonthList.get(month - 1);
	}

	public double getAmountToFitPerMonthList(int month) {
		return amountToFitPerMonthList.get(month - 1);
	}

	public void print() {

		for (CategoryDto category : Categories.getAllCategory()) {
			if (sumByCategory.get(category) != 0.0) {
				System.out.print(category + "\t\t\t");
				for (DraftBudget draftBudget : draftBudgetList) {
					System.out.print("\t" + df2.format(draftBudget.getAmount(category)));

				}
				System.out.print("\n");
			}
		}
		System.out.print("AmountToFitPerMonthList \t\t\t");
		for (int month = 1; month < 13; month++) {
			System.out.print(df2.format(getAmountToFitPerMonthList(month)) + "\t");

		}

		System.out.print("\nGloblaBalalnce \t\t\t");
		for (int month = 1; month < 13; month++) {
			System.out.print(df2.format(forecast.getGlobalBalanceList(month)) + "\t");

		}
		System.out.print("\nBalanceByMonth \t\t\t");
		for (int month = 1; month < 13; month++) {
			System.out.print(df2.format(getBalanceByMonth(month)) + "\t");
		}

	}

	public Double getAmount(CategoryDto category, int month) {
		return draftBudgetList.get(month - 1).getAmount(category);
	}

	public List<Double> getGlobalBalanceList() {
		return forecast.getGlobalBalanceList();
	}

	public List<Double> getAmountToFitPerMonthList() {
		return amountToFitPerMonthList;
	}

	public List<Double> getBalanceByMonth() {
		return balanceByMonthList;
	}

}
