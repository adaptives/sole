package models;

import java.text.ParseException;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import play.Logger;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class BookGroup extends Model {
	
	@Required
	@OneToOne
	public Book book;
	
	@Required
	@OneToOne(cascade=CascadeType.ALL)
	public Forum forum;
	
	@ManyToMany
	public Set<SocialUser> members;
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(BookGroup.class);
	
	public BookGroup(Book book) {
		this.book = book;
		this.forum = new Forum(book.title, "Forum for discussing the book '" + book.title + "'");
		this.members = new TreeSet<SocialUser>();
	}
	
	public boolean isMember(String sUserId) {
		boolean retVal = false;
		try {
			long userId = Long.parseLong(sUserId);
			SocialUser socialUser = BookGroup.find("find su from BookGroup bg join bg.members m where m.id = ?", userId).first();
			if(socialUser != null) {
				retVal = true;
			}
		} catch(NumberFormatException nfe) {
			
		}
		return retVal;
	}
	
	
//	@Override
//	public String toString() {
//		return this.id;
//	}
}
