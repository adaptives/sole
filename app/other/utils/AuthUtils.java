package other.utils;

import controllers.Security;
import models.SocialUser;

public class AuthUtils {

	public static SocialUser getSocialUser() {
		String sUserId = Security.connected();
		long userId = Long.parseLong(sUserId);
		SocialUser user = SocialUser.findById(userId);
		return user;
	}
}
