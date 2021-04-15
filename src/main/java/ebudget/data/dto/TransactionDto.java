package ebudget.data.dto;

import java.time.LocalDate;

public class TransactionDto {

	public static final AccountDto DEFAULT_ACCOUNT = new AccountDto("cpp", AccountType.CPP, "principal", 0.0);

	private LocalDate date;
	private CategoryDto category;
	private String description;
	private String payment;
	private Double amount;
	private PeriodDTo period;
	private AccountDto account;

	/**
	 * 
	 * @param date
	 * @param category
	 * @param description
	 * @param payment
	 * @param amount
	 * @param period
	 */
	public TransactionDto(LocalDate date, CategoryDto category, String description, String payment, Double amount, PeriodDTo period) {
		super();
		this.date = date;
		this.category = category;
		this.description = description;
		this.payment = payment;
		this.amount = amount;
		this.period = period;
		this.account = TransactionDto.DEFAULT_ACCOUNT;
	}

	/**
	 * 
	 * @param date
	 * @param category
	 * @param description
	 * @param payment
	 * @param amount
	 * @param period
	 */
	public TransactionDto(LocalDate date, String category, String description, String payment, Double amount, PeriodDTo period) {
		this(date, new CategoryDto(category.toLowerCase()), description, payment, amount, period);

	}

	public TransactionDto(LocalDate date, CategoryDto category, String description, String payment, Double amount, PeriodDTo period,
			AccountDto account) {
		super();
		this.date = date;
		this.category = category;
		this.description = description;
		this.payment = payment;
		this.amount = amount;
		this.period = period;
		this.account = account;
	}

	public PeriodDTo getPeriod() {
		return period;
	}

	public void setPeriod(PeriodDTo period) {
		this.period = period;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public CategoryDto getCategory() {
		return category;
	}

	public void setCategory(CategoryDto category) {
		this.category = category;
	}

	public AccountDto getAccount() {
		return account;
	}

	public void setAccount(AccountDto account) {
		this.account = account;
	}

	public boolean equals(Object o) {
		if (!(o instanceof TransactionDto))
			return false;

		TransactionDto transactiondto = (TransactionDto) o;

		return date.equals(transactiondto.date) && category.equals(transactiondto.category) && description.equals(transactiondto.description)
				&& payment.equals(transactiondto.payment) && amount.equals(transactiondto.amount);

	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + date.hashCode();
		hash = hash * 31 + category.hashCode();
		hash = hash * 31 + description.hashCode();
		hash = hash * 31 + payment.hashCode();
		return hash;
	}

}