package command;

import java.util.List;
import inputOrOutput.StandardError;
import storage.*;
import exception.CanNotRedirectException;
import exception.DataStorageExistException;
import exception.IllegalPathnameException;
import exception.InvalidArgumentSizeException;
import exception.InvalidPathException;
/**
 * represents mkdir command that can create new directories
 *
 */
public class MakeDir extends CommandThatChangeFileSystem {

  /**
   * Constructs a mkdir command with the specified arguments
   * 
   * @param arguments arguments of the command
   */
  public MakeDir(List<String> a, List<String> r) {
    super(a, r);
  }

  public MakeDir() { // an mkdir object without argument
  }

  /**
   * Execute mkdir command make a new directory for each paths in the argument
   * of this command and all invalid paths will be output, return true if there
   * are at least one argument
   * 
   * @return true if all new files are created
   */
  public boolean execute() {

    try {
      this.checkRedirectionParam();
      this.checkNumArgs();

      // use makeDir(param) to create all of the new files and get an array of
      // failed paths
      return makeDir(this.arguments);

    } catch (InvalidArgumentSizeException e) {
      StandardError.displayError("need at least one argument for mkdir");
    } catch (CanNotRedirectException e) {
      StandardError.displayError("the redirection params are wrong");
    } catch (Exception e) {
      StandardError.displayError(e.getMessage());
    }
    return false;


    /*
     * // we need at least one argument for mkdir to work if
     * (this.arguments.size() == 0) {
     * StandardError.displayError("mkdir command requires at least one argument"
     * ); return false; }
     * 
     * // use makeDir(param) to create all of the new files and get an array of
     * // failed paths int[] result = makeDir(this.arguments); // if not failed
     * path return true if (result.length == 0) { return true; } // go through
     * the array of failed paths and output message to user for (int index = 0;
     * index < result.length; index++) {
     * StandardError.displayError(this.arguments.get(index) +
     * " is an invalid path"); } return false;
     */
  }

  @Override
  public String toString() {
    return "mkdir " + super.toString();
  }

  /**
   * Make new directory with each path in the specified list of paths Return an
   * array that contains the index of the paths that aren't able to create a new
   * path
   * 
   * @param param list of paths of directories that will be created
   * @return true iff all of the dirs are made
   */
  private boolean makeDir(List<String> param) {
    boolean allMade = true;
    // loop through each path and try to create a new dir
    for (String path : param) {

      try {
        //System.out.println("pass" + path+"to mkdirS");
        makeSingleDir(path);     
      } catch (IllegalPathnameException e) {
        StandardError.displayError(e.getMessage());
        allMade = false;
        // continue to loop through arguments
      } catch (InvalidPathException e) {
        StandardError.displayError(e.getMessage());
        allMade = false;
      } catch (Exception e) {
        StandardError.displayError(e.getMessage());
        allMade = false;
      }

    }

    return allMade;

  }

  /**
   * Make a new directory based on a specified path, return true if the
   * directory is created.
   * 
   * @param path path of the directory that will be created
   * @return true if the new directory is created
   * @throws IllegalPathnameException if there's illegal character or repeated
   *         name
   * @throws InvalidPathException if the path is not a valid path
   * @throws DataStorageExistException if there's a same DataStorage with the same name
   */
  private boolean makeSingleDir(String path)
      throws IllegalPathnameException, InvalidPathException,
      DataStorageExistException {
    // get the name of the new file that the user wants to create
    String[] nameArr = path.split("/");
    String fileName = nameArr[nameArr.length - 1];
    
    //get parent's path by removing the dir's name in the path
    String parentPath = path.substring(0, path.length() - fileName.length());
    if (!(parentPath.equals("")||parentPath.equals("/"))){
      parentPath = parentPath.substring(0, parentPath.length() - 1);
    }
    fs.addDir(parentPath, fileName);
    return true;
  }

  @Override
  public void checkNumArgs() throws InvalidArgumentSizeException {
    if (this.arguments.size() == 0) {
      throw new InvalidArgumentSizeException();
    }

  }


}
