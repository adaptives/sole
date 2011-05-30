package models;

import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import other.utils.StringUtils;

import play.Logger;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class BookGroup extends Model {
	
	@Required
	@OneToOne
	public Book book;
	
	public String sanitizedTitle;
	
	@Required
	@OneToOne(cascade=CascadeType.ALL)
	public Forum forum;
	
	@ManyToMany
	@JoinTable(name="BOOK_GROUP_STARTED_READING")
	public Set<SocialUser> startedReading;
	
	@ManyToMany
	@JoinTable(name="BOOK_GROUP_COMPLETED_READING")
	public Set<SocialUser> completedReading;
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(BookGroup.class);
	
	public BookGroup(Book book) {
		this.book = book;
		this.sanitizedTitle = StringUtils.replaceSpaceWithDashes(book.title);
		this.forum = new Forum(book.title, "Forum for discussing the book '" + book.title + "'");
		this.startedReading = new TreeSet<SocialUser>();
		this.completedReading = new TreeSet<SocialUser>();
	}
	
	public static BookGroup findBySanitizedTitle(String sanitizedTitle) {
		String query = "select bg from BookGroup bg where bg.sanitizedTitle = ?";
		BookGroup bookGroup = BookGroup.find(query, sanitizedTitle).first();
		return bookGroup;
	}
	
	public void startReading(SocialUser user) {
		this.startedReading.add(user);
	}
	
	public boolean hasStartedReading(String sUserId) {
		boolean retVal = false;
		try {
			long userId = Long.parseLong(sUserId);
			retVal = hasStartedReading(userId);
		} catch(NumberFormatException nfe) {
			cLogger.error("Could not get SocialUser for id '" + sUserId + "'");
		}
		return retVal;
	}
	
	public boolean hasStartedReading(long userId) {
		boolean retVal = false;
		SocialUser socialUser = SocialUser.findById(userId);
		if(socialUser != null) {
			retVal = this.startedReading.contains(socialUser);
		} else {
			String msg = "SocialUser not found '" + userId + "'";
			cLogger.warn(msg);
		}
		return retVal;
	}
	
	public int getStartedReadingCount() {
		//TODO: Optimize at some point of time
		return this.startedReading.size();
	}
	
	public void completeReading(SocialUser user) {
		if(this.startedReading.contains(user)) {
			this.startedReading.remove(user);
			this.completedReading.add(user);
		}
	}
	
	public boolean hasCompletedReading(String sUserId) {
		boolean retVal = false;
		try {
			long userId = Long.parseLong(sUserId);
			retVal = hasCompletedReading(userId);
		} catch(NumberFormatException nfe) {
			cLogger.error("Could not get SocialUser for id '" + sUserId + "'");
		}
		return retVal;
	}
	
	public boolean hasCompletedReading(long userId) {
		boolean retVal = false;
		SocialUser socialUser = SocialUser.findById(userId);
		if(socialUser != null) {
			retVal = this.completedReading.contains(socialUser);
		} else {
			String msg = "SocialUser not found '" + userId + "'";
			cLogger.warn(msg);
		}
		return retVal;
	}
	
	public int getCompletedReadingCount() {
		return this.completedReading.size();
	}
	
	public List<Question> getPagedQuestions(long page, long size) {
		String query = "select q from Forum f join f.questions q where f.id = ? order by q.askedAt DESC";
		List<Question> questions = BookGroup.find(query, this.forum.id).fetch((int)page, (int)size);
		return questions;
	}
	
	
//	@Override
//	public String toString() {
//		return this.id;
//	}
}
