package command;

import java.util.List;
import exception.InvalidArgumentSizeException;
import inputOrOutput.FileOutput;
import inputOrOutput.StandardError;

public class AppendDecorator extends RedirectionDecorator {
  
  public FileOutput fileOutput; 
  
  /**
   * create a new instance of append decorator with command to 
   * decotate(c), and initialize a way of outputing the stdout.
   * @param c the command to decorate
   */
  public AppendDecorator(CommandThatHasOutput c) {
    super(c);
    this.fileOutput = new FileOutput();
  }

  
  /**
   * first evaluate command and get the output result. If result is not 
   * null, then append to the file indicated in the command's redirectionParam.
   * @return true iff redirection succeed 
         (i.e. OUTFILE succefully made and <res> is appended to OUTFILE)
   */
  @Override
  public boolean execute() {
    
      // evaluate() will return a string iff successfully evaluated, or null
      String res = this.cmd.evaluate();
      
      // no need to continue of the command doesn't have an output
      if (res == null) { 
        return false;
      }
      
      // check if args number is valid
      List<String> redirectionParam = this.cmd.getRedirectionParam();
      if (redirectionParam.size() != 2) {
        StandardError.displayError("wrong number of redirection parameters");
        return false;
      } else {
        // get the path 
        String path = redirectionParam.get(1);
       //redirectOutput() will return true iff redirection succeed 
        // (i.e. OUTFILE succefully made and <res> is appended to OUTFILE)        
        return this.fileOutput.redirectOutput(">>", path, res); 
      }
  }



  @Override
  public void checkNumArgs() throws InvalidArgumentSizeException {
    // TODO Auto-generated method stub

  }



}
