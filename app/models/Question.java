package models;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Question extends Model {
	public String title;
	public String content;
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
	
	public Question(String title, String content, User author) {
		this.title = title;
		this.content = content;
		this.author = author;
		this.askedAt = new Date();
		this.tags = new TreeSet<Tag>();
		this.answers = new TreeSet<Answer>();
	}

	public Question tagWith(String tag) {
		tags.add(Tag.findOrCreateByName("tag"));
        return this;
	}
	
	public String toString() {
		return id + " " + title;
	}
}
