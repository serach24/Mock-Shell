package command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import exception.CanNotRedirectException;
import exception.InvalidArgumentSizeException;
import inputOrOutput.Input;
import inputOrOutput.Output;
import inputOrOutput.StandardError;
import storage.*;

public class Save extends Command {

  public Save(List<String> a, List<String> r) {
    super(a, r);
  }

  
  /**
   * write entire state of program to file in xml format, whose name is 
   * specified in
   * arguments. If outfile exists on computer, rewrite it completely, if if
   * doesn't exist, then create it if it's parent folder exists.
   * 
   * @return true iff the state of program is successfully saved.
   */

  public boolean execute() {

      // get path
      String path = this.arguments.get(0);

      // check if valid path to a regular file (regardless of exists or not)
      try {
        this.checkNumArgs();
        File file = new File(path);
        if (!file.isDirectory()) { // path is not an existing directory
          if (!file.exists()) {
            try {
              file.createNewFile();
            } catch (Exception e) {
              StandardError.displayError("cannot create new file:" 
            + e.getMessage());
              return false;
         
            }
          }
          // now the file at given path exists, get string to save and overwrite
          // the file
          String info = this.storeEntireInfo();
          try {

            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(info);
            fileWriter.close();
            return true;

          } catch (IOException e) {
            StandardError.displayError("error writing to file" + 
          e.getMessage());
            return false;
          }

        } else {
          StandardError.displayError("the given path is an existing directory");
          return false;
        }

      } catch(InvalidArgumentSizeException e) {
        StandardError.displayError("save only need one arg");
        return false;
        
      }catch (NullPointerException e) {
        StandardError.displayError("invalid path");
        return false;
      }
  }
  


  
  /**
   * record the entire structure of the filesystem in xml format.
   * @return the XML representation of the root directory.
   */
  private String storeFileSystem() {
    String res = "";
    res = res + "<fileSystem>\n";
    res = res + this.fs.getRoot().toXMLString();
    res = res + "</fileSystem>\n";
    return res;
  }
  
  /**
   * record the full path of current working dir as a XML element. 
   * @return the xml element representation of full path of cwd
   */
  private String storeCurrWorkingDir() {
    String res = "";
    res = res + "<currentWorkingDir>";
    res = res + this.fs.getCurrWorkDir().getPath();
    res = res + "</currentWorkingDir>\n";
    return res;
    
  }
  
  /**
   * record the directory stack in xml format. Only store the full path 
   * of Directory objects in the order from bottom of the stack to top.
   * @return xml representation of directory stack
   * 
   */
  private String storeDirectoryStorage() {
    String res = "";
    res = res + "<DirectoryStack>\n";
    // loop through the directory stack(from bottom to top) to get each
    // directory
    for (Directory d : DirectoryStorage.storage) {
      // add the absolute path of current directory to res
      res = res + "<path>" +d.getPath() +"</path>\n";
    }
    res = res + "</DirectoryStack>\n";
    return res;
  }
  
  /**
   * record the history of commands in xml format. Store each history command
   * in string format from the most recent one to the oldest one.
   * @return res xml representation of history of commands.
   */
  private String storeHistoryCommand() {
    String res = "";
    res = res + "<history>\n";
    
    // loop through Input.storage
    for (String s: Input.storage) {
      res = res + "<record>" + s + "</record>\n";
    }
    res = res + "</history>\n";
    return res;
  }
  
  /**
   * record the entire current executing state of Jshell in xml representation.
   *  store file system structure, path of cwd, the absolute path of each 
   *  Directory in Directory Stack, and History of Commands.
   * @return res the entire current executing state of Jshell
   */
  public String storeEntireInfo() {
    String res = "";
    res = res + "<entireInfo>\n";
    res = res + this.storeFileSystem();
    res = res + this.storeCurrWorkingDir();
    res = res + this.storeDirectoryStorage();
    res = res + this.storeHistoryCommand();
    res = res + "</entireInfo>";
    return res;
  }


  
  @Override
  public void checkNumArgs() throws InvalidArgumentSizeException {
    if (this.arguments.size() != 1) {
      throw new InvalidArgumentSizeException();
    }
  }
} 
