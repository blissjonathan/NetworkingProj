package Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server 
{
    private static ServerSocket server;
    private Socket clientConnection;
    static ArrayList<clientFile> files = new ArrayList<clientFile>();
   
    private int portNumber;
    
        
    public Server(int portNumber)
    {
        server = null;
        clientConnection = null;
        this.portNumber = portNumber;
    }
    
    public void start() throws IOException
    { 
        server = new ServerSocket(portNumber); 
    }
    
    
    public void acceptConnection() throws IOException
    {
        clientConnection = server.accept();
        ClientHandler cch = new ClientHandler(clientConnection);
        cch.start();
        
    }
    
    public static void terminate() 
    {
        try
        {
            server.close();
        }
        catch(IOException e)
        {
            System.out.println("Error terminating server connection");
        }
    }
    
    public static void saveAllFiles() throws Exception {
    	for(int i = 0; i < files.size(); i++) {
    			PrintWriter writer;
				writer = new PrintWriter("./files/" + files.get(i).getName(), "UTF-8");
	    	    writer.println(files.get(i).toString());
	    	    writer.close();
	    	    System.out.println("Saved: " + files.get(i).getName());
    	}

    }
    
    public static clientFile getFile(String _name) {
    	clientFile rFile = null;
    	
    	for(int i = 0; i < files.size(); i++) {
    	    if(files.get(i).getName().equals(_name)){
    	     return files.get(i);
    	     }
    	 }		
    	return rFile;   
    }
    
    public static void getAllFiles() {
    	files.clear();
    	File dir = new File("./files/");
    	  File[] directoryListing = dir.listFiles();
    	  if (directoryListing != null) {
    	    for (File child : directoryListing) {
    	    	clientFile newFile = new clientFile(child);
    	    	files.add(newFile);
    	    	System.out.println(newFile.getName() + " added");
    	    }
    	  } else {
    	   
    	  }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
    	
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
    	    public void run() {
    	        try {
					saveAllFiles();
					terminate();
				} catch (Exception e) {
					e.printStackTrace();
				}
    	    }
    	}));
    	
        Server server = new Server(5021);
        
        try
        {  
            server.start();
            System.out.println("Server started");
            getAllFiles();
            
            while (true)
            {
                server.acceptConnection();     

            }
            
        }
         catch (IOException e)
         {

             System.out.println("Unable to establish "
                        + "server connection");
         }
        finally
        {
            server.terminate();
        }
        
    }
    
}


