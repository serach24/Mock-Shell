package command;

import java.util.List;

import exception.CanNotRedirectException;
import exception.InvalidArgumentSizeException;
import inputOrOutput.StandardError;
import storage.Directory;
import storage.DirectoryStorage;

public class Popd extends CommandThatChangeFileSystem {

	public Popd(List<String> a, List<String> r) {
		super(a, r);
	}

	/**
	 * remove the top entry of directory stack, and cd into it. If the stack is
	 * empty then report an error.
	 * 
	 * @return true iff the command is successfully exxecuted
	 */
	public boolean execute() {

		try {
			this.checkRedirectionParam();
			this.checkNumArgs();
			// remove top entry from Dir Stack
			Directory nextDir = DirectoryStorage.getDir();
			// continue only if the removed dir exists (i.e. the dir stack was
			// previously not empty
			if (nextDir != null) {
				// cd into it
				this.fs.setCurrWorkDir(nextDir);
			} else {
				StandardError.displayError("The directory stack is empty now.");
				return false;
			}

			return true;

		} catch (InvalidArgumentSizeException e) {
			StandardError.displayError("popd doesn't require any argument");
		} catch (CanNotRedirectException e) {
			StandardError.displayError("the redirection params are wrong");
		} catch (Exception e) {
			StandardError.displayError(e.getMessage());
		}
		return false;

	}

	@Override
	public String toString() {
		return "popd " + super.toString();
	}

	@Override
	public void checkNumArgs() throws InvalidArgumentSizeException {

		if (this.arguments.size() != 0) {
			throw new InvalidArgumentSizeException();
		}

	}

}
