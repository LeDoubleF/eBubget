package ebudget.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
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
	 * Solde d'un budget (les budgets sont mensuels),
	 * 
	 * pour un budget créditeur : balanceByMonthList>0
	 */
	private List<Double> balanceByMonthList = new ArrayList<>();
	private List<DraftBudget> draftBudgetList = new ArrayList<>();// new
	private TransactionSummary[] expenses = new TransactionSummary[12];
	private Map<CategoryDto, Double> sumByCategory = new HashMap<>();
	private Double balance;
	private Forecast forecast;
	private int year;
	private double initialBalance;
	BudgetState[] budgetStateList = new BudgetState[12];
	BaseBudget baseBudget;
	List<RecurringItem> recurringbudgetItemList;
	/**
	 * Montant des ajustement à faire par mois
	 */
	private List<Double> amountToFitPerMonthList = new ArrayList<>();

	public AnnualBudget(int year, double initialBalance, BaseBudget baseBudget, List<RecurringItem> recurringbudgetItemList) {
		df2.setRoundingMode(RoundingMode.UP);
		this.initialBalance = initialBalance;
		this.year = year;
		this.baseBudget = baseBudget;
		this.recurringbudgetItemList = recurringbudgetItemList;
		for (int month = 1; month < 13; month++) {
			budgetStateList[month - 1] = BudgetState.DARFT;
			expenses[month - 1] = new TransactionSummary(new PeriodDTo(year, month));
		}
		computeBudget();
	}

	public void computeBudget() {
		balanceByMonthList = new ArrayList<>();
		draftBudgetList = new ArrayList<>();
		sumByCategory = new HashMap<>();
		amountToFitPerMonthList = new ArrayList<>();
		balance = initialBalance;
		for (int month = 1; month < 13; month++) {
			if (getBudgetState(month).equals(BudgetState.DARFT)) {
				computeDraftBudget(month);
			} else {
				double expenseBalance = expenses[month - 1].getBalance();
				System.out.println("\nouiiiiiiiiii  " + expenseBalance);
				balanceByMonthList.add(expenseBalance);
				balance = balance + expenseBalance;
			}
		}
		analyseForecast(initialBalance);

		sumPerCategory();

		System.out.print("\n" + sumByCategory + "\n");
	}

	private void computeDraftBudget(int month) {
		PeriodDTo period = new PeriodDTo(year, month);
		List<BudgetItem> recurringBudgetItemList = new ArrayList<>();
		Map<CategoryDto, Double> budgetItemMap = new HashMap<>();
		for (RecurringItem recurringItem : recurringbudgetItemList) {
			LOGGER.log(Level.INFO, "recurringItem: {0} {1}  {2}",
					new Object[]{recurringItem.isForThisMonth(month), recurringItem.getCategory(), recurringItem.getAmount()});
			if (recurringItem.isForThisMonth(month)) {
				CategoryDto category = recurringItem.getCategory();
				double amount = recurringItem.getAmount();
				budgetItemMap.compute(category, (k, v) -> (v == null) ? amount : budgetItemMap.get(k) + amount);
			}
		}
		for (Map.Entry<CategoryDto, Double> map : budgetItemMap.entrySet()) {
			LOGGER.log(Level.INFO, "création de la ligne de budget pour la catégory {0} et le montant {1}",
					new Object[]{map.getKey(), map.getValue()});
			BudgetItem budgetItem = new BudgetItem(map.getKey(), map.getValue());
			recurringBudgetItemList.add(budgetItem);
		}

		addDraftBudget(baseBudget, period, recurringBudgetItemList);
	}

	public void setBaseBudget(BaseBudget baseBudget) {
		this.baseBudget = baseBudget;
	}

	public void setRecurringbudgetItemList(List<RecurringItem> recurringbudgetItemList) {
		this.recurringbudgetItemList = recurringbudgetItemList;
	}

	private void addDraftBudget(BaseBudget baseBudget, PeriodDTo period, List<BudgetItem> recurringBudgetItemList) {
		DraftBudget draftBudget = new DraftBudget(period, baseBudget, recurringBudgetItemList);
		draftBudgetList.add(draftBudget);
		balanceByMonthList.add(draftBudget.getBalance());
		balance = balance + draftBudget.getBalance();
	}

	private void sumPerCategory() {
		for (CategoryDto category : Categories.getAllCategory()) {
			double sumforThisCategory = 0.0;
			for (DraftBudget draftBudget : draftBudgetList) {
				sumforThisCategory = sumforThisCategory + draftBudget.getAmount(category);

			}
			sumByCategory.put(category, sumforThisCategory);
		}
	}

	private void analyseForecast(double initialBalance) {
		forecast = new Forecast(balanceByMonthList);
		forecast.analyseForecats(initialBalance);
		amountToFitPerMonthList = forecast.getAmountToFitPerMonthList();
	}

	public void setInitialeBalance(double initialBalance) {
		analyseForecast(initialBalance);
		balance = initialBalance;
		for (Double intermediateBalance : balanceByMonthList) {
			balance = intermediateBalance + balance;
		}
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

		System.out.print("\nGloblaBalance \t\t\t");
		for (int month = 1; month < 13; month++) {
			System.out.print(df2.format(forecast.getGlobalBalanceList(month)) + "\t");

		}
		System.out.print("\nBalanceByMonth \t\t\t");
		for (int month = 1; month < 13; month++) {
			System.out.print(df2.format(getBalanceByMonth(month)) + "\t");
		}

	}

	public double getBalanceByMonth(int month) {
		return balanceByMonthList.get(month - 1);
	}

	public double getAmountToFitPerMonthList(int month) {
		return amountToFitPerMonthList.get(month - 1);
	}

	public Double getAmount(CategoryDto category, int month) {
		return draftBudgetList.get(month - 1)
			.getAmount(category);
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

	public Double getBalance() {
		return balance;
	}

	public void closeBudget(int month) {
		budgetStateList[month - 1] = BudgetState.CLOSED;
		computeBudget();

	}

	public BudgetState getBudgetState(int month) {
		return budgetStateList[month - 1];
	}

	public void setTransaction(int month, CategoryDto category, LocalDate date, String description, String payment, double amount) {
		expenses[month - 1].addTransaction(category, date, description, payment, amount);

	}

}
