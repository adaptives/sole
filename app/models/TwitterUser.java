package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class TwitterUser extends Model {
	
	@Required
	public String username;
	
	@Lob
	public byte[] accessToken;
	
	@Required
	@OneToOne
	public SocialUser socialUser;
	
	public TwitterUser(String username, SocialUser socialUser) {
		this.username = username;
		this.socialUser = socialUser;
	}
}
