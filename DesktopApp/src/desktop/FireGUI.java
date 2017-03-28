package desktop;
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
	private JTextField nameField, numberField, miscField;
	private JCheckBox getBox, removeBox, addBox;
	private JPanel newPanel = new JPanel(new GridBagLayout());
	static int port = 5050;

	
	
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
		
// 		getBox.addItemListener(this);
//      removeBox.addItemListener(this);
//      addBox.addItemListener(this);

		
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
		numberField = new JTextField( 10);
		miscField = new JTextField( 10);
		nameField.setEditable(true);
		numberField.setEditable(true);
		miscField.setEditable(true);



         
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
        
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
        newPanel.add(miscField, constraints);                           
        constraints.gridy = 4;
        newPanel.add(buttonPanel, constraints);
        constraints.anchor = GridBagConstraints.CENTER;
        contentPane.add( newPanel );

		frame.setSize(350,300);
		frame.setLocation(100,100);
		frame.setVisible(true);

}


	public void actionPerformed(ActionEvent e) { 
		if (e.getSource().equals (enterButton))
		{
			if(!getBox.isSelected() && !removeBox.isSelected() && !addBox.isSelected()){
				JOptionPane.showMessageDialog( newPanel, "No box selected. Please select a box.");
				
			}
			
			if(nameField.getText().equals(""))
				JOptionPane.showMessageDialog( newPanel, "please enter the name of client");
				
			if(numberField.getText().equals(""))
				JOptionPane.showMessageDialog( newPanel, "please enter the number of client");
				
			if(miscField.getText().equals(""))
				JOptionPane.showMessageDialog( newPanel, "please enter the misc of client");
				
			
		}
		try{
		DatagramSocket serverSocket = new DatagramSocket(port);
		InetAddress host=InetAddress.getByName("server");
		
		if(getBox.isSelected() && !removeBox.isSelected() && !addBox.isSelected()){

			String sendString = "get:"+nameField.getText();

                  	byte[] sendData = sendString.getBytes();
                  	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, host, port);
					serverSocket.send(sendPacket); 
		}
		else if(!getBox.isSelected() && removeBox.isSelected() && !addBox.isSelected()){
		      	String sendString = "remove "+nameField.getText()+ " " +miscField.getText() + " " + numberField.getText();
                  	byte[] sendData = sendString.getBytes();
                  	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, host, port);
					serverSocket.send(sendPacket); 
		}
		
		else if(!getBox.isSelected() && !removeBox.isSelected() && addBox.isSelected()){
		      	String sendString = "add "+nameField.getText()+ " " +miscField.getText() + " " + numberField.getText();
                  	byte[] sendData = sendString.getBytes();
                  	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, host, port);
					serverSocket.send(sendPacket); 
		}
		else
			JOptionPane.showMessageDialog( newPanel, "please only select 1 box");			
		
		
		
	}
		 catch(Exception ex){

        }
	     
	}
}

