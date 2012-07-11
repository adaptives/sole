import other.utils.InitUtils;
import models.Activity;
import models.Course;
import models.CourseCategory;
import models.CourseSection;
import models.KeyValueData;
import models.Page;
import models.Role;
import models.SiteEvent;
import models.SocialUser;
import models.User;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
		if(Play.mode == Play.Mode.DEV && SocialUser.findAll().size() == 0) {
			//InitUtils.initData();
		}
	}
	
}
