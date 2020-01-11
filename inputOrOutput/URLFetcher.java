package inputOrOutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * represent a fetcher that fetches data from url
 *
 */
public class URLFetcher implements RemoteDataFetcher {
  
  /**
   * Return String that contains content of a txt or html with its url
   * @param url url of the webpage
   * @return String content of the webpage
   * @throws MalformedURLException
   * @throws IOException
   */
  @Override
  public String getURLContent(String urlString)
      throws MalformedURLException, IOException {
    URL url;
    String inputLine, urlContent = "";
    url = new URL(urlString);
    BufferedReader in;
    in = new BufferedReader(new InputStreamReader(url.openStream()));
    while ((inputLine = in.readLine()) != null) {
        urlContent += urlContent + "\n" + inputLine;
    }
    in.close();
    return urlContent.substring(2);
}

}
