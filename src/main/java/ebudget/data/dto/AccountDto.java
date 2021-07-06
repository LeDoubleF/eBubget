package ebudget.data.dto;

public class AccountDto {

	private String name;
	private AccountType accountType;
	private Double initialAmount;
	private Double finalAmount;
	boolean isMain;

	/**
	 * 
	 * @param string
	 * @param cpp
	 * @param string2
	 * @param initialBalance
	 * @param finalBalance
	 */
	public AccountDto(String name, AccountType accountType, boolean isMain, Double initialAmount) {
		super();
		this.name = name;
		this.accountType = accountType;
		this.initialAmount = initialAmount;
		this.finalAmount = initialAmount;
		this.isMain = isMain;
	}

	public boolean isMain() {
		return isMain;
	}

	public void setMain(boolean isMain) {
		this.isMain = isMain;
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