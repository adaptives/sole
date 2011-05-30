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
	
	public String isbn;
	
	public String isbn13;
	
	public String issn;
	
	@ManyToMany
	public Set<Author> authors;
	
	@ManyToMany
	public Set<BookCategory> categories;
	
	public Book(String title) {
		this.title = title;
		this.authors = new TreeSet<Author>();
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
