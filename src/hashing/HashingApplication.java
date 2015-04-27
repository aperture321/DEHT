package hashing;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;

import hashing.Index;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

import java.awt.TextArea;
public class HashingApplication implements Serializable{

	private JFrame frame;
	private final Action action = new SwingAction();
	Choice choice = new Choice();
	
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
	 * @throws IOException 
	 */
	public HashingApplication() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		final Index indexer = new Index("indexTest2","dataTest2",4);
		final zooIndex zindexer = new zooIndex("indexTest", "dataTest", 4);
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
		
		final JTextPane textPane_add = new JTextPane();
		splitPane_3.setRightComponent(textPane_add);
		
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
		
		final JTextPane textPane_remove = new JTextPane();
		splitPane_7.setRightComponent(textPane_remove);
		
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
		
		final TextArea label = new TextArea();
		label.setEditable(false);
		splitPane_8.setRightComponent(label);
		splitPane_8.setDividerLocation(180);
		
		JSplitPane splitPane_6 = new JSplitPane();
		splitPane_4.setLeftComponent(splitPane_6);
		
		final JTextPane textPane_find = new JTextPane();
		splitPane_6.setRightComponent(textPane_find);
		
		JLabel lblNewLabel = new JLabel("Find Number");
		splitPane_6.setLeftComponent(lblNewLabel);
		splitPane_6.setDividerLocation(180);
		
		//old choice code
		choice.add("Local");
		choice.add("Zookeeper");
		splitPane.setLeftComponent(choice);
		
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label.setText("");
				if(!textPane_add.getText().isEmpty()) {
					label.setText(label.getText() + "Adding number:");
					try {
						label.setText(label.getText() + Add(textPane_add.getText()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if(!textPane_remove.getText().isEmpty()) {
					label.setText(label.getText() + "Removing number:");
					try {
						label.setText(label.getText() + Remove(textPane_remove.getText()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if(!textPane_find.getText().isEmpty()) {
					label.setText(label.getText() + "Finding number:");
					try {
						label.setText(label.getText() + Find(textPane_find.getText()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

			private String Add(String text) throws IOException {
				int number;
				try {
					number = Integer.parseInt(text);
				} catch (NumberFormatException e ) {
					return "ERROR: Number not formatted correctly.\n";
				}
				if (choice.getSelectedIndex() == 0)
					indexer.insert(number);
				else if (choice.getSelectedIndex() == 1)
					zindexer.insert(number);
				return "Success.\n";
			}
			private String Remove(String text) throws IOException {
				int number;
				try {
					number = Integer.parseInt(text);
				} catch (NumberFormatException e ) {
					return "ERROR: Number not formatted correctly.\n";
				}
				if (choice.getSelectedIndex() == 0)
					indexer.delete(number);
				else if (choice.getSelectedIndex() == 1)
					zindexer.delete(number);
				return "Success.\n";
			}
			private String Find(String text) throws IOException {
				int number;
				try {
					number = Integer.parseInt(text);
				} catch (NumberFormatException e ) {
					return "ERROR: Number not formatted correctly.\n";
				}
				if (choice.getSelectedIndex() == 0)
					return (indexer.contains(number)) ? "Found.\n" : "Not Found";
				if (choice.getSelectedIndex() == 1)
					return (zindexer.contains(number)) ? "Found.\n" : "Not Found";
				return "Selected command invalid.";
			}
		});
	}
	
	public int getChoiceIndex(){
		return choice.getSelectedIndex();
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}
