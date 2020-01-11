package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import command.History;
import inputOrOutput.Input;

/*
 * test class for history test for if the history execute and if the output as
 * wanted
 */
public class HistoryTest {
	// collaborate with the history class that needed to be tested
	History history;
	// set up the mock argument
	ArrayList<String> mockArgument = new ArrayList<String>();
	ArrayList<String> mockRedirectionParam = new ArrayList<String>();

	@Before
	/*
	 * set up the foundation of input storage and add the mock argument to the
	 * history class
	 */
	public void setUp() {
		// add some input to the storage
		Input.storage.add("mock input 1");
		Input.storage.add("mock input 2");
		Input.storage.add("mock input 3");
		Input.storage.add("mock input 4");
		Input.storage.add("mock input 5");

		// set up a new history class
		history = new History(mockArgument, mockRedirectionParam);
	}

	@Test
	/*
	 * test for input arg is an int within the size of input
	 */
	public void testHistoryExecute1() {
		mockArgument.add("2");
		assertEquals(true, history.execute());
	}

	@Test
	/*
	 * test for the empty argument
	 */
	public void testHistoryExecute2() {
		assertEquals(true, history.execute());
	}

	@Test
	/*
	 * test for the argument is not int
	 */
	public void testHistoryExecute3() {
		mockArgument.add("2.4");
		assertEquals(false, history.execute());
	}

	@Test
	/*
	 * test for the input arg is a negative num
	 */
	public void testHistoryExecute4() {
		mockArgument.add("-1");
		assertEquals(false, history.execute());
	}

	@Test
	/*
	 * test for the input arg is beyond the input size
	 */
	public void testHistoryExecute5() {
		mockArgument.add("200");
		assertEquals(true, history.execute());
	}

	@Test
	/*
	 * test for the historyOutput method, it should output the desired result
	 * when we execute this history command
	 */
	public void testHistoryOutput1() {
		mockArgument.add("2");
		assertEquals("4. mock input 4\n5. mock input 5", history.evaluate());
	}

	@Test
	/*
	 * test for the historyOutput method, it should output the desired result
	 * when we execute this history command
	 */
	public void testHistoryOutput2() {
		mockArgument.add("1000");
		System.out.println(mockArgument.size());
		assertEquals(
				"1. mock input 1\n2. mock input 2\n3. mock input 3\n4. mock input 4\n5. mock input 5",
				history.evaluate());
	}

	@After
	/*
	 * clear up the input storage
	 */
	public void cleanUp() {
		Input.storage.clear();
	}
}
