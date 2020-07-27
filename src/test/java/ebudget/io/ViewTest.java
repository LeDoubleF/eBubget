package ebudget.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.junit.Test;

import ebudget.data.dto.PeriodDTo;
import ebudget.data.dto.TransactionDto;
import ebudget.testCommon.Common;

public class ViewTest {
	private View view = new View();

	@Test
	public final void testReadInitialBalance() {

		ByteArrayInputStream in = new ByteArrayInputStream("10".getBytes());
		assertEquals(10.0, view.readInitialBalance(in), 0.0);
	}

	@Test
	public final void testReadFilePath() {
		ByteArrayInputStream in = new ByteArrayInputStream("c:\\test".getBytes());
		assertEquals("c:\\test", view.readFilePath(in));
	}

	@Test
	public final void testReadPeriodError() {

		try {
			ByteArrayInputStream in = new ByteArrayInputStream("c:\\test".getBytes());
			view.readPeriod(in);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains("c:\\test"));
		}
	}

	@Test
	public final void testReadPeriod() {
		ByteArrayInputStream in = new ByteArrayInputStream("2020\r8".getBytes());
		assertEquals(new PeriodDTo(2020, 8), view.readPeriod(in));
	}

	@Test
	public final void readTransaction() {
		PeriodDTo periode = new PeriodDTo(2020, 7);
		String absolutePath = Common.getAbsolutePath("test.csv", getClass().getClassLoader());
		List<TransactionDto> transaction = view.readTransaction(absolutePath, periode);

		TransactionDto t1 = new TransactionDto("31/12/2019", "salaire", "entreprise", "virement", -3000.50, periode);
		TransactionDto t2 = new TransactionDto("02/01/2020", "alimentation", "Oumar", "Espèce", 2.10, periode);
		TransactionDto t3 = new TransactionDto("03/01/2020", "alimentation", "lait", "Espèce", 4.0, periode);

		assertEquals(t1, transaction.get(0));
		assertEquals(t2, transaction.get(1));
		assertEquals(t3, transaction.get(2));
	}
}
