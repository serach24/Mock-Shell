package test;

import java.io.IOException;
import java.net.MalformedURLException;
import inputOrOutput.RemoteDataFetcher;

public class MockFetcher implements RemoteDataFetcher {


  @Override
  public String getURLContent(String url)
      throws MalformedURLException, IOException {
    switch(url) {
      case("www.test.ca/abc/123.txt"):
        return "<test123.txt>";
      case("www.test.ca/234.html"):
        return "<test234>.html><2>";
      case("www.test.ca/f2.html"):
        return "<testf2";
      case("www.test.ca/illegaChar!.txt"):
        return "test";
      default:
        throw new MalformedURLException();
    }
  }

}
