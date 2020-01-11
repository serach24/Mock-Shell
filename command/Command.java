package command;

import java.util.List;
import exception.CanNotRedirectException;
import exception.InvalidArgumentException;
import exception.InvalidArgumentSizeException;
import exception.InvalidPathException;
import inputOrOutput.FileOutput;
import inputOrOutput.Output;
import storage.DataStorage;
import storage.File;
import storage.FileSystem;
import storage.JFileSystem;

public abstract class Command {


  public List<String> arguments;
  protected FileSystem fs; // each command class has a reference to fs
  protected List<String> redirectionParam;

  /**
   * Contruct a command object with specified arguments, also initialize it's
   * reference to fs.
   * 
   * @param arguments list of arguments for the command
   */
  public Command(List<String> a, List<String> r) {
    this.arguments = a;
    this.redirectionParam = r;
    this.fs = JFileSystem.getFileSystem();
  }
  
  /**
   * Contruct a command object with specified arguments, also initialize it's
   * reference to newfs.
   * 
   * @param arguments list of arguments for the command
   */
  public Command(List<String> a, List<String> r, FileSystem newfs) {
    this.arguments = a;
    this.redirectionParam = r;
    this.fs = newfs;
  }

  /**
   * construct a command object without specific argument.
   */
  public Command() {
    this.fs = FileSystem.getFileSystem();
  }

  /**
   * Return the string representation of this command "cmd argument: " +
   * arguments...
   * 
   * @return string representation of this command
   */
  @Override
  public String toString() {
    String argString = "";
    for (String arg : arguments) {
      argString += " " + arg;
    }

    return argString.trim();
  }


  /**
   * Execute command according to its type, return true if the command is ran
   * successfully
   * 
   * @return true if the the command is ran successfully
   */
  public abstract boolean execute();

  public abstract void checkNumArgs() throws InvalidArgumentSizeException;

  
  public List<String> getRedirectionParam(){
    return this.redirectionParam;
  }
}
