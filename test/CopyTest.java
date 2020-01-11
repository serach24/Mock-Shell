package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import command.Concatenate;
import command.Copy;
import command.MakeDir;
import storage.DataStorage;
import storage.Directory;
import storage.File;
import storage.FileSystem;

public class CopyTest {
  
  // declare the copy command to test
  // declare all the arguments of copy test
  // declare all the 
  public List<String> arg,redirection;
  public Directory d, a, b, b1;
  public FileSystem fs;
  public Copy cp1, cp2, cp3;


  @Before
  public void setUp() throws Exception {
    
    fs = FileSystem.getFileSystem();
    d = new Directory("d", fs.getRoot());
    a = new Directory("a",d);
    b = new Directory("b", d);
    b1 = new Directory("b1", b);

    
  }
  
  @After
  public void tearDown() throws Exception {
    Field field = (fs.getClass()).getDeclaredField("fs");
    field.setAccessible(true);
    field.set(null, null); // setting the fs parameter in FileSystem to null
  }

  @Test
  public void testExecute(){
    
    // move b1 to root
    arg = new ArrayList<String>();
    arg.add("d/b/b1");
    arg.add("d");
    redirection = new ArrayList<String>();
    
    cp1 = new Copy(arg, redirection);
    assertTrue(    cp1.execute());
    assertTrue(!(d.findChild("b1")==null)); // test d has b1
    // test b still has b1
    assertTrue(!((b.findChild("b1")) == null));
    
  }
  
  @Test
  public void testExecute2(){
    //noarg
    
    cp1 = new Copy(arg, redirection);
    assertTrue(    !cp1.execute());

    
  }


}
