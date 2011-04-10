package models;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.Logger;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class StudySessionApplication extends Model implements Comparable {
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(StudySessionApplication.class);
	
	@Required
	@OneToOne
	public SocialUser socialUser;
	
	@Required
	@ManyToOne
	public StudySession studySession;
	
	@Lob
	public String application;
	
	//-1 = rejected, 0 = pending, 1 = accepted, 2 = deregistered 
	public int currentStatus;
	
	public Date timestamp;
	
	@OneToMany(cascade=CascadeType.ALL)
	Set<StudySessionApplicationStatusChange> statusChanges;
	
	public StudySessionApplication(SocialUser socialUser,
								   StudySession studySession,
								   String application) {
		this.socialUser = socialUser;
		this.studySession = studySession;
		this.application = application;
		this.statusChanges = new TreeSet<StudySessionApplicationStatusChange>();
		this.timestamp = new Date();
		create();
	}

	@Override
	public int compareTo(Object arg0) {
		StudySessionApplication other = (StudySessionApplication)arg0;
		return this.socialUser.screenname.compareTo(other.socialUser.screenname);
	}
	
	public void changeStatus(int newStatus, String comment) {
		if(statusChangeValid(newStatus)) {
			StudySessionApplicationStatusChange ssasc = 
				new StudySessionApplicationStatusChange(this, 
														this.currentStatus, 
														newStatus, 
														comment);
			this.statusChanges.add(ssasc);
			this.currentStatus = newStatus;
		} else {
			String msg = "Attemp to do an illegal status change. Current '" + 
						 this.currentStatus + "' '" + newStatus + "'";
			cLogger.warn(msg);
		}
	}
	
	private boolean statusChangeValid(int newStatus) {
		boolean retVal = true;
		switch(this.currentStatus) {
			case -2: //deregistered
				retVal = false;
				break;
			case -1: //rejected
				retVal = false;
				break;
			case 0: //pending
				if(newStatus == 0) { //all states except itself possible
					retVal = false;
				}
				break;
			case 1: //accepted
				if(newStatus != -2) {
					retVal = false;
				}
				break;
			default:
				cLogger.warn("The application is in an illegal status '" + this.currentStatus + "'");
		}
		return retVal;
	}
}
