import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.net.*;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FireGUI implements ActionListener {

	private JButton enterButton;
	private JTextField nameField, numberField, lastName;
	private JTextArea displayBox;
	private JCheckBox getBox, removeBox, addBox;
	private JPanel newPanel = new JPanel(new GridBagLayout());
	static int port = 5050;
	static String hostIP = "10.0.0.51";
	static boolean debugging = true;

	
	public static void main(String[] args) {
		new FireGUI();
	}

	public FireGUI() {
	
		JFrame frame = new JFrame("Flame Monitor GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//background image		
		JPanel background= new JPanel() {
	        	@Override
			public void paintComponent(Graphics g) {
    				super.paintComponent(g);
				try {
					g.drawImage(ImageIO.read(new File("Images/backback.png")), 0, 0, null);
				}catch(Exception e){
					e.printStackTrace();
			        }
	          	}
	        };
		background.setLayout(new BorderLayout());
		frame.setContentPane(background);

		//FlameMonitor logo
		ImageIcon fmlogo = new ImageIcon("Images/flamemonitorlogo.png");
		JLabel logoLabel = new JLabel(fmlogo, JLabel.CENTER);

		//Action checkboxes
		getBox = new JCheckBox("Get");
		getBox.setSelected(false);
		removeBox = new JCheckBox("Remove");
		removeBox.setSelected(false);
		addBox = new JCheckBox("Add");
		addBox.setSelected(false);

		//Add checkboxes to a ButtonGroup so only one can can be selected		
		ButtonGroup actions = new ButtonGroup();
		actions.add(getBox);
		actions.add(removeBox);
		actions.add(addBox);
		
		JPanel checkPanel = new JPanel(new GridLayout(0, 1));
		checkPanel.add(getBox);
	    	checkPanel.add(removeBox);
	    	checkPanel.add(addBox);
		checkPanel.setBackground(new Color(0,0,0,0));
			
		
		JPanel buttonPanel = new JPanel();
		enterButton = new JButton ("ENTER");
		buttonPanel.add(enterButton);
		enterButton.addActionListener(this);
		buttonPanel.add(enterButton);
		buttonPanel.setOpaque(false);

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
		constraints.gridx = 2;
		newPanel.add(logoLabel,constraints);
		constraints.gridy = 0;
		constraints.gridx = 3;
		JLabel systemLabel = new JLabel("<html><u>FLAME MONITOR<br></u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<u>SETUP SYSTEM</u></html>");
		systemLabel.setForeground(Color.ORANGE);
		newPanel.add(systemLabel, constraints);
	
		constraints.gridy = 1;
		constraints.gridx = 1;
		newPanel.add(checkPanel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		JLabel firstNameLabel = new JLabel("FIRST NAME");
		firstNameLabel.setForeground(Color.ORANGE);
		newPanel.add(firstNameLabel, constraints);
		constraints.gridx = 2;
		constraints.gridy = 2;
		newPanel.add(nameField, constraints);

		constraints.gridx = 1;
		constraints.gridy = 4;
		JLabel numberLabel = new JLabel("NUMBER");
		numberLabel.setForeground(Color.ORANGE);
		newPanel.add(numberLabel, constraints);
		constraints.gridx = 2;
		constraints.gridy = 4;
		newPanel.add(numberField, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		JLabel lastNameLabel = new JLabel("LAST NAME");
		lastNameLabel.setForeground(Color.ORANGE);
		newPanel.add(lastNameLabel, constraints);
		constraints.gridx = 2;
		constraints.gridy = 3;
		newPanel.add(lastName, constraints);

		constraints.gridx = 3;
		constraints.gridy = 1;
		newPanel.add(displayBox, constraints); 
		constraints.gridy = 5;

		newPanel.add(buttonPanel, constraints);
		constraints.anchor = GridBagConstraints.CENTER;
		frame.add( newPanel );
		newPanel.setOpaque(false);

		frame.setSize(500,500);
		frame.setLocation(100,100);
		frame.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) { 
	
		if(!getBox.isSelected() && !removeBox.isSelected() && !addBox.isSelected()) {
			JOptionPane.showMessageDialog( newPanel, "No box selected. Please select a box.");			
		}			
		
		try {
			
		DatagramSocket serverSocket = new DatagramSocket(port);
		InetAddress host=InetAddress.getByName(hostIP);
		
		if(getBox.isSelected() && !removeBox.isSelected() && !addBox.isSelected()) {

			if (debugging) {System.out.println("get button pressed");}
			if(nameField.getText().equals("") || lastName.getText().equals("")){
				JOptionPane.showMessageDialog(newPanel, "Please enter the first and last name of the client");
				serverSocket.close();				
				return;
			}
			
			String sendString = "get:"+ nameField.getText() + lastName.getText();

		  	byte[] sendData = sendString.getBytes();
		  	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, host, port);
		  	serverSocket.send(sendPacket); 
		          	
		  	byte[] receiveData = new byte[256];       	
		    	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    	if (debugging) {System.out.printf("Listening on udp:%s:%d%n",InetAddress.getLocalHost().getHostAddress(), port);}
			serverSocket.setSoTimeout(3000);		    	
			try {
				serverSocket.receive(receivePacket);

				String dataReceived = new String(receivePacket.getData(), 0, receivePacket.getLength());
			    	if (debugging) {System.out.println("RECEIVED: " + dataReceived);}                  	
			    	displayBox.setText(dataReceived); 
			} catch (SocketTimeoutException se) {
				if (debugging) {System.out.printf("Failed to receive response from server");}
			}                 	
                  	
		}
		else if(!getBox.isSelected() && removeBox.isSelected() && !addBox.isSelected()){
			
			System.out.println("remove button pressed");
			if(nameField.getText().equals("") || lastName.getText().equals("")){
				JOptionPane.showMessageDialog(newPanel, "Please enter the first and last name of the client");
				serverSocket.close();				
				return;
			}
			
	      	String sendString = "remove:"+ nameField.getText() + lastName.getText();
	      	byte[] sendData = sendString.getBytes();
          	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, host, port);
          	serverSocket.send(sendPacket); 
		}
		
		else if(!getBox.isSelected() && !removeBox.isSelected() && addBox.isSelected()){
			
			System.out.println("Add button pressed");
			if(nameField.getText().equals("") || lastName.getText().equals("")){
				JOptionPane.showMessageDialog( newPanel, "Please enter the first and last name of the client");
				serverSocket.close();				
				return;
			}
				
			if(numberField.getText().equals("")){
				JOptionPane.showMessageDialog( newPanel, "Please enter the phone number of the client");
				serverSocket.close();				
				return;
			}
					
		      	String sendString = "add:"+ nameField.getText()+ ":" +lastName.getText() + ":" + numberField.getText();
		  	byte[] sendData = sendString.getBytes();
		  	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, host, port);
		  	serverSocket.send(sendPacket); 
		}
		else
		{	
			//Should never reach here	
		}		
		
		serverSocket.close();

	}
		 catch(Exception ex){ ex.printStackTrace();


        }
	     
	}

}

