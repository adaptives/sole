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
			course.sanitizedTitle = StringUtils.replaceSpaceWithDashes(course.title);
			course.save();
			
			Map<String, Object> argMap = new HashMap<String, Object>();
			argMap.put("courseId", course.id);
			ChangedUrl changedUrl = new ChangedUrl(Router.reverse("CourseC.course", argMap).url, "");
			
			List<CourseSection> courseSections = course.fetchSectionsByPlacement();
			
			for(CourseSection courseSection : courseSections) {
				Map<String, Object> argsMap1 = new HashMap<String, Object>();
				argsMap1.put("courseId", course.id);
				argsMap1.put("sectionId", courseSection.id);
				ChangedUrl changedUrlForCourseSection = 
					new ChangedUrl(Router.reverse("CourseC.section", argsMap1).url, "");
			}
		}
	}
}
