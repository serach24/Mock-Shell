package storage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import exception.DataStorageExistException;
import exception.IllegalPathnameException;
import inputOrOutput.StandardError;

/**
 * represents a general data storage object such as a directory of a text file
 *
 *
 */
public abstract class DataStorage {

  private String name = "";
  protected Directory parent = null;

  /**
   * Construct a DataStorage object with no parent.
   * 
   * @param name the name of this object
   */
  public DataStorage(String name) {
    try {
      this.setName(name);
      
    } catch (Exception e) {
      StandardError.displayError(e.getMessage());
    }
    
  }
  
  /**
   * Construct a DataStorage object under a parent directory.
   * 
   * @param name the name of this object
   * @param parent the parent directory of this object
   */
  public DataStorage(String name, Directory parent) {
    try {
      this.setName(name);
      setParent(parent);
    } catch (Exception e) {
      StandardError.displayError(e.getMessage());
    }
    
  }

  /**
   * @return the parent directory
   */
  public Directory getParent() {
    return parent;
  }

  /**
   * Remove this DataStorage object from the original parent directory and move
   * this to a new parent directory. Return true if this is moved to a new
   * parent
   * 
   * @param parent the new parent directory to set
   */
  public boolean setParent(Directory parent) {
    if (parent == null) {
      if (this.parent != null) {
        this.parent.deleteChild(this);
      }
      this.parent = parent;
      return true;
    }
    // if parent to add doesn't already have children with the same name, set
    // the new parent
    else if (parent.findChild(name) == null) {
      // before setting new parent, remove this from the original parent if
      // there is one
      parent.addChild(this);
      return true;
    } else {
      // if the new parent already has a child with the same name, dont change
      // anything
      return false;
    }
  }

  /**
   * @return the name of this DataStorage object
   */
  public String getName() {
    return name;
  }

  /**
   * Rename this file or dir with the specified name
   * 
   * @param name the new name to be set
   * @throws IllegalPathnameException 
   * @throws DataStorageExistException 
   */
  public void setName(String name) 
      throws IllegalPathnameException, DataStorageExistException {

    // check if there is special char in it before doing anything
    Pattern invalidChar = Pattern.compile("[/.\\s!@#$%^&*(){}~|<>?]");
    Matcher hasInvalid = invalidChar.matcher(name);
    boolean invalid = hasInvalid.find();
    // check if there is already file with the same name
    if (invalid) {
       throw new IllegalPathnameException("the name: " + name+" contains illegal character");
    } else if (parent != null && parent.findChild(name) != null) {
      throw new DataStorageExistException("the name: " + name+" is illegal because there"
       + " is a object with the same name in the same directory");
    } else {
      this.name = name;
    }
  }

  /**
   * @return the full path of the file or directory
   */
  public String getPath() {
    // case when it is root
    if (parent == null) {
      return "/";
    }
    // case when root is the parent
    else if (parent.getParent() == null) {
      return "/" + name;
    }
    // in normal case, the full path will be the full path of the parent plus
    // its name
    else {
      return parent.getPath() + "/" + name;
    }
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object object) {
    // compare the class first then compare their full path or memory location
    return (object != null) && (object.getClass().equals(this.getClass())
        && ((DataStorage) object).getPath().equals(this.getPath()));
  }
  
  public abstract String toXMLString();


}
