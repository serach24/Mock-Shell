package command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import exception.InvalidArgumentSizeException;
import inputOrOutput.StandardError;
import storage.*;


public class Tree extends CommandThatHasOutput {

  public Tree(List<String> a, List<String> r) {
    super(a, r);
  }

  /**
   * return a string containing the tree structure whose root is dir.
   * 
   * @param lvl the indentation level of dir
   * @param dir the dir to traverse
   * @return string representation of tree structure of dir
   */
  private String traversal(int lvl, Directory dir) {
    int count = 0;
    String tab = "";
    while (count < lvl) {
      tab += "\t";
      count++;
    }
    String result = tab + dir.getName() + "\n";
    for (DataStorage item : dir) {
      if (item instanceof Directory) {
        result += traversal(lvl + 1, (Directory) item);
      } else {
        result += tab + "\t" + item.getName() + "\n";
      }
    }
    return result;
  }


  @Override
  /**
   * return a string representing the structure of the whole file system.
   * 
   * @return result the representation of entire file system.
   */
  public String evaluate() {
    try {
      this.checkNumArgs();
      String result = "/\n";
      for (DataStorage item : this.fs.getRoot()) {
        if (item instanceof Directory) {
          result += traversal(1, (Directory) item);
        } else {
          result += "\t" + item.getName() + "\n";
        }
      }
      return result;

    } catch (Exception e) {
      StandardError.displayError(e.getMessage());
    }
    return null;

  }

  @Override
  public void checkNumArgs() throws InvalidArgumentSizeException {
    if (this.arguments.size() != 0) {
      throw new InvalidArgumentSizeException("tree doesnt need any arg");
    }
  }

}
