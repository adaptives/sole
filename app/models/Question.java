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

import controllers.Security;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class Question extends Model {
	
	public String title;
	public String content;
	@ManyToOne
	public User author;
	public Date askedAt;
	public int votes;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
    public Set<Tag> tags;
	
	@OneToMany(mappedBy="question", cascade=CascadeType.ALL)
	public Set<Answer> answers;
	
	public boolean closed;
	public boolean answered;
	public boolean flagged;
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(Question.class);
	
	public Question(String title, String content, User author) {
		this.title = title;
		this.content = content;
		this.author = author;
		this.askedAt = new Date();
		this.tags = new TreeSet<Tag>();
		this.answers = new TreeSet<Answer>();
		create();
	}

	public Question tagWith(String tag) {
		tags.add(Tag.findOrCreateByName(tag));
        return this;
	}
	
	public void like(User user) {
		if(QuestionLiked.count("select count(distinct ql) from QuestionLiked ql where ql.question = ? and ql.user=?", this, user) == 0) {
			new QuestionLiked(this, user);
		} else {
			//TODO
			cLogger.warn("question '" + this.id + "' is already liked by user '" + user.id + "'");
		}		
	}
	
	public boolean hasLiked(User user) {		
		if(QuestionLiked.count("select count(distinct ql) from QuestionLiked ql where ql.question = ? and ql.user=?", this, user) == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public int likes() {
		return (int)QuestionLiked.count("select count(distinct ql) from QuestionLiked ql where ql.question.id = ?", this.id);
	}
	
	public String toString() {
		return id + " " + title;
	}
}
