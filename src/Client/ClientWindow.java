package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JLabel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

public class ClientWindow {

	private JFrame frmClient;
	private JTextField txtField;
	private String userID;
	private ArrayList<clientFile> files = new ArrayList<clientFile>();
	private JList list;
	
	JPanel panel;
	
	JTextArea lblLastUserEdited;
	JTextArea lblDateModified;
	JTextArea lblIsOccupied;
	
	DefaultListModel<String> model;
	
	InputStream serverInput = null;
    OutputStream serverOutput = null;
    Scanner scan = null;
    OutputStreamWriter osw = null;
    
    String curFile = null;
    
    private boolean hasFile = false;

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
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		    public void run() {
		       try {
				osw.write("Exit\r\n");
			    osw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    }
		}));
		
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
	            osw.write(userID + "\r\n");
	            osw.flush();
	            
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		
		
		frmClient = new JFrame();
		frmClient.setTitle("Client - " + userID);
		frmClient.setBounds(100, 100, 430, 407);
		frmClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		frmClient.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JButton btnNewButton = new JButton("Check In");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(hasFile == true) {
				try {
					osw.write("Check In\r\n");
					osw.flush();
//					StringTokenizer st = new StringTokenizer(list.getSelectedValue().toString(), ".");
//					String sendName = st.nextToken();
					System.out.println("Attempting to check in ");
					osw.write(list.getSelectedValue().toString() + "\r\n");
					osw.flush();
					osw.write(txtField.getText() + "\r\n");
					osw.flush();
					hasFile = false;
					txtField.setText("");
					} catch (IOException e1) {
					
					e1.printStackTrace();
				}
			} else {	
				JOptionPane.showMessageDialog(panel, "You have not checked out a file.", "Error", JOptionPane.ERROR_MESSAGE);
			}
				
			}
		});
		btnNewButton.setBounds(10, 27, 123, 23);
		panel.add(btnNewButton);
		
		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		model = new DefaultListModel<>();
		list.setModel(model);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String tempFile = list.getSelectedValue().toString();
				for(int i = 0; i < files.size(); i++) {
					if(files.get(i).getName().equals(tempFile)) {
						lblLastUserEdited.setText("Last User: " + files.get(i).getUser());
						lblDateModified.setText("Date Modified: " + files.get(i).getDate());
						lblIsOccupied.setText("Is Occupied: " + files.get(i).isOccupied);
						
						lblLastUserEdited.setSize(lblLastUserEdited.getPreferredSize());
						lblDateModified.setSize(lblDateModified.getPreferredSize());
						
					}
				}
			}
		});

		list.setBounds(200, 20, 204, 64);
		panel.add(list);
		
		txtField = new JTextField();
		txtField.setHorizontalAlignment(SwingConstants.CENTER);
		txtField.setBounds(200, 95, 204, 238);
		panel.add(txtField);
		txtField.setColumns(10);
		
		JButton button = new JButton("Check Out");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(hasFile == false) {
					osw.write("Check Out\r\n");
					osw.flush();
//					System.out.println("Attempting to check out " + list.getSelectedValue().toString());
//					StringTokenizer st  = new StringTokenizer(list.getSelectedValue().toString(), ".");
//					String sendName = st.nextToken();
					osw.write(list.getSelectedValue().toString() + "\r\n");
					osw.flush();
					String message = scan.nextLine();
					System.out.println("Got message: " + message);
					if(message.equals("Success")) {
						String data = scan.nextLine();
						System.out.println("Got text data from file: " + data );
						txtField.setText(data);
						txtField.repaint();
						hasFile = true;
					} else {
						JOptionPane.showMessageDialog(panel, "File is in use.", "Error", JOptionPane.ERROR_MESSAGE);
					}
					} else {
						JOptionPane.showMessageDialog(panel, "You have a file currently checked out. Check in first.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button.setBounds(10, 61, 123, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("Refresh");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					getList();
					osw.write("Refresh\r\n");
					osw.flush();

					while(true) {
						String message = scan.nextLine();
						if(!message.equals("Refreshed")) {
							String fName = message;
							String isActive = scan.nextLine();
							for(int i =0;  i < files.size(); i++) {
								if(files.get(i).getName().equals(fName)) {
									if(isActive.equalsIgnoreCase("true")) {
										files.get(i).setActive();
									}
									
									if(isActive.equalsIgnoreCase("false")) {
										files.get(i).setInactive();
									}
								}
							}
						} else {
							break;
						}
					}
					refreshList();
					
					
//					while(true){								//OLD CODE
//						String message = scan.nextLine();
//					if(!message.equals("Refreshed")){
//						
//						for(int i=0;i<files.size();i++){
//							
//							files.get(i).setUser(scan.nextLine());
//							files.get(i).setDate(scan.nextLine());
//							if (scan.nextLine() == "true")
//								files.get(i).setActive();
//							else
//								files.get(i).setInactive();
//						}
//					}
//					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_1.setBounds(10, 100, 123, 23);
		panel.add(button_1);
		
		JButton button_2 = new JButton("Latest");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getList();
				refreshList();
			}
		});
		button_2.setBounds(10, 136, 123, 23);
		panel.add(button_2);
		
		lblLastUserEdited = new JTextArea("Last User Edited:");
		lblLastUserEdited.setRows(4);
		lblLastUserEdited.setLineWrap(true);
		lblLastUserEdited.setEditable(false);
		lblLastUserEdited.setBounds(10, 170, 180, 71);
		panel.add(lblLastUserEdited);
		
		lblDateModified = new JTextArea("Date Modified:");
		lblDateModified.setRows(4);
		lblDateModified.setLineWrap(true);
		lblDateModified.setEditable(false);
		lblDateModified.setBounds(10, 252, 180, 71);
		panel.add(lblDateModified);
		
		lblIsOccupied = new JTextArea("Is Occupied:");
		lblIsOccupied.setLineWrap(true);
		lblIsOccupied.setEditable(false);
		lblIsOccupied.setBounds(10, 334, 180, 23);
		panel.add(lblIsOccupied);
		
		getList();
		refreshList();
	}
	
	
	public void refreshList() {
		model.clear();
		

		for ( int i = 0; i < files.size(); i++ ){
		  model.addElement(files.get(i).getName());
		}
		
		panel.revalidate();
		panel.repaint();
		
	}
	
	public void getList() {
		try {
			osw.write("Latest\r\n");
			osw.flush();
			files.clear();
			String message  = null;
			while ( true ) {
				message = scan.nextLine();
				if(!message.equals("Finished Sending")) {
					System.out.println(message);
					try{
					    PrintWriter writer = new PrintWriter(message, "UTF-8");
					    String data = scan.nextLine();
					    writer.println(data);
					    writer.close();
					    File newFile = new File("./" + message);
					    clientFile curFile = new clientFile(newFile);
					    if(curFile.check() == true) {
					    files.add(curFile);
					    } else {
					    	//bad sending message...
					    	newFile.delete();
					    	System.out.println("downloaded bad file");
					    	break;
					    }
					} catch (IOException e) {
					   System.out.println("Error downloading files");
					}
				} else {
					System.out.println("Finished downloading files");
					break;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
