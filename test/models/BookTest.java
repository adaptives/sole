package models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class BookTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCreateAndRetrieve() {
		String title = "A nice techie book";
		String isbn = "123-A45";
		String isbn13 = "123-B45";

		Book book = new Book(title);
		book.isbn = isbn;
		book.isbn13 = isbn13;
		book.save();
		
		Book retrievedBook = Book.find("select b from Book b where b.isbn = ?", isbn).first();
		assertEquals(title, retrievedBook.title);
		assertEquals(isbn, retrievedBook.isbn);
		assertEquals(isbn13, retrievedBook.isbn13);
	}
	
	//TODO: This test needs to pass
	@Ignore
	@Test(expected = Exception.class)
	public void testSavingBookWithoutTitle() {
		String isbn = "ISBN";
		String isbn13 = "ISBN2";
		
		Book book = new Book(null);
		book.isbn = isbn;
		book.isbn13 = isbn13;
		book.save();
	}
	
	public void testSetAuthors() {
		Author auth1 = new Author("Some", "A", "Dude", "somedude.com");
		Author auth2 = new Author("Some", "B", "Dudess", "somedudess.com");
		Set<Author> authors = new TreeSet<Author>();
		authors.add(auth1);
		authors.add(auth2);
		
		String title = "A nice techie book";
		String isbn = "123-A45";
		String isbn13 = "123-B45";

		Book book = new Book(title);
		book.isbn = isbn;
		book.isbn13 = isbn13;
		book.save();
		
		book.setAuthors(authors);
		
		Book retrievedBook = Book.find("select b from Book b where b.isbn = ?", isbn).first();
		Set<Author> retriedAuthors = retrievedBook.authors;
		assertEquals(2, retriedAuthors.size());
		for(Author author : authors) {
			assertTrue(retriedAuthors.contains(author));
		}
	}
	
	@Test
	public void testAddAuthor() {
		//AdmissionSchool
		Author auth1 = new Author("Some", "A", "Dude", "somedude.com");
		Author auth2 = new Author("Some", "B", "Dudess", "somedudess.com");
		
		String title = "A nice techie book";
		String isbn = "123-A45";
		String isbn13 = "123-B45";

		Book book = new Book(title);
		book.isbn = isbn;
		book.isbn13 = isbn13;
		book.save();
		
		book.addAuthor(auth1);
		Book retrievedBook = Book.find("select b from Book b where b.isbn = ?", isbn).first();
		Set<Author> retrievedAuthors = retrievedBook.authors;
		assertEquals(1, retrievedBook.authors.size());
		assertTrue(retrievedAuthors.contains(auth1));
		
		book.addAuthor(auth2);
		retrievedBook = Book.find("select b from Book b where b.isbn = ?", isbn).first();
		retrievedAuthors = retrievedBook.authors;
		assertEquals(2, retrievedBook.authors.size());
		assertTrue(retrievedAuthors.contains(auth2));
	}
	
}
