package storage;

import inputOrOutput.StandardError;

/**
 * represents a file that hold content
 *
 * @param <E> the type of content that this file holds
 */
public class File<E> extends DataStorage {
  private E content;

  /**
   * Construct a file under a parent directory.
   * 
   * @param name the name of this object
   * @param parent the parent directory of this object
   */
  public File(String fileName, Directory parent) {
    super(fileName, parent);
  }

  /**
   * Construct a file under a parent directory with specified content.
   * 
   * @param name the name of this object
   * @param content the content of this object
   * @param parent the parent directory of this object
   */
  public File(String fileName, E content, Directory parent) {
    super(fileName, parent);
    this.content = content;
  }

  /**
   * Replace existing content with specified content
   * 
   * @param content the content to be set
   */
  public void setContent(E content) {
    this.content = content;
  }

  /**
   * Replace existing content with specified content
   * 
   * @param content the content to be set
   */
  public void addContent(String content) {
    if (this.getContent() instanceof String) {
      this.content = (E) (this.getContent() + content);
      
    } else {
      StandardError.displayError("cannot append string to a file that doesn't hold string");
    }

  }
  
  /**
   * @return content in this file
   */
  public E getContent() {
    return content;
  }

  @Override
  /**
   * return an xml representation of file.
   * @return res file in xml form
   */
  public String toXMLString() {
    String res = "";
    res = res + "<File>" + this.getPath() + " " + 
    (String) this.getContent() + "</File>\n";
    return res;
  }

}
