package database;

import java.io.IOException;
import java.net.*;

public class server {

	private static boolean debugging = true;

	private static String fileLocation = "database.xml";
	static int port = 5050;

    public static void main(String[] args) {
    	new server().run(port);
    }

    public void run(int port) {
    	try {
            database database = new database("Carleton", fileLocation);

            @SuppressWarnings("resource")
            DatagramSocket serverSocket = new DatagramSocket(port);

            byte[] receiveData = new byte[256];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            if (debugging) {System.out.printf("Listening on udp:%s:%d%n",InetAddress.getLocalHost().getHostAddress(), port);}

        while(true) {
            serverSocket.receive(receivePacket);
            String dataReceived = new String(receivePacket.getData(), 0, receivePacket.getLength());
            if (debugging) {System.out.println("RECEIVED: " + dataReceived);}
            InetAddress IPAddress = receivePacket.getAddress();

            if (dataReceived.contains("notification") && dataReceived.contains("fire")) {

				String sendString = "received";
				byte[] sendData = sendString.getBytes("UTF-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());
				serverSocket.send(sendPacket);
					
				//InetAddress Ips[] = database.getAllIps(fileLocation);
				String Ips[] = database.getAllIps(fileLocation);
		
				sendString = "notification";
				sendData = sendString.getBytes("UTF-8");		
		
				for (int i = 0; i < Ips.length; i++)
				{
					sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(Ips[i]), 5050);
					serverSocket.send(sendPacket);
					}
					
				} 
			else if (dataReceived.contains("add") || dataReceived.contains("remove") || dataReceived.contains("get")) {
	
	                    if (dataReceived.contains("get:")){
	
	                        String[] entryData = dataReceived.split(":");
	                        if (entryData.length > 1) 
	                        {
	                            String member = database.getMemberById(fileLocation, entryData[1]);
	                            if (member != null) {
	                                byte[] sendData = member.getBytes("UTF-8");
	                                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());
	                                serverSocket.send(sendPacket);
									
									if (dataReceived.contains("phone"))
									{
										database.addIP(fileLocation, entryData[1], IPAddress.toString());
									}					
					                            
	                            }
	                       }
	                    } 
	                    
	                    else if (dataReceived.contains("add:")) {
	
	                        String[] entryData = dataReceived.split(":");
	                        if (entryData.length > 3) {
	                            database.addMember(fileLocation, entryData[1], entryData[2], entryData[3]);
	                        }
	
	                    } else if (dataReceived.contains("remove:")) {
	
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
