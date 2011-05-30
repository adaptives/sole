package models;

import static org.junit.Assert.*;

import java.util.List;
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
		String isbn = "ISBNGI2CS";
		Book book = new Book("A gentle introduction to Computer Science");
		book.isbn = isbn;
		book.save();
		
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
	
	@Test
	public void testStartedReading() throws Exception {
		String isbn = "ISBNGI2CS";
		Book book = new Book("A gentle introduction to Computer Science");
		book.isbn = isbn;
		book.save();
		
		Author author = (Author)Author.findAll().get(0);
		Set<Author> authors = new TreeSet<Author>();
		authors.add(author);
		book.setAuthors(authors);
		book.save();
		
		Forum forum = new Forum("A gentle introduction to Computer Science", "Forum for discussing - A gentle introduction to Computer Science");
		
		BookGroup bookGroup = new BookGroup(book);
		bookGroup.forum = forum;
		bookGroup.save();
		
		List<SocialUser> users = SocialUser.findAll();
		
		for(SocialUser user : users) {
			bookGroup.startReading(user);
			bookGroup.save();
		}
		
		assertEquals(users.size(), bookGroup.getStartedReadingCount());
		
		for(SocialUser user : users) {
			assertTrue(bookGroup.hasStartedReading(user.id));
		}
		
		for(SocialUser user : users) {
			assertTrue(bookGroup.hasStartedReading(String.valueOf(user.id)));
		}
	}
	
	@Test
	public void testCompletedReading() throws Exception {
		String isbn = "ISBNGI2CS";
		Book book = new Book("A gentle introduction to Computer Science");
		book.isbn = isbn;
		book.save();
		
		Author author = (Author)Author.findAll().get(0);
		Set<Author> authors = new TreeSet<Author>();
		authors.add(author);
		book.setAuthors(authors);
		book.save();
		
		Forum forum = new Forum("A gentle introduction to Computer Science", "Forum for discussing - A gentle introduction to Computer Science");
		
		BookGroup bookGroup = new BookGroup(book);
		bookGroup.forum = forum;
		bookGroup.save();
		
		List<SocialUser> users = SocialUser.findAll();
		
		for(SocialUser user : users) {
			bookGroup.startReading(user);
			bookGroup.save();
		}
		
		bookGroup.completeReading(users.get(0));
		bookGroup.save();
		
		assertEquals(users.size()-1, bookGroup.getStartedReadingCount());
		assertEquals(1, bookGroup.getCompletedReadingCount());
		
		assertFalse(bookGroup.hasStartedReading(users.get(0).id));
		assertTrue(bookGroup.hasCompletedReading(users.get(0).id));
		
		for(int i=1; i<users.size(); i++) {
			assertTrue(bookGroup.hasStartedReading(users.get(i).id));
			assertFalse(bookGroup.hasCompletedReading(users.get(i).id));
		}
	}

}
