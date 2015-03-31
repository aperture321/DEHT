package hashing;

import com.windowtester.runtime.swing.SwingWidgetLocator;
import javax.swing.JSplitPane;
import com.windowtester.runtime.swing.UITestCaseSwing;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swing.locator.JTextComponentLocator;
import javax.swing.JTextPane;
import com.windowtester.runtime.swing.locator.JButtonLocator;

public class RemoveNumber extends UITestCaseSwing {

	/**
	 * Create an Instance
	 */
	public RemoveNumber() {
		super(hashing.HashingApplication.class);
	}

	/**
	 * Main test method.
	 */
	public void testRemoveNumber() throws Exception {
		IUIContext ui = getUI();
		ui.assertThat(new SwingWidgetLocator(JSplitPane.class).isVisible(false));
		ui.click(new JTextComponentLocator(
				JTextPane.class,
				new SwingWidgetLocator(
						JSplitPane.class,
						0,
						new SwingWidgetLocator(
								JSplitPane.class,
								0,
								new SwingWidgetLocator(
										JSplitPane.class,
										1,
										new SwingWidgetLocator(JSplitPane.class))))));
		ui.enterText("12");
		ui.click(new JButtonLocator("Apply"));
		ui.click(new JTextComponentLocator(JTextPane.class,
				new SwingWidgetLocator(JSplitPane.class, 1,
						new SwingWidgetLocator(JSplitPane.class))));
		ui.enterText("12");
		ui.click(new JButtonLocator("Apply"));
	}

}