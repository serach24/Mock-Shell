package command;

import java.util.List;
import exception.InvalidArgumentSizeException;
import inputOrOutput.FileOutput;
import storage.FileSystem;

/**
 * a decorator class representing all the commands decorated with >
 * or >>. this decorator only decorates command that has stadrard output,
 * i.e. the command of type CommandThatHasOutput.
 */
public abstract class RedirectionDecorator extends CommandThatHasOutput {
  
  
  protected CommandThatHasOutput cmd; // command to decorate
  protected FileOutput fileOutput;


  /**
   * create a new instance of redirection decorator with command to 
   * decotate(c), and initialize a way of outputing the stdout.
   * @param c the command to decorate
   */
  public RedirectionDecorator(CommandThatHasOutput c) {
    super();
    //System.out.println("calling redirection decorator constructor");
    this.cmd = c;
    this.fileOutput = new FileOutput();
  }
  
  /**
   * return the resultant string (standard output) of the command, if the
   * command is evaluated successfully, otherwise return null.
   * @return res the standard output string of command
   */
  public String evaluate() {
    String res = (this.cmd).evaluate();
    return res;
  }

}
