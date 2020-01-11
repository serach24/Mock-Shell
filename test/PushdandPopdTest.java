package test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import command.Popd;
import command.Pushd;
import storage.Directory;
import storage.FileSystem;

/*
 * test class for pushd and popd test for if the pushd and popd executed as
 * wanted
 */
public class PushdandPopdTest {
	// collaborate with the tesing class
	public Pushd push1, push2, push3;
	public Popd pop;
	public List<String> pushArg, popArg, redirection;
	public Directory a, b, c, d, e, f;
	public FileSystem fs;

	@Before
	public void setUp() {
		fs = FileSystem.getFileSystem();
		a = new Directory("a", fs.getRoot());
		b = new Directory("b", a);
		c = new Directory("c", a);
		d = new Directory("d", b);
		e = new Directory("e", c);
		f = new Directory("f", e);
	}

	@Test
	/*
	 * test for pushing two items and pop the most recently added one
	 */
	public void ExecuteTest1() {
		popArg = new ArrayList<String>();
		redirection = new ArrayList<String>();

		pushArg = new ArrayList<String>();
		pushArg.add("/a/c/e");
		push1 = new Pushd(pushArg, redirection);

		assertTrue(push1.execute());
		assertTrue((fs.getCurrWorkDir() == e));

		pushArg = new ArrayList<String>();
		pushArg.add("/a/b/d");
		push1 = new Pushd(pushArg, redirection);
		assertTrue(push1.execute());
		assertTrue((fs.getCurrWorkDir() == d));

		pop = new Popd(popArg, redirection);
		assertTrue((fs.getCurrWorkDir() == d));
	}

}
