package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class StudySessionMeta extends Model {
	//Once enrollment is closed, further registrations can only be done if the
	//administrator adds more people
	public boolean enrollmentClosed;
	//Once a session is locked, participants cannot post any threads to it
	public boolean locked;
	//Specifies that this course has been canceled
	public boolean canceled;
	
	@OneToOne
	public StudySession studySession;
	
	public StudySessionMeta(StudySession studySession) {
		this.studySession = studySession;
		create();
	}
	
	public static StudySessionMeta forStudySession(long studySessionId) {
		StudySessionMeta studySessionMeta = 
			StudySessionMeta.find("select ssm from StudySessionMeta ssm where ssm.studySession.id = ?", 
								  studySessionId).first();
		return studySessionMeta;
	}
}
