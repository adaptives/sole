import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import other.utils.StringUtils;

import models.ChangedUrl;
import models.Course;
import models.CourseSection;
import models.Question;
import models.SessionPart;
import models.SocialUser;
import models.StudySession;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.mvc.Router;
import play.test.Fixtures;

@OnApplicationStart
public class CreateChangedUrls extends Job {
	
	public void doJob() {
		List<Course> courses = Course.findAll();
		for(Course course : courses) {
			
			List<CourseSection> courseSections = course.fetchSectionsByPlacement();
			
			for(CourseSection courseSection : courseSections) {
				courseSection.sanitizedTitle = StringUtils.replaceSpaceWithDashes(courseSection.title);
				courseSection.save();
				
			}
		}
	}
}
