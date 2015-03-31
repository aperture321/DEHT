package hashing;

import javax.swing.JSplitPane;
import javax.swing.JTextPane;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swing.SwingWidgetLocator;
import com.windowtester.runtime.swing.UITestCaseSwing;
import com.windowtester.runtime.swing.locator.JButtonLocator;
import com.windowtester.runtime.swing.locator.JRadioButtonLocator;
import com.windowtester.runtime.swing.locator.JTextComponentLocator;
import com.windowtester.runtime.swing.locator.NamedWidgetLocator;

public class AddNumber extends UITestCaseSwing {

	/**
	 * Create an Instance
	 */
	public AddNumber() {
		super(hashing.HashingApplication.class);
	}

	/**
	 * Main test method.
	 */
	public void testAddNumber() throws Exception {
		IUIContext ui = getUI();
		ui.assertThat(new JRadioButtonLocator("Local").isVisible(false));
		ui.click(new JTextComponentLocator(JTextPane.class,
				new SwingWidgetLocator(JSplitPane.class, 0,
						new SwingWidgetLocator(JSplitPane.class, 0,
								new SwingWidgetLocator(JSplitPane.class,
										new NamedWidgetLocator(
												"null.contentPane"))))));
		ui.enterText("12");
		ui.click(new JButtonLocator("Apply"));
		ui.click(new JTextComponentLocator(JTextPane.class,
				new SwingWidgetLocator(JSplitPane.class, 1,
						new SwingWidgetLocator(JSplitPane.class))));
		ui.enterText("12");
		ui.click(new JButtonLocator("Apply"));
	}

}