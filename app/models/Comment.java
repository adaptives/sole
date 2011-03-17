package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Comment extends Model {
	public String author;
	public Date postedAt;
	
	@Lob
	public String content;
	
	@ManyToOne
	public Post post;
	
	public Comment(String author, String content, Post post) {
		this.author = author;
		this.content = content;
		this.post = post;
		this.postedAt = new Date();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((post == null) ? 0 : post.hashCode());
		result = prime * result
				+ ((postedAt == null) ? 0 : postedAt.hashCode());
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
		Comment other = (Comment) obj;
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
		if (post == null) {
			if (other.post != null)
				return false;
		} else if (!post.equals(other.post))
			return false;
		if (postedAt == null) {
			if (other.postedAt != null)
				return false;
		} else if (!postedAt.equals(other.postedAt))
			return false;
		return true;
	}
	
	
}
