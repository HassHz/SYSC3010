package database;


import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.*;
//import java.util.*;


public class server {

	private static String fileLocation = "database.xml"; 	
	static int port = 5000;
	
    public static void main(String[] args) {
    	    	
    	new server().run(port);
    }

    public void run(int port) {    
      try {
    	
    	database database = new database("Carleton", fileLocation);
        
    	DatagramSocket serverSocket = new DatagramSocket(port);
        
    	byte[] receiveData = new byte[8];

        System.out.printf("Listening on udp:%s:%d%n",
                InetAddress.getLocalHost().getHostAddress(), port);     
        DatagramPacket receivePacket = new DatagramPacket(receiveData,
                           receiveData.length);

        while(true)
        {
              serverSocket.receive(receivePacket);
              String dataReceived = new String( receivePacket.getData() );
              System.out.println("RECEIVED: " + dataReceived);
              
              if (dataReceived.contains("notification")){
            	  
                  InetAddress IPAddress = receivePacket.getAddress();
                  String sendString = "received";
                  byte[] sendData = sendString.getBytes("UTF-8");
                  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                       IPAddress, receivePacket.getPort());
                  serverSocket.send(sendPacket);	  
              
              }
              
              else if (dataReceived.contains("add") 
            		  || dataReceived.contains("remove")
            		  || dataReceived.contains("get")){
            	   
	            	  if (dataReceived.contains("get:")){
	            		  
	            		  String[] entryData = dataReceived.split("get:");
	            		  if (entryData.length > 2){
	            		  database.getMemberById(fileLocation, entryData[1]);}
	            		  else{continue;}
	            		  
	            	  }
	            	  else if (dataReceived.contains("add:")){
	            		  
	            		  String[] entryData = dataReceived.split("add:");
	            		  if (entryData.length > 4){
	            			  
	            		  database.addMember(fileLocation, entryData[1], entryData[2], entryData[3]);}
	            		  
	            		  else{continue;}
	            	  } 
	            	  else if (dataReceived.contains("remove:")){
            		  
	            		  String[] entryData = dataReceived.split("remove:");
	            		  if (entryData.length > 3){
	            			  
	            		  database.removeMember(fileLocation, entryData[1], entryData[2]);}
	            		  
	            		  else{continue;}
            		  
            	  }
              }
         
              
              
              
            
        }
      } catch (IOException e) {
              System.out.println(e);
      }
    }
}