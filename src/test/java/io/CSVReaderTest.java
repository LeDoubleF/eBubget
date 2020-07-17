package io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import data.dto.PeriodDTo;
import data.dto.TransactionDto;
import exception.Message;
import testCommon.Common;

public class CSVReaderTest {
	CSVReader csvReader = new CSVReader();
	ClassLoader classLoader = getClass().getClassLoader();

	@Test
	public final void testNoFileToRead() {
		try {
			PeriodDTo periode = new PeriodDTo(2020, 1);
			csvReader.readFile("noFile", periode);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.FILE_PATH_KO));
		}
	}

	@Test
	public final void testReadFileCsv() {
		String absolutePath = Common.getAbsolutePath("test.csv", classLoader);
		PeriodDTo periode = null;
		try {
			periode = new PeriodDTo(2020, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<TransactionDto> transaction = csvReader.readFile(absolutePath, periode);

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
			String absolutePath = Common.getAbsolutePath("testAmountFormatError.csv", classLoader);
			csvReader.readFile(absolutePath, periode);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			System.out.println(aExp.getMessage());
			assert (aExp.getMessage().contains(Message.FILE_CONTENT_KO));
		}

	}

	@Test
	public final void testReadFileCsvWithoutGoodSeparator() {

		try {
			String absolutePath = Common.getAbsolutePath("testSeparatorError.csv", classLoader);
			PeriodDTo periode = new PeriodDTo(2020, 1);
			csvReader.readFile(absolutePath, periode);
			fail("Exception not thrown");
		} catch (Exception aExp) {

			assert (aExp.getMessage().contains(Message.FILE_CONTENT_KO));
		}
	}

	@Test
	public final void testverifyFileCsv() {
		// todo code dupliqué
		String absolutePath = Common.getAbsolutePath("test.csv", classLoader);
		csvReader.verifyFile(absolutePath);
		absolutePath = Common.getAbsolutePath("test2.CSV", classLoader);
		csvReader.verifyFile(absolutePath);

	}

	@Test
	public final void testverifyFileFileExtensionKO() {
		try {
			String absolutePath = Common.getAbsolutePath("test.jpg", classLoader);
			csvReader.verifyFile(absolutePath);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.FILE_EXTENSION_KO));
		}
	}

	@Test
	public final void testverifyFileMultipleExtensionKO() {
		try {
			String absolutePath = Common.getAbsolutePath("test.csv.csv", classLoader);
			csvReader.verifyFile(absolutePath);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.FILE_EXTENSION_KO));
		}
	}

	@Test
	public final void testverifyFileNoFileKO() {
		try {
			String absolutePath = Common.getAbsolutePath("test.csv", classLoader);
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
			String absolutePath = Common.getAbsolutePath("test.csv", classLoader);
			absolutePath = absolutePath.replace("\\test.csv", "");
			csvReader.verifyFile(absolutePath);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.FILE_PATH_KO));
		}
	}

}
