package Server;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ClientHandler extends Thread
{
    private Socket connection;
    private InputStream clientInput;
    private OutputStream clientOutput;
    private Scanner scan;
    private OutputStreamWriter osw;
    ArrayList<clientFile> files = new ArrayList<clientFile>();
    private String userID;
    private String curFile = null;
    
    
    public ClientHandler(Socket conn)
    {
        connection = conn;
        
        try
        {
            clientInput = connection.getInputStream();
            clientOutput = connection.getOutputStream();
            scan = new Scanner(clientInput);
            osw = new OutputStreamWriter(clientOutput);
        }
        catch(IOException e)
        {
        	
            System.out.println("Error reading/writing from/to client");
        }
            
    }
    
   
    public void refresh() throws IOException {
    
    	for(int i = 0; i < Server.files.size(); i++) {
    			osw.write(Server.files.get(i).getName() + "\r\n");
    			osw.flush();
				osw.write(Server.files.get(i).isOccupied + "\r\n");
				osw.flush();
    }
			osw.write("Refreshed\r\n");
			osw.flush();
   }
  
 
    
    public void checkOut() throws IOException {
    	String rFile = scan.nextLine();
    	System.out.println("Client is attempting to checkout " + rFile);
    	for(int i = 0; i < Server.files.size(); i++) {
    		if(Server.files.get(i).getName().equals(rFile)) {
    			if(Server.files.get(i).isActive()==false) {
    				Server.files.get(i).setActive(); 
					osw.write("Success\r\n");
	    			osw.flush();
	    			osw.write(Server.files.get(i).getText() + "\r\n");
	    			osw.flush();
	    			curFile = rFile;
    			} else {
						osw.write("Fail\r\n");
						osw.flush();	
    			}	
    		}
    	}
    }
    
    public void checkIn() throws Exception {
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    	Date date = new Date();
    	//System.out.println(dateFormat.format(date));
    	String fName = scan.nextLine();
    	String clientData = scan.nextLine();
    	
    	System.out.println("Client is attempting to check in file with name and data: " + fName + clientData);
    	
    	for(int i = 0; i < Server.files.size(); i++) {
    		if(Server.files.get(i).getName().equals(fName)) {
    			Server.files.get(i).setInactive();
    			Server.files.get(i).setUser(userID);
    			Server.files.get(i).setDate(dateFormat.format(date));
    			Server.files.get(i).setText(clientData);
    			System.out.println("Set all properties for file " + Server.files.get(i).getName());
    			curFile = null;
    		}
    	}
    	Server.saveAllFiles();
    }
    
    public void getLatest() throws IOException {
    	ArrayList<clientFile> getList = Server.files;
    	for(int i = 0; i  < getList.size(); i++) {
				osw.write(getList.get(i).getName() + "\r\n");
				osw.flush();
				osw.write(getList.get(i).toString() + "\r\n");
				osw.flush();
    	}
    	osw.write("Finished Sending\r\n");
		osw.flush();	
    }
    
    public void closeConnection() throws IOException
    {
    	if(curFile != null) {
    	for(int i = 0; i < Server.files.size(); i++) {
    		if(Server.files.get(i).getName().equals(curFile)) {
    			Server.files.get(i).setInactive();
    		}
    		}
    	}
         osw.close();
         clientInput.close();
         connection.close();
    }
    
    public void writeFile(String name) {
    	clientFile wr = Server.getFile(name);
    	
    }
    
    @Override
    public void run()
    {
        
        try
        {
            osw.write("Welcome to Server\r\n");
            osw.flush();
            userID = scan.nextLine();
            System.out.println("Set userID " + userID);
            while( true )
            {
            	String message = scan.nextLine();
                if (!( message.equals("Exit")))
                {
                	if(message.equals("Check In")) {
                		try {
							checkIn();
						} catch (Exception e) {
							e.printStackTrace();
						}
                	}
                	if(message.equals("Check Out")) {
                		checkOut();
                	}
                	if(message.equals("Refresh")) {
                		refresh();
                	}
                	
                	if(message.equals("Latest")) {
                		getLatest();
                	}
                    
                }
                else
                {
                    closeConnection();
                    break;
                }
                
            }
        }
        catch(IOException e)
        {
            System.out.println("Error reading/writing from/to client");
        }
    }
            
    
}

