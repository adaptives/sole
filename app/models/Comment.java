package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Comment extends Model {
	public String content;
	public String author;
	public String email;
	public String website;
	public Date time;
	public boolean flagged;

	
	public Comment(String content, 
				   String author, 
				   String email, 
				   String website) {
		this.content = content;
		this.author = author;
		this.email = email;
		this.website = website;
		this.time = new Date();
	}


	@Override
	public String toString() {
		return "Comment [content=" + content + ", author=" + author
				+ ", email=" + email + ", website=" + website + ", flagged="
				+ flagged + "]";
	}
	
	
}
