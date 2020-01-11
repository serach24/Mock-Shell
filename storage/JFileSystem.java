package storage;

import exception.IllegalPathnameException;
import exception.InvalidPathException;

/**
 * Represents the file system of the jshell
 *
 *
 */
public class JFileSystem implements FileSystem {

	private static FileSystem fs = null; // FileSystem reference
	private Directory root;
	private Directory cwd;

	/**
	 * create an instance if FileSystem, initialize the root directory and set
	 * the default current working directory to root.
	 */
	private JFileSystem() {
		root = new Directory("", null);
		cwd = root;
	}

	/**
	 * get the FileSystem instance if fs is already created, or create a new one
	 * of the the fs is null
	 * 
	 * @return fs the existing fs reference, or a new reference
	 */
	public static FileSystem getFileSystem() {
		if (fs == null) {
			fs = new JFileSystem();
		}
		return fs;
	}

	/**
	 * @return the root directory of the file system
	 */
	public Directory getRoot() {
		return root;
	}

	/**
	 * @return the current working directory of this file system
	 */
	public Directory getCurrWorkDir() {
		return cwd;
	}

	/**
	 * change the current working directory to the specified directory
	 * 
	 * @param cwd the new current working directory to set
	 */
	public void setCurrWorkDir(Directory cwd) {
		this.cwd = cwd;
	}

	/**
	 * return the name of the file or directory of the specified path
	 * 
	 * @param path the path of the file or directory
	 * @return the name of the file or directory
	 */
	public String getTargetName(String path) {
		// split the given path in to individual name
		String[] pathArr = path.split("/");
		// the last non empty name will be the name of the object of this path
		if (pathArr[pathArr.length - 1] == "") {
			return pathArr[pathArr.length - 2];
		} else {
			return pathArr[pathArr.length - 1];
		}
	}

	/**
	 * return the target file or directory in the
	 * specified path return null if the path is not a valid path
	 * 
	 * @param path the path of the file or directory
	 * @return the parent directory of the file or directory
	 * @throws InvalidPathException
	 */
	public DataStorage searchByPath(String path) throws InvalidPathException {
		DataStorage currentDir;
		int count = 0;
		// if path points to the root or cwd,return their parent directly
		if (path.equals("/")) {
			return root;
		} else if (path.equals("")) {
			return cwd;
		} else if (path.charAt(0) == "/".charAt(0)) {
			// if the path starts with a "/" treat it as a abs path and start
			// searching from root
			currentDir = root;
			// and start counter from 1 to ignore the first "/" in the path
			count = 1;
		} else {
			// if not start searching from current working directory
			currentDir = cwd;
		}
		// split path into individual dir names
		String[] pathArr = path.split("/");
		int endOfList = pathArr.length - 1;
		// if user typed a "/" at the end do not consider the last "" as a file
		// name
//    if (pathArr[endOfList].equals("")) {
//      endOfList--;
//    }
		// loop through each file name
		while (count < endOfList) {
			// if we have a directory and have not reach the end yet, continue
			// searching
			currentDir = ((Directory) currentDir).findChild(pathArr[count]);
			// if a file is found or no directory is found, the path is
			// incorrect, so
			// return null
			if (currentDir == null || currentDir instanceof File) {
				throw new InvalidPathException(
						path + " is an invalid path for a directory");
			}

//      else if (count <= endOfList) {
//        currentDir = ((Directory) currentDir).findChild(pathArr[count]);
//      }
			count++;
		}

		DataStorage ds = ((Directory) currentDir).findChild(pathArr[count]);
		if (ds == null) {
			throw new InvalidPathException(path + " is an invalid path");

		}
		return ds;
	}

	/**
	 * return the target String file in the specified path
	 * 
	 * @param path the path of the target file
	 * @return the target file
	 * @throws InvalidPathException
	 */
	public File<?> searchFileByPath(String path) throws InvalidPathException {
		// search the object and check if it is a file
		DataStorage target = searchByPath(path);
		if (target instanceof File) {
			return (File<?>) target;
		} else {
			throw new InvalidPathException(
					path + " is not a valid path for a file");
		}
	}

	/**
	 * return the target directory in the specified path, including ".." or "."
	 * that indicates the parent directory or itself, throw an exception if the
	 * path is not a valid path for a directory.
	 * 
	 * @param path the path of the target directory
	 * @return the target directory
	 * @throws InvalidPathException
	 */
	public Directory searchDirByPath(String path) throws InvalidPathException {
		// search for the target and check if it is a directory and also
		// consider
		// the case of ".." and "."
		DataStorage target = searchByPath(path);
		if (target instanceof Directory) {
			return (Directory) target;
		} else {
			throw new InvalidPathException(
					path + " is not a valid path for a directory");
		}
	}

	/**
	 * Attach the specified file to the directory that the specified path points
	 * to and remove the file from the original directory if there is one.
	 * 
	 * @param path   the directory that the file will be attached onto
	 * @param target the file that will be attached
	 * @return the attached file
	 * @throws InvalidPathException
	 * @throws IllegalPathnameException
	 */
	public DataStorage attach(String path, DataStorage target)
			throws InvalidPathException, IllegalPathnameException {
		Directory newDir = searchDirByPath(path);
		// if target was not added in the new directory throw an exception
		if (!newDir.addChild(target)) {
			throw new IllegalPathnameException(
					"cannot attach, " + target.getName() + " because"
							+ " there already exist file with the same name");
		} else {
			return target;
		}
	}

	/**
	 * Add a new Directory based on a specified path, return the new Directory.
	 * 
	 * @param path path of the directory that will be created
	 * @param name the name of the new dir
	 * @return newDS the new DataStorage is created
	 * @throws IllegalPathnameException if the name of the new dir is illegal
	 * @throws InvalidPathException
	 */
	public Directory addDir(String path, String name)
			throws IllegalPathnameException, InvalidPathException {
		// get the parent of the new file that the user wants to create
		Directory parent = searchDirByPath(path);
		// if path is correct but there exist folder with the same name throw
		// exception
		if (parent.findChild(name) != null) {
			throw new IllegalPathnameException(
					"cannot create, " + name + " because"
							+ " there already exist file with the same name");
		}
		// else we can create a new directory in the parent we got
		else {
			Directory newDir = new Directory(name, parent);
			// name is illegal throw exception
			if (parent.findChild(name) == null) {
				throw new IllegalPathnameException("cannot create " + name
						+ ", because" + " this is not a legal name");
			}
			return newDir;
		}
	}

	/**
	 * Add a new File based on a specified path, return the new File.
	 * 
	 * @param         <T> the type of data that the file will hold
	 * 
	 * @param path    path of the File that will be created
	 * @param name    the name of the new file
	 * @param content the content of the file
	 * @return the new file that is created
	 * @throws InvalidPathException
	 * @throws IllegalPathnameException
	 */
	public <T> File<T> addFile(String path, String name, T content)
			throws IllegalPathnameException, InvalidPathException {
		Directory parent = searchDirByPath(path);
		// if path is correct but there exist folder with the same name throw
		// exception
		File<T> file = new File<T>(name, content, null);
		attach(path, file);
		// if name is illegal throw exception
		if (parent.findChild(name) == null) {
			throw new IllegalPathnameException("cannot create " + name
					+ ", because" + " this is not a legal name");
		}
		return file;

	}

}
