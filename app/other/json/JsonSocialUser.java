package other.json;

import models.SocialUser;

public class JsonSocialUser {
	
	public long id;
	public String screenname;
	
	public JsonSocialUser(SocialUser socialUser) {
		this.id = socialUser.id;
		this.screenname = socialUser.screenname;
	}
}
