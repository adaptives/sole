package models;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

public class AuthorTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSaveAndRetrieve() {
		String firstName = "Jack";
		String middleName = "n";
		String lastName = "Jill";
		String url = "diycomputerscience.com";
		
		Author author = new Author(firstName, middleName, lastName, url);
		
		List<Author> authors = Author.findAll();
		
		Author retrievedAuthor = (Author)Author.find("select a from Author a where a.firstName = ? and a.lastName = ?", firstName, lastName).first();
		assertEquals(firstName, retrievedAuthor.firstName);
		assertEquals(middleName, retrievedAuthor.middleName);
		assertEquals(lastName, retrievedAuthor.lastName);
		assertEquals(url, retrievedAuthor.url);
	}

}
