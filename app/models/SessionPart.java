package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class SessionPart extends Model {
	
	public String title;	
	public Date startDate;
	public Date endDate;
	public String content;
	
	@Required
	@ManyToOne
	public StudySession studySession;
	
	public SessionPart(String title, 
					   Date startDate,
					   Date endDate,
					   String content) {
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.content = content;
	}
}
