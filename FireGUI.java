

import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.net.*;

public class FireGUI implements ActionListener {

	private JButton enterButton;
	private JTextField nameField, numberField, lastName;
	private JTextArea displayBox;
	private JCheckBox getBox, removeBox, addBox;
	private JPanel newPanel = new JPanel(new GridBagLayout());
	static int port = 5050;
	static String hostIP = "10.0.0.52";
	static boolean debugging = true;

	
	public static void main(String[] args) {
		new FireGUI();
	}

	public FireGUI(){
	
		JFrame frame = new JFrame("Fire Alarm GUI");
		Container contentPane = frame.getContentPane();
		contentPane.setLayout( new BorderLayout());

		getBox = new JCheckBox("Get");
		getBox.setSelected(false);
		
		removeBox = new JCheckBox("Remove");
		removeBox.setSelected(false);
		
		addBox = new JCheckBox("Add");
		addBox.setSelected(false);
		
		JPanel checkPanel = new JPanel(new GridLayout(0, 1));
	    	checkPanel.add(getBox);
	    	checkPanel.add(removeBox);
	    	checkPanel.add(addBox);
			
		
		JPanel buttonPanel = new JPanel();
		enterButton = new JButton ("ENTER");
		buttonPanel.add(enterButton);
		enterButton.addActionListener(this);
		buttonPanel.add(enterButton);

		nameField = new JTextField( 10);
		numberField = new JTextField(10);
		lastName = new JTextField(10);
		displayBox = new JTextArea("", 3,20);
		nameField.setEditable(true);
		numberField.setEditable(true);
		lastName.setEditable(true);
		displayBox.setEditable(false);
         
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		
		constraints.gridy = 0;
		constraints.gridx = 1;        
		newPanel.add(checkPanel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		newPanel.add(new JLabel("FIRST NAME"), constraints);
		constraints.gridx = 2;
		constraints.gridy = 1;
		newPanel.add(nameField, constraints);
		constraints.gridx = 1;
		constraints.gridy = 3;
		newPanel.add(new JLabel("NUMBER"), constraints);
		constraints.gridx = 2;
		constraints.gridy = 3;
		newPanel.add(numberField, constraints);
		constraints.gridx = 1;
		constraints.gridy = 2;
		newPanel.add(new JLabel("LAST NAME"), constraints);
		constraints.gridx = 2;
		constraints.gridy = 2;
		newPanel.add(lastName, constraints);    
		constraints.gridx = 3;
		constraints.gridy = 0;
		newPanel.add(displayBox, constraints); 
		constraints.gridy = 4;
		newPanel.add(buttonPanel, constraints);
		constraints.anchor = GridBagConstraints.CENTER;
		contentPane.add( newPanel );

		frame.setSize(1000,1000);
		frame.setLocation(100,100);
		frame.setVisible(true);





}

	public void actionPerformed(ActionEvent e) { 
	
		if(!getBox.isSelected() && !removeBox.isSelected() && !addBox.isSelected()){
			JOptionPane.showMessageDialog( newPanel, "No box selected. Please select a box.");			
		}			
		
		try{
			
		DatagramSocket serverSocket = new DatagramSocket(port);
		InetAddress host=InetAddress.getByName(hostIP);
		
		if(getBox.isSelected() && !removeBox.isSelected() && !addBox.isSelected()){

			System.out.println("get button pressed");
			if(nameField.getText().equals("")){
				JOptionPane.showMessageDialog( newPanel, "please enter the first name of the client");
				serverSocket.close();				
				return;}

			
			if(lastName.getText().equals("")){
				JOptionPane.showMessageDialog( newPanel, "please enter the last name of the client");
				serverSocket.close();				
				return;}
			
			String sendString = "get:"+ nameField.getText() + lastName.getText();

		  	byte[] sendData = sendString.getBytes();
		  	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, host, port);
		  	serverSocket.send(sendPacket); 
		          	
		  	byte[] receiveData = new byte[256];       	
		    	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    	if (debugging) {System.out.printf("Listening on udp:%s:%d%n",InetAddress.getLocalHost().getHostAddress(), port);}
		    	serverSocket.receive(receivePacket);
		    
		    	String dataReceived = new String(receivePacket.getData(), 0, receivePacket.getLength());
		    	if (debugging) {System.out.println("RECEIVED: " + dataReceived);}                  	
		    	displayBox.setText(dataReceived);                  	
                  	
		}
		else if(!getBox.isSelected() && removeBox.isSelected() && !addBox.isSelected()){
			
			System.out.println("remove button pressed");
			if(nameField.getText().equals("")){
				JOptionPane.showMessageDialog( newPanel, "please enter the first name of the client");
				serverSocket.close();				
				return;}
			
			if(lastName.getText().equals("")){
				JOptionPane.showMessageDialog( newPanel, "please enter the last name of the client");
				serverSocket.close();				
				return;}
			
	      	String sendString = "remove:"+ nameField.getText() + lastName.getText();
	      	byte[] sendData = sendString.getBytes();
          	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, host, port);
          	serverSocket.send(sendPacket); 
		}
		
		else if(!getBox.isSelected() && !removeBox.isSelected() && addBox.isSelected()){
			
			System.out.println("Add button pressed");
			if(nameField.getText().equals("")){
				JOptionPane.showMessageDialog( newPanel, "please enter the first name of the client");
				serverSocket.close();				
				return;}
				
			if(numberField.getText().equals("")){
				JOptionPane.showMessageDialog( newPanel, "please enter the number of the client");
				serverSocket.close();				
				return;}
				
			if(lastName.getText().equals("")){
				JOptionPane.showMessageDialog( newPanel, "please enter the last name of the client");
				serverSocket.close();				
				return;}
					
		      	String sendString = "add:"+ nameField.getText()+ ":" +lastName.getText() + ":" + numberField.getText();
		  	byte[] sendData = sendString.getBytes();
		  	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, host, port);
		  	serverSocket.send(sendPacket); 
		}
		else
		{	JOptionPane.showMessageDialog( newPanel, "please only select 1 box");	}		
		
		serverSocket.close();

	}
		 catch(Exception ex){ ex.printStackTrace();


        }
	     
	}
}

