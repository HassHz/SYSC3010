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

	//GUI Components
	private JButton enterButton;
	private JTextField nameField, numberField, lastName;
	private JTextArea displayBox;
	private JCheckBox getBox, removeBox, addBox;
	private JPanel newPanel = new JPanel(new GridBagLayout());

	//Server information
	static int port = 5050;
	static String hostIP = "10.0.0.51";
	static boolean debugging = true;

	
	public static void main(String[] args) {
		//Launch the GUI		
		new FireGUI();
	}

	public FireGUI() {
	
		JFrame frame = new JFrame("Flame Monitor GUI");
		//Terminate the program when the GUI is closed with the X button
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create a new JPanel with the background image
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

		//Set the JFrame's content pane as the background image panel
		background.setLayout(new BorderLayout());
		frame.setContentPane(background);

		//Create a JLabel with the FlameMonitor logo image
		ImageIcon fmlogo = new ImageIcon("Images/flamemonitorlogo.png");
		JLabel logoLabel = new JLabel(fmlogo, JLabel.CENTER);

		//Checkboxes for the Get/Remove/Add actions
		getBox = new JCheckBox("Get");
		getBox.setSelected(false);
		removeBox = new JCheckBox("Remove");
		removeBox.setSelected(false);
		addBox = new JCheckBox("Add");
		addBox.setSelected(false);

		//Add checkboxes to a ButtonGroup so only one can can be selected at a time
		ButtonGroup actions = new ButtonGroup();
		actions.add(getBox);
		actions.add(removeBox);
		actions.add(addBox);
		
		//Organize the checkboxes on its own panel in a gridlayout
		JPanel checkPanel = new JPanel(new GridLayout(0, 1));
		checkPanel.add(getBox);
	    	checkPanel.add(removeBox);
	    	checkPanel.add(addBox);
		checkPanel.setBackground(new Color(0,0,0,0));

		//Create a JPanel for the Enter button and set the action listener to this class
		JPanel buttonPanel = new JPanel();
		enterButton = new JButton ("ENTER");
		buttonPanel.add(enterButton);
		enterButton.addActionListener(this);
		buttonPanel.add(enterButton);
		buttonPanel.setOpaque(false); //set transparent

		//Initialize the input text fields
		nameField = new JTextField(10);
		numberField = new JTextField(10);
		lastName = new JTextField(10);
		displayBox = new JTextArea("", 3,20);
		nameField.setEditable(true);
		numberField.setEditable(true);
		lastName.setEditable(true);
		displayBox.setEditable(false);

		//Setup the gridbag layout with constraints
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);

		//The remaining lines of code organize the GUI components into a clean gridbag layout
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

		//Set the size of the frame and make it visible
		frame.setSize(600,600);
		frame.setLocation(100,100);
		frame.setVisible(true);

	}

	/**
          * Action handler for the "ENTER" button
          * Checks the checkboxes and input fields and generates an apprporiate response
          */
	public void actionPerformed(ActionEvent e) { 

		//If none of the boxes are selected, show an alert dialog prompting user to select atleast one box		
		if(!getBox.isSelected() && !removeBox.isSelected() && !addBox.isSelected()) {
			JOptionPane.showMessageDialog( newPanel, "No box selected. Please select a box.");			
		}			
		
		try {
			
			DatagramSocket serverSocket = new DatagramSocket(port);
			InetAddress host=InetAddress.getByName(hostIP);

			//If getBox is selected
			if(getBox.isSelected() && !removeBox.isSelected() && !addBox.isSelected()) {

				if (debugging) {System.out.println("get button pressed");}
				//Exit if any of the name fields are empty
				if(nameField.getText().equals("") || lastName.getText().equals("")){
					JOptionPane.showMessageDialog(newPanel, "Please enter the first and last name of the client");
					serverSocket.close();				
					return;
				}

				/* Generate UDP Packet and send it to server */
				String sendString = "get:"+ nameField.getText() + lastName.getText();

		  		byte[] sendData = sendString.getBytes();
		  		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, host, port);
		  		serverSocket.send(sendPacket); 

				/* Start listening for UDP packets expecting a response*/
		  		byte[] receiveData = new byte[256];       	
		    		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    		if (debugging) {System.out.printf("Listening on udp:%s:%d%n",InetAddress.getLocalHost().getHostAddress(), port);}
				//Set the timeout for the socket receive so program doesn't hang forever
				serverSocket.setSoTimeout(3000);		    	
				try {
					serverSocket.receive(receivePacket);
					//If packet is received, parse it and set the displaybox with the appropriate text
					String dataReceived = new String(receivePacket.getData(), 0, receivePacket.getLength());
				    	if (debugging) {System.out.println("RECEIVED: " + dataReceived);}                  	
				    	displayBox.setText(dataReceived); 
				} catch (SocketTimeoutException se) {
					if (debugging) {System.out.printf("Failed to receive response from server");}
					se.printStackTrace();
				}                 	
                  	
			}
			//If removeBox is selected
			else if(!getBox.isSelected() && removeBox.isSelected() && !addBox.isSelected()){

				if (debugging) {System.out.println("remove button pressed");}
				//Only proceed if name fields are not empty, otherwise alert user
				if(nameField.getText().equals("") || lastName.getText().equals("")){
					JOptionPane.showMessageDialog(newPanel, "Please enter the first and last name of the client");
					serverSocket.close();				
					return;
				}

				//Send a UDP packet to server telling it to remove the inputted entry
	      			String sendString = "remove:"+ nameField.getText() + lastName.getText();
	      			byte[] sendData = sendString.getBytes();
          			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, host, port);
          			serverSocket.send(sendPacket); 
			}
			//If addBox is selected
			else if(!getBox.isSelected() && !removeBox.isSelected() && addBox.isSelected()){

				System.out.println("Add button pressed");
				//If any of the name fields are empty, alert user and return
				if(nameField.getText().equals("") || lastName.getText().equals("")){
					JOptionPane.showMessageDialog( newPanel, "Please enter the first and last name of the client");
					serverSocket.close();				
					return;
				}
				//If number field is empty, alert user and return
				if(numberField.getText().equals("")){
					JOptionPane.showMessageDialog( newPanel, "Please enter the phone number of the client");
					serverSocket.close();				
					return;
				}

				//Send UDP packet containing the input fields to the server in order to register the user
		      		String sendString = "add:"+ nameField.getText()+ ":" +lastName.getText() + ":" + numberField.getText();
		  		byte[] sendData = sendString.getBytes();
		  		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, host, port);
		  		serverSocket.send(sendPacket); 
			}
			else
			{	
				//Should never reach here
				continue;
			}		
			//Close the server socket after network operations are done
			serverSocket.close();

		}
		catch(Exception ex){ex.printStackTrace();}
	     
	}

}

