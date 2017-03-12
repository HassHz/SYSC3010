package database;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;


public class databaseTest {

	private String fileLocation = "C:\\Users\\Judgment Day\\database.xml"; 
	
	
	@Test
	public void testCreateDatabase() {
		
		File oldFile = new File(fileLocation);
		if (oldFile.exists())
		{
			oldFile.delete();
		}
					
		database test = new database("Carleton", fileLocation);
		File file = new File(fileLocation);
		assertTrue(file.exists());
	}
	
	
	@Test
	public void testCreateDatabaseNullLocation() {
		
		File oldFile = new File(fileLocation);
		if (oldFile.exists())
		{
			oldFile.delete();
		}
					
		database test = new database("Carleton", null);
		File file = new File(fileLocation);
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
			assertTrue(test.removeMember(fileLocation,"Bill", "Peters"));			
			
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
			assertFalse(test.removeMember(fileLocation,"NotThere", "NotThere"));			
			
		}
	
	
	
	

}
