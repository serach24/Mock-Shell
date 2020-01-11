package command;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import exception.CanNotRedirectException;
import exception.InvalidArgumentSizeException;
import inputOrOutput.Input;
import inputOrOutput.RemoteDataFetcher;
import inputOrOutput.StandardError;
import inputOrOutput.URLFetcher;
import storage.*;
/**
 * represents get command that creates file with the content from a specified
 * url
 *
 */
public class Get extends CommandThatChangeFileSystem {
  private RemoteDataFetcher fetcher;
  public Get(List<String> a, List<String> r) {
    super(a, r);
    fetcher = new URLFetcher();
  }
  /**
   * execute get command, a new file containing string will be created. the
   * file will hold the raw content of the document that the url is pointing
   *  towards
   */
  @Override
  public boolean execute() {
    String url = "";
    String name, content;
    try {
      this.checkNumArgs();
      url = arguments.get(0);
      this.checkRedirectionParam();
      content = fetcher.getURLContent(url);
      name = getFileName(url);
      if (fs.getCurrWorkDir().findChild(name)!=null) {
        StandardError.displayError("cannot create " + name+" because "
            + " there already "
            + "exist file with this name");
        return false;
      }
      // create file with the string from the input method and 
      // file name then attach it to cwd
      File<String> file = new File<String>(name, content, 
          this.fs.getCurrWorkDir());
      // check if the file is actually added
      if (fs.getCurrWorkDir().findChild(name)==null) {
        StandardError.displayError("cannot create " + name+" because "
            + "of the illegal"
            + "characters");
        return false;
      }
      
    } catch (MalformedURLException e) {
      StandardError.displayError(url + " is not a valid url");
      return false;
    } catch (IOException e) {
      StandardError.displayError("an error occured when reading this url");
      return false;
    } catch (InvalidArgumentSizeException e) {
      StandardError.displayError("get needs only the url as argument");
      return false;
    } catch (CanNotRedirectException e) {
      StandardError.displayError("redirection params are wrong:" 
    + e.getMessage());
      return false;
    }
    return true;
  }
  
  
  /**
   * get the name of the file that the url is pointing towards
   * for example getFileName("www.123.com/123.txt") returns "123"
   * @param url
   * @return the name of the file
   */
  private String getFileName(String url) {
    // get the begin and end index of name which will be between the
    //last "/" and "."
    int beginIndex = url.lastIndexOf("/")+1;
    int endIndex = url.lastIndexOf(".");
    return url.substring(beginIndex, endIndex);
    
  }

  @Override
  public void checkNumArgs() throws InvalidArgumentSizeException {
    if (this.arguments.size() != 1) {
      throw new InvalidArgumentSizeException();
    }
  }

}
