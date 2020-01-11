package test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import command.Save;
import inputOrOutput.Input;
import storage.Directory;
import storage.File;
import storage.FileSystem;

public class SaveTest {

  public List<String> arg,redirection;
  public File a, b;
  public Directory d1, d2, d3, d4;
  public FileSystem fs;
  public String res, expected;
  public Save save;
  
  @Before
  public void setUp() {
    
    fs = FileSystem.getFileSystem();
    // add some input to the storage
    Input.storage.add("mock input 1");
    Input.storage.add("mock input 2");
    Input.storage.add("mock input 3");
    Input.storage.add("mock input 4");
    Input.storage.add("mock input 5");
    d1 = new Directory("d1", fs.getRoot());
    d2 = new Directory("d2", fs.getRoot());
    d3 = new Directory("d3", fs.getRoot());
    d4 = new Directory("d4", d1);
    a = new File("a","a", d2);
  }
  
  @Test
  public void testEvaluate() { // test for the right xml
    arg = new ArrayList<String>();
    redirection = new ArrayList<String>();
    save = new Save(arg, redirection);
    res =  save.storeEntireInfo();
    expected = "<entireInfo>\r\n" + 
        "<fileSystem>\r\n" + 
        "<Directory>/</Directory>\r\n" + 
        "<Directory>/d1</Directory>\r\n" + 
        "<Directory>/d1/d4</Directory>\r\n" + 
        "\r\n" + 
        "\r\n" + 
        "<Directory>/d2</Directory>\r\n" + 
        "<File>/d2/a a</File>\r\n" + 
        "\r\n" + 
        "\r\n" + 
        "<Directory>/d3</Directory>\r\n" + 
        "\r\n" + 
        "</fileSystem>\r\n" + 
        "<currentWorkingDir>/</currentWorkingDir>\r\n" + 
        "<DirectoryStack>\r\n" + 
        "</DirectoryStack>\r\n" + 
        "<history>\r\n" + 
        "<record>mock input 1</record>\r\n" + 
        "<record>mock input 2</record>\r\n" + 
        "<record>mock input 3</record>\r\n" + 
        "<record>mock input 4</record>\r\n" + 
        "<record>mock input 5</record>\r\n" + 
        "</history>\r\n" + 
        "</entireInfo>";
    assertEquals(expected.trim(),res.trim());
    
  }
  
  
  

}
