package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JLabel;

public class ClientWindow {

	private JFrame frmClient;
	private JTextField textField;
	private String userID;
	private ArrayList<clientFile> files = new ArrayList<clientFile>();
	private JList list;
	
	InputStream serverInput = null;
    OutputStream serverOutput = null;
    Scanner scan = null;
    OutputStreamWriter osw = null;

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
		userID = UUID.randomUUID().toString();
		System.out.println("User ID set to: " + userID);


            Socket socket;
			try {
				socket = new Socket("127.0.0.1", 5021);
	            serverOutput = socket.getOutputStream();
	            serverInput = socket.getInputStream();
	            scan = new Scanner(serverInput);
	            osw = new OutputStreamWriter(serverOutput);
	            String message = scan.nextLine();
	            System.out.println(message);
	            osw.write(userID);
	            osw.flush();
	             
	            Scanner keyboard = new Scanner(System.in);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		
		
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
				try {
					osw.write("Check In");
					osw.flush();
					osw.write(list.getSelectedValue().toString());
					osw.flush();
					} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton.setBounds(10, 27, 89, 23);
		panel.add(btnNewButton);
		
		list = new JList();
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
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					osw.write("Check Out");
					osw.flush();
					osw.write(list.getSelectedValue().toString());
					osw.flush();
					if(scan.nextLine().equals("Success")) {
						//put data into text editor
					} else {
						//File is active already
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button.setBounds(10, 61, 89, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("Refresh");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					osw.write("Refresh");
					osw.flush();
					while(true){
					if(!scan.nextLine().equals("Refreshed")){
						
					}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_1.setBounds(10, 100, 89, 23);
		panel.add(button_1);
		
		JButton button_2 = new JButton("Latest");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getList();
			}
		});
		button_2.setBounds(10, 136, 89, 23);
		panel.add(button_2);
		
		JLabel lblLastUserEdited = new JLabel("Last User Edited:");
		lblLastUserEdited.setBounds(10, 228, 89, 14);
		panel.add(lblLastUserEdited);
		
		JLabel lblDateModified = new JLabel("Date Modified:");
		lblDateModified.setBounds(10, 260, 89, 14);
		panel.add(lblDateModified);
		
		JMenuBar menuBar = new JMenuBar();
		frmClient.setJMenuBar(menuBar);
		
		JMenuItem menuItem = new JMenuItem("File");
		menuBar.add(menuItem);
	}
	
	public void getList() {
		try {
			osw.write("Latest");
			osw.flush();
			String message  = null;
			while ( true ) {
				message = scan.nextLine();
				if(!(message.equals("Finished Sending"))) {
					try{
					    PrintWriter writer = new PrintWriter(message, "UTF-8");
					    writer.println(scan.nextLine());
					    writer.close();
					} catch (IOException e) {
					   // do something
					}
				} else {
					break;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
