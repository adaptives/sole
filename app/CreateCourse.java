import java.util.List;

import models.Course;
import models.CourseCategory;
import models.CourseSection;
import models.Feedback;
import models.ModeratedFeedback;
import models.Pastebin;
import models.SocialUser;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class CreateCourse extends Job {
	
	public void doJob() {
		
	}
}
