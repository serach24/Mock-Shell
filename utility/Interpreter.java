package utility;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Hashtable;
import command.*;
import inputOrOutput.Output;
import inputOrOutput.StandardError;

public class Interpreter {

  private static Hashtable<String, String> commandTable = initializeHashTable();

  /**
   * create a hash table that link command keywords to its command class
   * 
   * @return the hashtable that contains the command keyword and each command
   *         class
   */

  private static Hashtable<String, String> initializeHashTable() {
    Hashtable<String, String> hashtable = new Hashtable<String, String>();
    // create hash table that like commands to their class name
    hashtable.put("exit", "Exit");
    hashtable.put("mkdir", "MakeDir");
    hashtable.put("cd", "ChangeDir");
    hashtable.put("ls", "ListSegments");
    hashtable.put("pwd", "PrintWorkingDir");
    hashtable.put("pushd", "Pushd");
    hashtable.put("popd", "Popd");
    hashtable.put("history", "History");
    hashtable.put("cat", "Concatenate");
    hashtable.put("echo", "Echo");
    hashtable.put("man", "Manual");
    hashtable.put("find", "Find");
    hashtable.put("get", "Get");
    hashtable.put("load", "Load");
    hashtable.put("mv", "Move");
    hashtable.put("save", "Save");
    hashtable.put("tree", "Tree");
    hashtable.put("cp", "Copy");
    return hashtable;
  }

  public Interpreter() {

  }

  /**
   * split the user's input string with delimiter any number of whitespace; if
   * there is a substring of the input string surrounded by double quotation,
   * then the substring should be split into one single element regardless of
   * how many whitespaces it have. (i.e. "" has higher priority than whitespace)
   * 
   * @param input string to split
   * @return finalCmd a list of string containing all of the input words
   */
  public List<String> splitInput(String input) {

    // initialize new arrayList to store the splited words
    List<String> finalCmd = new ArrayList<String>();

    /*
     * compile the regex into a pattern, then get a new matcher for this
     * pattern.
     */

    Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(input);
    /*
     * continuously search for the occurences of regular expressions
     * [^\"]\\S*|\".+?\")\\s* in the string unDisposedCommand, and add the
     * substrings matched into finalCmd
     */
    while (m.find())
      finalCmd.add(m.group(1));

    /*
     * if unDisposedCommand starts with whitespace, the first element in <list>
     * is empty. In this case, we remove it.
     */
    if (finalCmd.size() > 0 && finalCmd.get(0).trim().isEmpty()) {
      finalCmd.remove(0);
    }

    return finalCmd;
  }


  /**
   * generate a new command.
   * 
   * @param s command name
   * @return new command object whose name is specified by <s>
   */
  public Command generateCommand(String cmd, List<String> args,
      List<String> redirectionParam) {
    Command finalCmd = null;
    String className = commandTable.get(cmd);
    try {
      // find the class with the class name from the hash table
      Class<?> commandClass = Class.forName("command." + className);

      // create a command with the arguments
      Class[] cArg = new Class[2];
      cArg[0] = List.class;
      cArg[1] = List.class;
      Constructor<?> constructor = commandClass.getDeclaredConstructor(cArg);

      finalCmd = (Command) constructor.newInstance(args, redirectionParam);

    } catch (ClassNotFoundException e) {
      StandardError.displayError("command is not valid");
    } catch (InstantiationException e) {
      StandardError.displayError("command or arguments are not valid");
    } catch (IllegalAccessException e) {
      StandardError.displayError("command or arguments are not valid");
    } catch (IllegalArgumentException e) {
      StandardError.displayError("arguments are not valid");
    } catch (InvocationTargetException e) {
      StandardError.displayError(e.getMessage());
    } catch (NoSuchMethodException e) {
      StandardError.displayError("command is not valid");
    } catch (SecurityException e) {
      StandardError.displayError(e.getMessage());
    }
    return finalCmd;
  }


  /**
   * interpreter process: read from input string; use splitInput to convert
   * input string to list of words; if the first word is a valid command,
   * generate corresponding command object and arguments list, then call the
   * command; if not valid command, report to Output an error message.
   * 
   * @param input the inout of user
   */
  public void interpreterProcess(String input) {

    // define what are valid commands

    // split input string into list of words
    List<String> finalInput = splitInput(input);

    /*
     * if finalInput has no element (which means the user only typed some number
     * of whitespaces), ignore it
     */
    if (finalInput.size() > 0) {

      // get the first word
      String cmdName = finalInput.get(0);



      // get a list of arguments, which is finalInput except the first item,
      // first item is the cmd name(this list may be empty)
      finalInput.remove(0);

      // get both the arrayList of finalInput and the arrayList of redirection
      // param (identified by > or >>)

      int appendIndex = finalInput.indexOf(">");
      int rewriteIndex = finalInput.indexOf(">>");

      int splitIndex;

      if (appendIndex == -1) {
        splitIndex = rewriteIndex;
      } else if (rewriteIndex == -1) {
        splitIndex = appendIndex;
      } else {
        splitIndex = Math.min(appendIndex, rewriteIndex);
      }

      List<String> args = new ArrayList<>();
      List<String> redirectionParam = new ArrayList<>(); // empty

      if (splitIndex == 0) { // if input contains > or >> but it's immediatly
                             // after cmd name, the args should be empty
        for (String s : finalInput) {
          redirectionParam.add(s);
        }

      } else if (splitIndex > 0) { // required redirection, so split the
                                   // finalInput to two parts:
        // arguments and redirectioenParam
        // args should be starting from 0 to splitIndex (exclusive)

        for (int i = 0; i < splitIndex; i++) {
          args.add(finalInput.get(i));
        }

        for (int j = splitIndex; j < finalInput.size(); j++) {
          redirectionParam.add(finalInput.get(j));
        }

      } else if (splitIndex == -1) {

        for (String t : finalInput) {
          args.add(t);
        }
      }

      // generate finalCommand object

      // first generate the command object regardless of redirection
      // parameters
      Command newCommand;

      newCommand = this.generateCommand(cmdName, args, redirectionParam);


      if (newCommand == null) {
        return;
      }

      /*
       * if the newCommand is of type CommandThatHasOutput, and its redirection
       * param is not empty, downcast then command, then create AppendDecorator
       * or OverwriteDecorator object (we know that first item in
       * redirectionParam must be > or >>)and have it collaborate with the
       * command
       */

      if (newCommand instanceof CommandThatHasOutput) {
        if (redirectionParam.size() > 0) {

          if (redirectionParam.get(0).equals(">")) {

            newCommand =
                new OverwriteDecorator((CommandThatHasOutput) newCommand);
          } else if (redirectionParam.get(0).equals(">>")) {
            newCommand = new AppendDecorator((CommandThatHasOutput) newCommand);

          } else {
            StandardError.displayError(
                "something went wrong with redirection parameters");
          }

        }

      }

      // execute new command
      newCommand.execute();

    }

  }


}
