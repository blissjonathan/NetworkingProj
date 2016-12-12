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
import java.util.UUID;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ListSelectionModel;

public class ClientWindow {

	private JFrame frmClient;
	private JTextField textField;
	private String userID;
	private ArrayList<clientFile> files = new ArrayList<clientFile>();
	private JList list;
	
	JLabel lblLastUserEdited;
	JLabel lblDateModified;
	
	InputStream serverInput = null;
    OutputStream serverOutput = null;
    Scanner scan = null;
    OutputStreamWriter osw = null;
    
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
				osw.write("Exit");
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
		frmClient.setTitle("Client");
		frmClient.setBounds(100, 100, 430, 407);
		frmClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmClient.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JButton btnNewButton = new JButton("Check In");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(hasFile == true) {
				try {
					osw.write("Check In\r\n");
					osw.flush();
					osw.write(list.getSelectedValue().toString() +"\r\n");
					osw.flush();
					osw.write(textField.getText());
					osw.flush();
					hasFile = false;
					} catch (IOException e1) {
					
					e1.printStackTrace();
				}
			} else {	
				JOptionPane.showMessageDialog(panel, "You have not checked out a file.", "Error", JOptionPane.ERROR_MESSAGE);
			}
				
			}
		});
		btnNewButton.setBounds(10, 27, 89, 23);
		panel.add(btnNewButton);
		
		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String tempFile = list.getSelectedValue().toString();
				for(int i = 0; i < files.size(); i++) {
					if(files.get(i).getName().equals(tempFile)) {
						lblLastUserEdited.setText(files.get(i).getUser());
						lblDateModified.setText(files.get(i).getDate());
						
					}
				}
			}
		});
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
					osw.write("Check Out\r\n");
					osw.flush();
					osw.write(list.getSelectedValue().toString() + "\r\n");
					osw.flush();
					if(scan.nextLine().equals("Success")) {
						String data = scan.nextLine();
						textField.setText(data);
						textField.repaint();
						hasFile = true;
					} else {
						JOptionPane.showMessageDialog(panel, "File is in use.", "Error", JOptionPane.ERROR_MESSAGE);
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
					osw.write("Refresh\r\n");
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
		
		lblLastUserEdited = new JLabel("Last User Edited:");
		lblLastUserEdited.setBounds(10, 228, 89, 14);
		panel.add(lblLastUserEdited);
		
		lblDateModified = new JLabel("Date Modified:");
		lblDateModified.setBounds(10, 260, 89, 14);
		panel.add(lblDateModified);
	}
	
	
	public void refreshList() {
		DefaultListModel<String> model = new DefaultListModel<>();
		list = new JList<>(model);

		for ( int i = 0; i < files.size(); i++ ){
		  model.addElement(files.get(i).getName());
		}
		list.revalidate();
		list.repaint();
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
					    PrintWriter writer = new PrintWriter(message + ".txt", "UTF-8");
					    String data = scan.nextLine();
					    writer.println(data);
					    writer.close();
					    File newFile = new File("./" + message + ".txt");
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
