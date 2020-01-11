package command;

import java.util.ArrayList;
import java.util.List;
import exception.InvalidArgumentSizeException;
import exception.InvalidPathException;
import inputOrOutput.StandardError;
import storage.*;
/**
 * Represents find command that can find all file or dir within a specified path
 *
 */
public class Find extends CommandThatHasOutput {

  public Find(List<String> a, List<String> r) {
    super(a, r);
  }
  /**
   * Find all object under the specified path with the specified type and
   * specified name, then return their full path
   * 
   * @return the full paths of the file or dir with the specified name
   */
  @Override
  public String evaluate() {
   // System.out.println("in evaluate");
    String res = "";
    int pathCheck = 0, typeCheck = -1, nameCheck = -1;
    String type = "", name = "";
    ArrayList<String> pathList = new ArrayList<String>();
    ArrayList<DataStorage> foundItems = new ArrayList<DataStorage>();
    // for each argument check for the name and type
    for (String arg : arguments) {
      if (arg.equals("-name")) {
        nameCheck++;
      } else if (arg.equals("-type")) {
        typeCheck++;
      } else if (nameCheck >= 0&&nameCheck<=1) {
        name = checkName(arg);
        if (!name.equals("")) {
          nameCheck++;
        }
      } else if (typeCheck >= 0) {
        if (arg.equals("f")||arg.equals("d")) {
          type = arg;
          typeCheck++;
        }
      } else if (pathCheck >= 0) {
        pathList.add(arg);
        pathCheck++;
      } else {
        nameCheck = 2;
        break;
      }
    }
    // if all conditions for the parameter matched, call fin to get all 
    //object for each path
    if (pathCheck > 0 && typeCheck == 1 && nameCheck == 1) {
      for (String path : pathList) {
        try {
          Directory startDir = this.fs.searchDirByPath(path);
          find(startDir, type, name, foundItems);
        } catch (InvalidPathException e) {
          // error, but continue for loop
          StandardError.displayError(path+ " is not a valid path");
        }
      }
      // convert founded items into string
      for (DataStorage items : foundItems) {
          res = res + items.getPath() + "\n";
            
      }
    } 
    else {
      // display error
      StandardError.displayError("number/type of parameters is wrong");
      StandardError.displayError("the argument parameters do not follow "
          + "the format of:"
          + " path... -type [f|d] -name expression");
    }
    return res;
  }
  /**
   * remove quotes from the name of the file, return empty String if there are 
   * not
   * quotes around the name
   * @param name the name  with quotes
   * @return the name without quote
   */
  private String checkName(String name) {
    //remove the quotation marks iff name is surrounded by them
    if(name.startsWith("\"")&&name.endsWith("\"")) {
      return name.substring(1, name.length() - 1);
    }
    return "";
  }
  /**
   * Find every file with the specified name and type (F being file and d being
   * dir) that is under the specified directory and add them on to the specified
   * arrayList
   * 
   * @param startDir the directory that will be searched
   * @param type the type of the target
   * @param name the name of the target
   * @param foundItems the list of found items with the specified name
   */
  private void find(Directory startDir, String type, String name,
      ArrayList<DataStorage> foundItems) {
    // search for a file with the target name
    DataStorage target = startDir.findChild(name);
    // dont change the found item list if nothing is found
    if (target == null) {
    }
    // if the file is found and type matches add it to the found item list
    else if ((type.equals("d") && target instanceof Directory)
        || (type.equals("f") && target instanceof File)) {
      foundItems.add(target);
    }
    // go through and check every child
    for (DataStorage child : startDir) {
      // if it is a directory search for the item in that directory
      if (child instanceof Directory) {
        find((Directory) child, type, name, foundItems);
      }
    }
  }


  @Override
  public void checkNumArgs() throws InvalidArgumentSizeException {
    if (arguments.size()<5) {
      throw new InvalidArgumentSizeException("there should be at least 5 a"
          + "rguments"
          + " and they should follow the format of"
          + ": path... -type [f|d] -name expression");
    }

  }


}
