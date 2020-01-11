package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import storage.File;

public class FileTest {
  private static File<String> file;

  @Before
  public void setUp() throws Exception {
    file = new File<String>("file", "Content123", null);
  }

  @Test
  public void testGetContent() {
    // test getter method
    assertEquals("Content123", file.getContent());
  }

  @Test
  public void testSetContent() {
    // reset string content
    file.setContent("456");
    assertEquals("456", file.getContent());
  }

}
