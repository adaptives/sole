package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Answer extends Model {
	public String content;
	@ManyToOne
	public User author;
	public Date answeredAt;
	public int votes;
	public boolean flagged;
	
	@ManyToOne
	@Required
	public Question question;
	
	
	
	public Answer(String content, User author, Question question) {
		this.content = content;
		this.author = author;
		this.answeredAt = new Date();
		this.question = question;
		create();
	}
	
	public String toString() {
		return id + " " + content;
	}
}
