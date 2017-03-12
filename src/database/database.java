package database;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



public class database {
	
	private static boolean debugging = false;
	
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
		
		if (firstName.matches(".*\\d+.*") || lastName.matches(".*\\d+.*"))
			
		{
			if (debugging){System.out.println("Failed to add element");}
			return false;		
			
		}
		
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
	
	public boolean removeMember(String fileLocation, String firstName, String lastName)
	{
		if (fileLocation == null || firstName == null || lastName == null)
		{
			if (debugging){System.out.println("Failed to remove element");}
			return false;

		}
		
		if (firstName.matches(".*\\d+.*") || lastName.matches(".*\\d+.*"))
			
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
			if (node.hasAttribute("id")&& node.getAttribute("id").equals(firstName+lastName) )
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
}