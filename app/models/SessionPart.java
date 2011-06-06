package models;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import other.utils.StringUtils;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class SessionPart extends Model {
	
	public String title;	
	
	@Column(unique = true)
	public String sanitizedTitle;
	
	public Date startDate;
	public Date endDate;
	
	@Lob
	@MaxSize(20000)
	public String content;
	
	@Required
	@ManyToOne
	public StudySession studySession;
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("id")
	public Set<Activity> activities;
	
	
	
	public SessionPart(String title, 
					   Date startDate,
					   Date endDate,
					   String content,
					   StudySession studySession) {
		this.title = title;
		this.sanitizedTitle = StringUtils.replaceSpaceWithDashes(title);
		this.startDate = startDate;
		this.endDate = endDate;
		this.content = content;
		this.studySession = studySession;
		this.activities = new TreeSet<Activity>();
	}
		
	public static SessionPart findBySanitizedTitle(String sanitizedTitle) {
		String query = "select sp from SessionPart sp where sp.sanitizedTitle = ?";
		SessionPart sessionPart = SessionPart.find(query, sanitizedTitle).first();
		return sessionPart;
	}
	
	@Override
	public String toString() {
		return this.studySession.title + " - " + this.title;
	}
}
