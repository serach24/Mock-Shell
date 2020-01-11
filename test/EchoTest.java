package test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import command.Echo;
import exception.IllegalPathnameException;
import exception.InvalidPathException;
import storage.DataStorage;
import storage.Directory;
import storage.File;
import storage.FileSystem;

public class EchoTest {
  
  // declare the vars
  Echo e1, e2, e3, e4, e5;
  FileSystem fs;
  Directory d, a, a1;

  @Before
  public void setUp()  {
    
    // set up the file system, by creating it and create some dirs
    fs = FileSystem.getFileSystem();
    
    d = new Directory("d", fs.getRoot());
    a = new Directory("a", d);
    a1 = new Directory("a1", a);

  

    // make some echo commands.


    // e1: just print the argument (no redirection)
    List<String> args1 = new ArrayList<String>();
    List<String> rarg1 = new ArrayList<String>();
    args1.add("\"abc\"");
    e1 = new Echo(args1, rarg1);


    // e2: overwrite non file
    List<String> args2 = new ArrayList<String>();
    List<String> rargs2 = new ArrayList<String>();
    args2.add("\"f1new\"");
    rargs2.add(">");
    rargs2.add("a/a1/f1");
    e2 = new Echo(args2, rargs2);

    // e4: correct append with existing file
    List<String> args4 = new ArrayList<String>();
    List<String> rargs4 = new ArrayList<String>();
    
    try {
      File myFile = fs.searchFileByPath("/a/a1/f1");
      myFile.setContent("f1new");
    } catch (Exception e) {
      
    }
    
    args4.add("\"appendf1\"");
    rargs4.add(">>");
    rargs4.add("/a/a1/f1");

    e4 = new Echo(args4, rargs4);

    // e5: correct >> with non-existing file (same as overwrite)
    List<String> args5 = new ArrayList<String>();
    List<String> rargs5 = new ArrayList<String>();

    args5.add("\"f5\"");
    rargs5.add(">>");
    rargs5.add("a/a1/f5");
    e5 = new Echo(args5, rargs5);
  }

  @Test
  // test e1: correct
  public void testEvaluateEchoForSingleArg() {
    assertEquals("abc", e1.evaluate());
  }

  // test e2: correct > with existing directory
  @Test
  public void testEvaluateEchoForOverwriteExistingDir() {

    e2.execute();
    
    try {
      File myFile = fs.searchFileByPath("/a/a1/f1");
      assertEquals("f1new", myFile.getContent());

      
    } catch (InvalidPathException e) {
      
    }
  }
  
  @Test
  // e4
  public void testEvluateEchoForAppendExistingFile() {

    e4.execute();
    
    try {
      File myFile = fs.searchFileByPath("/a/a1/f1");
      assertEquals("f1newappendf1", myFile.getContent());

      
    } catch (InvalidPathException e) {
      
    }
  }
  
  
  // e5: correct >> with non-existing file
  @Test
  public void testEvaluateEchoForAppendNonexistingFile() {
    e5.execute();
    
    try {
      File myFile = fs.searchFileByPath("/a/a1/f5");
      assertEquals("f5", myFile.getContent());
      
    } catch (InvalidPathException e) {
      
    }
  }

}
