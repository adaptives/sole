package models;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class BookGroupTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
		Fixtures.load("users-and-study-sessions.yml");
		Fixtures.load("book-study-groups.yml");
	}

	@After
	public void tearDown() throws Exception {
	
	}
	
	@Test
	public void testSaveAndRetrieve() {
		Book book = new Book("A gentle introduction to Computer Science", "ISBNGI2CS", null);
		Author author = (Author)Author.findAll().get(0);
		Set<Author> authors = new TreeSet<Author>();
		authors.add(author);
		book.setAuthors(authors);
		book.save();
		
		Forum forum = new Forum("A gentle introduction to Computer Science", "Forum for discussing - A gentle introduction to Computer Science");
		
		BookGroup bookGroup = new BookGroup(book);
		bookGroup.forum = forum;
		bookGroup.save();
		
		BookGroup bookGroupRetrieved = BookGroup.find("select bg from BookGroup bg where bg.book.id = ?", book.id).first();
		
		assertEquals(book.id, bookGroupRetrieved.book.id);
		assertEquals(forum.id, bookGroupRetrieved.forum.id);
	}

}
