package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class StudySession extends Model {
	
	public String title;
	public String description;
	public Date startDate;
	public Date endDate;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "studySession")
	Set<SessionPart> sessionParts;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="StudySession_Facilitators")
	public Set<User> facilitators;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="StudySession_Participants")
	public Set<User> participants;
	
	@OneToOne(cascade=CascadeType.ALL)
	public Forum forum;
	
	//If this is a private session, then users will not be able to register 
	//for it. Registrations will happen only by invitation
	public boolean privateSession;
	//Once enrollment is closed, further registrations can only be done if the
	//administrator adds more people
	public boolean enrollmentClosed;
	//Once a session is locked, participants cannot post any threads to it
	public boolean locked;
	//Specifies that this course has been canceled
	public boolean canceled;
	public boolean closed;
	
	public StudySession(String title,
						String description,
						Date startDate,
						Date endDate) {
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.facilitators = new TreeSet<User>();
		this.forum = new Forum(title, 
							   "Forum for discussing doubts in course '" + title + "'");
		create();
	}
	
	public static List<StudySession> fetchCurrent() {
		Date now = new Date();
		return StudySession.find("endDate >= ?, canceled = false, closed = false, locked = false order by startDate asc", now).fetch();
	}
	
	public static List<StudySession> fetchArchives() {
		Date now = new Date();
		return StudySession.find("endDate < ? order by startDate asc", now).fetch();
	}
	
	public boolean isUserEnrolled(String username) {
		System.out.println("EN isUserEnrolled - " + username);
		User user = User.findByEmail(username);
		System.out.println("user '" + user + "'");
		System.out.println("participants '" + participants + "'");
		return participants.contains(user);
	}
}
