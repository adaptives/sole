package models;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Activity extends Model {
	
	public String title;
	
	@Required
	@Lob
	@MaxSize(20000)
	public String content;
	
	@OneToMany(mappedBy="activity", cascade=CascadeType.ALL)
	@OrderBy("timestamp")
	public Set<ActivityResponse> activityResponses;
	
	public Activity(String content) {
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
	
	@Override
	public String toString() {
		return String.valueOf(this.id) + " " + this.title;
	}
}
