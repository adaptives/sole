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
	public String applicationText;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "studySession")
	Set<SessionPart> sessionParts;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="StudySession_Facilitators")
	public Set<SocialUser> facilitators;
	
	@OneToOne(cascade=CascadeType.ALL)
	public Forum forum;
	
	@OneToOne(mappedBy="studySession", cascade=CascadeType.ALL)
	public ApplicationStore applicationStore;
	
	public StudySession(String title,
						String description,
						Date startDate,
						Date endDate) {
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.facilitators = new TreeSet<SocialUser>();
		this.applicationStore = new ApplicationStore(this);
		
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
	
	public boolean canEnroll(String sUserId) {
		if(sUserId != null) {
			return canEnroll(Long.parseLong(sUserId));
		} else {
			return false;
		}		
	}
	
	public boolean canEnroll(long userId) {		
		SocialUser user = SocialUser.findById(userId);
		//TODO: We need to get the latest application and use it's status to
		//find out if the user can enroll
		return (!this.facilitators.contains(user) && 
				this.applicationStore.canEnroll(userId));
	}
	
	public boolean isUserEnrolled(String userId) {
		return this.isUserEnrolled(Long.parseLong(userId));
	}
	
	public boolean isUserEnrolled(long userId) {
		return 
			this.applicationStore.
				isUserApplicationAccepted(userId);
	}
	
	public boolean isFacilitator(long userId) {
		SocialUser user = SocialUser.findById(userId);
		return this.facilitators.contains(user);
	}
	
	public void addApplication(SocialUser user, String application) {
		//A user who already has a pending or accepted application cannot create a new application
		if(canEnroll(user.id)) {
			StudySessionApplication studySessionApplication = new StudySessionApplication(user, this, application);
			this.applicationStore.applications.add(studySessionApplication);
			//TODO: Should we save here or should the client call save ... because they may want this to be part of a transaction
			save();
		} else {
			String msg = "User '" + user.id + 
						 "' cannot enroll for this course because they are " +
						 "either enrolled or their enrollment application is pending";
			cLogger.warn(msg);
		}		
	}
	
	public void acceptApplication(long userId, String comment) {
		this.applicationStore.acceptApplication(userId, comment);
	}
	
	public void deregister(long userId, String comment) {
		this.applicationStore.deregister(userId, comment);
	}
	
	public List<SocialUser> getPendingApplicants() {
		return this.applicationStore.getPendingApplications();
	}
}
