

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
	
	private static boolean debugging = true;
	
	public database(String monitorLocation, String fileLocation) throws NullPointerException {
		
		if(fileLocation == null || monitorLocation == null || fileLocation == ""){
			if (debugging){System.out.println("Database creation failed");}
			return;
		}
	  try {
		  
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("database");
		doc.appendChild(rootElement);

		Attr rootAttr = doc.createAttribute("location");
		rootAttr.setValue(monitorLocation);
		rootElement.setAttributeNode(rootAttr);
		
		
		//Write Changes to file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(fileLocation));


		transformer.transform(source, result);

		if (debugging){System.out.println("Database created!");}

	  } 
	  catch (Exception e) 
	  {
		  if (debugging){System.out.println("Database creation failed");}
		  e.printStackTrace();
		  return;
	  } 
	}
	
	public boolean addMember(String fileLocation, String firstName, String lastName, String phoneNumber )
	{
		
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
		
		//regex expression checks to make sure numbers are int's
		if (phoneNumber.length() > 10 || !phoneNumber.matches("[0-9]+"))
		{
			if (debugging){System.out.println("Failed to add element");}
			return false;
		}
			
		File oldFile = new File(fileLocation);
		
		if (!oldFile.exists())
		{
			if (debugging){System.out.println("Failed to add element");}
			return false;
		}	
		
		try {
		File file = new File(fileLocation);
		
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.parse(file);

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
		
		catch (Exception e) 
			{
				if (debugging){System.out.println("Failed to add element");}
				e.printStackTrace();
				return false;
			} 
	}
	
	public boolean removeMember(String fileLocation, String id)
	{
		if (fileLocation == null || id == null)
		{
			if (debugging){System.out.println("Failed to remove element");}
			return false;

		}
		
		//regex expression checks to make sure names are strings
		if (id.matches(".*\\d+.*"))
			
		{
			if (debugging){System.out.println("Failed to remove element");}
			return false;		
			
		}
			
		File oldFile = new File(fileLocation);
		
		if (!oldFile.exists())
		{
			if (debugging){System.out.println("Failed to remove element");}
			return false;
		}	
		
		try {
		File file = new File(fileLocation);
		
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.parse(file);

		NodeList items = document.getElementsByTagName("contact");
		
		if (items== null || items.getLength() < 0)
		{
			if (debugging){System.out.println("Failed to remove element");}
			return false; 
		}
		boolean foundItem = false;
		
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
		
		catch (Exception e) 
			{
				if (debugging){System.out.println("Failed to remove element");}
				e.printStackTrace();
				return false;
			} 
	}
	
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
		
		} catch (Exception e) 
			{
				if (debugging){System.out.println("Failed to get element");}
				e.printStackTrace();
				return "";
			} 
	}
	
	public String addIP(String fileLocation, String id, String ip)
	{
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



		} catch (Exception e) 
			{
				if (debugging){System.out.println("Failed to get element");}
				e.printStackTrace();
				return "";
			} 
	}
	public String[] getAllIps(String fileLocation)
	{
		File oldFile = new File(fileLocation);
		
		if (!oldFile.exists())
		{
			if (debugging){System.out.println("Failed to get element");}
			return null;
		}	
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document document = docBuilder.parse(oldFile);

			NodeList items = document.getElementsByTagName("ip");
			String[] IpList = new String[items.getLength()] ;

			for (int i = 0; i < items.getLength(); i++)
			{
				Element node = (Element)items.item(i);
				IpList[i] = node.getTextContent();
				
			}
			return IpList;
		}
		catch (Exception e)
		{	
			if (debugging){System.out.println("Failed to get element");}
			e.printStackTrace();
			return null;
		}

	}
}
