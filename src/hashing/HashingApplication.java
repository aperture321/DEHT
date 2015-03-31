package hashing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.ComponentOrientation;

public class HashingApplication {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HashingApplication window = new HashingApplication();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HashingApplication() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);
		
		JSplitPane splitPane_3 = new JSplitPane();
		splitPane_1.setLeftComponent(splitPane_3);
		
		JTextPane textPane = new JTextPane();
		splitPane_3.setRightComponent(textPane);
		
		JLabel lblAddNumber = new JLabel("Add Number");
		splitPane_3.setLeftComponent(lblAddNumber);
		splitPane_3.setDividerLocation(180);
		
		JSplitPane splitPane_4 = new JSplitPane();
		splitPane_4.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_1.setRightComponent(splitPane_4);
		
		JSplitPane splitPane_5 = new JSplitPane();
		splitPane_5.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_4.setRightComponent(splitPane_5);
		
		JSplitPane splitPane_7 = new JSplitPane();
		splitPane_5.setLeftComponent(splitPane_7);
		
		JTextPane textPane_2 = new JTextPane();
		splitPane_7.setRightComponent(textPane_2);
		
		JLabel lblNewLabel_1 = new JLabel("Remove Number");
		splitPane_7.setLeftComponent(lblNewLabel_1);
		splitPane_7.setDividerLocation(180);
		
		JSplitPane splitPane_8 = new JSplitPane();
		splitPane_8.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		splitPane_8.setAlignmentY(Component.CENTER_ALIGNMENT);
		splitPane_8.setAlignmentX(Component.CENTER_ALIGNMENT);
		splitPane_5.setRightComponent(splitPane_8);
		
		JButton btnApply = new JButton("Apply");
		splitPane_8.setLeftComponent(btnApply);
		
		JLabel label = new JLabel("");
		splitPane_8.setRightComponent(label);
		splitPane_8.setDividerLocation(180);
		
		JSplitPane splitPane_6 = new JSplitPane();
		splitPane_4.setLeftComponent(splitPane_6);
		
		JTextPane textPane_1 = new JTextPane();
		splitPane_6.setRightComponent(textPane_1);
		
		JLabel lblNewLabel = new JLabel("Find Number");
		splitPane_6.setLeftComponent(lblNewLabel);
		splitPane_6.setDividerLocation(180);
		
		JSplitPane splitPane_2 = new JSplitPane();
		splitPane.setLeftComponent(splitPane_2);
		
		JRadioButton rdbtnZookeeper = new JRadioButton("Zookeeper");
		splitPane_2.setRightComponent(rdbtnZookeeper);
		
		JRadioButton rdbtnLocal = new JRadioButton("Local");
		rdbtnLocal.setHorizontalAlignment(SwingConstants.TRAILING);
		rdbtnLocal.setSelected(true);
		splitPane_2.setLeftComponent(rdbtnLocal);
		splitPane_2.setDividerLocation(180);
	}

}
