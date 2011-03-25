package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Answer extends Model {
	@ManyToOne
	@Required
	public Question question;
	
	public String content;
	
	public Answer(String content, Question question) {
		this.content = content;
		this.question = question;
		create();
	}
	
	public String toString() {
		return id + " " + content;
	}
}
