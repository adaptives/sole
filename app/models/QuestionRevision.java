package models;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class QuestionRevision extends Model {
	public String note;
	@Lob
	public String content;
	
	@ManyToOne
	public SocialUser author;
	public Date revisedAt;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
    public Set<Tag> tags;
	
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
		this.tags = new TreeSet<Tag>();
		this.revisedAt = new Date();
	}
	
	public void tagWith(String tag) {
		tags.add(Tag.findOrCreateByName(tag));
	}
	
	public String toString() {
		return id + " " + this.question.title;
	}
}
