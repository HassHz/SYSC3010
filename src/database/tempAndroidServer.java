import java.io.File;
import java.io.IOException;
import java.net.*;

public class tempAndroidServer {

	private static String fileLocation = "database.xml";
	static int port = 8080;

	public static void main(String[] args) {
		new tempAndroidServer().run(port);
	}

	public void run(int port) {
		try {
			database database = new database("Carleton", fileLocation);

			DatagramSocket serverSocket = new DatagramSocket(port);
			byte[] receiveData = new byte[256];

       			System.out.printf("Listening on udp:%s:%d%n","192.168.0.22", port);
        		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			while(true) {
              			serverSocket.receive(receivePacket);
              			String dataReceived = new String( receivePacket.getData(), 0, receivePacket.getLength());
              			System.out.println("RECEIVED: " + dataReceived);

				if(dataReceived.contains("login:")){
					String[] entryData = dataReceived.split(" ");
					if (entryData.length > 3) {
						String member = database.getMemberById(fileLocation, entryData[1]);
						if (member != null && member.substring(member.lastIndexOf(" ")+1).equals(entryData[2])) {
							InetAddress destination = InetAddress.getByName("192.168.0.16");
							byte[] message = "Success".getBytes();
							DatagramPacket packet = new DatagramPacket(message, message.length, destination, 8080);
							serverSocket.send(packet);
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
