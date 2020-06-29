package io;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class ViewTest {
	private View view = new View();

	@Test
	public final void testReadInitialBalance() {

		ByteArrayInputStream in = new ByteArrayInputStream("10".getBytes());
		assertEquals(10, view.readInitialBalance(in), 0);
	}

	@Test
	public final void testReadFilePath() {
		ByteArrayInputStream in = new ByteArrayInputStream("c:\\test".getBytes());
		assertEquals("c:\\test", view.readFilePath(in));
	}

}
