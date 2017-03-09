package database;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;


public class databaseTest {

	private String fileLocation = "C:\\Users\\vigorpopovic\\database.xml"; 
	
	
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
		//fail("Not yet implemented");
	}
	
	
	@Test(expected = NullPointerException.class)
	public void testCreateDatabaseNullLocation() {
		
		File oldFile = new File(fileLocation);
		if (oldFile.exists())
		{
			oldFile.delete();
		}
					
		database test = new database("Carleton", null);
		File file = new File(fileLocation);
		assertFalse(file.exists());
		//fail("Not yet implemented");
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
			
			//fail("Not yet implemented");
		}
	
	
	@Test
	public void testAddToDatabaseMissingParameter() {
			
			File oldFile = new File(fileLocation);
			if (oldFile.exists())
			{
				oldFile.delete();
			}
						
			database test = new database("Carleton", fileLocation);			
			assertFalse(test.addMember(fileLocation,"Vigor", "Haffeez", null));			
			
			//fail("Not yet implemented");
		}
	
	

}
