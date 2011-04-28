package models;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class BlogPost extends Model {
	
	@ManyToOne
	public SocialUser author;
	
	public String title;
	public Date postedAt;
	public Date lastUpdatedAt;
	public String content;
	
	@ManyToMany
	public Set<Tag> tags;
	
	@OneToMany
	public Set<Comment> comments;
	
	public BlogPost(SocialUser author, 
					String title,  
					String content) {
		this.author = author;
		this.title = title;
		this.postedAt = new Date();
		this.lastUpdatedAt = postedAt;
		this.content = content;
		this.tags = new TreeSet<Tag>();
		this.comments = new TreeSet<Comment>();
		//create();
	}
	
	public BlogPost tagWith(String tag) {
		tags.add(Tag.findOrCreateByName(tag));
        return this;
	}
	
	public static List<BlogPost> findLatest(int count) {
		List<BlogPost> blogPosts = 
			BlogPost.find("select b from BlogPost b order by b.lastUpdatedAt desc").fetch(1, count);
		return blogPosts;
	}
}
