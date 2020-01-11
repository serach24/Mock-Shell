package storage;

import java.util.ArrayList;
import java.util.Iterator;
import exception.IllegalPathnameException;
/**
 * represents a directory that stores other directory
 *
 */
public class Directory extends DataStorage implements Iterable<DataStorage> {
  private ArrayList<DataStorage> childList;

  /**
   * Construct a directory under a parent directory.
   * 
   * @param folderName the name of this directory
   * @param parent the parent directory of this directory
   */
  public Directory(String folderName, Directory parent) {
    super(folderName, parent);
    this.childList = new ArrayList<DataStorage>();
  }
  

  /**
   * Add the specified object under this directory and remove it from the
   * original directory. Return true if the specified object is added under
   * this.
   * 
   * @param child the object to add
   * @return true if the object is added under this directory
   */
  public boolean addChild(DataStorage child) {
    // check if there are already file or dir with the same name
    if (findChild(child.getName()) == null) {
      childList.add(child);
      if (child.parent != null) {
        child.parent.deleteChild(child);
      }
      child.parent = this;
      return true;
    }
    // will not add file if there are file or dir with the same name
    else {
      return false;
    }
  }

  /**
   * Remove the specified object from this directory
   * 
   * @param target the object that will be removed
   */
  public void deleteChild(DataStorage target) {
    childList.remove(target);
  }

  /**
   * Return the object under this directory that has the specified name, return
   * null if no object has the same name
   * 
   * @param name the name of the object that needs to be found
   * @return the object with the specified name
   */
  public DataStorage findChild(String name) {
    // loop through each item then check the name of each child object
    if (name.equals(".")) {
      return (DataStorage) this;
    } else if (name.equals("..")) {
      if (this.getName().equals("")) {
        return this;
      } else {
        return (DataStorage) this.getParent();
      }

    }
    for (DataStorage items : childList) {
      if (items.getName().equals(name)) {
        return items;
      }
    }
    return null;
  }

  @Override
  public Iterator<DataStorage> iterator() {
    return new DirIterator(this);
  }

  private static class DirIterator implements Iterator<DataStorage> {
    private ArrayList<DataStorage> childList;
    private int index;

    public DirIterator(Directory dir) {
      childList = dir.childList;
      index = 0;
    }

    @Override
    public boolean hasNext() {
      // we've reached the end if index is larger than the size of the list
      return childList.size() - 1 >= index;
    }

    @Override
    public DataStorage next() {
      DataStorage next = childList.get(index);
      index++;
      return next;
    }

  }

  @Override
  /**
   * return an xml representation of directory.
   * @return res directory in xml form
   */
  public String toXMLString() {
    String res = "";
    res = res + "<Directory>" + this.getPath() + "</Directory>\n";
    for (DataStorage i : this) {
      res = res + i.toXMLString() + "\n";
    }
    return res;
  }
}
