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
    
    public void getFiles() {
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
    
    public String readFile(String fileName) throws IOException {
    	String output = "";
    	String line = null;
    	
    		 FileReader fileReader;
			try {
				fileReader = new FileReader(fileName);
				BufferedReader bufferedReader = new BufferedReader(fileReader);

	            while((line = bufferedReader.readLine()) != null) {
	                output = output + line;
	            }   
	            bufferedReader.close();         
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
    	
    	 return line;           
    	}
    
    public void refresh() {
    	
    	this.getFiles();
    }
    
    public void checkIn() {
    	
    	
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

