package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class CodeSnippet extends Model {
	
	@Required
	@ManyToOne
	public SocialUser user;
	
	public String language;	
	
	@Required
	public String title;
	
	@Required
	@Lob
	public String code;
	
	@Required
	@ManyToOne
	public Pastebin pastebin;
	
	public Date timestamp;
	
	public CodeSnippet(SocialUser user, 
					   Pastebin pastebin, 
					   String title, 
					   String code) {
		this.user = user;
		this.title = title;
		this.code = code;
		this.pastebin = pastebin;
		this.timestamp = new Date();
	}
	
	public boolean owns(String userId) {
		boolean retVal = false;
		try {
			Long lUserId = Long.parseLong(userId);
			if(this.user.id == lUserId) {
				retVal = true;
			}
		} catch(Exception e) {
			
		}
		return retVal;
	}
	
	@Override
	public String toString() {
		return "[" + this.user.id + "]" + " [" + this.pastebin.id + "]";
	}
	
}
