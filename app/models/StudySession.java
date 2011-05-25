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
import javax.persistence.OrderBy;

import controllers.Security;

import play.Logger;
import play.data.validation.MaxSize;
import play.db.jpa.Model;

@Entity
public class StudySession extends Model {
	
	public static final org.apache.log4j.Logger cLogger = 
									Logger.log4j.getLogger(StudySession.class);
	
	public String title;
	
	@Lob
	@MaxSize(20000)
	public String description;
	
	public Date startDate;
	public Date endDate;
	
	@Lob
	public String applicationText;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "studySession")
	@OrderBy("id")
	public Set<SessionPart> sessionParts;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="StudySession_Facilitators")
	public Set<SocialUser> facilitators;
	
	@OneToOne(cascade=CascadeType.ALL)
	public Forum forum;
	
	@Lob
	@MaxSize(20000)
	public String resources;
	
	@OneToOne(mappedBy="studySession", cascade=CascadeType.ALL)
	public ApplicationStore applicationStore;
	
	@OneToOne
	public Pic studySessionPic;
	
	@OneToMany(cascade=CascadeType.ALL)
	public Set<Activity> activities;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "studySession")
	public StudySessionMeta studySessionMeta;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "studySession")
	public List<StudySessionAffiliateSpace> affiliateSpaces;
	
	public StudySession(String title,
						String description,
						Date startDate,
						Date endDate) {
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.facilitators = new TreeSet<SocialUser>();
		this.applicationStore = new ApplicationStore(this);
		this.studySessionMeta = new StudySessionMeta(this);
		this.forum = new Forum(title, 
							   "Forum for discussing doubts in course '" + title + "'");
		
		this.activities = new TreeSet<Activity>();
		
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
	
	public static List<StudySession> tail(int count) {
		String query = "select ss from StudySession ss order by ss.startDate desc";
		List<StudySession> studySessions = 
			StudySession.find(query).fetch(1, count);
		return studySessions;
	}
	
	public boolean canEnroll(String sUserId) throws InvalidUserIdException {
		if(sUserId != null && !sUserId.equals("")) {
			try {
				long userId = Long.parseLong(sUserId);
				return canEnroll(userId);
			} catch(NumberFormatException nfe) {
				throw new InvalidUserIdException(sUserId + " is not a valid number");
			}
		} else {
			throw new InvalidUserIdException(sUserId + " is not a valid userid");
		}		
	}
	
	public boolean canEnroll(long userId) {		
		SocialUser user = SocialUser.findById(userId);
		//TODO: We need to get the latest application and use it's status to
		//find out if the user can enroll
		if(!this.studySessionMeta.enrollmentClosed && 
		   !this.studySessionMeta.canceled && 
		   !studySessionMeta.locked) {
			
			return (!this.facilitators.contains(user) && 
					this.applicationStore.canEnroll(userId));
		} else {
			return false;
		}
	}
	
	public boolean isApplicationPending(String sUserId) throws InvalidUserIdException {
		if(sUserId != null && !sUserId.equals("")) {
			try {
				long userId = Long.parseLong(sUserId);
				return isApplicationPending(userId);
			} catch(NumberFormatException nfe) {
				throw new InvalidUserIdException(sUserId + " is not a valid userid");
			}
		} else {
			throw new InvalidUserIdException(sUserId + " is not a valid userid");
		}		
	}
	
	public boolean isApplicationPending(long userId) {
		return this.applicationStore.isUserApplicationPending(userId);
	}
	
	public boolean isUserEnrolled(String sUserId) throws InvalidUserIdException {
		if(sUserId != null && !sUserId.equals("")) {
			try {
				long userId = Long.parseLong(sUserId);
				return this.isUserEnrolled(userId);
			} catch(NumberFormatException nfe) {
				throw new InvalidUserIdException(sUserId + " is not a valid userid");
			}
		} else {
			throw new InvalidUserIdException(sUserId + " is not a valid userid");
		}
	}
	
	public boolean isUserEnrolled(long userId) {
		return 
			this.applicationStore.
				isUserApplicationAccepted(userId);
	}
	
	public boolean isFacilitator(String sUserId) throws InvalidUserIdException {
		if(sUserId != null && !sUserId.equals("")) {
			try {
				long userId = Long.parseLong(sUserId);
				return isFacilitator(userId);
			} catch(NumberFormatException nfe) {
				throw new InvalidUserIdException(sUserId + " is not a valid userid");
			}			
		} else {
			throw new InvalidUserIdException(sUserId + " is not a valid userid");
		}
		
	}
	
	public boolean isFacilitator(long userId) {
		SocialUser user = SocialUser.findById(userId);
		return this.facilitators.contains(user);
	}
	
	public boolean canManageParticipants(String userId) throws InvalidUserIdException {
		return (Security.check("admin") || isFacilitator(userId));
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
	
	public void addFacilitator(long userId) {
		SocialUser user = SocialUser.findById(userId);
		this.facilitators.add(user);
	}
	
	public void acceptApplication(long userId, String comment) {
		this.applicationStore.acceptApplication(userId, comment);
	}
	
	public void deregister(long userId, String comment) {
		this.applicationStore.deregister(userId, comment);
	}
	
	public List<SocialUser> getAcceptedUsers() {
		return this.applicationStore.getAcceptedApplications();
	}
	
	public long getAcceptedUsersCount() {
		return this.applicationStore.getAcceptedApplicationsCount();
	}
	
	public List<SocialUser> getPendingApplicants() {
		return this.applicationStore.getPendingApplicants();
	}
	
	public List<StudySessionApplication> getPendingApplications() {
		return this.applicationStore.getPendingApplications();
	}

	public StudySessionApplication getMostRecentApplicationForUser(String userId) {
		StudySessionApplication application = null;
		try {
			long lUserId = Long.parseLong(userId);
			application = this.applicationStore.getMostRecentApplicationForUser(lUserId);
		} catch(Exception e) {
			cLogger.error("Could not get application for user '" + userId + "'", e);
		}
		return application;
	}
	
	public StudySessionApplication getMostRecentApplicationForUser(long userId) {
		StudySessionApplication application = null;
		try {
			application = this.applicationStore.getMostRecentApplicationForUser(userId);
		} catch(Exception e) {
			cLogger.error("Could not get application for user '" + userId + "'", e);
		}
		return application;
	}
	
	public List<StudySessionEvent> getUpdates() {
		List<StudySessionEvent> studySessionEvents = 
									StudySessionEvent.tail(this.id, 1, 100);
		return studySessionEvents;
	}
	
	public boolean isEnrollmentClosed() {
		return this.studySessionMeta.enrollmentClosed;
	}
	
	public boolean isLocked() {
		return this.studySessionMeta.locked;
	}
	
	public boolean isCancelled() {
		return this.studySessionMeta.canceled;
	}
	
	public static List<StudySession> getYetToStart(Date since) {
		String query = "select ss from StudySession ss where ss.startDate > ?";
		List<StudySession> studySessions =
			StudySession.find(query, since).fetch();
		return studySessions;
	}
	
	public static List<StudySession> getOngoing(Date since) {
		String query = "select ss from StudySession ss where ss.startDate <= ? and ss.endDate >= ?";
		List<StudySession> studySessions =
			StudySession.find(query, since, since).fetch();
		return studySessions;
	}
	
	public static List<StudySession> getOver(Date since) {
		String query = "select ss from StudySession ss where ss.endDate < ?";
		List<StudySession> studySessions =
			StudySession.find(query, since).fetch();
		return studySessions;
	}
	
	/**
	 * A StudySession is said to be archived, when it is over and marked closed
	 * @return
	 */
	public static List<StudySession> getArchived() {
		return null;
	}
	
	@Override
	public String toString() {
		return this.title;
	}

}
