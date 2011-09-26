package models;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import play.Logger;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Activity extends Model {
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(Activity.class);
	
	public String title;
	
	@Required
	@Lob
	@MaxSize(20000)
	public String content;
	
	@OneToMany(mappedBy="activity", cascade=CascadeType.ALL)
	@OrderBy("timestamp DESC")
	public Set<ActivityResponse> activityResponses;
	
	public Activity(String title, String content) {
		this.title = title;
		this.content = content;
		this.activityResponses = new TreeSet<ActivityResponse>();
		create();
	}
	
	public StudySession findStudySession() {
		StudySession studySession = null;
		//First look for this activity in SessionParts
		studySession = StudySession.find("select ss from StudySession ss join ss.sessionParts sp join sp.activities a where a.id = ?", this.id).first();		
		if(studySession == null) {
			//Since activity is not found in SessionPart let's look for it in StudySession
			studySession = StudySession.find("select ss from StudySession ss join ss.activities a where a.id = ?", this.id).first();
		}
		return studySession;
	}
	
	public SessionPart findSessionPart() {
		SessionPart sessionPart = 
			SessionPart.find("select sp from SessionPart sp join sp.activities a where a.id = ?", this.id).first();
		return sessionPart;
	}
	
	public boolean hasResponded(String sUserId) {
		boolean retVal = false;
		try {
			long userId = Long.parseLong(sUserId);
			String query = "select count(distinct ar) from Activity a join a.activityResponses ar where a.id = ? and ar.user.id = ?";
			if (ActivityResponse.count(query, this.id, userId) > 0) {
				retVal = true;
			}			
		} catch(Exception e) {
			cLogger.warn("Recieved incorrect userId '" + sUserId + "'");
		}
		return retVal;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.id) + " " + this.title;
	}
}
