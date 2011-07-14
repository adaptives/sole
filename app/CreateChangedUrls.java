import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class CreateChangedUrls extends Job {
	private String chapters[] = {
			"Programming Methodologies Using Java - Lecture 1", 
			"Programming Methodologies Using Java - Lecture 2",
			"Programming Methodologies Using Java - Lecture 3",
			"Programming Methodologies Using Java - Lecture 4", 
			"Programming Methodologies Using Java - Lecture 5", 
			"Programming Methodologies Using Java - Lecture 6", 
			"Programming Methodologies Using Java - Lecture 7", 
			"Programming Methodologies Using Java - Lecture 8", 
			"Programming Methodologies Using Java - Lecture 9", 
			"Programming Methodologies Using Java - Lecture 10", 
			"Programming Methodologies Using Java - Lecture 11", 
			"Programming Methodologies Using Java - Lecture 12",
			"Programming Methodologies Using Java - Lecture 13",
			"Programming Methodologies Using Java - Lecture 14", 
			"Programming Methodologies Using Java - Lecture 15", 
			"Programming Methodologies Using Java - Lecture 16", 
			"Programming Methodologies Using Java - Lecture 17", 
			"Programming Methodologies Using Java - Lecture 18", 
			"Programming Methodologies Using Java - Lecture 19", 
			"Programming Methodologies Using Java - Lecture 20", 
			"Programming Methodologies Using Java - Lecture 21", 
			"Programming Methodologies Using Java - Lecture 22",
			"Programming Methodologies Using Java - Lecture 23",
			"Programming Methodologies Using Java - Lecture 24", 
			"Programming Methodologies Using Java - Lecture 25", 
			"Programming Methodologies Using Java - Lecture 26", 
			"Programming Methodologies Using Java - Lecture 27", 
			"Programming Methodologies Using Java - Lecture 28",
			};
	
			String courseSanitizedTitle = "programming-methodologies-Using Java";
			
	public void doJob() {
		Course course = Course.findBySanitizedTitle(courseSanitizedTitle);
		if(course != null) {
			for (int i=0; i<chapters.length; i++) {
				CourseSection courseSection = new CourseSection(course, chapters[i], "");
				courseSection.placement = i + 1;
				courseSection.save();
			}
		}
	}
}
