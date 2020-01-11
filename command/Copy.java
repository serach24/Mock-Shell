package command;

import java.util.List;
import exception.CanNotRedirectException;
import exception.IllegalPathnameException;
import exception.InvalidArgumentSizeException;
import exception.InvalidPathException;
import inputOrOutput.StandardError;
import storage.DataStorage;
import storage.Directory;
import storage.File;

public class Copy extends CommandThatChangeFileSystem {

  
  
  /**
   * Constructs a cp command with the specified arguments, also with its
   * reference to FileSystem.
   * 
   * @param arguments arguments of the command
   */
  public Copy(List<String> a, List<String> r) {
    super(a, r);
  }
  
  @Override
  /**
   * get the two arguments src and des, copy item src to item des. If src is
   * File, des is Directory, copy the file to des folder. If both directories,
   * recursively copy all of the content to des folder; If src is a Folder but
   * des is File, give an error. If both are File, then overwrite des
   * regardless if name. If src is existing file but des is a nonexisting file
   * in an existing directory, create des and overwrite.
   * 
   * Note: If des is a directory, src has the same name (not necessarily the
   * same type) as a child of folder des, overwrite the child with the same
   * name.
   * 
   * Note: If des is a node in any subtree of src, copy cannot be done.
   * 
   * @return true iff the file/directory is successfully copied
   */
  public boolean execute() {
    try {
      this.checkNumArgs();
      this.checkRedirectionParam();

      String desPath = this.arguments.get(1);
      String srcPath = this.arguments.get(0);
      String srcName = this.fs.getTargetName(srcPath);

      // get the src object(File or Dir) and des object (File or Dir)
      DataStorage src = this.fs.searchByPath(srcPath);
      DataStorage des = this.fs.searchByPath(desPath);
      
      // get full path of src and des
      String desFullPath = des.getPath();
      String srcFullPath = src.getPath();

      // check if des is in subtree of src using their full paths
      if (desFullPath.contains(srcFullPath)) {
        StandardError.displayError(
            "If des is a node in any subtree of src, copy cannot be done.");
        return false;
      }

      /*
       * if src is File/Dir, des is Dir and has a child with the same name as
       * src , delete the child in des, and copy the src to des using helper.
       */
      if (des instanceof Directory) {

        // check if there is a child of des that has the same name as src's
        DataStorage possibleChild = ((Directory) des).findChild(srcName);

        if (possibleChild != null) { // found repeated name
          ((Directory) des).deleteChild(possibleChild);
        }

        // copy the src(File or Diectory) into des
        this.copy(src, ((Directory) des));
        return true;
      }

      /*
       * If src is Dir and des is File, return false
       */
      if (src instanceof Directory && des instanceof File) {
        StandardError.displayError("cannot copy a directory to a file");
        return false;
      }

      /*
       * If src is File and des is File, overwrite the content of des.
       */
      else if (des instanceof File && src instanceof File) {
        Object content = ((File) src).getContent();
        ((File) des).setContent(content);
        return true;
      } else {
        StandardError.displayError("sonething is wrong with File/Directory");
      }

    } catch (InvalidArgumentSizeException e) {
      StandardError.displayError("there must be exactly two args for cp");

    } catch (CanNotRedirectException e) {
      StandardError.displayError("redirection params are invalid");

    } catch (InvalidPathException e) {
      StandardError.displayError("the oldpath or new path is invalid");

    } catch (Exception e) {
      StandardError.displayError(e.getMessage());
    }
    return false;
  }



  /**
   * copy item src to directory des. already assumed: no repeated name; des is
   * not a node in any subtree of src; src and des are valid.
   * 
   * If src is File, copy the file to des folder; If src is Directory,
   * recursively copy all of the content to des folder;
   * 
   * @param src the item to copy, either a Directory object or a File Object
   * @param des the destination directory
   */
  private void copy(DataStorage src, Directory des) {
    
    System.out.println("args are: " + src.getPath() + "  " + des.getPath());
    
    // basis: src is a file
    if (src instanceof File) {

      // create a new file in des
      File newFile = new File(src.getName(), ((File) src).getContent(), des);

      // make sure that newFile is not null
      if (newFile == null) {
        StandardError.displayError("cannot copy");
        return;
      }

    } else if (src instanceof Directory) {
      
      
      // copy(create a dir with the same name as src) under the folder des
      System.out.println("creating new directory: " + src.getName());
      Directory newDir = new Directory(src.getName(), des);
      if (newDir == null) {
        
        StandardError.displayError("cannot copy");
        return;
      }

      // copy each child of src into des
      for (DataStorage i : (Directory) src) {
        System.out.println("about to copy new file/dir" + i.getPath());
        this.copy(i, newDir);
      }
      return;

    } else {
      StandardError.displayError("something went wrong");
      return;
    }
    return;


  }

  @Override
  public void checkNumArgs() throws InvalidArgumentSizeException {
    if (this.arguments.size() != 2) {
      throw new InvalidArgumentSizeException();
    }

  }



}
