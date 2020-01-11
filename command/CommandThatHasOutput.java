package command;

import java.util.List;
import inputOrOutput.Output;
import storage.FileSystem;
import storage.JFileSystem;

/**
 * this class represents all the commands that possibly has standard output.
 * i.e. ls, pwd, cat, echo, man, history, find, tree
 *
 */
public abstract class CommandThatHasOutput extends Command {

  public CommandThatHasOutput(List<String> a, List<String> r) {
    super(a, r);
  }


  /**
   * create a CommandThatHasOutput object without argument.
   */
  public CommandThatHasOutput() {
    this.fs = JFileSystem.getFileSystem();

  }

  /**
   * get the output string for this command, if any. If there's error preventing
   * the output string to be evaluated, throw exception.
   * 
   * @return output string
   */
  public abstract String evaluate();
  
  
  public boolean execute() {
    // evaluate() will return a string iff successfully evaluated, or null
    String res = this.evaluate();
    if (res != null) {
      Output.getOutput(res);
      return true;
    }

  return false;
}


}
