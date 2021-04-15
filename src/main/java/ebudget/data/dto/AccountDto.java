package ebudget.data.dto;

public class AccountDto {

	private String name;
	private AccountType accountType;
	private String description;
	private Double initialAmount;
	private Double finalAmount;

	/**
	 * 
	 * @param string
	 * @param cpp
	 * @param string2
	 * @param initialBalance
	 * @param finalBalance
	 */
	public AccountDto(String name, AccountType accountType, String description, Double initialAmount) {
		super();
		this.name = name;
		this.accountType = accountType;
		this.description = description;
		this.initialAmount = initialAmount;
		this.finalAmount = initialAmount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getInitialAmount() {
		return initialAmount;
	}

	public void setInitialAmount(Double initialAmount) {
		this.initialAmount = initialAmount;
	}

	public Double getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(Double finalAmount) {
		this.finalAmount = finalAmount;
	}

}