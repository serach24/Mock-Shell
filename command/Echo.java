package command;

import java.util.List;

import exception.InvalidArgumentException;
import exception.InvalidArgumentSizeException;
import inputOrOutput.StandardError;

public class Echo extends CommandThatHasOutput {

	private boolean validArg;

	public Echo(List<String> a, List<String> r) {
		super(a, r); // same as this.arguments = arguments
	}

	/**
	 * given a string, return true iff the string is surrounded by double
	 * quotation and no double quotation inside
	 * 
	 * @param s the string to check
	 * @return the result if s is a real string
	 * @throws InvalidArgumentException if is not real string
	 */
	private void isRealString(String s) throws InvalidArgumentException {

		// check if surrounded by double quotation
		if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
			// extract the inner string
			String inner = s.substring(1, s.length() - 1);

			if (inner.indexOf('"') != -1) {
				throw new InvalidArgumentException();
			}
		} else {
			throw new InvalidArgumentException();
		}
	}

	/**
	 * evaluate the Echo command and return the output string for echo command.
	 * return null if there shouldn't be any output.
	 * 
	 * @return res if successfully evaluated and there should be output. or null
	 *         if there should not be any output due to some error.
	 */
	@Override
	public String evaluate() {
		try {
			String res = "";
			this.checkNumArgs();
			// extract the input string
			String inputStr = this.arguments.get(0);

			// if a real string (i.e. surrounded by "" and no " inside)
			this.isRealString(inputStr);

			String content = inputStr.substring(1, inputStr.length() - 1);

			res = content;

			return res;

		} catch (InvalidArgumentSizeException e) {
			StandardError.displayError("invalid arguments");

		} catch (InvalidArgumentException e) {
			StandardError.displayError("input string is invalid");

		} catch (Exception e) {
			StandardError.displayError(e.getMessage());
		}
		// null indicates there should not be any output due to some error
		return null;
	}

	@Override
	public String toString() {
		return "echo " + super.toString();
	}

	@Override
	public void checkNumArgs() throws InvalidArgumentSizeException {
		if (this.arguments.size() != 1) {
			throw new InvalidArgumentSizeException();
		}

	}

}
