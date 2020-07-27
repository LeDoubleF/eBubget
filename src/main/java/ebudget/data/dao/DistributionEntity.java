package ebudget.data.dao;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Distribution")
public class DistributionEntity implements Serializable {
	private static final long serialVersionUID = -6374291052868881648L;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@EmbeddedId
	private DistributionPK id;

	@Column(name = "expectedExpense", unique = false, nullable = false)
	private Double expectedExpense;

	@Column(name = "expense", unique = false, nullable = false)
	private Double expense;

	@Column(name = "availableAmount", unique = false, nullable = false)
	private Double availableAmount;

	public Double getExpectedExpense() {
		return expectedExpense;
	}

	public void setExpectedExpense(Double expectedExpense) {
		this.expectedExpense = expectedExpense;
	}

	public Double getExpense() {
		return expense;
	}

	public void setExpense(Double expense) {
		this.expense = expense;
	}

	public Double getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(Double availableAmount) {
		this.availableAmount = availableAmount;
	}

}
