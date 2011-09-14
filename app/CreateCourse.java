import java.util.List;

import models.Course;
import models.CourseCategory;
import models.CourseSection;
import models.Pastebin;
import models.SocialUser;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class CreateCourse extends Job {

	public void doJob() {
		List<Course> courses = Course.findAll();
		for(Course course : courses) {
			Pastebin pastebin = new Pastebin(course.sanitizedTitle);
			pastebin.save();
		}
	}
}
