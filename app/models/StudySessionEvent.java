package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class StudySessionEvent extends Model {
	
	@ManyToOne
	public StudySession studySession;
	
	public String title;
	public String text;	
	public Date timestamp;
		
	public StudySessionEvent(StudySession studySession,
							 String title,
							 String text) {

		this.studySession = studySession;
		this.title = title;
		this.text = text;
		this.timestamp = new Date();
		create();
	}
	
	public static List<StudySessionEvent> tail(long studySessionId, int page, int pageSize) {
		String q = "select e from StudySessionEvent e where e.studySession.id = ? order by e.timestamp desc";
		List<StudySessionEvent> events = 
								StudySessionEvent.find(q, studySessionId).fetch(page, pageSize);
		return events;
	}
}
