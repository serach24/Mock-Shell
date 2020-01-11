package inputOrOutput;

import java.io.IOException;
import java.net.MalformedURLException;
/**
 * represent a fetcher that fetch remote data
 *
 */
public interface RemoteDataFetcher {
  public String getURLContent(String url) throws MalformedURLException, IOException;
}
