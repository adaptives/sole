package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class StudySessionApplicationStatusChange extends Model implements Comparable {
	
	@Required
	@ManyToOne
	public StudySessionApplication studySessionApplication;
	
	@Required
	public int previousStatus;
	
	@Required
	public int newStatus;
	
	@Required
	public String comment;
	
	@Required
	public Date timestamp;
	
	public StudySessionApplicationStatusChange(StudySessionApplication studySessionApplication,
											   int previousStatus,
											   int newStatus,
											   String comment) {
		
		this.studySessionApplication = studySessionApplication;
		this.previousStatus = previousStatus;
		this.newStatus = newStatus;
		this.comment = comment;
		this.timestamp = new Date();
		create();
	}

	/**
	 * The object with the most recent timestamp is considered to be larger
	 * by this method
	 */
	@Override
	public int compareTo(Object arg0) {
		StudySessionApplicationStatusChange other = (StudySessionApplicationStatusChange)arg0;
		long diff = this.timestamp.getTime() - other.timestamp.getTime();
		if(diff > 0) {
			return 1;
		} else if (diff < 0) {
			return -1;
		} else {
			return 0;
		}
	}
	 
}
