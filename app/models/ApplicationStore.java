package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import controllers.StudySessions;

import play.Logger;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class ApplicationStore extends Model {
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(ApplicationStore.class);
	
	@Required
	@OneToOne
	public StudySession studySession;
	
	@OneToMany(cascade=CascadeType.ALL)
	public Set<StudySessionApplication> applications;
	
	public ApplicationStore(StudySession studySession) {
		this.studySession = studySession;
		this.applications = new TreeSet<StudySessionApplication>();
	}
	
	public boolean isUserApplicationPending(long userId) {
		List<StudySessionApplication> studySessionApplications = 
			StudySessionApplication.find("select a from StudySessionApplication a where a.studySession.id = ? and a.socialUser.id = ?", this.studySession.id, userId).fetch();
		//If we have even one pending application then we can assume that it is 
		//the most recent application. We can have later appliations only after
		//rejected and deregistered applications
		for(StudySessionApplication application : studySessionApplications) {
			if(application.currentStatus == 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isUserApplicationAccepted(long userId) {
		List<StudySessionApplication> studySessionApplications = 
			StudySessionApplication.find("select a from StudySessionApplication a where a.studySession.id = ? and a.socialUser.id = ?", this.studySession.id, userId).fetch();
		//If we have even one accepted application then we can assume that the
		//user has been accepted. This is because we should not be having a 
		//later application with any other state if an accepted appliation
		//exists
		for(StudySessionApplication application : studySessionApplications) {
			if(application.currentStatus == 1) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canEnroll(long userId) {
		return (!isUserApplicationAccepted(userId) && 				  
				!isUserApplicationPending(userId));
	}
	
	public Set<SocialUser> getParticipants() {
		Set<SocialUser> participants = new TreeSet<SocialUser>();
		for(StudySessionApplication application : this.applications) {
			if(application.currentStatus == 1) {
				participants.add(application.socialUser);
			}
		}
		return participants;
	}

	public void deregister(long userId, String comment) { 
		StudySessionApplication application = getMostRecentApplicationForUser(userId);
		if(applications != null) {			
			application.changeStatus(-2, comment);
			application.save();						
		} else {
			cLogger.warn("Attempt to deregister a user for whom an application does not exist '" + userId + "'");
		}
	}
	
	public void reject(long userId, String comment) {
		StudySessionApplication application = getMostRecentApplicationForUser(userId);
		if(application != null) {
			if(application.currentStatus == 0) {
				application.changeStatus(-1, comment);
				application.save();
			} else {
				cLogger.error("The state of the current application for user '" + userId + "' for StudySession '" + this.studySession.id + "' is '" + application.currentStatus + "' Hence could not reject it.");
			}
						
		} else {
			String msg = "Could not reject user becasue we could not find the application '" + userId + "'";
			cLogger.warn(msg);
		}
	}
	
	public List<SocialUser> getAcceptedApplications() {
		List<SocialUser> acceptedUsers = new ArrayList<SocialUser>();
		
		List<StudySessionApplication> acceptedApplications = 
			StudySessionApplication.find("select a from StudySessionApplication a where a.studySession.id = ? and a.currentStatus = 1", this.studySession.id).fetch();
		
		for(StudySessionApplication application : acceptedApplications) {
			acceptedUsers.add(application.socialUser);
		}
		
		return acceptedUsers;
	}
	
	public List<SocialUser> getPendingApplicants() {
		List<SocialUser> pendingUsers = new ArrayList<SocialUser>();
		
		List<StudySessionApplication> pendingApplications = 
			StudySessionApplication.find("select a from StudySessionApplication a where a.studySession.id = ? and a.currentStatus = 0", this.studySession.id).fetch();
		
		for(StudySessionApplication application : pendingApplications) {
			pendingUsers.add(application.socialUser);
		}
		
		return pendingUsers;
	}
	
	public List<StudySessionApplication> getPendingApplications() {
		List<StudySessionApplication> pendingApplications = 
			StudySessionApplication.find("select a from StudySessionApplication a where a.studySession.id = ? and a.currentStatus = 0 order by a.timestamp", this.studySession.id).fetch();
		return pendingApplications;
	}
	
	public void acceptApplication(long userId, String comment) {
		//The first application in the list will be the most recent one
		List<StudySessionApplication> studySessionApplications = 
			StudySessionApplication.find("select a from StudySessionApplication a where a.studySession.id = ? and a.socialUser.id = ? order by a.timestamp desc", this.studySession.id, userId).fetch();
		if(studySessionApplications.size() > 0) {
			StudySessionApplication studySessionApplication = studySessionApplications.get(0);
			if(studySessionApplication.currentStatus == 0) {
				StudySessionApplicationStatusChange ssasc = new StudySessionApplicationStatusChange(studySessionApplication, 0, 1, comment);
				studySessionApplication.statusChanges.add(ssasc);
				studySessionApplication.currentStatus = 1;
			}
		}
		
	}
	
	private StudySessionApplication getMostRecentApplicationForUser(long userId) {
		String query = 
			"select a from StudySessionApplication a " +
				"where a.studySession.id = ? and " +
					  "a.socialUser.id = ? order by a.timestamp desc";
		
		List<StudySessionApplication> applications = 
			StudySessionApplication.find(query, this.studySession.id, userId).fetch();
		StudySessionApplication application = null;
		if(applications.size() > 0) {
			application = applications.get(0);			
		} else {
			cLogger.warn("Could not find StudySessionApplication for user '" + userId + "' and query '" + query + "'");
		}
		return application;
	}

}
