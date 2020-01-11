package command;

import java.util.List;
import exception.CanNotRedirectException;
import exception.InvalidPathException;
import inputOrOutput.StandardError;
import storage.FileSystem;
import storage.JFileSystem;

/**
 * this class represents all the commands that can possibly make change to file
 * system, including change to the tree structure, to content/attribute of each
 * File, to the cwd pointer.etc. This class Doesn't include the commands that
 * can have an output redirection.
 * 
 * i.e. mkdir, cd, mv, cp, get, pushd, popd,
 *
 *
 */
public abstract class CommandThatChangeFileSystem extends Command {

  public CommandThatChangeFileSystem(List<String> a, List<String> r) {
    super(a, r);
  }

  public CommandThatChangeFileSystem() {
    this.fs = JFileSystem.getFileSystem();


  }
  
  
  
  
  /**
   * check if the redirection can be done. throw exception if not. the
   * redirection cannot be done if: there's more than one argument after > or
   * >>; the path is an existing directory; if there's no redirection param at
   * all, then the redirection can still be done.(no exception)
   * 
   * @throws CanNotRedirectException if the redirection param is invalid
   */
  public void checkRedirectionParam() throws CanNotRedirectException {

    if (this.redirectionParam.size() != 0) {

      // check the number of arguments in the redirection param
      if (this.redirectionParam.size() != 2) {
        throw new CanNotRedirectException("needs exactly one arg after > or >>");
      } else { // == 2
        String path = this.redirectionParam.get(1);
        try {
          
          this.fs.searchDirByPath(this.redirectionParam.get(1));
          throw new CanNotRedirectException("path is existing directory");
        
        } catch (InvalidPathException e) { // not an existing directory
          
          String allPath = this.redirectionParam.get(1);
          // check if the parent folder of file exists and exists as a folder
          String fileName = this.fs.getTargetName(allPath);
          String parentName = allPath.substring(0, allPath.length()
              - fileName.length());
          
          try {
            this.fs.searchDirByPath(parentName);
          } catch (InvalidPathException ex) {
            StandardError.displayError("path doesn't have parent");
          }
        }
      }
    }
  }

}
