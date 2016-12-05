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

    }
    
    public void checkIn() {
    	String rFile = scanner.nextLine();
    	for(int i = 0; i < Server.files.size(); i++) {
    		if(Server.files.get(i).getName().equals(rFile)) {
    			if(userID.equals(Server.files.get(i).getUser())) {
    			Server.files.get(i).setActive();
    			curFile = Server.files.get(i);
    			
    			while( true ) {
    				
    			}
    			
    			} else {
    				//send not owner error
    			}
    			
    		}
    	}
    	
    
    	
    	
    }
    
    public void checkOut() {
    	
    }
    
    public void getLatest() {
    	
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
                    osw.write(message + "\r\n");
                    osw.flush();
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

