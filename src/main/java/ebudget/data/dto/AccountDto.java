package ebudget.data.dto;

import ebudget.data.dao.AccountEntity;

public class AccountDto {

	private String name;
	private AccountType accountType;
	private Double initialBalance;
	private Double finalBalance;
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
		this.initialBalance = initialAmount;
		this.finalBalance = initialAmount;
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

	public Double getInitialBalance() {
		return initialBalance;
	}

	public Double getFinalBalance() {
		return finalBalance;
	}

	public void setFinalBalance(Double finalBalance) {
		this.finalBalance = finalBalance;

	}

	public void setInitialBalance(Double initialBalance) {
		this.initialBalance = initialBalance;
	}

}