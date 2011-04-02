package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class AnswerLiked extends Model {
	
	public Answer answer;
	public User user;
	
	public AnswerLiked(Answer answer, User user) {
		this.answer = answer;
		this.user = user;
		create();
	}
}
