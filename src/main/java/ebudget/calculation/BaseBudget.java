package ebudget.calculation;

import java.util.Map;
import ebudget.data.dto.CategoryDto;

/**
 * Budget de référence
 * 
 * @author ffazer
 *
 */
public class BaseBudget extends Budget {

	public BaseBudget() {
		super();
	}

	public BaseBudget(Map<CategoryDto, Double> budgetItemList) {
		this.budgetItemList = budgetItemList;
		sumBalance();
	}

	public void add(CategoryDto category, Double amount) {
		budgetItemList.put(category, amount);
	}

}
