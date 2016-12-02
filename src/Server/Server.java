package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server 
{
    private ServerSocket server;
    private Socket clientConnection;
   
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
    
    public void terminate() 
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        Server server = new Server(5021);
        
        try
        {  
            server.start();
            
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


