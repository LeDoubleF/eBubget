package ebudget.calculation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.PeriodDTo;
import ebudget.data.dto.TransactionDto;

public class TransactionSummary {

	protected static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private PeriodDTo period;

	public TransactionSummary(PeriodDTo period) {
		super();
		this.period = period;
	}

	private Map<CategoryDto, Double> summary = new HashMap<>();
	private List<TransactionDto> transactionList = new ArrayList<>();
	protected double balance = 0.0;

	public void addTransaction(CategoryDto category, LocalDate date, String description, String payment, double amount) {
		transactionList.add(new TransactionDto(date, category, description, payment, amount, period));
		if (Boolean.TRUE.equals(category.isIncome())) {
			balance = balance + amount;
		} else {
			balance = balance - amount;
		}

	}

	public PeriodDTo getPeriod() {
		return period;
	}

	public double getBalance() {
		return balance;
	}

}
