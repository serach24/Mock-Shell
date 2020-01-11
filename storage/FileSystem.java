package storage;

import exception.IllegalPathnameException;
import exception.InvalidPathException;

/**
 * represents a generic file system
 *
 *
 */
public interface FileSystem {
  public static FileSystem getFileSystem() {
    return JFileSystem.getFileSystem();
  }

  public Directory addDir(String path, String name)
      throws InvalidPathException, IllegalPathnameException;

  public DataStorage attach(String path, DataStorage taget)
      throws InvalidPathException, IllegalPathnameException;

  public Directory getCurrWorkDir();

  public Directory getRoot();

  public DataStorage searchByPath(String path) throws InvalidPathException;

  public Directory searchDirByPath(String path) throws InvalidPathException;

  public File<?> searchFileByPath(String path) throws InvalidPathException;

  public void setCurrWorkDir(Directory cwd);

  public String getTargetName(String path);

  public <T> File<T> addFile(String parentName, String fileName, T content) throws IllegalPathnameException, InvalidPathException;

}
