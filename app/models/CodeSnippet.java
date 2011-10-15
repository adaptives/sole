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
		cLogger.info("Determing if user '" + userId + "' owns the codeSnippet whose owner is '" + this.user.id + "'");
		boolean retVal = false;
		try {
			Long lUserId = Long.parseLong(userId);
			if((long)this.user.id == (long)lUserId) {
				retVal = true;
			} else {
				cLogger.info(lUserId + " is not equal to " + this.user.id);
			}
		} catch(Exception e) {
			cLogger.warn("Unparseable userId '" + userId + "'");
		}
		cLogger.info("They do ? " + retVal);
		return retVal;
	}
	
	@Override
	public String toString() {
		return "[" + this.user.id + "]" + " [" + this.pastebin.id + "]";
	}
	
}
