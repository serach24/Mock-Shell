/**
 * 
 */
package test;

import exception.IllegalPathnameException;
import exception.InvalidPathException;
import storage.DataStorage;
import storage.Directory;
import storage.File;
import storage.FileSystem;

/**
 * represent  a mock file system that will be used in unit test
 * @author eugene
 *
 */
public class MockFileSystem implements FileSystem {
  public Directory root, cwd, d1, d2, d11;
  public File<String> f1,f2;
  /**
   * 
   */
  public MockFileSystem() {
    // TODO Auto-generated constructor stub
  }

  /*
  private static Directory d1, d2, d11;
  private static File<String> f1,f2;
  
  d1 = new Directory("d1",fs.getRoot());
  fs.setCurrWorkDir(d1);
  d2 = new Directory("d2",fs.getRoot());
  d11 = new Directory("d1-1",d1);
  f1 = new File<String>("f1",fs.getRoot());
  f2 = new File<String>("f2",d1);
  
                         "root"
                   /           |            \
            "d1"        "d2"        "f2"
           /      \ 
    "d1-1"  "f2"
  */
  
  /* (non-Javadoc)
   * @see storage.FileSystem#addDir(java.lang.String, java.lang.String)
   */
  @Override
  public Directory addDir(String path, String name)
      throws InvalidPathException, IllegalPathnameException {
    switch(path) {
      case("/"):
        if (name.equals("d3"))
          return new Directory("d3",root);
        else if(name.equals("d1"))
          throw new IllegalPathnameException("illegal name: d1");
        return null;
      case("/d1"):
        if (name.equals("d1-2"))
          return new Directory("d1-2",d1);
        else if (name.equals("dir?"))
          throw new IllegalPathnameException("illegal name: dir?");
        return null;
      case("/d2"):
        if (name.equals("d2-1"))
          return new Directory("d2-1",d2);
        return null;
      case("/f1"):
        return null;
      case("/d1/d1-1"):
        return d11;
      case("/d1/f2"):
        return null;
      case(""):
        if (name.equals("d1-3"))
          return new Directory("d1-3",d1);
      default:
        throw new InvalidPathException("invalid path");
    }
  }
  
  /* (non-Javadoc)
   * @see storage.FileSystem#attach(java.lang.String, storage.DataStorage)
   */
  @Override
  public DataStorage attach(String path, DataStorage taget)
      throws InvalidPathException, IllegalPathnameException {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see storage.FileSystem#getCurrWorkDir()
   */
  @Override
  public Directory getCurrWorkDir() {
    return cwd;
  }

  /* (non-Javadoc)
   * @see storage.FileSystem#getRoot()
   */
  @Override
  public Directory getRoot() {
    // TODO Auto-generated method stub
    return root;
  }

  @Override
  public DataStorage searchByPath(String path) throws InvalidPathException {
    switch(path) {
      case("/"):
        return root;
      case("/d1"):
        return d1;
      case("/d2"):
        return d2;
      case("/f1"):
        return f1;
      case("/d1/d1-1"):
        return d11;
      case("/d1/f2"):
        return f2;
      case("d1-1"):
        return d11;
      default:
        throw new InvalidPathException("invalid path");
    }
  }

  /* (non-Javadoc)
   * @see storage.FileSystem#searchDirByPath(java.lang.String)
   */
  @Override
  public Directory searchDirByPath(String path) throws InvalidPathException {
    switch(path) {
      case(".."):
      case("/"):
        return root;
      case("."):
        return cwd;
      case("/d1"):
        return d1;
      case("/d2"):
        return d2;
      case("d1-1"):
      case("/d1/d1-1"):
        return d11;
      default:
        throw new InvalidPathException("invalid path");
    }
  }

  /* (non-Javadoc)
   * @see storage.FileSystem#searchFileByPath(java.lang.String)
   */
  @Override
  public File<?> searchFileByPath(String path) throws InvalidPathException {
    switch(path) {
      case("/f1"):
        return f1;
      case("/d1/f2"):
        return f2;
      default:
        throw new InvalidPathException("invalid path");
    }
  }

  /* (non-Javadoc)
   * @see storage.FileSystem#setCurrWorkDir(storage.Directory)
   */
  @Override
  public void setCurrWorkDir(Directory cwd) {
    this.cwd = cwd;

  }

  @Override
  public String getTargetName(String path) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> File<T> addFile(String parentName, String fileName, T content)
      throws IllegalPathnameException, InvalidPathException {
    // TODO Auto-generated method stub
    return null;
  }

}
