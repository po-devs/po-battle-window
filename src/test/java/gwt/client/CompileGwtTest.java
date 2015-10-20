package gwt.client;

import com.google.gwt.junit.client.GWTTestCase;

public class CompileGwtTest extends GWTTestCase {
  
  @Override
  public String getModuleName() {
    return "gwt.gwt";
  }

  public void testSandbox() {
    assertTrue(true);
  }
  
}
