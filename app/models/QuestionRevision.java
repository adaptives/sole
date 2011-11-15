package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class QuestionRevision extends Model {
	public String note;
	public String content;
	
	@ManyToOne
	public SocialUser author;
	public Date revisedAt;
	
	@ManyToOne
	@Required
	public Question question;
	
	public QuestionRevision(String note,
							String content,
							SocialUser author,
							Question question) {
		this.note = note;
		this.content = content;
		this.author = author;
		this.question = question;
		this.revisedAt = new Date();
	}
}
