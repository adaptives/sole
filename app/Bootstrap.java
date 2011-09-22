import models.Activity;
import models.Course;
import models.CourseCategory;
import models.CourseSection;
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
			Fixtures.load("users-and-study-sessions.yml");
			Fixtures.load("pages.yml");
			Fixtures.load("diycourses.yml");
			Fixtures.load("kvdata.yml");
			Fixtures.load("site-events.yml");
			createCourse();
		}

	}
	
	private void createCourse() {
		CourseCategory cat = new CourseCategory("courses");
		cat.save();
		
		Course course = new Course("Play Framework", "Play framework course");
		course.category = cat;
		course.save();
		CourseSection section = new CourseSection(course, "introduction", "Introductory section");
		Activity activity = new Activity("Blog", "Please write a blog post");
		section.activities.add(activity);
		section.save();
	}
	
}
