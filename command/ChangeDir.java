package command;

import java.util.List;
import exception.CanNotRedirectException;
import exception.InvalidArgumentSizeException;
import exception.InvalidPathException;
import inputOrOutput.StandardError;
import storage.*;
/**
 * represents a cd command that can change the current working directory
 *
 */
public class ChangeDir extends CommandThatChangeFileSystem {
  /**
   * Constructs a cd command with the specified arguments, also with its
   * reference to FileSystem.
   * 
   * @param arguments arguments of the command
   */
  public ChangeDir(List<String> a, List<String> r) {
    super(a, r);
  }

  @Override
  public String toString() {
    return "cd " + super.toString();
  }

  public void checkNumArgs() throws InvalidArgumentSizeException {
    // check number of arguments since changeDir only accepts 1 argument
    if (this.arguments.size() != 1) {
      throw new InvalidArgumentSizeException(
          "changeDir command only accepts one argument");
    }

  }


  @Override
  /**
   * Execute cd command change the current working directory to the one
   * specified in the argument
   * 
   * @return true if the current working directory is changed
   */
  public boolean execute() {

    try {
      this.checkRedirectionParam();
      this.checkNumArgs(); 
      // search for the location with the given path
      String path = this.arguments.get(0);

      Directory cwd = this.fs.searchDirByPath(path);

      // if the target directory exists, change dir and returns true
      this.fs.setCurrWorkDir(cwd);
      return true;

    } catch (InvalidPathException e) {
      /*
       * if the target directory does not exists, then the arg is invalid, so
       * throw exception and return false
       */
      StandardError.displayError("the path entered is invalid");
    } catch (CanNotRedirectException e) {
      StandardError.displayError("redirection parameters are wrong: "
    + e.getMessage());
    } catch (Exception e) {
      StandardError.displayError(e.getMessage());
    }
    return false;


  }
}
