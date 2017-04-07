
import java.io.IOException;
import java.net.*;

public class server {

	//set to false to disable logging statements
	private static boolean debugging = true;

	//entries will be written to "database.xml", which will be located in the current directory
	private static String fileLocation = "database.xml";
	
	//All parts of the system headless, desktop, mobile-app, will be sending/receiving 
	static int port = 5050;

	//starts a new server
    public static void main(String[] args) {
    	new server().run(port);
    }
    
    
    public void run(int port) {
    	try {
    		
    		//creates a new database in the file "fileLocation" 
            database database = new database("Carleton", fileLocation);

            @SuppressWarnings("resource")

            //constructs socket to receive/send. Creates packet "receivePacket" that will be used to receive packets
            DatagramSocket serverSocket = new DatagramSocket(port);
            byte[] receiveData = new byte[256];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            if (debugging) {System.out.printf("Listening on udp:%s:%d%n",InetAddress.getLocalHost().getHostAddress(), port);}

            
            //server runs continuously
        while(true) {
        	
        	
        	//receive a packet and get the containing command string along with source IP
            serverSocket.receive(receivePacket);
            String dataReceived = new String(receivePacket.getData(), 0, receivePacket.getLength());
            if (debugging) {System.out.println("RECEIVED: " + dataReceived);}
            InetAddress IPAddress = receivePacket.getAddress();

            //if the packet is a notification from the headless-pi
            if (dataReceived.contains("notification") && dataReceived.contains("fire")) {
            	
            	//prepare and send the packet acknowledging the server received the notification
				String sendString = "received";
				byte[] sendData = sendString.getBytes("UTF-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());
				serverSocket.send(sendPacket);
				
				//Get the IP's of all the phones registered in the database
				String Ips[] = database.getAllIps(fileLocation);
				if (debugging){System.out.println("notification received");}	
				
				//prepare notification to be sent to the mobile app on the registered phones
				sendString = "notification";
				sendData = sendString.getBytes("UTF-8");		
				
				//send notification to every mobile IP registered in database
				for (int i = 0; i < Ips.length; i++)
				{
					if (debugging){System.out.println("notification received, sending to: " + Ips[i]);}
					sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(Ips[i]), 5050);
					serverSocket.send(sendPacket);
					}
					
				} 
            
            //if the packet is a database action request from the desktop app
			else if (dataReceived.contains("add") || dataReceived.contains("remove") || dataReceived.contains("get")) {
	
						//if the database action request is a get
	                    if (dataReceived.contains("get:")){
	                    		
	                    	//command string contains the id of the entry to get
	                    	//split the command string so that we can get the id and look it up in the database	
	                        String[] entryData = dataReceived.split(":");
	                        
	                        if (entryData.length > 1) 
	                        {
	                        	//get the database entry with the id specified by entryData[1]. Send the entry data back to the 
	                        	//database
	                        	String member = database.getMemberById(fileLocation, entryData[1]);
	                            if (member != null) {
	                                byte[] sendData = member.getBytes("UTF-8");
	                                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());
	                                serverSocket.send(sendPacket);
									
	                                //mobile apps issue a get request when logging in
	                                //identify if this get request came from a phone, and if it did, store their
	                                //ip in the database.
									if (dataReceived.contains("phone"))
									{
										String theIP = IPAddress.toString().substring(1);	
										if (debugging){System.out.println("HERE IS THE IP: " + theIP);}
										database.addIP(fileLocation, entryData[1], theIP);
									}					
					                            
	                            }
	                       }
	                    }
	                    
	                    //if the database action request is an add
	                    else if (dataReceived.contains("add:")) {
	                    	
	                    	//command string contains the information necessary to add an entry
	                    	//split the command string so that we can get the necessary information for an add
	                    	//this information is put in EntryData then passed to the add method
	                    	String[] entryData = dataReceived.split(":");
	                        if (entryData.length > 3) {
	                            database.addMember(fileLocation, entryData[1], entryData[2], entryData[3]);
	                        }
	
	                    } 
	                    
	                    //if the database action request is a remove 
	                    else if (dataReceived.contains("remove:")) {
	                    	
	                    	
	                    	//command string contains the information necessary to remove an entry
	                    	//split the command string so that we can get id we need to remove a string
	                        String[] entryData = dataReceived.split(":");
	                        if (entryData.length > 1) {
	                            database.removeMember(fileLocation, entryData[1]);
	                        }
	                    }
	
	                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
