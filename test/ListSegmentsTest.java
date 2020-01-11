package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import command.ListSegments;
import command.MakeDir;
import storage.*;

public class ListSegmentsTest {
  private ListSegments ls;
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
  public void testEvaluateR() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // ls with no argument
    arg.add("-R");
    ls = new ListSegments(arg, argr);
    //insert mfs into ls
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(ls, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("d1-1\nf2\n\n/d1/d1-1:\n\n",ls.evaluate());
  }
  @Test
  public void testEvaluate2R() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // ls with 1 valid dir argument
    arg.add("-R");
    arg.add("/d1");
    ls = new ListSegments(arg, argr);
    //insert mfs into ls
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(ls, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("/d1:\nd1-1\nf2\n\n/d1/d1-1:\n\n",ls.evaluate());
  }
  @Test
  public void testEvaluate3R() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // ls with 1 valid file argument
    arg.add("-R");
    arg.add("/d1/f2");
    ls = new ListSegments(arg, argr);
    //insert mfs into ls
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(ls, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("/d1/f2\n",ls.evaluate());
  }
  @Test
  public void testEvaluate4R() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // ls with 1 invalid argument
    arg.add("-R");
    arg.add("/d1/abcd/this_file doesn't_exist");
    ls = new ListSegments(arg, argr);
    //insert mfs into ls
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(ls, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals(null,ls.evaluate());
  }
  @Test
  public void testEvaluate5R() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // multiple valid relative and full path as argument
    arg.add("-R");
    arg.add("/d1");
    arg.add("/d2");
    arg.add("/d1/f2");
    ls = new ListSegments(arg, argr);
    //insert mfs into ls
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(ls, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("/d1:\nd1-1\nf2\n\n/d1/d1-1:\n\n/d2:\n\n/d1/f2\n",ls.evaluate());
  }
  @Test
  public void testEvaluate6R() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // multiple valid and invalid relative and full path as argument
    arg.add("-R");
    arg.add("d1-1");
    arg.add("/d2");
    arg.add("/d1");
    arg.add("invalid_path");
    arg.add("/d1/f2");
    arg.add("/d2/d1-1");
    ls = new ListSegments(arg, argr);
    //insert mfs into ls
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(ls, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("d1-1:\n\n/d2:\n\n/d1:\nd1-1\nf2\n\n/d1/d1-1:\n\n/d1/f2\n",ls.evaluate());
  }
  @Test
  public void testEvaluate7R() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // multiple invalid relative and full path as argument
    arg.add("-R");
    arg.add("d1-1/a/b/d/d");
    arg.add("d2");
    arg.add("/not valid/f2");
    ls = new ListSegments(arg, argr);
    //insert mfs into ls
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(ls, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals(null,ls.evaluate());
  }
  
  @Test
  public void testEvaluate() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // ls with no argument
    ls = new ListSegments(arg, argr);
    //insert mfs into ls
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(ls, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("d1-1\nf2\n\n",ls.evaluate());
  }
  @Test
  public void testEvaluate2() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // ls with 1 valid dir argument
    arg.add("/d1");
    ls = new ListSegments(arg, argr);
    //insert mfs into ls
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(ls, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("/d1:\nd1-1\nf2\n\n",ls.evaluate());
  }
  @Test
  public void testEvaluate3() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // ls with 1 valid file argument
    arg.add("/d1/f2");
    ls = new ListSegments(arg, argr);
    //insert mfs into ls
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(ls, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("/d1/f2\n",ls.evaluate());
  }
  @Test
  public void testEvaluate4() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // ls with 1 invalid argument
    arg.add("/d1/abcd/this_file doesn't_exist");
    ls = new ListSegments(arg, argr);
    //insert mfs into ls
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(ls, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals(null,ls.evaluate());
  }
  @Test
  public void testEvaluate5() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // multiple valid relative and full path as argument
    arg.add("d1-1");
    arg.add("/d2");
    arg.add("/d1/f2");
    ls = new ListSegments(arg, argr);
    //insert mfs into ls
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(ls, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("d1-1:\n\n/d2:\n\n/d1/f2\n",ls.evaluate());
  }
  @Test
  public void testEvaluate6() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // multiple valid and invalid relative and full path as argument
    arg.add("d1-1");
    arg.add("/d2");
    arg.add("invalid_path");
    arg.add("/d1/f2");
    arg.add("/d2/d1-1");
    ls = new ListSegments(arg, argr);
    //insert mfs into ls
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(ls, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals("d1-1:\n\n/d2:\n\n/d1/f2\n",ls.evaluate());
  }
  @Test
  public void testEvaluate7() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // multiple invalid relative and full path as argument
    arg.add("d1-1/a/b/d/d");
    arg.add("d2");
    arg.add("/not valid/f2");
    ls = new ListSegments(arg, argr);
    //insert mfs into ls
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(ls, mfs); // setting the fs parameter in FileSystem to null
    
    assertEquals(null,ls.evaluate());
  }

}
