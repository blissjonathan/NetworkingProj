package Server;
import java.io.File;
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
    
    public void readFile() {
    	File dir = new File("./files/");
    	  File[] directoryListing = dir.listFiles();
    	  if (directoryListing != null) {
    	    for (File child : directoryListing) {
    	    	clientFile newFile = new clientFile(child, "");
    	    	files.add(newFile);
    	    }
    	  } else {
    	   
    	  }
    }
    
    public void refresh(String fileName) {
    	
    }
    
    public void checkIn() {
    	
    	
    }
    
    public void closeConnection() throws IOException
    {
         osw.close();
         clientInput.close();
         connection.close();
    }
    
    @Override
    public void run()
    {
        
        try
        {
            osw.write("Welcome to Server\r\n");
            osw.flush();
            while( true )
            {
            	String message = scanner.nextLine();
                if (!( message.equals("Exit")))
                {
                	if(message.equals("Check In")) {
                		
                	}
                	if(message.equals("Check Out")) {
                		
                	}
                	if(message.equals("Refresh")) {
                		
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
