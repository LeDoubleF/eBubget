package ebudget.calculation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ebudget.data.Accounts;
import ebudget.data.Categories;
import ebudget.data.dto.AccountDto;
import ebudget.data.dto.AccountType;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.PaymentType;
import ebudget.data.dto.PeriodDTo;
import ebudget.data.dto.TransactionDto;

class TransactionSummaryTest {

	private static final PeriodDTo PERIOD = new PeriodDTo(2021, 4);
	private static final double PRECISION = 0.01;
	private final CategoryDto DIVERS = new CategoryDto("Divers");
	private final CategoryDto LOYER = new CategoryDto("Loyer");
	private final CategoryDto SALAIRE = new CategoryDto("Salaire", true);
	private final CategoryDto TAXI = new CategoryDto("Taxi");
	private final CategoryDto IMPOT = new CategoryDto("Impot");
	private final LocalDate date = LocalDate.of(2014, Month.APRIL, 8);

	@BeforeEach
	public void setUp() {

		Categories.addCategory(SALAIRE);
		Categories.addCategory(LOYER);
		Categories.addCategory(TAXI);
		Categories.addCategory(DIVERS);
		Categories.addCategory(IMPOT);
		Categories.setDefaultCategory(DIVERS);

	}

	@Test
	void getBalanceTest() {
		TransactionSummary transactionSummary = new TransactionSummary(PERIOD);

		transactionSummary.addTransaction(SALAIRE, date, "description", PaymentType.ESPECE, 400.0);
		transactionSummary.addTransaction(TAXI, date, "description", PaymentType.ESPECE, 100.0);
		transactionSummary.addTransaction(TAXI, date, "description", PaymentType.ESPECE, 100.0);
		transactionSummary.addTransaction(TAXI, date, "description", PaymentType.ESPECE, 100.0);

		assertEquals(100, transactionSummary.getBalance(), PRECISION);
	}

	@Test
	void getCategorySummaryWhenCategoryDontExistTest() {
		TransactionSummary transactionSummary = new TransactionSummary(PERIOD);

		transactionSummary.addTransaction(LOYER, date, "description", PaymentType.ESPECE, 100.0);

		assertEquals(0, transactionSummary.getSummary(TAXI), PRECISION);
	}

	@Test
	void getCategorySummaryTest() {
		TransactionSummary transactionSummary = new TransactionSummary(PERIOD);

		transactionSummary.addTransaction(LOYER, date, "description", PaymentType.ESPECE, 100.0);
		transactionSummary.addTransaction(TAXI, date, "description", PaymentType.ESPECE, 100.0);
		transactionSummary.addTransaction(TAXI, date, "description", PaymentType.ESPECE, 100.0);
		transactionSummary.addTransaction(TAXI, date, "description", PaymentType.ESPECE, 100.0);

		assertEquals(300, transactionSummary.getSummary(TAXI), PRECISION);
	}

	@Test
	void getAccountSummaryTest() {
		TransactionSummary transactionSummary = new TransactionSummary(PERIOD);
		AccountDto LivretA = new AccountDto("pactole", AccountType.EPARGNE, false, 100.0);

		transactionSummary.addTransaction(LOYER, date, "description", PaymentType.ESPECE, 100.0, LivretA);
		transactionSummary.addTransaction(TAXI, date, "description", PaymentType.ESPECE, 100.0);
		transactionSummary.addTransaction(TAXI, date, "description", PaymentType.ESPECE, 100.0);
		transactionSummary.addTransaction(TAXI, date, "description", PaymentType.ESPECE, 100.0);
		transactionSummary.addTransaction(SALAIRE, date, "description", PaymentType.ESPECE, 500.0);

		assertEquals(-100, transactionSummary.getSummary(LivretA), PRECISION);
		assertEquals(200, transactionSummary.getSummaryMainAccount(), PRECISION);
	}

	@Test
	void getSummaryMainAccountIsDefaultAccount() {
		TransactionSummary transactionSummary = new TransactionSummary(PERIOD);

		assertEquals(Accounts.getDefaultAccount(), transactionSummary.getMainAccount());
	}

}
