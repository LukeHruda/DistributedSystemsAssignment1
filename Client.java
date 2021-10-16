import java.net.*;
import java.io.*;

public class Client {
   public static void main(String [] args) {
      //get the host name from the first argument
      String hostName = args[0];
      //get the port number from the second argument
      int port = Integer.parseInt(args[1]);
      try {
         //attempt the connection
         System.out.println("Attempting connection to " + hostName + " on port " + port);
         Socket client = new Socket(hostName, port);
         
         //create the output stream
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream write = new DataOutputStream(outToServer);
         
         //write the client address to the server
         write.writeUTF("Attempting to connect from " + client.getLocalSocketAddress());

         //create the input stream
         InputStream inFromServer = client.getInputStream();
         DataInputStream in = new DataInputStream(inFromServer);
         
         //print server response
         System.out.println(in.readUTF());

         while(true)
         {
            // Enter data using BufferReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Reading data using readLine
            String input = reader.readLine();

            //if the user enters close
            if(input.equals("close"))
            {
               //write close to server
               write.writeUTF(input);
               
               //recieve the close confirmation
               System.out.println(in.readUTF());

               //close the client
               client.close();
               //end code here
               System.exit(0);
            }
            //write the user input to the server
            else
            {
               write.writeUTF(input);
               //read the server response
               System.out.println(in.readUTF());
            }
         }        
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}