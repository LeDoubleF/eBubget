package ebudget.calculation;

import ebudget.data.dto.CategoryDto;

/**
 * Poste de dépense dans un budget réel ou prévisionel
 * 
 * @author ffazer
 *
 */
public class BudgetItem implements Comparable<BudgetItem> {

	private CategoryDto category;
	private double amount;

	/**
	 * par défaut ce sera la catégory divers et un montant à zéro
	 */
	public BudgetItem() {
		super();
		this.category = new CategoryDto("Divers");
		this.amount = 0.0;
	}

	public BudgetItem(CategoryDto category, double amount) {
		super();
		this.category = category;
		this.amount = amount;
	}

	@Override
	public int compareTo(BudgetItem other) {
		return this.category.compareTo(other.category);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BudgetItem other = (BudgetItem) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		return true;
	}

	public CategoryDto getCategory() {
		return category;
	}

	public void setCategory(CategoryDto category) {
		this.category = category;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
