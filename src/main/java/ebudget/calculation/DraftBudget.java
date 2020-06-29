package ebudget.calculation;

import java.util.List;
import java.util.logging.Level;
import ebudget.data.Categories;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.PeriodDTo;

/**
 * Budget prévisionnel pour un mois donné
 * 
 * @author ffazer
 *
 */
public class DraftBudget extends Budget {

	private PeriodDTo period;

	public DraftBudget(PeriodDTo period, BaseBudget baseBudget) {
		initBudget(period, baseBudget);
		sumBalance();

	}

	public DraftBudget(PeriodDTo period, BaseBudget baseBudget, List<BudgetItem> recurringBudgetItemList) {
		initBudget(period, baseBudget);

		for (BudgetItem recurringItem : recurringBudgetItemList) {
			CategoryDto reccurringCategory = recurringItem.getCategory();
			double reccuringAmount = recurringItem.getAmount();
			if (reccuringAmount > budgetItemList.get(reccurringCategory)) {

				budgetItemList.replace(reccurringCategory, reccuringAmount);
				LOGGER.log(Level.INFO, "mise à jour de la catégorie {0} pour un mont de montant :{1}",
						new Object[]{reccurringCategory.getName(), reccuringAmount});
			}
		}
		sumBalance();
	}

	private void initBudget(PeriodDTo period, BaseBudget baseBudget) {
		for (CategoryDto category : Categories.getAllCategory()) {
			budgetItemList.put(category, 0.0);
		}
		budgetItemList.putAll(baseBudget.getBudgetItemList());
		this.period = period;
	}

	public PeriodDTo getPeriod() {
		return period;
	}

	public void setPeriod(PeriodDTo period) {
		this.period = period;
	}

}
