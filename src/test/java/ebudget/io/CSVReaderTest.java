package ebudget.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import ebudget.calculation.RecurringItem;
import ebudget.data.Accounts;
import ebudget.data.Categories;
import ebudget.data.dto.AccountDto;
import ebudget.data.dto.AccountType;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.PaymentType;
import ebudget.data.dto.PeriodDTo;
import ebudget.data.dto.TransactionDto;
import ebudget.exception.Message;

@TestInstance(Lifecycle.PER_CLASS)
class CSVReaderTest {

	CSVReader csvReader;
	ClassLoader classLoader = getClass().getClassLoader();

	private static final String AMOUNT = "amount";
	private static final String CATEGORY = "category";

	private static final CategoryDto DIVERS = new CategoryDto("Divers");
	private static final CategoryDto LOYER = new CategoryDto("Loyer");
	private static final CategoryDto SALAIRE = new CategoryDto("Salaire", true);
	private static final CategoryDto TAXI = new CategoryDto("Taxi");

	private static final AccountDto PORTEFEUILLE = new AccountDto("portefeuille", AccountType.ESPECE, false, 0.0);

	@BeforeAll

	public void setUp() {
		csvReader = new CSVReader();

		Categories.addCategory(SALAIRE);
		Categories.addCategory(LOYER);
		Categories.addCategory(TAXI);
		Categories.addCategory(DIVERS);
		Categories.setDefaultCategory(DIVERS);

		Accounts.addAccount(PORTEFEUILLE);
	}

	@Test
	final void testNoFileToRead() {
		try {
			PeriodDTo periode = new PeriodDTo(2020, 1);
			csvReader.readTransactionFile("noFile", periode);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage()
				.contains(Message.FILE_PATH_KO));
		}
	}

	@Test
	final void testReadRecurringItemFile() {
		File fileNameRecurringItem = new File(classLoader.getResource("recurringLittle.csv")
			.getFile());
		List<RecurringItem> reccuringItemList = csvReader.readRecurringItemFile(fileNameRecurringItem.getAbsolutePath());

		RecurringItem loyer = reccuringItemList.get(0);
		assertEquals(LOYER, loyer.getCategory());
		assertTrue(loyer.isForThisMonth(2));

		RecurringItem taxi = reccuringItemList.get(2);
		assertEquals(TAXI, taxi.getCategory());
		assertFalse(taxi.isForThisMonth(2));
	}

	@Test
	final void testReadRecurringItemFileWithError() {
		try {
			File fileNameRecurringItem = new File(classLoader.getResource("recurringError.csv")
				.getFile());
			csvReader.readRecurringItemFile(fileNameRecurringItem.getAbsolutePath());
			fail("Exception not thrown");
		} catch (

		Exception aExp) {
			assert (aExp.getMessage()
				.contains("File content "));
		}
	}

	@Test
	final void testReadBudgetFileWithError() {
		try {

			String fileName = getResourceAbsolutePath("budgetReferenceError.csv", classLoader);
			csvReader.readBudgetFile(fileName);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage()
				.contains("File content"));
		}
	}

	@Test
	final void testReadFileCsv() {
		for (PaymentType day : PaymentType.values()) {
			System.out.println(day);
		}
		String absolutePath = getResourceAbsolutePath("test.csv", classLoader);
		PeriodDTo periode = null;
		try {
			periode = new PeriodDTo(2020, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<TransactionDto> transaction = csvReader.readTransactionFile(absolutePath, periode);

		TransactionDto t1 = new TransactionDto(LocalDate.of(2019, Month.DECEMBER,
				31), "salaire", "entreprise", PaymentType.VIREMENT, -3000.50, periode);
		TransactionDto t2 = new TransactionDto(LocalDate.of(2020, Month.JANUARY, 2), "alimentation", "Oumar", PaymentType.ESPECE, 2.10, periode);
		TransactionDto t3 = new TransactionDto(LocalDate.of(2020, Month.JANUARY, 3), "alimentation", "lait", PaymentType.ESPECE, 4.0, periode);

		assertEquals(t1, transaction.get(0));
		assertEquals(t2, transaction.get(1));
		assertEquals(t3, transaction.get(2));

	}

	@Test
	final void testReadFileWithAmountEroorCsv() {
		try {
			PeriodDTo periode = new PeriodDTo(2020, 1);
			String absolutePath = getResourceAbsolutePath("testAmountFormatError.csv", classLoader);
			csvReader.readTransactionFile(absolutePath, periode);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage()
				.contains(Message.FILE_CONTENT_KO));
		}

	}

	@Test
	final void testReadFileCsvWithoutGoodSeparator() {

		try {
			String absolutePath = getResourceAbsolutePath("testSeparatorError.csv", classLoader);
			PeriodDTo periode = new PeriodDTo(2020, 1);
			csvReader.readTransactionFile(absolutePath, periode);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage()
				.contains(Message.FILE_CONTENT_KO));
		}
	}

	@Test
	final void testverifyFileCsv() {
		// todo code dupliqué
		String absolutePath = getResourceAbsolutePath("test.csv", classLoader);
		csvReader.verifyFile(absolutePath);
		absolutePath = getResourceAbsolutePath("test2.CSV", classLoader);
		csvReader.verifyFile(absolutePath);

	}

	@Test
	final void testverifyFileFileExtensionKO() {
		try {
			String absolutePath = getResourceAbsolutePath("test.jpg", classLoader);
			csvReader.verifyFile(absolutePath);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage()
				.contains(Message.FILE_EXTENSION_KO));
		}
	}

	@Test
	final void testverifyFileMultipleExtensionKO() {
		try {
			String absolutePath = getResourceAbsolutePath("test.csv.csv", classLoader);
			csvReader.verifyFile(absolutePath);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage()
				.contains(Message.FILE_EXTENSION_KO));
		}
	}

	@Test
	final void testverifyFileNoFileKO() {
		try {
			String absolutePath = getResourceAbsolutePath("test.csv", classLoader);
			absolutePath = absolutePath.replace("test", "noFile");
			csvReader.verifyFile(absolutePath);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage()
				.contains(Message.FILE_PATH_KO));
		}
	}

	@Test
	final void testverifyFileIsDirectory() {
		try {
			String absolutePath = getResourceAbsolutePath("test.csv", classLoader);
			absolutePath = absolutePath.replace("\\test.csv", "");
			csvReader.verifyFile(absolutePath);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage()
				.contains(Message.FILE_PATH_KO));
		}
	}

	private String getResourceAbsolutePath(String resourceName, ClassLoader classLoader) {
		File file = new File(classLoader.getResource(resourceName)
			.getFile());
		String absolutePath = file.getAbsolutePath();
		return absolutePath;
	}

	@Test
	final void transactionFileDescriptionTest() {

		Map<Integer, ColumnDescription> columnDescription = csvReader.getTransactionFileDescription();
		Map<Integer, ColumnDescription> transactionFileColumn = new HashMap<>();

		transactionFileColumn.put(0, new ColumnDescription(CVSParameter.DATE, "date"));
		transactionFileColumn.put(1, new ColumnDescription(CVSParameter.STRING, CATEGORY));
		transactionFileColumn.put(2, new ColumnDescription(CVSParameter.STRING, "description"));
		transactionFileColumn.put(3, new ColumnDescription(CVSParameter.STRING, "payment"));
		transactionFileColumn.put(4, new ColumnDescription(CVSParameter.DOUBLE, AMOUNT));
		transactionFileColumn.put(5, new ColumnDescription(CVSParameter.STRING, "account"));
		assertEquals(transactionFileColumn, columnDescription);
	}

	@Test
	final void getBudgetFileDescriptionTest() {
		Map<Integer, ColumnDescription> columnDescription = csvReader.getBudgetFileDescription();

		Map<Integer, ColumnDescription> expectedDescription = new HashMap<>();
		expectedDescription.put(1, new ColumnDescription(CVSParameter.DOUBLE, AMOUNT));
		expectedDescription.put(0, new ColumnDescription(CVSParameter.STRING, CATEGORY));

		System.out.println("Are maps equal (using equals):" + columnDescription.equals(columnDescription));
		System.out.println("Map1:" + expectedDescription.toString());
		System.out.println("Map2:" + columnDescription.toString());
		assertEquals(expectedDescription, columnDescription);

	}

	@Test
	final void getRecurringItemFileDescriptionTest() {
		Map<Integer, ColumnDescription> columnDescription = new HashMap<>();
		columnDescription.put(0, new ColumnDescription(CVSParameter.STRING, CATEGORY));
		columnDescription.put(1, new ColumnDescription(CVSParameter.STRING, "description"));
		columnDescription.put(2, new ColumnDescription(CVSParameter.BOOLEAN, "mandatory"));
		columnDescription.put(3, new ColumnDescription(CVSParameter.BOOLEAN, "variable"));
		columnDescription.put(4, new ColumnDescription(CVSParameter.DOUBLE, AMOUNT));
		// Month
		columnDescription.put(5, new ColumnDescription(CVSParameter.BOOLEAN, "1"));
		columnDescription.put(6, new ColumnDescription(CVSParameter.BOOLEAN, "2"));
		columnDescription.put(7, new ColumnDescription(CVSParameter.BOOLEAN, "3"));
		columnDescription.put(8, new ColumnDescription(CVSParameter.BOOLEAN, "4"));
		columnDescription.put(9, new ColumnDescription(CVSParameter.BOOLEAN, "5"));
		columnDescription.put(10, new ColumnDescription(CVSParameter.BOOLEAN, "6"));
		columnDescription.put(11, new ColumnDescription(CVSParameter.BOOLEAN, "7"));
		columnDescription.put(12, new ColumnDescription(CVSParameter.BOOLEAN, "8"));
		columnDescription.put(13, new ColumnDescription(CVSParameter.BOOLEAN, "9"));
		columnDescription.put(14, new ColumnDescription(CVSParameter.BOOLEAN, "10"));
		columnDescription.put(15, new ColumnDescription(CVSParameter.BOOLEAN, "11"));
		columnDescription.put(16, new ColumnDescription(CVSParameter.BOOLEAN, "12"));

		Map<Integer, ColumnDescription> column = csvReader.getRecurringItemFileDescription();

		assertEquals(columnDescription, column);
	}

	@Test
	final void parameterNotExistTest() {
		String parameter = "toto";

		Assertions.assertThrows(MissingResourceException.class, () -> {
			csvReader.getColumnNumber(parameter);
		});
	}

	@Test
	final void testTransactionFile() {
		PeriodDTo periode = new PeriodDTo(2021, 1);
		File fileNameTansaction = new File(classLoader.getResource("transaction.csv")
			.getFile());
		List<TransactionDto> transactionList = csvReader.readTransactionFile(fileNameTansaction.getAbsolutePath(), periode);

		TransactionDto firstTransaction = transactionList.get(0);
		// 16/01/2020;Alimentation;Marché;Espèce;-17,5;portefeuille

		assertEquals(LocalDate.of(2020, Month.JANUARY, 16), firstTransaction.getDate());
		assertEquals(new CategoryDto("Alimentation"), firstTransaction.getCategory());
		assertEquals("Marché", firstTransaction.getDescription());
		assertEquals(PaymentType.ESPECE, firstTransaction.getPayment());
		assertEquals(-17.5, firstTransaction.getAmount());
		assertEquals(PORTEFEUILLE, firstTransaction.getAccount());
		assertEquals(periode, firstTransaction.getPeriod());

	}

	@Test
	final void ConvertStringToPayment() {

		assertEquals(PaymentType.CB, csvReader.convertStringToPayment("cb"));

		assertEquals(PaymentType.ESPECE, csvReader.convertStringToPayment("espèce"));
		assertEquals(PaymentType.ESPECE, csvReader.convertStringToPayment("espéce"));
		assertEquals(PaymentType.ESPECE, csvReader.convertStringToPayment("Espèce"));

		assertEquals(PaymentType.VIREMENT, csvReader.convertStringToPayment("virement"));
		assertEquals(PaymentType.RETRAIT, csvReader.convertStringToPayment("retrait"));
		assertEquals(PaymentType.CHEQUE, csvReader.convertStringToPayment("chèque"));
		assertEquals(PaymentType.INCONNU, csvReader.convertStringToPayment("rrrr"));

	}
}
