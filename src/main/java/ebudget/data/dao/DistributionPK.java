package ebudget.data.dao;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class DistributionPK implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@ManyToOne
	@JoinColumn(name = "budget", nullable = false)
	private CategoryEntity budget;

	@ManyToOne
	@JoinColumn(name = "Category", nullable = false)
	private CategoryEntity category;

	public CategoryEntity getBudget() {
		return budget;
	}

	public void setBudget(CategoryEntity budget) {
		this.budget = budget;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

}
