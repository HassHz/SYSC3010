import java.io.File;
import java.io.IOException;
import java.net.*;

public class server {

	private static String fileLocation = "database.xml";
	static int port = 8080;

	public static void main(String[] args) {
		new server().run(port);
	}

	public void run(int port) {
		try {
			//database database = new database("Carleton", fileLocation);

			DatagramSocket serverSocket = new DatagramSocket(port);
			byte[] receiveData = new byte[256];

       			System.out.printf("Listening on udp:%s:%d%n","192.168.0.22", port);
        		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			while(true) {
              			serverSocket.receive(receivePacket);
              			String dataReceived = new String( receivePacket.getData(), 0, receivePacket.getLength());
              			System.out.println("RECEIVED: " + dataReceived);
				dataReceived = "";
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
