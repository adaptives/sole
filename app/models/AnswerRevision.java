package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class AnswerRevision extends Model {
	public String note;
	public String content;
	
	@ManyToOne
	public SocialUser author;
	public Date revisedAt;
	
	@ManyToOne
	@Required
	public Answer answer;
	
	public AnswerRevision(String note, 
						  String content, 
						  SocialUser author, 
						  Answer answer) {
		this.note = note;
		this.content = content;
		this.author = author;
		this.answer = answer;
		this.revisedAt = new Date();
	}
}
