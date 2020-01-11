package test;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import command.Move;
import storage.Directory;
import storage.FileSystem;

public class MoveTest {
	// declare the move command to test
	// declare all the arguments of copy test
	// declare all the
	public List<String> arg, redirection;
	public Directory d, a, b, b1;
	public FileSystem fs;
	public Move mv1, mv2, mv3;

	@Before
	public void setUp() throws Exception {

		fs = FileSystem.getFileSystem();
		d = new Directory("d", fs.getRoot());
		a = new Directory("a", d);
		b = new Directory("b", d);
		b1 = new Directory("b1", b);

	}

	@Test
	public void testExecute() {

		// move b1 to root
		arg = new ArrayList<String>();
		arg.add("d/b/b1");
		arg.add("d");
		redirection = new ArrayList<String>();

		mv1 = new Move(arg, redirection);
		assertTrue(mv1.execute());
		assertTrue(!(d.findChild("b1") == null));
		assertTrue((a.findChild("b1") == null));

		// move b1 to root
		arg = new ArrayList<String>();
		arg.add("d/a");
		arg.add("d/b");
		redirection = new ArrayList<String>();

		mv2 = new Move(arg, redirection);
		assertTrue(mv2.execute());
		assertTrue(!(b.findChild("a") == null));
		assertTrue((d.findChild("a") == null));
	}

	@After
	public void tearDown() throws Exception {
		Field field = (fs.getClass()).getDeclaredField("fs");
		field.setAccessible(true);
		field.set(null, null); // setting the fs parameter in FileSystem to null
	}

}
