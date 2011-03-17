package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Post extends Model {
	
	public String title;
	public Date postedAt;
	
	@Lob
	public String content;
	
	@ManyToOne
	public User author;
	
	@OneToMany(mappedBy="post",cascade=CascadeType.ALL)
	public List<Comment> comments;
	
	public Post(User author, String title, String content) {
		this.comments = new ArrayList<Comment>();
		this.author = author;
		this.title = title;
		this.content = content;
		this.postedAt = new Date();
	}
	
	//utility method
	public Post addComment(String author, String content) {
		Comment comment = new Comment(author, content, this);
		this.comments.add(comment);
		this.save();
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((postedAt == null) ? 0 : postedAt.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (postedAt == null) {
			if (other.postedAt != null)
				return false;
		} else if (!postedAt.equals(other.postedAt))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	
	
}
