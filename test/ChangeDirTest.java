package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import command.ChangeDir;
import command.MakeDir;
import storage.*;

public class ChangeDirTest {
  private ChangeDir cd;
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

  @Test
  public void testExecute() 
      throws NoSuchFieldException, SecurityException, ClassNotFoundException
      , IllegalArgumentException, IllegalAccessException {
    // cd with no argument
    cd = new ChangeDir(arg, argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(cd, mfs); // setting the fs parameter in FileSystem to null
    assertTrue(!cd.execute());
    assertEquals(d1, mfs.getCurrWorkDir());
  }

  @Test
  public void testExecute2() 
      throws NoSuchFieldException, SecurityException, ClassNotFoundException
      , IllegalArgumentException, IllegalAccessException {
    // cd with a valid relative path as argument
    arg.add("d1-1");
    cd = new ChangeDir(arg, argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(cd, mfs); // setting the fs parameter in FileSystem to null
    
    assertTrue(cd.execute());
    assertEquals(d11, mfs.getCurrWorkDir());
  }

  @Test
  public void testExecute3() 
      throws NoSuchFieldException, SecurityException, ClassNotFoundException
      , IllegalArgumentException, IllegalAccessException {
    // cd with 1 valid full path as argument
    arg.add("/d2");
    cd = new ChangeDir(arg, argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(cd, mfs); // setting the fs parameter in FileSystem to null
    
    assertTrue(cd.execute());
    assertEquals(d2, mfs.getCurrWorkDir());
  }

  @Test
  public void testExecute4() 
      throws NoSuchFieldException, SecurityException, ClassNotFoundException
      , IllegalArgumentException, IllegalAccessException {
    // cd with 1 invalid path as argument
    arg.add("/d1/d3/d2");
    cd = new ChangeDir(arg, argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(cd, mfs); // setting the fs parameter in FileSystem to null
    
    assertTrue(!cd.execute());
    assertEquals(d1, mfs.getCurrWorkDir());
  }

  @Test
  public void testExecute5() 
      throws NoSuchFieldException, SecurityException, ClassNotFoundException
      , IllegalArgumentException, IllegalAccessException {
    // cd with ".." as argument, should point to root
    arg.add("..");
    cd = new ChangeDir(arg, argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(cd, mfs); // setting the fs parameter in FileSystem to null
    
    assertTrue(cd.execute());
    assertEquals(root, mfs.getCurrWorkDir());
  }
  
  @Test
  public void testExecute5_2() 
      throws NoSuchFieldException, SecurityException, ClassNotFoundException
      , IllegalArgumentException, IllegalAccessException {
    // cd with "." as argument, should point to cwd which is d1
    arg.add(".");
    cd = new ChangeDir(arg, argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(cd, mfs); // setting the fs parameter in FileSystem to null
    
    assertTrue(cd.execute());
    assertEquals(d1, mfs.getCurrWorkDir());
  }

  @Test
  public void testExecute6()
      throws NoSuchFieldException, SecurityException, ClassNotFoundException
      , IllegalArgumentException, IllegalAccessException {
    // cd with multiple path as argument
    arg.add("d1/d1-1");
    arg.add("/d1");
    cd = new ChangeDir(arg, argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(cd, mfs); // setting the fs parameter in FileSystem to null
    
    assertTrue(!cd.execute());
    assertEquals(d1, mfs.getCurrWorkDir());
    }
  @Test
  public void testExecute7()
      throws NoSuchFieldException, SecurityException, ClassNotFoundException
      , IllegalArgumentException, IllegalAccessException {
    // cd with file path as argument
    arg.add("/d1/f2");
    cd = new ChangeDir(arg, argr);
    //insert mfs into mkdir
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(cd, mfs); // setting the fs parameter in FileSystem to null
    
    assertTrue(!cd.execute());
    assertEquals(d1, mfs.getCurrWorkDir());
    }

}
