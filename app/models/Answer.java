package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import controllers.Security;

import play.Logger;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Answer extends Model implements Comparable {
	@Lob
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
	
	public boolean canEdit(String sUserId) {
		boolean retVal = false;
		try {
			long userId = Long.parseLong(sUserId);
			if(this.author.id == userId) {
				retVal = true;
			}
		} catch(Exception e) {
			//no action
		}
		return retVal;
	}
	
	//TODO: Inconsistent naming since this method begins with a 'fetch' in model.Question
	public String getLatestRevision() {
		String defaultContent = this.content;
		String query = "select ar from AnswerRevision ar where ar.answer.id = ? order by ar.revisedAt asc";
		AnswerRevision ar = AnswerRevision.find(query, this.id).first();
		if(ar != null) {
			defaultContent = ar.content;
		}
		return defaultContent;
	}
		
	public String toString() {
		return id + " " + content;
	}

	@Override
	//TODO: Create a compareUtils which will compare long objects once and for all
	public int compareTo(Object arg0) {
		Answer answer = (Answer)arg0;
		long diff = this.id - answer.id;
		if(diff < 0) {
			return -1;
		} else if(diff > 0) {
			return 1;
		} else {
			return 0;
		}
	}

}
