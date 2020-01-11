package command;

import java.util.ArrayList;
import java.util.List;
import exception.CanNotRedirectException;
import exception.InvalidArgumentException;
import exception.InvalidArgumentSizeException;
import inputOrOutput.Output;
import inputOrOutput.StandardError;
import inputOrOutput.Input;


public class History extends CommandThatHasOutput {
  // private String output = "";
  private boolean hasOutput;

  /**
   * Constructs a history command with the specified arguments, also it's
   * reference to fs
   * 
   * @param arguments arguments of the command
   */
  public History(List<String> a, List<String> r) {
    super(a, r);
  }

  /**
   * evaluate the history command and return the output string for it return
   * null if there shouldn't be any output due to some error
   * 
   * @return res if successfully evaluated and there should be output. or null
   *         if no need to putput
   */
  @Override
  public String evaluate() {

    String res = "";

    // get the data stored in the input storage
    ArrayList<String> storage = Input.getStorage();
    Integer numHistory = storage.size();

    try {
      this.checkNumArgs();

      if (this.arguments.size() > 0) {
        // get number of history required
        // may throw numFormatException if arg cannot be parsed to int
        numHistory = Integer.parseInt(arguments.get(0));
      }

      if (numHistory > 1) {
        // get the starting index, if number is too big then ignore it and
        // print the complete history
        int index = Math.max(0, storage.size() - numHistory);

        // loop from the (required) starting point to the end of the
        // storage

        while (index < storage.size() - 1) {

          res = res + Integer.toString(index + 1) + ". " + storage.get(index)
              + "\n";
          index = index + 1;
        }
        res = res + Integer.toString(index + 1) + ". " + storage.get(index);
        return res;

      } else if (numHistory < 0) {
        StandardError.displayError("the input should be nonnegative integer");
      }
      return null;

    } catch (NumberFormatException e) {
      StandardError.displayError("the input argument should be an integer");

    } catch (InvalidArgumentSizeException e) {
      StandardError.displayError("history needs 0 or 1 argumment");
    }

    catch (Exception e) {
      StandardError.displayError(e.getMessage());
    }

    return null;
  }


  @Override
  public String toString() {
    return "history " + super.toString();
  }

  @Override
  public void checkNumArgs() throws InvalidArgumentSizeException {
    // check number of arguments since changeDir only accepts 1 argument
    if (this.arguments.size() > 1) {
      throw new InvalidArgumentSizeException();
    }

  }


}
