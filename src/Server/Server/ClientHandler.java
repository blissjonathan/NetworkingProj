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
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler extends Thread
{
    private Socket connection;
    private InputStream clientInput;
    private OutputStream clientOutput;
    private Scanner scanner;
    private OutputStreamWriter osw;
    ArrayList<clientFile> files = new ArrayList<clientFile>();
    private String userID;
    private clientFile curFile;
    
    
    public ClientHandler(Socket conn)
    {
        connection = conn;
        
        try
        {
            clientInput = connection.getInputStream();
            clientOutput = connection.getOutputStream();
            scanner = new Scanner(clientInput);
            osw = new OutputStreamWriter(clientOutput);
        }
        catch(IOException e)
        {
        	
            System.out.println("Error reading/writing from/to client");
        }
            
    }
    
   
    public void refresh() {
    
    	for(int i = 0; i < Server.files.size(); i++) {
    		
    		try {
				osw.write(Server.files.get(i).toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
   }
  
 
    
    public void checkOut() {
    	String rFile = scanner.nextLine();
    	for(int i = 0; i < Server.files.size(); i++) {
    		if(Server.files.get(i).getName().equals(rFile)) {
    			if(files.get(i).isActive()==false) {
    			Server.files.get(i).setActive(); 
    			Server.files.get(i).setUser(userID);
    			try {
					osw.write("Success\r\n");
	    			osw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			} else {
    				try {
						osw.write("Fail\r\n");
						osw.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				}
    			
    		}
    	}
    }
    
    public void checkIn() {
    	
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
    	osw.write("Finished Sending\r\n");
		osw.flush();	
    }
    
    public void closeConnection() throws IOException
    {
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
            userID = scanner.nextLine();
            System.out.println("Set userID " + userID);
            while( true )
            {
            	String message = scanner.nextLine();
                if (!( message.equals("Exit")))
                {
                	if(message.equals("Check In")) {
                		checkIn();
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

