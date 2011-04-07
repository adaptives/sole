package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class StudySession extends Model {
	
	public static final org.apache.log4j.Logger cLogger = 
									Logger.log4j.getLogger(StudySession.class);
	
	public String title;
	public String description;
	public Date startDate;
	public Date endDate;
	@Lob
	public String applicationPage;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "studySession")
	Set<SessionPart> sessionParts;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="StudySession_Facilitators")
	public Set<SocialUser> facilitators;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="StudySession_Participants")
	public Set<SocialUser> participants;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="StudySession_PendingApplications")
	public Set<SocialUser> pendingApplications;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="StudySession_RejectedApplications")
	public Set<SocialUser> rejectedApplications;

	@OneToOne(cascade=CascadeType.ALL)
	public Forum forum;
	
	public StudySession(String title,
						String description,
						Date startDate,
						Date endDate) {
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.facilitators = new TreeSet<SocialUser>();
		this.participants = new TreeSet<SocialUser>();
		this.pendingApplications = new TreeSet<SocialUser>();
		this.rejectedApplications = new TreeSet<SocialUser>();
		
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
	
	public boolean isUserEnrolled(long userId) {
		SocialUser user = SocialUser.findById(userId);
		return (this.participants.contains(user) || facilitators.contains(user));	
	}
	
	public boolean isUserApplicationPending(long userId) {
		SocialUser user = SocialUser.findById(userId);
		return (this.pendingApplications.contains(user));
	}
	
	public boolean canEnroll(String sUserId) {
		return canEnroll(Long.parseLong(sUserId));
	}
	
	public boolean canEnroll(long userId) {
		SocialUser user = SocialUser.findById(userId);
		System.out.println("Determining if user '" + userId + "' can enroll in study session");
		return (!this.participants.contains(user) && 
				!this.facilitators.contains(user) && 
				!this.rejectedApplications.contains(user) && 
				!this.pendingApplications.contains(user));
	}
	
	public boolean isUserEnrolled(String userId) {
		if(userId == null) {
			return false;
		}
		try {
			long lUserId = Long.parseLong(userId);
			return isUserEnrolled(lUserId);
		} catch(Exception e) {
			cLogger.error("Could not parse the userId string to get a long " +
						  "value '" + userId + "'", e);
			return false;
		}	
	}
}
