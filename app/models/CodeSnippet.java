package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import controllers.CourseSecureC;

import play.Logger;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class CodeSnippet extends Model {
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(CodeSnippet.class); 
	
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
			//TODO: Why do we need this cast ???
			if((long)this.user.id == (long)lUserId) {
				retVal = true;
			}
		} catch(Exception e) {
			cLogger.warn("Unparseable userId '" + userId + "'");
		}
		return retVal;
	}
	
	@Override
	public String toString() {
		return "[" + this.user.id + "]" + " [" + this.pastebin.id + "]";
	}
	
}
