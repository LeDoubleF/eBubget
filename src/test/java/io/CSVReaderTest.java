package io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

import data.dto.PeriodDTo;
import data.dto.TransactionDto;
import exception.Message;

public class CSVReaderTest {
	CSVReader csvReader = new CSVReader();

	@Test
	public final void testNoFileToRead() {
		try {
			PeriodDTo periode = new PeriodDTo(2020, 1, 1);
			csvReader.readFile("noFile", periode);

		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.FILE_PATH_KO));
		}
	}

	@Test
	public final void testReadFileCsv() {
		String absolutePath = getAbsolutePath("test.csv");
		PeriodDTo periode = new PeriodDTo(2020, 1, 1);

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
			PeriodDTo periode = new PeriodDTo(2020, 1, 1);
			String absolutePath = getAbsolutePath("testAmountFormatError.csv");
			csvReader.readFile(absolutePath, periode);

		} catch (Exception aExp) {
			assert (aExp.getMessage().contains("NumberFormatException"));
		}

	}

	@Test
	public final void testReadFileCsvWithoutGoodSeparator() {

		try {
			String absolutePath = getAbsolutePath("testSeparatorError.csv");
			PeriodDTo periode = new PeriodDTo(2020, 1, 1);
			csvReader.readFile(absolutePath, periode);
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.FILE_CONTENT_KO));
		}
	}

	@Test
	public final void testverifyFileCsv() {
		// todo code dupliqué
		String absolutePath = getAbsolutePath("test.csv");
		csvReader.verifyFile(absolutePath);
		absolutePath = getAbsolutePath("test2.CSV");
		csvReader.verifyFile(absolutePath);

	}

	@Test
	public final void testverifyFileFileExtensionKO() {
		try {
			String absolutePath = getAbsolutePath("test.jpg");
			csvReader.verifyFile(absolutePath);
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.FILE_EXTENSION_KO));
		}
	}

	@Test
	public final void testverifyFileMultipleExtensionKO() {
		try {
			String absolutePath = getAbsolutePath("test.csv.csv");
			csvReader.verifyFile(absolutePath);
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.FILE_EXTENSION_KO));
		}
	}

	@Test
	public final void testverifyFileNoFileKO() {
		try {
			String absolutePath = getAbsolutePath("test.csv");
			absolutePath = absolutePath.replace("test", "noFile");
			csvReader.verifyFile(absolutePath);
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.FILE_PATH_KO));
		}
	}

	@Test
	public final void testverifyFileIsDirectory() {
		try {
			String absolutePath = getAbsolutePath("test.csv");
			absolutePath = absolutePath.replace("\\test.csv", "");
			csvReader.verifyFile(absolutePath);
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.FILE_PATH_KO));
		}
	}

	private String getAbsolutePath(String resourceName) {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(resourceName).getFile());
		String absolutePath = file.getAbsolutePath();
		return absolutePath;
	}
}
