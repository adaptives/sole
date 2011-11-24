package other.dob;

import models.SocialUser;

public class UserCourseProgress {
	
	public final SocialUser user;
	public final double progress;
	
	public UserCourseProgress(SocialUser user, double progress) {
		this.user = user;
		this.progress = progress;
	}
}
