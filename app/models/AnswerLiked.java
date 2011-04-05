package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class AnswerLiked extends Model {
	
	@OneToOne
	public Answer answer;
	@OneToOne
	public SocialUser user;
	
	public AnswerLiked(Answer answer, SocialUser user) {
		this.answer = answer;
		this.user = user;
		create();
	}
}
