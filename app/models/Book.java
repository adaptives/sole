package models;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Book extends Model {
	
	@Required
	public String title;
	
	//@javax.persistence.Column(unique = true)
	public String isbn;
	
	//@javax.persistence.Column(unique = true)
	public String isbn2;
	
	@ManyToMany
	public Set<Author> authors;
	
//	@ManyToMany
//	@javax.persistence.Column(required = true)
//	public Set<BookCategory> categories;
	
	public Book(String title, String isbn, String isbn2) {
		this.title = title;
		this.isbn = isbn;
		this.isbn2 = isbn2;
		this.authors = new TreeSet<Author>();
		//this.categories = new TreeSet<BookCategory>();
		create();
	}
	
	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
		save();
	}
	
	public void addAuthor(Author author) {
		this.authors.add(author);
		save();
	}
	
	@Override
	public String toString() {
		return this.title;
	}
}
