package command;

import java.util.ArrayList;
import java.util.List;
import exception.InvalidArgumentSizeException;
import exception.InvalidPathException;
import inputOrOutput.Output;
import inputOrOutput.StandardError;
import storage.*;
/**
 * represents ls command that can list contents of a specified file or dir
 *
 */
public class ListSegments extends CommandThatHasOutput {
  /**
   * Constructs a ls command with the specified arguments
   * 
   * @param arguments arguments of the command
   */
  public ListSegments(List<String> a, List<String> r) {
    super(a, r);
  }

  /**
   * generate ls command output: the content of the current directory if there
   * are no argument output the content of dir or file specified by each path in
   * the arguments if there more than one argument
   * 
   * @return strResult the desired output in string form
   */
  @Override
  public String evaluate() {
    String strResult = "";
    String currResult = "";

    boolean valid = false;
    boolean recursive = false;
    boolean empty = false;
    String cwdPath=fs.getCurrWorkDir().getPath();
    // if there's more than one arg then recursive depends on the first arg
    if (this.arguments.size() > 0) {
      recursive = arguments.get(0).equals("-R");
    }

    if (recursive) {
      arguments.remove("-R");
    }
    if (arguments.isEmpty()) {
      empty = true;
      // if args are now empty, then default is to list all the
      //DataStorage in cwd
      arguments.add(cwdPath);
    }
    

    // if there are more than one arguments loop through each one of them
    for (String path : this.arguments) {
      try {
        if (recursive) {
          currResult = recursivelyExecute(path);
          valid = true;

        } else {
          currResult = listSegment(path);
          valid = true;
        }
        strResult = strResult + currResult + "\n";
      } catch (InvalidPathException e) {
        StandardError.displayError(e.getMessage());
      } catch (Exception e) {
        StandardError.displayError(e.getMessage());
      }
    }
    if (empty) {
      // if there are only one arg, we dont need to show the full path of cwd
      arguments.remove(0);
      //System.out.println(strResult+"before");
      strResult = strResult.substring(cwdPath.length()+2);
      //System.out.println("after"+strResult+"after");
    }
    if (recursive) {
      arguments.add(0, "-R");
    }

    // if at least one of the path is valid then the command should have an
    // output
    if (valid) {
      return strResult;
    }
    return null;

  }

  /**
   * Return an array of string that contains the content of the specified
   * directory or file with the given path
   * 
   * @param path path of the file or dir
   * @return an array of String that contains the content of the file or dir
   * @throws InvalidPathException
   */
  private String listSegment(String path) throws InvalidPathException {
    DataStorage target = fs.searchByPath(path);
    // if the given path is a file, output path
    if (target instanceof File) {
      return path;
    }
    // if the given path is a dir, print path then the content
    else if (target instanceof Directory) {
      ArrayList<String> result = new ArrayList<>();
      //result = path + ":";
      //Output.getOutput(path + ":");
      for (DataStorage item : (Directory) target) {
        result.add(item.getName());
      }
      return path + ":\n" + this.convertItemsToString(result);
    }
    return null;
  }

  /**
   * Recursively call execution()for all of the sub directories of the specified
   * path
   * 
   * @param path the path of the directory that will be called recursively
   * @return result all of the sub directories of the specified path
   * @throws InvalidPathException
   */
  private String recursivelyExecute(String path) throws InvalidPathException {
    String result = listSegment(path);
    DataStorage target = fs.searchByPath(path);
    if (target instanceof File) {
      return result;
    }
    for (DataStorage item : (Directory) target) {
      if (item instanceof Directory) {
        result = result + "\n" + recursivelyExecute(item.getPath());
      }
    }
    return result;
  }

  /**
   * convert every strings in the specified array into a big string
   * 
   * @param items the arrayList of Strings
   */
  private String convertItemsToString(ArrayList<String> items) {

    String res = "";
    // loop though each string in arrayList then add it to result
    for (String s : items) {
      res += s + "\n";
    }
    return res;
  }

  @Override
  public String toString() {
    return "ls " + super.toString();
  }


  @Override
  /**
   * check args number in ls. any number of arguments is valid.
   */
  public void checkNumArgs() throws InvalidArgumentSizeException {
    return;

  }

}
