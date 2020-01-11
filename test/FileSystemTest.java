package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import exception.IllegalPathnameException;
import exception.InvalidPathException;
import storage.Directory;
import storage.File;
import storage.FileSystem;
import storage.JFileSystem;

public class FileSystemTest {
  private FileSystem fs;
  private static Directory d1, d2, d11;
  private static File<String> f1, f2;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  @AfterClass
  public static void tearDownAfterClass() throws Exception {

  }

  @Before
  public void setUp() throws Exception {
    fs = FileSystem.getFileSystem();
    d1 = new Directory("d1", fs.getRoot());
    fs.setCurrWorkDir(d1);
    d2 = new Directory("d2", fs.getRoot());
    d11 = new Directory("d1-1", d1);
    f1 = new File<String>("f1", fs.getRoot());
    f2 = new File<String>("f2", d1);
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fs.getClass()).getDeclaredField("fs");
    field.setAccessible(true);
    field.set(null, null); // setting the fs parameter in FileSystem to null
  }

  @Test
  public void testGetFileSystem() {
    FileSystem fs1 = JFileSystem.getFileSystem();
    assertTrue(fs1 == fs);
  }

  @Test
  public void testGetRoot() {
    Directory root = fs.getRoot();
    Directory root2 = fs.getRoot();
    assertTrue(root == root2);
    assertEquals("", root.getName());
    assertEquals("/", root.getPath());
    assertEquals(null, root.getParent());
  }

  @Test(expected = InvalidPathException.class)
  public void testSearchByPath1() throws InvalidPathException {
    // path that doesn't exist
    fs.searchByPath("a/b/c/d");
  }

  @Test
  public void testSearchByPath2() throws InvalidPathException {
    // valid relative path
    assertEquals(fs.searchByPath("d1-1"), d11);
  }

  @Test
  public void testSearchByPath3() throws InvalidPathException {
    // valid full path
    assertEquals(fs.searchByPath("/d2"), d2);
  }

  @Test
  public void testSearchByPath4() throws InvalidPathException {
    // valid relative path for file
    assertEquals(fs.searchByPath("f2"), f2);
  }

  @Test
  public void testSearchByPath5() throws InvalidPathException {
    // valid full path for file
    assertEquals(fs.searchByPath("/f1"), f1);
  }

  @Test(expected = InvalidPathException.class)
  public void testSearchFileByPath1() throws InvalidPathException {
    // search for a dir
    fs.searchFileByPath("d1-1");
  }

  @Test
  public void testSearchFileByPath2() throws InvalidPathException {
    // valid relative path
    assertEquals(fs.searchFileByPath("f2"), f2);
  }

  @Test
  public void testSearchFileByPath3() throws InvalidPathException {
    // valid full path
    assertEquals(fs.searchFileByPath("/f1"), f1);
  }

  @Test(expected = InvalidPathException.class)
  public void testSearchDirByPath1() throws InvalidPathException {
    // search for a file
    fs.searchDirByPath("f2");
  }

  @Test
  public void testSearchDirByPath2() throws InvalidPathException {
    // valid relative path
    assertEquals(fs.searchDirByPath("d1-1"), d11);
  }

  @Test
  public void testSearchDirByPath3() throws InvalidPathException {
    // valid full path
    assertEquals(fs.searchDirByPath("/d2"), d2);
  }

  @Test
  public void testSearchDirByPath4() throws InvalidPathException {
    // "." that should get the current dir
    assertEquals(fs.searchDirByPath("."), fs.getCurrWorkDir());
  }

  @Test
  public void testSearchDirByPath5() throws InvalidPathException {
    // ".." that should get the parent dir
    assertEquals(fs.searchDirByPath(".."), fs.getRoot());
  }

  @Test
  public void testAttach1()
      throws InvalidPathException, IllegalPathnameException {
    // move a file from a dir to another dir
    assertEquals(f2, fs.attach("/d1/d1-1", f2));
    assertEquals(d11, f2.getParent());
    assertEquals(null, d1.findChild("f2"));
    assertEquals(f2, d11.findChild("f2"));
  }

  @Test
  public void testAttach2()
      throws InvalidPathException, IllegalPathnameException {
    // move an existing dir with subdir from a dir to another dir
    assertEquals(d1, fs.attach("/d2", d1));
    assertEquals(d2, d1.getParent());
    assertEquals(null, fs.getRoot().findChild("d1"));
    assertEquals(d1, d2.findChild("d1"));
    assertEquals(f2, d1.findChild("f2"));
    assertEquals(d11, d1.findChild("d1-1"));
    assertEquals(d1, f2.getParent());
    assertEquals(d1, d11.getParent());
  }

  @Test(expected = IllegalPathnameException.class)
  public void testAttach3()
      throws InvalidPathException, IllegalPathnameException {
    // move object to a dir that already has a another with the same name
    Directory dir = new Directory("d1-1", d2);
    fs.attach("/d1", dir);
  }

  @Test(expected = InvalidPathException.class)
  public void testAttach4()
      throws InvalidPathException, IllegalPathnameException {
    // move object to a file
    fs.attach("f2", d2);
  }

  @Test(expected = InvalidPathException.class)
  public void testAttach5()
      throws InvalidPathException, IllegalPathnameException {
    // move object to an invalid path
    fs.attach("d2", f2);
  }

  @Test
  public void testAddDir1()
      throws IllegalPathnameException, InvalidPathException {
    // valid name with a valid path
    fs.addDir("/d1", "d1-2");
    assertEquals("d1-2", d1.findChild("d1-2").getName());
    assertEquals(d1, d1.findChild("d1-2").getParent());
  }

  @Test(expected = IllegalPathnameException.class)
  public void testAddDir2()
      throws IllegalPathnameException, InvalidPathException {
    // invalid name with a valid path
    fs.addDir("/d1", "f2");
  }

  @Test(expected = IllegalPathnameException.class)
  public void testAddDir3()
      throws IllegalPathnameException, InvalidPathException {
    // invalid name (illegal char) with a valid path
    fs.addDir("/d1", "dir!");
  }

  @Test(expected = InvalidPathException.class)
  public void testAddDir4()
      throws IllegalPathnameException, InvalidPathException {
    // valid name but an invalid path
    fs.addDir("/d1/f2", "dir");
  }

  @Test(expected = InvalidPathException.class)
  public void testAddDir5()
      throws IllegalPathnameException, InvalidPathException {
    // valid name but an invalid path
    fs.addDir("/d1/invalidpathhhhhhh", "dir");
  }
  
  @Test
  public void testAddFile1()
      throws IllegalPathnameException, InvalidPathException {
    // valid name with a valid path
    File<String> f3 = fs.addFile("/d1", "f3", "content123");
    assertEquals(f3, d1.findChild("f3"));
    assertEquals("content123", f3.getContent());
    assertEquals(d1, f3.getParent());
  }

  @Test(expected = IllegalPathnameException.class)
  public void testAddFile2()
      throws IllegalPathnameException, InvalidPathException {
    // invalid name with a valid path
    fs.addFile("/d1", "f2","content123");
  }

  @Test(expected = IllegalPathnameException.class)
  public void testAddFile3()
      throws IllegalPathnameException, InvalidPathException {
    // invalid name (illegal char) with a valid path
    fs.addFile("/d1", "File?","content123");
  }

  @Test(expected = InvalidPathException.class)
  public void testAddFile4()
      throws IllegalPathnameException, InvalidPathException {
    // valid name but an invalid path
    fs.addDir("/f1", "dir");
  }

  @Test(expected = InvalidPathException.class)
  public void testAddFile5()
      throws IllegalPathnameException, InvalidPathException {
    // valid name but an invalid path
    fs.addDir("/not a valid path/abcd", "dir");
  }
  


}
