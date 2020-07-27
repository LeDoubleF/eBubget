package ebudget.data.dao;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "Budget", uniqueConstraints = { @UniqueConstraint(columnNames = "ID") })
public class BudgetEntity implements Serializable {
	private static final long serialVersionUID = -9075199373073264717L;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public PeriodEntity getPeriode() {
		return periode;
	}

	public void setPeriode(PeriodEntity periode) {
		this.periode = periode;
	}

	public Double getExpectedIncome() {
		return expectedIncome;
	}

	public void setExpectedIncome(Double expectedIncome) {
		this.expectedIncome = expectedIncome;
	}

	public Double getIncome() {
		return income;
	}

	public void setIncome(Double income) {
		this.income = income;
	}

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

	public Double getExpectedFinalBalance() {
		return expectedFinalBalance;
	}

	public void setExpectedFinalBalance(Double expectedFinalBalance) {
		this.expectedFinalBalance = expectedFinalBalance;
	}

	public Double getFinalBalance() {
		return finalBalance;
	}

	public void setFinalBalance(Double finalBalance) {
		this.finalBalance = finalBalance;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public Integer getBudgetId() {
		return budgetId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer budgetId;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "annee", nullable = false), @JoinColumn(name = "trimestre", nullable = false),
			@JoinColumn(name = "mois", nullable = false) })
	private PeriodEntity periode;

	// TODO comparaison Le temps d’accès champs calculés+ triggers VS requetes
	@Column(name = "expectedIncome", unique = false, nullable = false)
	private Double expectedIncome;

	@Column(name = "income", unique = false, nullable = false)
	private Double income;

	@Column(name = "expectedExpense", unique = false, nullable = false)
	private Double expectedExpense;

	@Column(name = "expense", unique = false, nullable = false)
	private Double expense;

	@Column(name = "expectedFinalBalance", unique = false, nullable = false)
	private Double expectedFinalBalance;

	@Column(name = "finalBalance", unique = false, nullable = false)
	private Double finalBalance;

	@Column(name = "closed", unique = false, nullable = false)
	private boolean closed;

}
