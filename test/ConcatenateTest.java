package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import command.ChangeDir;
import command.Concatenate;
import command.MakeDir;
import exception.InvalidPathException;
import storage.*;

public class ConcatenateTest {
  public Concatenate cat;
  public List<String> arg,redirection;
  public File a, b;
  public Directory d;
  public FileSystem fs;
  

  @Before
  public void setUpBeforeClass() throws Exception {
    
    fs = FileSystem.getFileSystem();
    d = new Directory("d", fs.getRoot());
    a = new File("a", "Applepie", d);
    b = new File("b", "BinaryTree", d);

  }

  @Test
  // test the result of evaluation
  public void testExecute() {
    // no argument
    arg = new ArrayList<String>();
    redirection = new ArrayList<String>();
    arg.add("/d/a");
    cat = new Concatenate(arg, redirection);
    
    assertEquals("Applepie", cat.evaluate().trim());
  }
  
  
  @Test
  public void testExecute2() {
    // no argument
    arg = new ArrayList<String>();
    redirection = new ArrayList<String>();
    arg.add("/d/a");
    redirection.add("/d/out");
    cat = new Concatenate(arg, redirection);
    
    File myFile = null;
    try {
       myFile = fs.searchFileByPath("/d/a");
    } catch (InvalidPathException e) {
      e.printStackTrace();
    }
    
    assertEquals("Applepie", cat.evaluate().trim());
    assertEquals("Applepie", ((String)myFile.getContent()).trim());
  }
  
}
