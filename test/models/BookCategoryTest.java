package models;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class BookCategoryTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testSaveAndRetrieve() {
		BookCategory bcat = new BookCategory("Java");
		BookCategory retrievedBookCategory = BookCategory.findByCategotyName("Java");
		assertEquals(retrievedBookCategory.categoryName, "Java");
	}

}
