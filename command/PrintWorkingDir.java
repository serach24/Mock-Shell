package command;

import java.util.List;
import exception.CanNotRedirectException;
import exception.InvalidArgumentSizeException;
import inputOrOutput.Output;
import inputOrOutput.StandardError;
import storage.DataStorage;
/**
 * represents pwd command that can show the path of current working directory
 *
 */
public class PrintWorkingDir extends CommandThatHasOutput {
  /**
   * Constructs a pwd command with the specified arguments
   * 
   * @param arguments arguments of the command
   */
  public PrintWorkingDir(List<String> a, List<String> r) {
    super(a, r);
  }


  @Override
  /**
   * get the string representation of current working directory. Or null if the
   * number of parameters is wrong.
   * 
   * @return res the full path of cwd
   */
  public String evaluate() {

    try {
      String res = "";
      this.checkNumArgs();
      // get the whole path of cwd
      res = this.fs.getCurrWorkDir().getPath();
      return res;
    } catch (InvalidArgumentSizeException e) {
      StandardError.displayError("pwd doesn't require any argument");
    } catch (Exception e) {
      StandardError.displayError(e.getMessage());
    }
    return null;
  }

  @Override
  public String toString() {
    return "pwd " + super.toString();
  }

  @Override
  public void checkNumArgs() throws InvalidArgumentSizeException {
    // execute pwd only if there's no input argument

    if (this.arguments.size() != 0) {
      throw new InvalidArgumentSizeException();
    }

  }
}
