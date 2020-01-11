package driver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import command.Command;
import command.Exit;
import inputOrOutput.Input;
import inputOrOutput.Output;
import utility.Interpreter;
import storage.DataStorage;
import storage.Directory;
import storage.FileSystem;

public class JShell {

  /**
   * This class represents the java shell process. The Jshell first initialize a
   * new file system, and continuously prompt the user for input command; it
   * then pass the input to interpreter and have the interpreter to process the
   * input.
   *
   */
  public static void main(String[] args) {

    // initialize new file system
    FileSystem fs = FileSystem.getFileSystem();
    Interpreter interpreter = new Interpreter();
    
    // continuously prompt the user for input command
    
    while (true) {

      Output.getOutput("enter a command: ");
      String userInput = Input.getInput();
      interpreter.interpreterProcess(userInput);
    }
    
    
  }
}


