package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import command.MakeDir;
import storage.DataStorage;
import storage.Directory;
import storage.File;
import storage.FileSystem;

public class MakeDirTest {
  private MakeDir mkdir;
  private List<String> arg, argr;
  private FileSystem fs;
  private MockFileSystem mfs;
  private static Directory d1, d2, d11, root;
  private static File<String> f1, f2;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    arg = new ArrayList<>();
    argr = new ArrayList<>();
    fs = FileSystem.getFileSystem();
    root = new Directory("",null);
    d1 = new Directory("d1", root);
    fs.setCurrWorkDir(d1);
    d2 = new Directory("d2", root);
    d11 = new Directory("d1-1", d1);
    f1 = new File<String>("f1", root);
    f2 = new File<String>("f2", d1);
    mfs = new MockFileSystem();
    mfs.d1 = d1;
    mfs.d11 = d11;
    mfs.d2 = d2;
    mfs.f1 = f1;
    mfs.f2 = f2;
    mfs.root = root;
    mfs.cwd = d1;
    
  }
  
  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testExecute()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // mkdir with no argument
    mkdir = new MakeDir(arg, argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(mkdir, mfs); // setting the fs parameter in FileSystem to null
    assertTrue(mkdir.execute() == false);
  }

  @Test
  public void testExecute1() 
      throws NoSuchFieldException, SecurityException, ClassNotFoundException
      , IllegalArgumentException, IllegalAccessException {
    // mkdir with 1 valid path as argument
    arg.add("/d3");
    mkdir = new MakeDir(arg, argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(mkdir, mfs); // setting the fs parameter in FileSystem to null
    
    assertTrue(mkdir.execute() == true);
    Directory d3 = (Directory) root.findChild("d3");
    assertEquals("/d3", d3.getPath());
    assertEquals("d3", d3.getName());
    assertEquals(root, d3.getParent());
    int count = 0;
    for (DataStorage items : root) {
      count++;
    }
    assertEquals(4, count);
  }

  @Test
  public void testExecute2() 
      throws NoSuchFieldException, SecurityException, ClassNotFoundException
      , IllegalArgumentException, IllegalAccessException {
    // mkdir with 1 valid path with multiple level as argument
    arg.add("/d1/d1-2");
    mkdir = new MakeDir(arg, argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(mkdir, mfs); // setting the fs parameter in FileSystem to null
    
    assertTrue(mkdir.execute() == true);
    Directory d12 = (Directory) d1.findChild("d1-2");
    assertEquals("/d1/d1-2", d12.getPath());
    assertEquals("d1-2", d12.getName());
    assertEquals(d1, d12.getParent());
    int count = 0;
    for (DataStorage items : d1) {
      count++;
    }
    assertEquals(3, count);
    
  }
  
  @Test
  public void testExecute3() 
      throws NoSuchFieldException, SecurityException, ClassNotFoundException
      , IllegalArgumentException, IllegalAccessException {
    // mkdir with 1 valid relative path as argument
    arg.add("d1-3");
    mkdir = new MakeDir(arg, argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(mkdir, mfs); // setting the fs parameter in FileSystem to null
    
    assertTrue(mkdir.execute() == true);
    Directory d13 = (Directory) d1.findChild("d1-3");
    assertEquals("/d1/d1-3", d13.getPath());
    assertEquals("d1-3", d13.getName());
    assertEquals(d1, d13.getParent());
    int count = 0;
    for (DataStorage items : d1) {
      count++;
    }
    assertEquals(3, count);
  }

  @Test
  public void testExecute4() 
      throws NoSuchFieldException, SecurityException, ClassNotFoundException
      , IllegalArgumentException, IllegalAccessException {
    // mkdir with invalid (already exist dir) as argument
    arg.add("/d1");
    mkdir = new MakeDir(arg, argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(mkdir, mfs); // setting the fs parameter in FileSystem to null
    
    assertTrue(!mkdir.execute());
    int count = 0;
    for (DataStorage items : root) {
      count++;
    }
    assertEquals(3, count);
  }
  
  @Test
  public void testExecute5()
      throws NoSuchFieldException, SecurityException, ClassNotFoundException
      , IllegalArgumentException, IllegalAccessException {
    // mkdir with multiple valid paths as argument
    arg.add("d1-3");
    arg.add("/d1/d1-2");
    arg.add("/d3");
    arg.add("/d2/d2-1");
    mkdir = new MakeDir(arg,argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(mkdir, mfs); // setting the fs parameter in FileSystem to null
    
    assertTrue(mkdir.execute() == true);
    
    Directory d13 = (Directory) d1.findChild("d1-3");
    assertEquals("/d1/d1-3", d13.getPath());
    assertEquals("d1-3", d13.getName());
    assertEquals(d1, d13.getParent());
    
    Directory d12 = (Directory) d1.findChild("d1-2");
    assertEquals("/d1/d1-2", d12.getPath());
    assertEquals("d1-2", d12.getName());
    assertEquals(d1, d12.getParent());
    
    Directory d3 = (Directory) root.findChild("d3");
    assertEquals("/d3", d3.getPath());
    assertEquals("d3", d3.getName());
    assertEquals(root, d3.getParent());
    
    Directory d21 = (Directory) d2.findChild("d2-1");
    assertEquals("/d2/d2-1", d21.getPath());
    assertEquals("d2-1", d21.getName());
    assertEquals(d2, d21.getParent());
    
    int count = 0;
    for (DataStorage items : root) {
      count++;
    }
    assertEquals(4, count);
    count = 0;
    for (DataStorage items : d1) {
      count++;
    }
    assertEquals(4, count);
    count = 0;
    for (DataStorage items : d2) {
      count++;
    }
    assertEquals(1, count);

  }

  @Test
  public void testExecute6() 
      throws NoSuchFieldException, SecurityException, ClassNotFoundException
      , IllegalArgumentException, IllegalAccessException {
    // mkdir with multiple path as argument, some are valid, some aren't
    arg.add("/d3");
    arg.add("/d1/d2/d3");
    arg.add("/d1");
    mkdir = new MakeDir(arg,argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(mkdir, mfs); // setting the fs parameter in FileSystem to null
    
    assertTrue(!mkdir.execute());
    
    Directory d3 = (Directory) root.findChild("d3");
    assertEquals("/d3", d3.getPath());
    assertEquals("d3", d3.getName());
    assertEquals(root, d3.getParent());
    int count = 0;
    for (DataStorage items : d1) {
      count++;
    }
    assertEquals(2, count);
    count = 0;
    for (DataStorage items : d2) {
      count++;
    }
    assertEquals(0, count);
    count = 0;
    for (DataStorage items : root) {
      count++;
    }
    assertEquals(4, count);
    
  }

  @Test
  public void testExecute7() 
      throws NoSuchFieldException, SecurityException, ClassNotFoundException
      , IllegalArgumentException, IllegalAccessException {
    // mkdir with invalid character in filename as argument
    arg.add("/d1/dir?");
    mkdir = new MakeDir(arg, argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(mkdir, mfs); // setting the fs parameter in FileSystem to null
    
    assertTrue(!mkdir.execute());
    int count = 0;
    for (DataStorage items : d1) {
      count++;
    }
    assertEquals(2, count);
  }

}
