package ebudget.calculation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import ebudget.data.dto.AccountDto;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.PeriodDTo;
import ebudget.data.dto.TransactionDto;

public class TransactionSummary {

	protected static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private PeriodDTo period;
	private Map<CategoryDto, Double> summaryByCategory = new HashMap<>();
	private Map<AccountDto, Double> summaryByAccount = new HashMap<>();
	private List<TransactionDto> transactionList = new ArrayList<>();
	private AccountDto mainAccount;
	protected double balance = 0.0;

	public TransactionSummary(PeriodDTo period) {
		super();
		this.period = period;
		this.mainAccount = TransactionDto.DEFAULT_ACCOUNT;
	}

	public void addTransaction(CategoryDto category, LocalDate date, String description, String payment, double amount) {
		this.addTransaction(category, date, description, payment, amount, mainAccount);
	}

	public void addTransaction(CategoryDto category, LocalDate date, String description, String payment, double amount, AccountDto account) {
		transactionList.add(new TransactionDto(date, category, description, payment, amount, period, account));
		double credit;
		if (Boolean.TRUE.equals(category.isIncome())) {
			credit = amount;
		} else {
			credit = -amount;
		}
		balance = balance + credit;
		summaryByCategory.compute(category, (k, v) -> (v == null) ? amount : summaryByCategory.get(k) + amount);
		summaryByAccount.compute(account, (k, v) -> (v == null) ? credit : summaryByAccount.get(k) + credit);
	}

	public PeriodDTo getPeriod() {
		return period;
	}

	public double getBalance() {
		return balance;
	}

	public Double getSummary(CategoryDto category) {

		return summaryByCategory.containsKey(category) ? summaryByCategory.get(category) : 0.0;
	}

	public Double getSummary(AccountDto account) {
		return summaryByAccount.get(account);
	}

	public Double getSummaryMainAccount() {
		return summaryByAccount.get(mainAccount);
	}

	public AccountDto getMainAccount() {
		return mainAccount;
	}

	public void setMainAccount(AccountDto mainAccount) {
		this.mainAccount = mainAccount;
	}

}
