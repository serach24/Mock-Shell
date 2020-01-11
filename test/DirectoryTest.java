package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import exception.IllegalPathnameException;
import storage.*;

public class DirectoryTest {
  private static Directory root, d1, d2, d11, dir;
  private static File<String> f1, f2;

  @Before
  public void setUp() throws Exception {
    root = new Directory("", null);
    d1 = new Directory("d1", root);
    dir = new Directory("dir", d1);
    d2 = new Directory("d2", root);
    d11 = new Directory("d1-1", d1);
    f1 = new File<String>("f1", root);
    f2 = new File<String>("f2", d1);
  }

  @Test
  public void testAddChild() {
    // add existing child
    assertTrue(dir.addChild(d2));
    assertTrue(dir.findChild("d2") != null);
    assertTrue(root.findChild("d2") == null);
  }

  @Test
  public void testAddChild2() throws IllegalPathnameException {
    // add a new file child
    File<String> f3 = new File<String>("f3", d1);
    assertTrue(dir.addChild(f3));
    assertTrue(dir.findChild("f3") != null);
    assertTrue(d1.findChild("f3") == null);
  }

  @Test
  public void testDeleteChild() {
    // delete existing child
    d1.deleteChild(d11);
    assertTrue(d1.findChild("f11") == null);
  }

  @Test
  public void testDeleteChild2() {
    // delete not existing child
    d1.deleteChild(f1);
    assertTrue(d1.findChild("f1") == null);
  }

  @Test
  public void testFindChild() {
    // find existing child
    assertEquals(d1.findChild("d1-1"), d11);
  }

  @Test
  public void testFindChild2() {
    // find existing file child
    assertEquals(d1.findChild("f2"), f2);
  }

  @Test
  public void testFindChild3() {
    // find not existing child
    assertEquals(d1.findChild("f1"), null);
  }

  @Test
  public void testIterator() {
    String name = "";
    for (DataStorage i : d1) {
      name += i.getName();
    }
    assertEquals("dird1-1f2", name);
  }

  @Test
  public void testIterator2() {
    String name = "";
    for (DataStorage i : dir) {
      name += i.getName();
    }
    assertEquals("", name);
  }

  @Test
  public void testIterator3() throws IllegalPathnameException {
    String name = "";
    new Directory("dir2", dir);
    for (DataStorage i : dir) {
      name += i.getName();
    }
    assertEquals("dir2", name);
  }

}
