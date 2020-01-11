package test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import command.Echo;
import command.Manual;
import exception.IllegalPathnameException;
import exception.InvalidPathException;
import storage.DataStorage;
import storage.Directory;
import storage.File;
import storage.FileSystem;

public class ManualTest {

  // declare the vars
  Manual m1, m2;
  FileSystem fs;
  Directory d;
  File a;

  @Before
  public void setUp()  {
    
    // set up the file system, by creating it 
    fs = FileSystem.getFileSystem();
    d = new Directory("d", fs.getRoot());
    //a = new File("a", "Applepie", d);
    
    List<String> args1 = new ArrayList<String>();
    List<String> rarg1 = new ArrayList<String>();
    args1.add("exit");
    m1 = new Manual(args1, rarg1);


    List<String> args2 = new ArrayList<String>();
    List<String> rargs2 = new ArrayList<String>();
    args2.add("history");
    rargs2.add(">");
    rargs2.add("/d/a");
    m2 = new Manual(args2, rargs2);
    
  }

  @Test
  // test m1: correct
  public void testEvaluateEchoForSingleArg() {
    assertEquals(
        "Quit the program.also, if the command is not one of mkdir, cd, mv,"
        + " cp, get, pushd, "
            + "popd Command support output redirection:\nIf the input is"
            + " [command] > OUTFILE or [command] >>"
            + " OUTFILE where OUTFILE is a valid path of a existing file, or"
            + " nonexisting file"
            + " but you can create its parent folder by calling mkdir, then the"
            + "standard output will "
            + "be redirect to OUTFILE instead of console.",
        m1.evaluate());
  }

  // test e2: correct > with existing directory
  @Test
  public void testEvaluateEchoForOverwriteExistingDir() {

    m2.execute();
    
    File myFile = null;
    try {
       myFile = fs.searchFileByPath("/d/a");
       assertEquals("This command will print out recent commands, one command per"
           + " line.also, if the command is not one of mkdir, cd, mv, cp, get, "
           + "pushd, popd Command support output redirection:If the input is "
           + "[command] > OUTFILE or [command] >> OUTFILE where OUTFILE is a valid"
           + " path of a existing file, or nonexisting filebut you can create "
           + "its parent folder by calling mkdir, then thestandard output will be"
           + " redirect to OUTFILE instead of console.",(myFile.getContent()));

      
    } catch (InvalidPathException e) {
      System.out.println("------------------------------------------");
    }
  }


}
