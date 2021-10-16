# Distributed Systems Assignment 1

The client - server can be run in the following steps
1. Open two command prompts in the directory where the files are stored.
2. In the first terminal enter 'java Server.java <portNumber>'
3. In the second terminal enter 'java Client.java localhost <portNumber>'

ex. Server => java Server.java 6066
ex. Client => java Client.java localhost 6066

You may then begin to type requests in the client terminal
Commands include:  
  fib  [int]  
  prime [int]  
  stats  
  help  
  close  
  
  Descrition:  
  This Client-Server architecture has two main features, a prime number checker and fibonacci sequence generator. The prime number checker looks for all the prime numbers from 2 to the given number. So if the command 'prime 10' is called, the output becomes 2 3 5 7. In the same sense, the fibonacci sequence generator outputs the fibonacci sequence up to the given value in the sequence. So if the command 'fib 10' is called, the output becomes 1 1 2 3 5 8 13 21 34 55. The server also handles commands help, which prints the list of commands; close, which closes the client connection to the server; and stats, which is the first of my novel features, keeps track of how many clients have connected to the server, how many requests have been processed and how long the server has been running for, which then gets printed to the client. My second novel feature, is that after a request is completed, it tells you how long it took to complete that request, for example prime 10 is calculated in 0.007 seconds.
