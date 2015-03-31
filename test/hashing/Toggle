package hashing;

import com.windowtester.runtime.swing.locator.JRadioButtonLocator;
import com.windowtester.runtime.swing.UITestCaseSwing;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swing.SwingWidgetLocator;
import javax.swing.JSplitPane;

public class ToggleSystem extends UITestCaseSwing {

	/**
	 * Create an Instance
	 */
	public ToggleSystem() {
		super(hashing.HashingApplication.class);
	}

	/**
	 * Main test method.
	 */
	public void testToggleSystem() throws Exception {
		IUIContext ui = getUI();
		ui.click(new JRadioButtonLocator("Zookeeper"));
		ui.assertThat(new SwingWidgetLocator(JSplitPane.class).isVisible(false));
	}

}