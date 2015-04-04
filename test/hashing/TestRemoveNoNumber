package hashing;

import com.windowtester.runtime.swing.locator.JTextComponentLocator;
import javax.swing.JTextPane;
import com.windowtester.runtime.swing.SwingWidgetLocator;
import javax.swing.JSplitPane;
import com.windowtester.runtime.swing.UITestCaseSwing;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swing.locator.JButtonLocator;

public class TestRemoveNoNumber extends UITestCaseSwing {

	/**
	 * Create an Instance
	 */
	public TestRemoveNoNumber() {
		super(hashing.HashingApplication.class);
	}

	/**
	 * Main test method.
	 */
	public void testTestRemoveNoNumber() throws Exception {
		IUIContext ui = getUI();
		ui.click(new JTextComponentLocator(JTextPane.class,
				new SwingWidgetLocator(JSplitPane.class, 0,
						new SwingWidgetLocator(JSplitPane.class, 0,
								new SwingWidgetLocator(JSplitPane.class)))));
		ui.enterText("9");
		ui.click(new JButtonLocator("Apply"));
	}

}