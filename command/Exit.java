package command;

import inputOrOutput.Output;
import inputOrOutput.StandardError;
import java.util.List;
import driver.JShell;
import exception.InvalidArgumentException;
import exception.InvalidArgumentSizeException;

public class Exit extends Command {


  public Exit(List<String> a, List<String> r) {
    super(a, r); // same as this.arguments = arguments
  }


  @Override
  public String toString() {
    return "exit " + super.toString();
  }

  @Override
  /**
   * exit the program
   * 
   * @return true iff exit successfully
   */
  public boolean execute() {

    try {
      this.checkNumArgs();
      int status = 0;
      System.exit(status);
      return true;

    } catch (InvalidArgumentSizeException e) {
      StandardError.displayError("exit doesn't need any arg");
    }
    return false;
  }



  @Override
  public void checkNumArgs() throws InvalidArgumentSizeException {

    /*
     * only execute the Exit command if there's no other input argument from the
     * user
     */
    if (this.arguments.size() != 0) {
      throw new InvalidArgumentSizeException();
    }
  }
}


