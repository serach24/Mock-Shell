package command;

import java.util.List;
import exception.IllegalPathnameException;
import exception.InvalidArgumentSizeException;
import exception.InvalidPathException;
import inputOrOutput.FileOutput;
import inputOrOutput.StandardError;
import storage.DataStorage;
import storage.Directory;
import storage.File;

public class Move extends CommandThatChangeFileSystem {

  /**
   * Constructs a move command with the specified arguments, also with its
   * reference to FileSystem.
   * 
   * @param a arguments of the command
   */
  public Move(List<String> a, List<String> r) {
    super(a, r);
  }

  /**
   * Support users to move a file or directory to the target path if the new
   * path is of type directory, then no matter the data storage with the old
   * path, we can just append it to the new path. if the new path is of type
   * file, then if the type of the old path is file then the content of the file
   * in the new path need to be overwritten. if the new path is of type file,
   * and the old path is a directory, in general, we cannot append a directory
   * to a file, which means that we need to report an error for that
   * 
   * @return true if the the data storage in the old path is moved to the new
   *         path
   */

  @SuppressWarnings("unchecked")
  public boolean execute() {

    try {
      this.checkRedirectionParam();
      this.checkNumArgs();
      // get the old path and the new path string from the argument
      String oldpath = this.arguments.get(0);
      String newpath = this.arguments.get(1);

      // get the data storage with the old path
      DataStorage item = this.fs.searchByPath(oldpath);

      DataStorage location = null;

      try {
        // get the data storage with the new path
        location = this.fs.searchByPath(newpath);

      } catch (InvalidPathException e) {
        // do nothing
      }

      // if the old path does not exist
      if (item == null) {
        return false;
      }
      // if location is null, the only chance that move can continue is
      // item is a file
      if (location == null && (!(item instanceof File))) {
        return false;
      }

      // if the new path is a sub data storage of the old path throws an
      // error
      if (item instanceof Directory) {
        String completeOldPath = item.getPath();
        String completeNewPath = location.getPath();
        if (completeNewPath.contains(completeOldPath)) {
          StandardError.displayError(
              "the new path cannot be a sub data storage of the old path");
          return false;
        }
      }
      // if the new path is a directory, then no matter the data type of
      // the old path, put it into the directory
      if (location instanceof Directory) {
        item.setParent((Directory) location);
        return true;
      }

      // if the new path and the old path are both file, then the content
      // in the new path needs to be overwritten
      // or if item is file, and location is null
      else if (item instanceof File) {
        Object content = ((File<?>) item).getContent();
        if (location instanceof File) {

          ((File<Object>) location).setContent(content);

        } else { // location is null
          System.out.println("location is null");
          // get the parent of the location
          String newFileName = this.fs.getTargetName(newpath);
          String parentName =
              newpath.substring(0, newpath.length() - newFileName.length());
          Directory parent = this.fs.searchDirByPath(parentName);
          if (parent == null) {
            throw new IllegalPathnameException(
                "the new path of file doesn't have a parent");
          } else {
            // create new file under it's parent directory and set content to
            // old content
            File newFile = new File(newFileName, content, parent);
          }
        }

        // delete the item in the old path from its parent
        item.setParent(null);
        return true;

      } else {
        throw new IllegalPathnameException(
            "cannot move a directory into a file.");
      }

    } catch (InvalidPathException e) {
      /*
       * if the target directory does not exists, then the arg is invalid, so
       * throw exception and return false
       */
      StandardError.displayError("the path entered is invalid");
    } catch (IllegalPathnameException e) {
      /*
       * if the target directory does not exists, then the arg is invalid, so
       * throw exception and return false
       */
      StandardError.displayError(e.getMessage() + "the path is invalid");
    } catch (InvalidArgumentSizeException e) {
      StandardError.displayError("The Move Command needs exactly 2 parameters");
    } catch (Exception e) {
      StandardError.displayError(e.getMessage());
    }
    return false;
  }

  @Override
  public void checkNumArgs() throws InvalidArgumentSizeException {
    if (this.arguments.size() != 2) {
      throw new InvalidArgumentSizeException();
    }

  }

}
