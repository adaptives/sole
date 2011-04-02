package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class QuestionLiked extends Model {
	
	@OneToOne
	public Question question;
	@OneToOne
	public User user;
	
	public QuestionLiked(Question question, User user) {
		this.question = question;
		this.user = user;
		create();
	}
}
