package database;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;


public class databaseTest {

	private String fileLocation = "database.xml"; 
	
	
	@Test
	public void testCreateDatabase() {
		
		File oldFile = new File(fileLocation);
		if (oldFile.exists())
		{
			oldFile.delete();
		}
					
		new database("Carleton", fileLocation);
		File file = new File(fileLocation);
		assertTrue(file.exists());
	}
	
	
	@Test
	public void testCreateDatabase_EmptyLocation() {
		
		File oldFile = new File(fileLocation);
		if (oldFile.exists())
		{
			oldFile.delete();
		}
					
		new database("Carleton", "");
		File file = new File("");
		assertFalse(file.exists());
	}
	
	@Test
	public void testAddToDatabase() {
			
			File oldFile = new File(fileLocation);
			if (oldFile.exists())
			{
				oldFile.delete();
			}
						
			database test = new database("Carleton", fileLocation);			
			assertTrue(test.addMember(fileLocation,"Vigor", "Haffeez", "6135672001"));			
			
		}
	
	
	@Test
	public void testAddToDatabase_MissingParameter() {
			
			File oldFile = new File(fileLocation);
			if (oldFile.exists())
			{
				oldFile.delete();
			}
						
			database test = new database("Carleton", fileLocation);			
			assertFalse(test.addMember(fileLocation,"Vigor", "Haffeez", null));			
			
		}
	
	@Test
	public void testAddToDatabase_InvalidParameter() {
			
			File oldFile = new File(fileLocation);
			if (oldFile.exists())
			{
				oldFile.delete();
			}
						
			database test = new database("Carleton", fileLocation);			
			assertFalse(test.addMember(fileLocation,"Vigor", "Haf1223feez", "6135672001"));			
			
		}
	
	@Test
	public void testRemoveFromDatabase() {
			
			File oldFile = new File(fileLocation);
			if (oldFile.exists())
			{
				oldFile.delete();
			}
						
			database test = new database("Carleton", fileLocation);	
			test.addMember(fileLocation,"Vigor", "Haffeez", "6135672001");
			test.addMember(fileLocation,"Bill", "Peters", "6135672001");
			test.addMember(fileLocation,"Phil", "Billy", "6135672001");
			assertTrue(test.removeMember(fileLocation,"BillPeters"));			
			
		}
	
	
	@Test
	public void testRemoveFromDatabase_itemNotInDatabase() {
			
			File oldFile = new File(fileLocation);
			if (oldFile.exists())
			{
				oldFile.delete();
			}
						
			database test = new database("Carleton", fileLocation);	
			test.addMember(fileLocation,"Vigor", "Haffeez", "6135672001");
			test.addMember(fileLocation,"Bill", "Peters", "6135672001");
			test.addMember(fileLocation,"Phil", "Billy", "6135672001");
			assertFalse(test.removeMember(fileLocation,"NotThereNotThere"));			
			
		}
	
	
	@Test
	public void testGetFromDatabase() {
			
			File oldFile = new File(fileLocation);
			if (oldFile.exists())
			{
				oldFile.delete();
			}
						
			database test = new database("Carleton", fileLocation);	
			test.addMember(fileLocation,"Hassaan", "Popovic", "6135672001");
			test.addMember(fileLocation,"Lemon", "Pies", "6135672001");
			assertTrue(test.getMemberById(fileLocation,"HassaanPopovic").equals("Full Name: Hassaan Popovic\nPhone Number: 6135672001"));			
			
		}
	
	@Test
	public void testGetFromDatabase_itemNotInDatabase() {
			
			File oldFile = new File(fileLocation);
			if (oldFile.exists())
			{
				oldFile.delete();
			}
						
			database test = new database("Carleton", fileLocation);	
			test.addMember(fileLocation,"Lemon", "Pies", "6135672001");
			assertTrue(test.getMemberById(fileLocation,"HassaanPopovic").equals("No member with id: HassaanPopovic found in database"));			
			
		}
	
	@Test
	public void testAddIp() {
			
		File oldFile = new File(fileLocation);
		if (oldFile.exists())
		{
			oldFile.delete();
		}
					
		database test = new database("Carleton", fileLocation);	
		test.addMember(fileLocation,"Hassaan", "Popovic", "6135672001");
		test.addMember(fileLocation,"Lemon", "Pies", "6135672001");
		assertTrue(test.addIP(fileLocation, "HassaanPopovic", "10.0.0.52").equals("Element added"));	
			
		}
	
	
	@Test
	public void testAddIp_invalidIp() {
			
		File oldFile = new File(fileLocation);
		if (oldFile.exists())
		{
			oldFile.delete();
		}
					
		database test = new database("Carleton", fileLocation);	
		test.addMember(fileLocation,"Hassaan", "Popovic", "6135672001");
		test.addMember(fileLocation,"Lemon", "Pies", "6135672001");
		assertFalse(test.addIP(fileLocation, "THIS ID DOES NOT EXIST", "10.0.0.52").equals("Element added"));	
			
		}
	
	@Test
	public void testGetAllIps() {
			
		File oldFile = new File(fileLocation);
		if (oldFile.exists())
		{
			oldFile.delete();
		}
					
		database test = new database("Carleton", fileLocation);	
		test.addMember(fileLocation,"Hassaan", "Popovic", "6135672001");
		test.addMember(fileLocation,"Lemon", "Pies", "6135672001");
		test.addIP(fileLocation, "HassaanPopovic", "10.0.0.52");
		test.addIP(fileLocation, "LemonPies", "10.0.0.51");
		
		String [] ipTestArray = {"10.0.0.52","10.0.0.51"};
		
		assertFalse(test.getAllIps(fileLocation).equals(ipTestArray));	
			
		}
	
	
	

}
