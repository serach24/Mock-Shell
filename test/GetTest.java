package test;

import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import command.Get;
import inputOrOutput.RemoteDataFetcher;
import storage.DataStorage;
import storage.Directory;
import storage.File;
import storage.FileSystem;

public class GetTest {
  private Get get;
  private RemoteDataFetcher fetcher;
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
    f1 = new File<String>("f1", root);
    f2 = new File<String>("f2", d1);
    mfs = new MockFileSystem();
    fetcher = new MockFetcher();
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
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // get with no argument
    get = new Get(arg, argr);
    //insert mfs into get
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(get, mfs); // setting the fs parameter in FileSystem to null
  //insert fetcher into get
    Field field2 = Class.forName("command.Get").getDeclaredField("fetcher");
    field2.setAccessible(true);
    field2.set(get, fetcher); // setting the fs parameter in FileSystem to null
    
    assertTrue(!get.execute());
  }

  @Test
  public void testExecute2()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // get with multiple argument
    arg.add("www.test.ca/abc/123.txt");
    arg.add("www.test.ca/234.html");
    get = new Get(arg, argr);
    //insert mfs into get
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(get, mfs); // setting the fs parameter in FileSystem to null
  //insert fetcher into get
    Field field2 = Class.forName("command.Get").getDeclaredField("fetcher");
    field2.setAccessible(true);
    field2.set(get, fetcher); // setting the fs parameter in FileSystem to null
    
    assertTrue(!get.execute());
    int count = 0;
    for (DataStorage item: d1) {
      count++;
    }
    assertEquals(2,count);
  }
  
  @Test
  public void testExecute3()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException {
    // get with an incorrect argument
    arg.add("incorrect.txt");
    get = new Get(arg, argr);
    //insert mfs into get
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(get, mfs); // setting the fs parameter in FileSystem to null
  //insert fetcher into get
    Field field2 = Class.forName("command.Get").getDeclaredField("fetcher");
    field2.setAccessible(true);
    field2.set(get, fetcher); // setting the fs parameter in FileSystem to null
    
    assertTrue(!get.execute());
    int count = 0;
    for (DataStorage item: d1) {
      count++;
    }
    assertEquals(2,count);
  }
  
  @Test
  public void testExecute4()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException, MalformedURLException
      , IOException {
    // get with an correct argument
    arg.add("www.test.ca/abc/123.txt");
    get = new Get(arg, argr);
    //insert mfs into get
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(get, mfs); // setting the fs parameter in FileSystem to null
  //insert fetcher into get
    Field field2 = Class.forName("command.Get").getDeclaredField("fetcher");
    field2.setAccessible(true);
    field2.set(get, fetcher); // setting the fs parameter in FileSystem to null
    
    assertTrue(get.execute());
    File f = (File) d1.findChild("123");
    assertEquals("/d1/123", f.getPath());
    assertEquals("123", f.getName());
    assertEquals(d1, f.getParent());
    assertEquals(fetcher.getURLContent("www.test.ca/abc/123.txt"), f.getContent());
    int count = 0;
    for (DataStorage items : d1) {
      count++;
    }
    assertEquals(3, count);
  }
  
  @Test
  public void testExecute5()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException, MalformedURLException
      , IOException {
    // get with an correct argument but there are already file with the same name
    arg.add("www.test.ca/f2.html");
    get = new Get(arg, argr);
    //insert mfs into get
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(get, mfs); // setting the fs parameter in FileSystem to null
  //insert fetcher into get
    Field field2 = Class.forName("command.Get").getDeclaredField("fetcher");
    field2.setAccessible(true);
    field2.set(get, fetcher); // setting the fs parameter in FileSystem to null
    
    assertTrue(!get.execute());
    int count = 0;
    for (DataStorage items : d1) {
      count++;
    }
    assertEquals(2, count);
  }
  
  @Test
  public void testExecute6()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException
      , IllegalAccessException, ClassNotFoundException, MalformedURLException
      , IOException {
    // get with an correct argument but there are already file with the same name
    arg.add("www.test.ca/illegaChar!.txt");
    get = new Get(arg, argr);
    //insert mfs into get
    Field field = Class.forName("command.Command").getDeclaredField("fs");
    field.setAccessible(true);
    field.set(get, mfs); // setting the fs parameter in FileSystem to null
  //insert fetcher into get
    Field field2 = Class.forName("command.Get").getDeclaredField("fetcher");
    field2.setAccessible(true);
    field2.set(get, fetcher); // setting the fs parameter in FileSystem to null
    
    assertTrue(!get.execute());
    int count = 0;
    for (DataStorage items : d1) {
      count++;
    }
    assertEquals(2, count);
  }
  
}
