package ebudget.calculation;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import ebudget.data.Categories;
import ebudget.data.dto.CategoryDto;

public class Budget {

	protected static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	Map<CategoryDto, Double> budgetItemList = new HashMap<>();
	protected double balance = 0.0;

	/**
	 * retourne le solde de chaque mois, pour un budget créditeur ,balance>0
	 * 
	 * @return balance
	 */
	public double getBalance() {
		return balance;
	}

	public Map<CategoryDto, Double> getBudgetItemList() {
		return budgetItemList;
	}

	public void setBudgetItemList(Map<CategoryDto, Double> budgetItemList) {
		this.budgetItemList = budgetItemList;
		sumBalance();
	}

	protected void sumBalance() {
		balance = 0.0;
		for (Map.Entry<CategoryDto, Double> mapentry : budgetItemList.entrySet()) {
			CategoryDto category = mapentry.getKey();
			Double amount = mapentry.getValue();
			if (category.isIncome()) {
				balance = balance + amount;
			} else {
				balance = balance - amount;
			}
		}
	}

	public double getAmount(CategoryDto category) {
		return budgetItemList.get(category);
	}

	@Override
	public String toString() {
		return budgetItemList.toString();
	}

	public void print() {
		for (Map.Entry<CategoryDto, Double> mapentry : budgetItemList.entrySet()) {
			CategoryDto category = mapentry.getKey();
			Double amount = mapentry.getValue();
			System.out.print("\n" + category.getName() + " " + amount + "\t");
		}

	}
}
