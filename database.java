package database;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class database {

	public database(String monitorLocation, String fileLocation) throws NullPointerException {
		
		if(fileLocation == null){
			throw new NullPointerException();
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

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(fileLocation));


		transformer.transform(source, result);

		System.out.println("File saved!");

	  } 
	  catch (Exception e) 
	  {
		e.printStackTrace();
	  } 
	}
	
	public boolean addMember(String fileLocation, String firstName, String lastName, String phoneNumber )
	{
		
		if (fileLocation == null || firstName == null || lastName == null || phoneNumber == null)
		{
			return false;

		}
		
		if (firstName.matches(".*\\d+.*") || lastName.matches(".*\\d+.*"))
			
		{
			return false;		
			
		}
		
		if (phoneNumber.length() > 10 || !phoneNumber.matches("[0-9]+"))
		{
			return false;
		}
			
		File oldFile = new File(fileLocation);
		
		if (!oldFile.exists())
		{
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
		attr.setValue("1");
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
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(fileLocation);
		transformer.transform(source, result);
		
		
		return true;
		  } 
		
		catch (Exception e) 
			{
				e.printStackTrace();
				return false;
			} 
	}
	
	public void removeMember(String firstName, String lastName)
	{
		
	}
	
}