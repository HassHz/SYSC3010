package database;

import java.io.IOException;
import java.net.*;


public class server {

	private static String fileLocation = "database.xml"; 	
	static int port = 5050;
	
    public static void main(String[] args) {
    	    	
    	new server().run(port);
    }

    public void run(int port) {    
      try {
    	
    	database database = new database("Carleton", fileLocation);
        
    	DatagramSocket serverSocket = new DatagramSocket(port);
        
    	byte[] receiveData = new byte[8];
  
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        System.out.printf("Listening on udp:%s:%d%n",InetAddress.getLocalHost().getHostAddress(), port);   
        
        while(true)
        {
              serverSocket.receive(receivePacket);
              String dataReceived = new String( receivePacket.getData() );
              System.out.println("RECEIVED: " + dataReceived);
              InetAddress IPAddress = receivePacket.getAddress();
              
              
              if (dataReceived.contains("notification") && dataReceived.contains("fire") ){
            	  
                  String sendString = "received";
                  byte[] sendData = sendString.getBytes("UTF-8");
                  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());
                  serverSocket.send(sendPacket);	  
              
              }
              
              else if (dataReceived.contains("add") || dataReceived.contains("remove")|| dataReceived.contains("get")){
            	   
	            	  if (dataReceived.contains("get:")){
	            		    
	            		  String[] entryData = dataReceived.split(":");
	            		  if (entryData.length > 2){
	            			
	                      String sendString = database.getMemberById(fileLocation, entryData[1]);
	                      
	                      byte[] sendData = sendString.getBytes("UTF-8");
	                      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());
	                      serverSocket.send(sendPacket);	
	            		  }	            		    
	            	  }
	            	  
	            	  else if (dataReceived.contains("add:")){
	            		  
	            		  String[] entryData = dataReceived.split(":");
	            		  if (entryData.length > 3){
	            			  
	            			  database.addMember(fileLocation, entryData[1], entryData[2], entryData[3]);}
	            		  
	            	  }
	            	  
	            	  else if (dataReceived.contains("remove:")){
            		  
	            		  String[] entryData = dataReceived.split(":");
	            		  if (entryData.length > 3){
	            			  database.removeMember(fileLocation, entryData[1], entryData[2]);}	            		              		  
            	  }
              }  
        }
      } catch (IOException e) {
              System.out.println(e);
      }
    }
}
