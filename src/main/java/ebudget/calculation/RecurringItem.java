package ebudget.calculation;

import java.util.List;
import ebudget.data.Categories;
import ebudget.data.dto.CategoryDto;

/**
 * dépenses ou revenus réccurents
 * 
 * @author ffazer
 *
 */
public class RecurringItem {

	private CategoryDto category;
	private double amount;
	private Boolean mandatory;
	private Boolean variable;
	private List<Boolean> frequenceList;
	private String description;

	public RecurringItem(CategoryDto category, String description, double amount, Boolean mandatory, Boolean variable, List<Boolean> frequenceList) {

		super();
		this.description = description;
		this.category = Categories.isCategory(category) ? category : Categories.getDefaultCategory();
		this.amount = amount;
		this.mandatory = mandatory;
		this.variable = variable;
		this.frequenceList = frequenceList;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isForThisMonth(int month) {
		return frequenceList.get(month - 1);
	}

	public CategoryDto getCategory() {
		return category;
	}

	public double getAmount() {
		return amount;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public Boolean getVariable() {
		return variable;
	}

	public void setVariable(Boolean variable) {
		this.variable = variable;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return category + "," + description + "," + amount + "," + mandatory + "," + variable + "," + frequenceList;
	}
}
