package command;

import java.util.List;
import exception.InvalidArgumentSizeException;
import inputOrOutput.FileOutput;
import inputOrOutput.StandardError;

public class OverwriteDecorator extends RedirectionDecorator {

  public CommandThatHasOutput cmd;
  public FileOutput fileOutput;

  /**
   * create a new instance of append decorator with command to 
   * decotate(c), and initialize a way of outputing the stdout.
   * @param c the command to decorate
   */
  public OverwriteDecorator(CommandThatHasOutput c) {
    super(c);
    this.cmd = c;
    
    this.fileOutput = new FileOutput();
    // System.out.println("in OD:" + this.cmd.redirectionParam);

  }

  /**
   * first evaluate command and get the output result. If result is not 
   * null, then overwrite to the file indicated in the command's 
   * redirectionParam.
   * @return true iff redirection succeed 
         (i.e. OUTFILE succefully made and <res> is appended to OUTFILE)
   */
  @Override
  public boolean execute() {
    
    
    // evaluate() will return a string iff successfully evaluated, or null
    String res = this.cmd.evaluate();
    
    if (res == null) {
      return false;
    }

    // check if args number is valid
 
    
    // List<String> redirectionParam = this.cmd.getRedirectionParam();

    if (this.cmd.redirectionParam.size() != 2) {
      StandardError.displayError("wrong number of redirection parameters");
    } else {
      // get the path
      String path = this.cmd.redirectionParam.get(1);
      return this.fileOutput.redirectOutput(">", path, res); // return true iff
                                                             // this returns
                                                             // true
    }
    return false;
  }


  @Override
  public void checkNumArgs() throws InvalidArgumentSizeException {
    // TODO Auto-generated method stub

  }



}
