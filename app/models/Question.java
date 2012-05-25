package models;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import other.utils.StringUtils;

import controllers.Security;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class Question extends Model {
	
	public String title;
	public String sanitizedTitle;
	public String content;
	@ManyToOne
	public SocialUser author;
	public Date askedAt;
	public int votes;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
    public Set<Tag> tags;
	
	@OneToMany(mappedBy="question", cascade=CascadeType.ALL)
	@OrderBy("answeredAt") 
	public Set<Answer> answers;
	
	public boolean closed;
	public boolean answered;
	public boolean flagged;	
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(Question.class);
	
	public Question(String title, String content, SocialUser author) {
		this.title = title;
		this.sanitizedTitle = StringUtils.replaceSpaceWithDashes(this.title);
		this.content = content;
		this.author = author;
		this.askedAt = new Date();
		this.tags = new TreeSet<Tag>();
		this.answers = new TreeSet<Answer>();
		create();
	}
	
	@Override
	public Question delete() {
		this.tags.removeAll(this.tags);
		this.save();
		return super.delete();
	}

	public Question tagWith(String tag) {
		tags.add(Tag.findOrCreateByName(tag));
        return this;
	}
	
	public void like(SocialUser user) {
		//Users cannot upvote their own question and they cannot upvote any 
		//question more than once 
		if(this.author.id != user.id) {
			if(!hasLiked(user)) {
				new QuestionLiked(this, user);
			} else {
				//TODO Rate check ?
				cLogger.warn(user.id + "'" + " has already liked question '" + this.id);
			}			
		} else {
			//TODO Rate check ?
			cLogger.warn(user.id + "'" + " cannot like own question '" + this.id);
		}		
	}
	
	public boolean hasLiked(SocialUser user) {
		if(AnswerLiked.count("select count(distinct ql) from QuestionLiked ql where ql.question = ? and ql.user=?", this, user) == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public int likes() {
		return (int)QuestionLiked.count("select count(distinct ql) from QuestionLiked ql where ql.question.id = ?", this.id);
	}
	
	public String fetchLatestRevision() {
		String defaultContent = this.content;
		String query = "select qr from QuestionRevision qr where qr.question.id = ? order by qr.revisedAt asc";
		QuestionRevision qr = QuestionRevision.find(query, this.id).first();
		if(qr != null) {
			defaultContent = qr.content;
		}
		return defaultContent;
	}
	
	public Set<Tag> fetchLatestTags() {
		Set<Tag> defaultTags = this.tags;
		String query = "select qr from QuestionRevision qr where qr.question.id = ? order by qr.revisedAt asc";
		QuestionRevision qr = QuestionRevision.find(query, this.id).first();
		if(qr != null) {
			defaultTags = qr.tags;
		}
		return defaultTags;
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
	
	public String toString() {
		return id + " " + title;
	}
}
