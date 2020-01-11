package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import command.MakeDir;
import command.PrintWorkingDir;
import inputOrOutput.Output;
import inputOrOutput.RemoteDataFetcher;
import storage.DataStorage;
import storage.Directory;
import storage.File;
import storage.FileSystem;

public class PrintWorkingDirTest {
  private PrintWorkingDir pwd;
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
  public void testEvaluate()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // pwd with no argument
    pwd = new PrintWorkingDir(arg, argr);
    //insert mfs into pwd
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(pwd, mfs); // setting the fs parameter in FileSystem to null
    assertEquals("/d1",pwd.evaluate());
  }

  @Test
  public void testEvaluate2()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // pwd with no argument
    pwd = new PrintWorkingDir(arg, argr);
    mfs.setCurrWorkDir(root);
    //insert mfs into pwd
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(pwd, mfs); // setting the fs parameter in FileSystem to null
    assertEquals("/",pwd.evaluate());
  }
  
  @Test
  public void testEvaluate3()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // pwd with one argument
    arg.add("/d2");
    pwd = new PrintWorkingDir(arg, argr);
    mfs.setCurrWorkDir(root);
    //insert mfs into pwd
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(pwd, mfs); // setting the fs parameter in FileSystem to null
    assertEquals(null,pwd.evaluate());
  }
  
  @Test
  public void testEvaluate4()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // pwd with multiple argument
    arg.add("f1");
    arg.add("d1");
    pwd = new PrintWorkingDir(arg, argr);
    mfs.setCurrWorkDir(root);
    //insert mfs into pwd
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(pwd, mfs); // setting the fs parameter in FileSystem to null
    assertEquals(null,pwd.evaluate());
  }
}
