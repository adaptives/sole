package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.Logger;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Answer extends Model {
	public String content;
	@ManyToOne
	public SocialUser author;
	public Date answeredAt;
	public int votes;
	public boolean flagged;
	
	@ManyToOne
	@Required
	public Question question;
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(Answer.class);
	
	public Answer(String content, SocialUser author, Question question) {
		this.content = content;
		this.author = author;
		this.answeredAt = new Date();
		this.question = question;
		create();
	}
	
	public void like(SocialUser user) {
		//Users cannot upvote their own answer and they cannot upvote any answer 
		//more than once 
		if(user.id != this.author.id) {
			if(!hasLiked(user)) {
				new AnswerLiked(this, user);
			} else {
				//TODO Rate check ?
				cLogger.warn(user.id + "'" + " has already liked answer '" + this.id);
			}			
		} else {
			//TODO Rate check ?
			cLogger.warn(user.id + "'" + " cannot like own answer '" + this.id);
		}		
	}

	public int likes() {
		return (int)AnswerLiked.count("select count(distinct al) from AnswerLiked al where al.answer.id = ?", this.id);
	}
	
	public boolean hasLiked(SocialUser user) {
		if(AnswerLiked.count("select count(distinct al) from AnswerLiked al where al.answer = ? and al.user=?", this, user) == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public String toString() {
		return id + " " + content;
	}
}
