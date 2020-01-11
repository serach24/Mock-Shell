package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import command.Find;
import storage.Directory;
import storage.File;
import storage.FileSystem;

public class FindTest {
  private Find find;
  private List<String> arg, argr;
  private FileSystem fs;
  private MockFileSystem mfs;
  private static Directory d1, d2, d11, root;
  private static File<String> f1, f2;
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

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
    f1 = new File<String>("f1", "contentf1", root);
    f2 = new File<String>("f2","contentf2",d1);
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
  public void testEvaluate() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // find with no argument
    find = new Find(arg, argr);
    //insert mfs into find
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(find, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("",find.evaluate());
  }
  @Test
  public void testEvaluate2() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // find with incorrect type parameter
    arg.add("/d1");//path
    arg.add("-type");//type
    arg.add("a");//f|d
    arg.add("-name");//-name
    arg.add("\"d1-1\"");//expression
    find = new Find(arg, argr);
    //insert mfs into find
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(find, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("",find.evaluate());
  }
  
  @Test
  public void testEvaluate3() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // find with incorrect name parameter
    arg.add("/d1");//path
    arg.add("-type");//type
    arg.add("f");//f|d
    arg.add("-name");//-name
    arg.add("d1-1");//expression
    find = new Find(arg, argr);
    //insert mfs into find
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(find, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("",find.evaluate());
  }
  
  @Test
  public void testEvaluate4() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // find with incorrect parameter
    arg.add("/d1");//path
    arg.add("-typ");//type
    arg.add("d");//f|d
    arg.add("-name");//-name
    arg.add("\"d1-1\"");//expression
    find = new Find(arg, argr);
    //insert mfs into find
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(find, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("",find.evaluate());
  }
  
  @Test
  public void testEvaluate5() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // find with incorrect parameter
    arg.add("/d1");//path
    arg.add("-name");//type
    arg.add("\"d1\"");//f|d
    arg.add("-type");//-name
    arg.add("d");//expression
    find = new Find(arg, argr);
    //insert mfs into find
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(find, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("",find.evaluate());
  }
  
  @Test
  public void testEvaluate6() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // find with correct parameter and searching for file that is not under this path
    arg.add("/d1");//path
    arg.add("-type");//type
    arg.add("f");//f|d
    arg.add("-name");//-name
    arg.add("\"f1\"");//expression
    find = new Find(arg, argr);
    //insert mfs into find
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(find, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("",find.evaluate());
  }
  
  @Test
  public void testEvaluate7() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    new File<String>("f2", d11);
    // find with correct parameter and searching for file
    arg.add("/d1");//path
    arg.add("-type");//type
    arg.add("f");//f|d
    arg.add("-name");//-name
    arg.add("\"f2\"");//expression
    find = new Find(arg, argr);
    //insert mfs into find
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(find, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("/d1/f2\n/d1/d1-1/f2\n",find.evaluate());
  }
  
  @Test
  public void testEvaluate8() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // find with correct parameter and searching for single file
    arg.add("/d1");//path
    arg.add("-type");//type
    arg.add("f");//f|d
    arg.add("-name");//-name
    arg.add("\"f2\"");//expression
    find = new Find(arg, argr);
    //insert mfs into find
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(find, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("/d1/f2\n",find.evaluate());
  }
  
  @Test
  public void testEvaluate9() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // find with multiple correct parameter and searching for directories
    new Directory("d2",d1);
    new Directory("d2",d2);
    new Directory("d2",d11);
    arg.add("/d1");//path
    arg.add("/d2");
    arg.add("-type");//type
    arg.add("d");//f|d
    arg.add("-name");//-name
    arg.add("\"d2\"");//expression
    find = new Find(arg, argr);
    //insert mfs into find
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(find, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("/d1/d2\n/d1/d1-1/d2\n/d2/d2\n",find.evaluate());
  }
  
  @Test
  public void testEvaluate10() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // find with multiple correct parameter and searching for directories
    new Directory("d2",d1);
    new Directory("d2",d2);
    new Directory("d2",d11);
    arg.add("/d1");//path
    arg.add("-type");//type
    arg.add("d");//f|d
    arg.add("-name");//-name
    arg.add("\"d2\"");//expression
    find = new Find(arg, argr);
    //insert mfs into find
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(find, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("/d1/d2\n/d1/d1-1/d2\n",find.evaluate());
  }
  
  @Test
  public void testEvaluate11() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // find with multiple correct and incorrect paths
    new Directory("d2",d1);
    new Directory("d2",d2);
    new Directory("d2",d11);
    arg.add("d2");//path
    arg.add("/d1");//path
    arg.add("/incorrect");//path
    arg.add("-type");//type
    arg.add("d");//f|d
    arg.add("-name");//-name
    arg.add("\"d2\"");//expression
    find = new Find(arg, argr);
    //insert mfs into find
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(find, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("/d1/d2\n/d1/d1-1/d2\n",find.evaluate());
  }
  
  @Test
  public void testEvaluate12() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // find with multiple all incorrect paths
    arg.add("d2");//path
    arg.add("/d1/d1-1/d123");//path
    arg.add("/incorrect");//path
    arg.add("-type");//type
    arg.add("d");//f|d
    arg.add("-name");//-name
    arg.add("\"d2\"");//expression
    find = new Find(arg, argr);
    //insert mfs into find
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(find, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("",find.evaluate());
  }
  
}
