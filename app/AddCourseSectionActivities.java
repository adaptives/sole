import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import other.utils.StringUtils;

import models.Activity;
import models.ChangedUrl;
import models.Course;
import models.CourseSection;
import models.KeyValueData;
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
public class AddCourseSectionActivities extends Job {
	
	public void doJob() {
		List<CourseSection> courseSections = CourseSection.findAll();
		for(CourseSection courseSection : courseSections) {
			courseSection.activities = new TreeSet<Activity>();
			courseSection.save();
		}
	}
}
