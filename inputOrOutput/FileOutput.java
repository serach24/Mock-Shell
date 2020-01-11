package inputOrOutput;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import command.MakeDir;
import exception.DirectoryExistException;
import exception.DirectoryNotExistException;
import exception.IllegalPathnameException;
import exception.InvalidPathException;
import exception.ParentDirNotExistException;
import storage.DataStorage;
import storage.Directory;
import storage.File;
import storage.FileSystem;
import storage.JFileSystem;

public class FileOutput {

  public FileSystem fs = JFileSystem.getFileSystem();

  /**
   * File output redirection. If symbol is >, overwrite content,to the file path
   * (absolute or relative) specified by the second element of args, if the file
   * path does not exist, create a file at that path with new content. If symbol
   * is >>, append content to the file path, if does not exist then the
   * functionality is the same as rewrite.
   * 
   * @param symbol > or >>
   * @param args arrayList of the part of user input after and including >
   *        seperated by space.
   * @param content content that need to overwrite
   * @return true iff the redirection succeed
   */
  public boolean redirectOutput(String symbol, String path, String content) {

    String fileName = this.fs.getTargetName(path);
    String parentName = path.substring(0, path.length() - fileName.length());
    try {
      Directory parentDir = this.fs.searchDirByPath(parentName);

      boolean fileNameExist;
      File myFile = (File) parentDir.findChild(fileName);

      if (myFile == null) {

        File newFile = new File(fileName, content, parentDir); 

      } else if (symbol.equals(">")) {
        myFile.setContent(content);
        return true;

      } else if (symbol.equals(">>")) {

        myFile.addContent(content);
        return true;

      } else {
        StandardError.displayError("shouldn't be there");
        return false;
      }

    } catch (InvalidPathException e) {
      StandardError
          .displayError("cannot redirect, parent folder does not exists");
      return false;

    } catch (Exception e) {
      StandardError.displayError(e.getMessage());
    }
    return true;
  }



}
