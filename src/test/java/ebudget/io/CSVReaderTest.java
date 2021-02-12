package ebudget.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.File;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import ebudget.calculation.RecurringItem;
import ebudget.data.Categories;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.PeriodDTo;
import ebudget.data.dto.TransactionDto;
import ebudget.exception.Message;

public class CSVReaderTest {

	CSVReader csvReader = new CSVReader();
	ClassLoader classLoader = getClass().getClassLoader();
	private static final CategoryDto DIVERS = new CategoryDto("Divers");
	private static final CategoryDto LOYER = new CategoryDto("Loyer");
	private static final CategoryDto SALAIRE = new CategoryDto("Salaire", true);
	private static final CategoryDto TAXI = new CategoryDto("Taxi");

	@Before
	public void setUp() {

		Categories.addCategory(SALAIRE);
		Categories.addCategory(LOYER);
		Categories.addCategory(TAXI);
		Categories.addCategory(DIVERS);
		Categories.setDefaultCategory(DIVERS);
	}

	@Test
	public final void testNoFileToRead() {
		try {
			PeriodDTo periode = new PeriodDTo(2020, 1);
			csvReader.readTransactionFile("noFile", periode);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.FILE_PATH_KO));
		}
	}

	@Test
	public final void testReadRecurringItemFile() {
		File fileNameRecurringItem = new File(classLoader.getResource("recurringLittle.csv").getFile());
		List<RecurringItem> reccuringItemList = csvReader.readRecurringItemFile(fileNameRecurringItem.getAbsolutePath());

		RecurringItem loyer = reccuringItemList.get(0);
		assertEquals(LOYER, loyer.getCategory());
		assertTrue(loyer.isForThisMonth(2));

		RecurringItem taxi = reccuringItemList.get(2);
		assertEquals(TAXI, taxi.getCategory());
		assertFalse(taxi.isForThisMonth(2));
	}

	@Test
	public final void testReadRecurringItemFileWithError() {
		try {
			File fileNameRecurringItem = new File(classLoader.getResource("recurringError.csv").getFile());
			csvReader.readRecurringItemFile(fileNameRecurringItem.getAbsolutePath());
			fail("Exception not thrown");
		} catch (

		Exception aExp) {
			assert (aExp.getMessage().contains("File content "));
		}
	}

	@Test
	public final void testReadBudgetFileWithError() {
		try {

			String fileName = getResourceAbsolutePath("budgetReferenceError.csv", classLoader);
			csvReader.readBudgetFile(fileName);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains("File content"));
		}
	}

	@Test
	public final void testReadFileCsv() {
		String absolutePath = getResourceAbsolutePath("test.csv", classLoader);
		PeriodDTo periode = null;
		try {
			periode = new PeriodDTo(2020, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<TransactionDto> transaction = csvReader.readTransactionFile(absolutePath, periode);

		TransactionDto t1 = new TransactionDto("31/12/2019", "salaire", "entreprise", "virement", -3000.50, periode);
		TransactionDto t2 = new TransactionDto("02/01/2020", "alimentation", "Oumar", "Espèce", 2.10, periode);
		TransactionDto t3 = new TransactionDto("03/01/2020", "alimentation", "lait", "Espèce", 4.0, periode);

		assertEquals(t1, transaction.get(0));
		assertEquals(t2, transaction.get(1));
		assertEquals(t3, transaction.get(2));

	}

	@Test
	public final void testReadFileWithAmountEroorCsv() {
		try {
			PeriodDTo periode = new PeriodDTo(2020, 1);
			String absolutePath = getResourceAbsolutePath("testAmountFormatError.csv", classLoader);
			csvReader.readTransactionFile(absolutePath, periode);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			System.out.println(aExp.getMessage());
			assert (aExp.getMessage().contains(Message.FILE_CONTENT_KO));
		}

	}

	@Test
	public final void testReadFileCsvWithoutGoodSeparator() {

		try {
			String absolutePath = getResourceAbsolutePath("testSeparatorError.csv", classLoader);
			PeriodDTo periode = new PeriodDTo(2020, 1);
			csvReader.readTransactionFile(absolutePath, periode);
			fail("Exception not thrown");
		} catch (Exception aExp) {

			assert (aExp.getMessage().contains(Message.FILE_CONTENT_KO));
		}
	}

	@Test
	public final void testverifyFileCsv() {
		// todo code dupliqué
		String absolutePath = getResourceAbsolutePath("test.csv", classLoader);
		csvReader.verifyFile(absolutePath);
		absolutePath = getResourceAbsolutePath("test2.CSV", classLoader);
		csvReader.verifyFile(absolutePath);

	}

	@Test
	public final void testverifyFileFileExtensionKO() {
		try {
			String absolutePath = getResourceAbsolutePath("test.jpg", classLoader);
			csvReader.verifyFile(absolutePath);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.FILE_EXTENSION_KO));
		}
	}

	@Test
	public final void testverifyFileMultipleExtensionKO() {
		try {
			String absolutePath = getResourceAbsolutePath("test.csv.csv", classLoader);
			csvReader.verifyFile(absolutePath);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.FILE_EXTENSION_KO));
		}
	}

	@Test
	public final void testverifyFileNoFileKO() {
		try {
			String absolutePath = getResourceAbsolutePath("test.csv", classLoader);
			absolutePath = absolutePath.replace("test", "noFile");
			csvReader.verifyFile(absolutePath);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.FILE_PATH_KO));
		}
	}

	@Test
	public final void testverifyFileIsDirectory() {
		try {
			String absolutePath = getResourceAbsolutePath("test.csv", classLoader);
			absolutePath = absolutePath.replace("\\test.csv", "");
			csvReader.verifyFile(absolutePath);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.FILE_PATH_KO));
		}
	}

	private String getResourceAbsolutePath(String resourceName, ClassLoader classLoader) {
		File file = new File(classLoader.getResource(resourceName).getFile());
		String absolutePath = file.getAbsolutePath();
		return absolutePath;
	}
}
