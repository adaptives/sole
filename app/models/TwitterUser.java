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
	
	public boolean isTwitterUser(long socialUserId) {
		String query = "select su from TwitterUser tu join tu.socialUser su where su.id = ?";
		SocialUser socialUser = TwitterUser.find(query, socialUserId).first();
		return socialUser != null;
	}
	
	public static String getTwitterUsernameForSocialUser(long socialUserId) {
		String twitterUsername = "";
		String query = "select tu from TwitterUser tu join tu.socialUser su where su.id = ?";
		TwitterUser twitterUser = TwitterUser.find(query, socialUserId).first();
		if(twitterUser != null) {
			twitterUsername = twitterUser.username;
		}
		return twitterUsername;
	}
}
