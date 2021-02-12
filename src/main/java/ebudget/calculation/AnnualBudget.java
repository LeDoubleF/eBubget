package ebudget.calculation;

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
	/**
	 * Solde d'un budget (les budgets sont mensuels), pour un budget créditeur
	 * ,balanceByMonthList>0
	 */
	private List<Double> balanceByMonthList = new ArrayList<>();
	private List<DraftBudget> draftBudgetList = new ArrayList<>();
	private Double balance;

	public Double getBalance() {
		return balance;
	}

	/**
	 * Montant des ajustement à faire par mois
	 */
	private List<Double> amountToFitPerMonthList = new ArrayList<>();

	public AnnualBudget(int year, double initialBalance, BaseBudget baseBudget, List<RecurringItem> recurringbudgetItemList) {
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
		Forecast forecast = new Forecast();
		forecast.setBalanceByMonth(balanceByMonthList);
		forecast.analyseForecats(initialBalance);
		amountToFitPerMonthList = forecast.getAmountToFitPerMonthList();
	}

	public double getBalanceByMonth(int month) {
		return balanceByMonthList.get(month - 1);
	}

	public double getAmountToFitPerMonthList(int month) {
		return amountToFitPerMonthList.get(month - 1);
	}

	public void print() {
		for (CategoryDto category : Categories.getAllCategory()) {
			System.out.print(category + "\t\t\t");
			for (DraftBudget draftBudget : draftBudgetList) {
				System.out.print("\t" + draftBudget.getAmount(category));
			}
			System.out.print("\n");
		}
		System.out.print("AmountToFitPerMonthList \t");
		for (int month = 1; month < 13; month++) {
			System.out.print(getAmountToFitPerMonthList(month) + "\t");

		}
		System.out.print("\nBalanceByMonth \t");
		for (int month = 1; month < 13; month++) {
			System.out.print(getBalanceByMonth(month) + "\t");
		}

	}

	public Double getAmount(CategoryDto category, int month) {
		return draftBudgetList.get(month - 1).getAmount(category);
	}

}
