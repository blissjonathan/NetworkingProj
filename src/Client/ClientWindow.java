package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ClientWindow {

	private JFrame frmClient;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientWindow window = new ClientWindow();
					window.frmClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmClient = new JFrame();
		frmClient.setTitle("Client");
		frmClient.setBounds(100, 100, 430, 407);
		frmClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmClient.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JButton btnNewButton = new JButton("Check In");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnNewButton.setBounds(10, 27, 89, 23);
		panel.add(btnNewButton);
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"1", "2", "3", "4"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setBounds(200, 20, 204, 64);
		panel.add(list);
		
		textField = new JTextField();
		textField.setBounds(200, 95, 204, 238);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("Check Out");
		button.setBounds(10, 61, 89, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("Refresh");
		button_1.setBounds(10, 100, 89, 23);
		panel.add(button_1);
		
		JButton button_2 = new JButton("Latest");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button_2.setBounds(10, 136, 89, 23);
		panel.add(button_2);
		
		JMenuBar menuBar = new JMenuBar();
		frmClient.setJMenuBar(menuBar);
		
		JMenuItem menuItem = new JMenuItem("File");
		menuBar.add(menuItem);
	}
}
