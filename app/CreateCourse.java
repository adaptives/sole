import java.util.List;

import models.Activity;
import models.Course;
import models.CourseCategory;
import models.CourseSection;
import models.MessageCenter;
import models.Pastebin;
import models.SocialUser;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class CreateCourse extends Job {
	
	public void doJob() {
//		for(Object socialUser : SocialUser.findAll()) {
//			MessageCenter messageCenter = new MessageCenter((SocialUser)socialUser);
//			messageCenter.save();
//		}
//		List<Activity> activities = Activity.findAll();
//		for(Activity activity : activities) {
//			long longId = activity.id;
//			activity.placement = (int)longId;
//			activity.save();
//		}
	}
}
