package data.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.logging.Logger;

import org.junit.Test;

import exception.Message;

public class PeriodeDtoTest {
	static Logger logger = Logger.getLogger("PeriodeDtoTest");

	@Test
	public void testEqualsPeriode() {
		PeriodDTo periode1;
		try {
			periode1 = new PeriodDTo(2020, 3);
			PeriodDTo periode2 = new PeriodDTo(2020, 3);
			PeriodDTo periode3 = null;
			PeriodDTo periode4 = new PeriodDTo(2020, 1);

			assertEquals(periode1, periode2);
			assertEquals(periode1, periode1);
			assertEquals(null, periode3);
			assertNotEquals(periode1, periode3);
			assertNotEquals(periode1, periode4);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testTrimestrePeriode() {
		try {
			PeriodDTo periode1 = new PeriodDTo(2020, 3);
			PeriodDTo periode2 = new PeriodDTo(2020, 4);
			PeriodDTo periode3 = new PeriodDTo(2020, 6);
			PeriodDTo periode4 = new PeriodDTo(2020, 7);
			PeriodDTo periode5 = new PeriodDTo(2020, 8);
			PeriodDTo periode6 = new PeriodDTo(2020, 11);

			assertEquals(1, periode1.getQuarter());
			assertEquals(2, periode2.getQuarter());
			assertEquals(2, periode3.getQuarter());
			assertEquals(3, periode4.getQuarter());
			assertEquals(3, periode5.getQuarter());
			assertEquals(4, periode6.getQuarter());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			PeriodDTo periode7 = new PeriodDTo(2020, 14);
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.INVALD_MONTH));
		}
	}
}