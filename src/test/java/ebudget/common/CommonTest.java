package ebudget.common;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import ebudget.data.Categories;
import ebudget.exception.Message;

public class CommonTest {

	public static List<Boolean> monthly = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true);
	public static final Integer NBCategories = 39;

	@Test
	void testMessageConstructorThrowError() {
		try {
			Message msg = new Message();
			fail("should throw exception");
		} catch (IllegalStateException aExp) {
			assertTrue(aExp.getMessage().contains("Utility class"));
		}
	}
}
