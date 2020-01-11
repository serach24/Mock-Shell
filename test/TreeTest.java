package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import command.Tree;
import storage.Directory;
import storage.File;
import storage.FileSystem;

public class TreeTest {
  private Tree tree;
  private List<String> arg, argr;
  private static Directory d1, d2, d11, root;
  private static File<String> f1, f2;
  private MockFileSystem mfs;
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  @Before
  public void setUp() throws Exception {
    arg = new ArrayList<>();
    argr = new ArrayList<>();
    root = new Directory("",null);
    mfs = new MockFileSystem();
    mfs.root = root;
    mfs.cwd = root;
  }

  @Test
  public void testEvaluation() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // tree with no argument and only root
    tree = new Tree(arg, argr);
    //insert mfs into tree
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(tree, mfs); // setting the fs parameter in FileSystem to null
    assertEquals("/\n",tree.evaluate() );
  }
  
  @Test
  public void testEvaluation2() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // tree with no argument and multiple level in file system
    d1 = new Directory("d1", root);
    d2 = new Directory("d2", root);
    d11 = new Directory("d1-1", d1);
    f1 = new File<String>("f1", root);
    f2 = new File<String>("f2", d1);
    tree = new Tree(arg, argr);
    //insert mfs into tree
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(tree, mfs); // setting the fs parameter in FileSystem to null
    assertEquals("/\n\td1\n\t\td1-1\n\t\tf2\n"
        + "\td2\n\tf1\n",tree.evaluate() );
  }

  @Test
  public void testEvaluation3() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // tree with single argument
    arg.add("/d1");
    tree = new Tree(arg, argr);
    //insert mfs into tree
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(tree, mfs); // setting the fs parameter in FileSystem to null
    assertEquals(null,tree.evaluate() );
  }
  
  @Test
  public void testEvaluation4() 
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // tree with no argument and multiple level in file system
    d1 = new Directory("d1", root);
    d2 = new Directory("d2", d1);
    d11 = new Directory("d1-1", d1);
    f1 = new File<String>("f1", root);
    f2 = new File<String>("f2", d11);
    tree = new Tree(arg, argr);
    //insert mfs into tree
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(tree, mfs); // setting the fs parameter in FileSystem to null
    assertEquals("/\n\td1\n\t\td2\n\t\td1-1\n"
        + "\t\t\tf2\n\tf1\n",tree.evaluate() );
  }
}
