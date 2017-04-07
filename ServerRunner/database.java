
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



public class database {
	
	//set to false to disable logging statements
	private static boolean debugging = true;
	
	
	//create database method
	public database(String monitorLocation, String fileLocation) throws NullPointerException {
		
		//checks for null/invalid parameters
		if(fileLocation == null || monitorLocation == null || fileLocation == ""){
			if (debugging){System.out.println("Database creation failed");}
			return;
		}
		
		//attempts to create an xml file at fileLocation. If XML file cannot be saved an exception is thrown, 
		//that exception gets handled in the catch statement	
	  try {
		 
		//create instance of XML document 
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		
		//add a root element to the new xml instance
		Element rootElement = doc.createElement("database");
		doc.appendChild(rootElement);

		//create the root element of the new document. This element will be the location of the fire monitor system
		Attr rootAttr = doc.createAttribute("location");
		rootAttr.setValue(monitorLocation);
		rootElement.setAttributeNode(rootAttr);
		
		
		//Write the instance of XML to a file, where it is saved. 
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(fileLocation));


		transformer.transform(source, result);

		if (debugging){System.out.println("Database created!");}

	  }
	  //In the event that creating the XML fails and throws an error, the error gets handled within this catch statement 
	  //by printing the stack trace
	  catch (Exception e) 
	  {
		  if (debugging){System.out.println("Database creation failed");}
		  e.printStackTrace();
		  return;
	  } 
	}
	
	
	//used to add a new member entry into an existing database. In order to be successful all member parameters need to be valid
	//and database must exist
	public boolean addMember(String fileLocation, String firstName, String lastName, String phoneNumber )
	{
		
		//checks for null parameters
		if (fileLocation == null || firstName == null || lastName == null || phoneNumber == null)
		{
			if (debugging){System.out.println("Failed to add element");}
			return false;

		}
		
		//regex expression checks to make sure names are strings
		if (firstName.matches(".*\\d+.*") || lastName.matches(".*\\d+.*"))
			
		{
			if (debugging){System.out.println("Failed to add element");}
			return false;		
			
		}
		
		//regex expression checks to make sure numbers are int's and are of proper length
		if (phoneNumber.length() > 10 || !phoneNumber.matches("[0-9]+"))
		{
			if (debugging){System.out.println("Failed to add element");}
			return false;
		}
		
		
		//Checks if file already exists
		File oldFile = new File(fileLocation);
		if (!oldFile.exists())
		{
			if (debugging){System.out.println("Failed to add element");}
			return false;
		}	
		
		//attempts to write and save changes to an existing XML file. If changes fail to write an exception is thrown, this is 
		//then caught in catch statement
		try {
			
			//opens the existing file
			File file = new File(fileLocation);
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document document = docBuilder.parse(file);
	
			//gets the root element and appends a new member (contact) entry to it. Adds the members personal information to the 
			//new entry.
			//Note: A members database id is simply the combinatino of their first and last name
			//ex. Name = Vigor Popovic, ID= VigorPopovic
			Element rootElement = document.getDocumentElement();
			Element contact = document.createElement("contact");
			rootElement.appendChild(contact);
			
			Attr attr = document.createAttribute("id");
			attr.setValue(firstName+lastName);
			contact.setAttributeNode(attr);
	
			Element firstname = document.createElement("firstname");
			firstname.appendChild(document.createTextNode(firstName));
			contact.appendChild(firstname);
	
			Element lastname = document.createElement("lastname");
			lastname.appendChild(document.createTextNode(lastName));
			contact.appendChild(lastname);
	
			Element number = document.createElement("PhoneNumber");
			number.appendChild(document.createTextNode(phoneNumber));
			contact.appendChild(number);
			
			
			//Write Changes to file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(fileLocation);
			transformer.transform(source, result);
			
			if (debugging){System.out.println("Element added!");}
			return true;
		  } 
		
	  //In the event that saving changes fails and throws an error, the error gets handled within this catch statement 
	  //by printing the stack trace. Catches any exceptions thrown when opening the file. 
		catch (Exception e) 
			{
				if (debugging){System.out.println("Failed to add element");}
				e.printStackTrace();
				return false;
			} 
	}
	
	
	//remove a member entry from an existing database. In order to succeed, the entry ID must be valid format and must exist in 
	//database. Returns true/false depending on if entry is sucessfully removed
	public boolean removeMember(String fileLocation, String id)
	{
		
		//fails if file or id is null
		if (fileLocation == null || id == null)
		{
			if (debugging){System.out.println("Failed to remove element");}
			return false;

		}
		
		//regex expression checks to make sure names (id) are strings
		if (id.matches(".*\\d+.*"))
			
		{
			if (debugging){System.out.println("Failed to remove element");}
			return false;		
			
		}
			
		//check if database exists
		File oldFile = new File(fileLocation);
		if (!oldFile.exists())
		{
			if (debugging){System.out.println("Failed to remove element");}
			return false;
		}	
		
		//attempts to write and save changes to an existing XML file. If changes fail to write an exception is thrown, this is 
		//then caught in catch statement
		try {
			
			
			//opens a file for changes
			File file = new File(fileLocation);
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document document = docBuilder.parse(file);
	
			NodeList items = document.getElementsByTagName("contact");
			
			//if database is empty return false, cannot remove entry from empty database
			if (items== null || items.getLength() < 0)
			{
				if (debugging){System.out.println("Failed to remove element");}
				return false; 
			}
			boolean foundItem = false;
			
			//search for entry id and remove if found, if not found return false
			for (int i = 0; i < items.getLength(); i++)
			{
				Element node = (Element)items.item(i);
				if (node.hasAttribute("id")&& node.getAttribute("id").equals(id) )
				{
					foundItem = true;
					items.item(i).getParentNode().removeChild(items.item(i));
				}
			}
			
			if (!foundItem)
			{
				return false;
			}
			
			//Write Changes to file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(fileLocation);
			transformer.transform(source, result);
			
			if (debugging){System.out.println("Element removed!");}
			return true;
		  } 
		
	  //In the event that saving changes fails and throws an error, the error gets handled within this catch statement 
	  //by printing the stack trace. Catches any exceptions thrown when opening the file. 
		catch (Exception e) 
			{
				if (debugging){System.out.println("Failed to remove element");}
				e.printStackTrace();
				return false;
			} 
	}
	
	
	//Uses the parameter ID to search an existing database for a member entry, if the entry is found, a string is created
	//that contains all the members information, this string is then returned. 
	//invalid id format or non existing id will result in an error message being returned
	
	public String getMemberById(String fileLocation, String id)
	{
		if (fileLocation == null || id == null)
		{
			if (debugging){System.out.println("Failed to get element");}
			return "";

		}
		
		//regex expression checks to make sure id's are strings
		if (id.matches(".*\\d+.*"))
			
		{
			if (debugging){System.out.println("Failed to get element");}
			return "";		
			
		}
			
		File oldFile = new File(fileLocation);
		
		if (!oldFile.exists())
		{
			if (debugging){System.out.println("Failed to get element");}
			return "";
		}	
		
		try {
			File file = new File(fileLocation);
			
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document document = docBuilder.parse(file);
	
			NodeList items = document.getElementsByTagName("contact");
			
			if (items== null || items.getLength() < 0)
			{
				if (debugging){System.out.println("Failed to get element");}
				return ""; 
			}
			
			for (int i = 0; i < items.getLength(); i++)
			{
				Element node = (Element)items.item(i);
				if (node.hasAttribute("id") && node.getAttribute("id").equals(id) )
				{
					if (node.getElementsByTagName("ip").getLength() == 0)
					{  
						String firstName = node.getElementsByTagName("firstname").item(0).getTextContent();
						String lastName = node.getElementsByTagName("lastname").item(0).getTextContent();
						String phoneNumber = node.getElementsByTagName("PhoneNumber").item(0).getTextContent();
						return "Full Name: " + firstName + " " + lastName + "\nPhone Number: " + phoneNumber;
					}
					
					else
					{
						String ip = node.getElementsByTagName("ip").item(0).getTextContent();		
						String firstName = node.getElementsByTagName("firstname").item(0).getTextContent();
						String lastName = node.getElementsByTagName("lastname").item(0).getTextContent();
						String phoneNumber = node.getElementsByTagName("PhoneNumber").item(0).getTextContent();
						return "Full Name: " + firstName + " " + lastName + "\nPhone Number: " + phoneNumber
						+ "\nIP: " + ip;
					}
					}
			}
			
			return "No member with id: " + id + " found in database";
		
		} 
		
		//Catches any exceptions thrown when opening the file.
		catch (Exception e) 
			{
				if (debugging){System.out.println("Failed to get element");}
				e.printStackTrace();
				return "";
			} 
	}
	
	
	//responsible for adding an IP to an existing database. Used by the server to store a phones IP whenever it logs into the system
	//returns a failure message if IP cannot be added
	public String addIP(String fileLocation, String id, String ip)
	{
		
		//checks for invalid parameters
		if (fileLocation == null || id == null || ip == null)
		{
			if (debugging){System.out.println("Failed to get element");}
			return "";

		}
		
		//regex expression checks to make sure id and ip are strings
		if (id.matches(".*\\d+.*") && ip.matches(".*\\d+.*"))
			
		{
			if (debugging){System.out.println("Failed to get element");}
			return "";		
			
		}
			
		
		//checks if database exists
		File oldFile = new File(fileLocation);
		if (!oldFile.exists())
		{
			if (debugging){System.out.println("Failed to get element");}
			return "";
		}	
		
		//attempts to write and save changes to an existing XML file. If changes fail to write an exception is thrown, this is 
		//then caught in catch statement
		try {
			
			//opens file for changes
			File file = new File(fileLocation);
			
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document document = docBuilder.parse(file);
	
			//get list of items in database and returns empty string if database is empty. 
			//cannot add an IP to empty database
			NodeList items = document.getElementsByTagName("contact");
			
			if (items== null || items.getLength() < 0)
			{
				if (debugging){System.out.println("Failed to get element");}
				return ""; 
			}
			
			//searches database member entries for id, if it exists, IP gets added to member entry, otherwise an error message gets returned
			boolean isFound = false;
			for (int i = 0; i < items.getLength(); i++)
			{
				Element node = (Element)items.item(i);
				if (node.hasAttribute("id") && node.getAttribute("id").equals(id) )
				{
					if(debugging){System.out.println("This is the IP being entered in the database: " + ip);}
					Element  IP = document.createElement("ip");
					IP.appendChild(document.createTextNode(ip));
					node.appendChild(IP);
					isFound = true;
					if (debugging) {System.out.println("IP #" + ip +" added!");} 
						
					}
			}
			
			
			if (!isFound) {return "No member with id: " + id + " found in database";}

		 	//Write Changes to file
                	TransformerFactory transformerFactory = TransformerFactory.newInstance();
	                Transformer transformer = transformerFactory.newTransformer();
        	        DOMSource source = new DOMSource(document);
               	 	StreamResult result = new StreamResult(fileLocation);
	                transformer.transform(source, result);
        	        return "Element added";



		} 
		
		//Catch any exception thrown while attempting to save changes to file. Catches any exceptions thrown when opening the file
		catch (Exception e) 
			{
				if (debugging){System.out.println("Failed to get element");}
				e.printStackTrace();
				return "";
			} 
	}
	
	//Gets all the IPs from an existing database. If Ips are found, returns a string array containing all of them. 
	//if getAllIps fails a null is returned, if there are no IPs an empty string array gets returned. 
	public String[] getAllIps(String fileLocation)
	{
		
		//checks if database exists
		File oldFile = new File(fileLocation);
		if (!oldFile.exists())
		{
			if (debugging){System.out.println("Failed to get element");}
			return null;
		}	
		
		//attempts to write and save changes to an existing XML file. If changes fail to write an exception is thrown, this is 
		//then caught in catch statement
		try {
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document document = docBuilder.parse(oldFile);

			//gets all items in database with "ip" tag, adds all these ips to the return string array 
			NodeList items = document.getElementsByTagName("ip");
			String[] IpList = new String[items.getLength()] ;

			for (int i = 0; i < items.getLength(); i++)
			{
				Element node = (Element)items.item(i);
				IpList[i] = node.getTextContent();
				
			}
			return IpList;
		}
		
		//Catches any exceptions thrown when opening the file. 
		catch (Exception e)
		{	
			if (debugging){System.out.println("Failed to get element");}
			e.printStackTrace();
			return null;
		}

	}
}
