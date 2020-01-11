package command;

import java.util.Hashtable;
import java.util.List;
import exception.CanNotRedirectException;
import exception.InvalidArgumentSizeException;
import inputOrOutput.Output;
import inputOrOutput.StandardError;

public class Manual extends CommandThatHasOutput {

  public Manual(List<String> a, List<String> r) {
    super(a, r);
  }

  /**
   * return a hashtable which maps each command name to its manual.
   * 
   * @return hashtable the table which maps each command name to its manual
   */
  private static Hashtable<String, String> initializeHashTable() {
    Hashtable<String, String> hashtable = new Hashtable<String, String>();
    // create hash table that links commands to their manual
    hashtable.put("exit", "Quit the program.");
    hashtable.put("mkdir", "Create directories, each of which may be relative "
        + "to the current directory or may be a full path.");
    hashtable.put("cd",
        "Change directory to DIR, which may be relative to the "
            + "current directory or may be a full path. As with "
            + "Unix, .. means a parent directory and a .means the "
            + "current directory. The directory must be /, the "
            + "forward slash. The foot of the file "
            + "system is a single slash: /. ");
    hashtable.put("ls",
        "if no paths are given, print the contents (file or "
            + "directory) of the current directory, with a new line "
            + "following each of the content (file or directory). "
            + "\r\nOtherwise, for each path p, the order listed:"
            + "If p specifies a file, print p. \r\n"
            + "If p specifies a directory, print p, a colon, then"
            + " the contents of that directory, then an extra new line."
            + "\r\n If p does not exist, print a suitable message. If -R option"
            + "is given, the only difference is to recursively list all of the"
            + "subdirectories.");
    hashtable.put("pwd",
        "Print the current working directory (including " + "the whole path).");
    hashtable.put("pushd",
        "Saves the current working directory by pushing onto directory"
            + " stack and then changes the new current working "
            + "directory to DIR.");
    hashtable.put("popd",
        "Remove the top entry from the directory stack, and cd into it.");
    hashtable.put("history",
        "This command will print out recent commands, one command"
            + " per line.");
    hashtable.put("cat",
        "Display the contents of FILE1 and other files (i.e. File2 ....) "
            + "concatenated in the shell.");
    hashtable.put("echo", "print string to stndardout");
    hashtable.put("mv",
        "mv src des, if des os a folder then move src to des "
            + "directory provided that destination is not a node in subtree of "
            + "src. If both are files, rename the drc file. If src is directory"
            + "but destination is file, give an error.");
    hashtable.put("cp", "cp src des, is the same as move, except that the old"
        + "path is not removed.");
    hashtable.put("get",
        "a new file containing string will be created inside the"
            + "cwd. the file will hold the raw content"
            + " of the document that the url is pointing towards");
    hashtable.put("save",
        "write entire state of program to file in xml format, whose name is"
        + " specified in arguments. If outfile exists on computer, rewrite it"
        + " completely, if it doesn't exist, then create it if it's parent"
        + " folder exists.");
    hashtable.put("load",
        "retrive the file created by save command and restore the current"
        + " executing state of program");
    hashtable.put("find", "This command will search the directory you input"
    	+ "for all directories "
    	+ "(indicated by type d) that have the name exactly what you input. "
    	+ "If at any point one of the path in this command is invalid, you "
    	+ "must print out an error for that path, however, you must continue"
    	+ " searching for any other file or directories that may exist in "
    	+ "other valid paths. ");
    hashtable.put("tree",
        "Print the entire filesystem tree of the file system, "
            + "the root is the root directory. this cannot show if"
            + " a leave is a "
            + "directory or a file.");
    return hashtable;
  }


  /**
   * print documentation (usage, expected input, output information) for each
   * command, specifically exit, mkdir, cd, ls, pwd, pushd, popd, history, cat
   * and man.
   * 
   * @return true iff the documentation is printed successfully
   */
  @Override
  public boolean execute() {

    String res = this.evaluate();

    if (res != null) { // print only when content is not empty

      Output.getOutput(res); // pass to standardOut
      return true;
    }
    return false;
  }


  @Override
  /**
   * evaualte the command and returns the output string consisting the manual of
   * the command that required. Return null if the input arg is not a valid
   * command.
   * 
   * @return res the manual of the specific command
   */
  public String evaluate() {
    try {
      this.checkNumArgs();
      Hashtable<String, String> manualHashTable = Manual.initializeHashTable();
      String res = manualHashTable.get(this.arguments.get(0));
      if (res != null){
        res = res
            + "also, if the command is not one of mkdir, cd, mv, cp, get,"
            + " pushd, "
            + "popd Command support output redirection:\nIf the "
            + "input is [command] > OUTFILE or [command] >> OUTFILE where "
            + "OUTFILE is a valid path of a existing file, or nonexisting file"
            + " but you can create its parent folder by calling mkdir, then the"
            + "standard output will be redirect to OUTFILE instead of console.";
        return res;
        
      }

      return null;

    } catch (InvalidArgumentSizeException e) {
      StandardError.displayError("Manual command should have one argument");
    } catch (NullPointerException e) {
      StandardError.displayError("this command is not a valid command");

    } catch (Exception e) {
      StandardError.displayError(e.getMessage());
    }
    return null;
  }


  @Override
  public void checkNumArgs() throws InvalidArgumentSizeException {
    if (this.arguments.size() != 1) {
      throw new InvalidArgumentSizeException();
    }

  }
}
