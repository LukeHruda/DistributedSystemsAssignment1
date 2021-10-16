import java.net.*;
import java.io.*;

public class Server extends Thread {

   //decalare socket value
   private ServerSocket socket;

   //implement variables for Novel Features
   private static int clientCount;
   private static int calculationsPerformed;
   private static long serverStart;
   
   //create server on given port
   //set time out to 1 minute
   public Server(int port) throws IOException {
      socket = new ServerSocket(port);
      socket.setSoTimeout(60000);
   }

   //function that parses a string to integer
   //returns -1 if the string is not able to be parsed
   public static Integer parseToInt(String value){
      try {
         return Integer.parseInt(value);
      } catch (NumberFormatException e) {
         return -1;
      }
   }

   //basic recursive fibonacci function
   public static int fibonacci(int n)
   {
      if (n <= 1)
         return n;
      return fibonacci(n-1) + fibonacci(n-2);
   }

   //prime number checker given range
   public static String primeNum(int n)
   {
      //create initial variables
      int limit = n;
      String  reponse = "";
      int i = 0;
      for (i = 1; i <= limit; i++)         
      { 		  	  
         int countDiv=0; 	  
         for(n =i; n>=1; n--)
         {
            if(i%n==0)
            {
               //count the number of factors that i has
               countDiv = countDiv + 1;
            }
	      }
         //if the number only has 2 factors 1 and itself, it is a prime number
         if (countDiv ==2)
         {
            //add that number onto the repsonse string
            reponse = reponse + i + " ";
	      }	
      }	
      return reponse;
   }

   //main run method for server
   public void run() {
      //create the response string
      String response = new String();
      while(true) {
         //call back for when client disconnects
         restart:
         try {
            //Notify that the server has opened on the given port
            System.out.println("Server opened on port  " + socket.getLocalPort());

            //accept the connection
            Socket server = socket.accept();
            
            //Get the connection address, and create the input stream for the server
            System.out.println("Incoming connection from: " + server.getRemoteSocketAddress());
            DataInputStream read = new DataInputStream(server.getInputStream());
            
            //read data packet sent from client
            System.out.println(read.readUTF());

            //create output stream and write to the client
            DataOutputStream write = new DataOutputStream(server.getOutputStream());
            write.writeUTF("Connection Established: " + server.getLocalSocketAddress());
            
            //increment the client counter
            clientCount++;

            //loop while the client is connectioned
            while(true)
            {
               //create / reset the request string and read from the client
               String request = new String();
               request = read.readUTF();
               
               //print the request and separate based on white spaces into string array
               System.out.println(request);
               String[] parsed = request.split("\\s+");
               
               //if the request is to close the server, 
               //write goodbye message and return to the restart call back
               if(parsed[0].equals("close"))
               {
                  write.writeUTF("Goodbye");
                  server.close();
                  break restart;
               }
               //user calls for Fibonacci
               else if(parsed[0].equals("fib"))
               {
                  //check if correct number of arguments
                  if(parsed.length == 2)
                  {
                     //parse the second argument to an integer
                     //check if it is not -1 for parse error, limit to 1 to 46 to prevent overflow
                     int value = parseToInt(parsed[1]);
                     if( (0 < value) && (value < 47))
                     {  
                        //incrememnt calculations counter
                        calculationsPerformed++;
                        //get start time of operation
                        long startTime = System.currentTimeMillis();
                        //begin formatting response
                        response = "The "+value+" values of fibonacci are:\n";
                        //calculate all the values of fibonacci up to given value
                        for(int i = 1; i<=value;i++)
                        { 
                           response += String.valueOf(fibonacci(i))+" ";
                        }
                        //grab the end time, and calculate the elapsed time in seconds
                        long endTime = System.currentTimeMillis();
                        long timeTake = endTime - startTime;
                        Double seconds = (double)(timeTake)/1000;
                        response += "\nOperation completed in: "+seconds+"s";
                        //write the repsonse to the client
                        write.writeUTF(response);
                     }
                     //if the value is 47 or above, write error message to prevent integer overflow
                     else if(value> 46)
                     {
                        write.writeUTF("ERROR. Value must be between 1 and 46 inclusive.");
                     }
                     //if -1 write back that the second value cannot be parsed to integer
                     else
                     {
                        write.writeUTF("ERROR. Value entered is not a number");
                     }
                  }
                  //if parsed length is not 2, write back error message
                  else{
                     write.writeUTF("ERROR. Incorrect number of arguments for fib command");
                  }
               }
               //user calls for prime number checker
               else if(parsed[0].equals("prime"))
               {
                  //check if correct number of arguments
                  if(parsed.length == 2)
                  {
                     //parse the second argument to an integer
                     //check if it is not -1 for parse error,
                     int value = parseToInt(parsed[1]);
                     if(value > 0)
                     {
                        //incrememnt calculations counter
                        calculationsPerformed++;
                        //get start time of operation
                        long startTime = System.currentTimeMillis();
                        //begin formatting response
                        response = "These are the prime numbers up to: " +value+"\n" + primeNum(value);
                        //grab the end time, and calculate the elapsed time in seconds
                        long endTime = System.currentTimeMillis();
                        long timeTake = endTime - startTime;
                        Double seconds = (double)(timeTake)/1000;
                        response += "\nOperation completed in: "+seconds+"s";
                        //write the repsonse to the client
                        write.writeUTF(response);
                     }
                     //if -1 write back that the second value cannot be parsed to integer
                     else
                     {
                        write.writeUTF("ERROR. Value entered is not a number");
                     }
                  }
                  //if parsed length is not 2, write back error message
                  else{
                     write.writeUTF("ERROR. Incorrect number of arguments for prime command");
                  }
               }
               //if user requests for stats
               else if(parsed[0].equals("stats"))
               { 
                  //get total run time of the server based on current time and server start time
                  long currentTime = System.currentTimeMillis();
                  long timeTake = currentTime - serverStart;

                  //convert the elapsed time into Hours - Minutes - Seconds
                  Double totalSecs = (double)(timeTake)/1000;
                  String hours = String.format("%.0f",totalSecs / 3600);
                  String minutes = String.format("%.0f",totalSecs % 3600 / 60);
                  String seconds = String.format("%.0f",totalSecs % 60);
                  //write back how many client has been connected, how many calculations performed and server runtime
                  write.writeUTF("The server has had a total of: "+clientCount+
                  " clients connected and performed: "+calculationsPerformed+" requests in total.\n"+
                  "The Server has been active for: "+hours+":"+minutes+":"+seconds);
               }
               //user requests help, write out the list of commands to the user
               else if(parsed[0].equals("help"))
               {
                  write.writeUTF("List of commands include:\nfib <number> - Returns the Fibonacci sequence up tp that number.\n"
                  +"prime <number> - Returns all the prime numbers from two to the given value\n"+
                  "stats - returns the stats of the server\nclose - terminates server connection");
               }
               //parsed command is not recognized, recommend the user call help
               else
               {
                  write.writeUTF("Command not recognized. Type \"help\" for list of commands.");
               }
               //reset the parsed string
               parsed = null;
            }
            //socket has timed out
         } catch (SocketTimeoutException s) {
            System.out.println("The Socket has timed out");
            break;
         } catch (IOException e) {
            e.printStackTrace();
            break;
         }
      }
   }

   public static void main(String [] args) {
      //get the port number from the user arguments
      int port = Integer.parseInt(args[0]);
      //set calculations performed and client counter to 0
      calculationsPerformed = 0;
      clientCount = 0;
      //get the server start time
      serverStart = System.currentTimeMillis();
      //create the server
      try {
         Thread t = new Server(port);
         t.start();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}