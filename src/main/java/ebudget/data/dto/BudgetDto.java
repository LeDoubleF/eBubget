package ebudget.data.dto;

import java.util.List;
import ebudget.calculation.DraftBudget;

public class BudgetDto {

	private PeriodDTo periode;
	private List<TransactionDto> transactionList;
	// private List<repartition> repartion;
	double initialBalance;
	double finlBalance;
	double realExpense;
	double forecastExpense;
	boolean open;

}
