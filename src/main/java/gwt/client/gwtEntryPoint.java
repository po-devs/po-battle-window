package gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class gwtEntryPoint implements EntryPoint {
  
  @Override
  public void onModuleLoad() {
    RootPanel.get().add(new HTML("GWT App has loaded."));
  }

}
