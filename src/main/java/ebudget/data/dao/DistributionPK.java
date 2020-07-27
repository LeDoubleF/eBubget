package ebudget.data.dao;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class DistributionPK implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@ManyToOne
	@JoinColumn(name = "budget", nullable = false)
	private CategoryEntity Budget;

	@ManyToOne
	@JoinColumn(name = "Category", nullable = false)
	private CategoryEntity category;

	public CategoryEntity getBudget() {
		return Budget;
	}

	public void setBudget(CategoryEntity budget) {
		Budget = budget;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

}
