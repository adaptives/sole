package models;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class BlogPost extends Model {
	
	public User author;
	public String title;
	public Date postedAt;
	public String content;
	
	@ManyToMany
	public Set<Tag> tags;
	
	@OneToMany
	public Set<Comment> comments;
	
	public BlogPost(User author, 
					String title,  
					String content) {
		this.author = author;
		this.title = title;
		this.postedAt = new Date();
		this.content = content;
		this.tags = new TreeSet<Tag>();
		this.comments = new TreeSet<Comment>();
		create();
	}
	
	public BlogPost tagWith(String tag) {
		tags.add(Tag.findOrCreateByName(tag));
        return this;
	}
}
