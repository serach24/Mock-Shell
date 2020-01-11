package command;

import java.util.List;

import exception.CanNotRedirectException;
import exception.InvalidArgumentSizeException;
import exception.InvalidPathException;
import inputOrOutput.StandardError;
import storage.Directory;
import storage.DirectoryStorage;

public class Pushd extends CommandThatChangeFileSystem {
	public Pushd(List<String> a, List<String> r) {
		super(a, r);
	}

	/**
	 * saves the current working dir by pushing it to directpry stack, change
	 * the current directory to the one specified by argument. only execute if
	 * there's one argument.
	 * 
	 * @return true iff the command is successfully executed.
	 * 
	 */

	public boolean execute() {
		try {
			this.checkRedirectionParam();
			this.checkNumArgs();
			DirectoryStorage.addDir(this.fs.getCurrWorkDir());
			// get the Directory object with the input
			String path = arguments.get(0);
			Directory newWorkingDir = this.fs.searchDirByPath(path);
			// change the current working directory to the one specified by user
			this.fs.setCurrWorkDir(newWorkingDir);
			return true;

		} catch (InvalidArgumentSizeException e) {
			StandardError.displayError("pushd need exactly one argument");
		} catch (InvalidPathException e) {
			StandardError.displayError("the path is invalid");

		} catch (CanNotRedirectException e) {
			StandardError.displayError("the redirection params are wrong");
		} catch (Exception e) {
			StandardError.displayError(e.getMessage());
		}
		

		return true;
	}

	@Override
	public String toString() {
		return "pushd " + super.toString();
	}

	@Override
	public void checkNumArgs() throws InvalidArgumentSizeException {
		if (this.arguments.size() != 1) {
			throw new InvalidArgumentSizeException();
		}
	}

}
