package command;

import java.util.List;
import exception.CanNotRedirectException;
import exception.InvalidArgumentException;
import exception.InvalidArgumentSizeException;
import exception.InvalidPathException;
import inputOrOutput.Output;
import inputOrOutput.StandardError;
import storage.File;
import storage.FileSystem;

/**
 * <p>
 * This class is used to get the contents from files. If input more than one
 * file, using three line to break them.
 * 
 */

public class Concatenate extends CommandThatHasOutput {

  public Concatenate(List<String> a, List<String> r) {
    super(a, r);
  }

  public void checkNumArgs() throws InvalidArgumentSizeException {
    // check number of arguments since changeDir only accepts 1 argument
    if (this.arguments.size() == 0) {
      throw new InvalidArgumentSizeException(
          "cat command needs at least one argument");
    }

  }

  /**
   * evaluate the cat command based on its arguments. First check if there are
   * at least one file path as input arguments. Then, For each file path in
   * arguments, if the file path points at a valid regular file, get the file
   * content and add it to the resultant string. If at least one path is a valid
   * path, hasValidPath will be true.
   * 
   * @return content the file content of files concatenated into one string
   */
  public String evaluate() {


    // execute only if there's at least one input argument
    try {
      String content = "";
      this.checkNumArgs();
      File myFile = null;
      for (String i : this.arguments) {

        myFile = this.fs.searchFileByPath(i);
        
        content += myFile.getContent();
        content += System.getProperty("line.separator")
            + System.getProperty("line.separator")
            + System.getProperty("line.separator")
            + System.getProperty("line.separator");

      }
      return content;


    } catch (InvalidPathException e) {
      // if one of the paths is invalid, ingore it and continue
    } 

    catch (Exception e) {
      StandardError.displayError(e.getMessage());
    }

    // indicating there should not be any output
    return null;
  }


  @Override
  public String toString() {
    return "cat " + super.toString();
  }



}
