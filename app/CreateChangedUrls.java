import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
//		List<Course> courses = Course.findAll();
//		for(Course course : courses) {
//			
//			List<CourseSection> courseSections = course.fetchSectionsByPlacement();
//			
//			for(CourseSection courseSection : courseSections) {
//				courseSection.sanitizedTitle = StringUtils.replaceSpaceWithDashes(courseSection.title);
//				courseSection.save();
//				
//			}
//		}
		
		List<ChangedUrl> changedUrls = ChangedUrl.findAll();
		for(ChangedUrl changedUrl : changedUrls) {
			if(changedUrl.newUrl.equals("")) {
				///courses/course/5
				String oldUrl = changedUrl.oldUrl;
				String pattern = "/courses/course/(\\d)$";
				Pattern p = Pattern.compile(pattern);
				Matcher matcher = p.matcher(oldUrl);
				
				boolean matches = matcher.matches();
				int groupCount = matcher.groupCount();
				
				if(matches && groupCount > 0) {
					int groupStart = matcher.start(1);
					int groupEnd = matcher.end(1);
					String groupValue = oldUrl.substring(groupStart, groupEnd);
					long courseId = Long.valueOf(groupValue);					
					Course course = Course.findById(courseId);
					if(course != null) {
						String newUrl = oldUrl.replaceAll("/\\d", "/" + course.sanitizedTitle);
						changedUrl.newUrl = newUrl;
						changedUrl.save();
						System.out.println("Changed url from '" + oldUrl + "' to '" + newUrl);
					}
				} else {
					// /courses/course/5/section/84
					pattern = "/courses/course/(\\d)/section/(\\d+)$";
					p = Pattern.compile(pattern);
					matcher = p.matcher(oldUrl);
					matches = matcher.matches();
					groupCount = matcher.groupCount();
					if(matches && groupCount > 1) {
						int group1Start = matcher.start(1);
						int group1End = matcher.end(1);
						String sCourseId = oldUrl.substring(group1Start, group1End);
						long courseId = Long.valueOf(sCourseId);					
						Course course = Course.findById(courseId);
						
						int group2Start = matcher.start(2);
						int group2End = matcher.end(2);
						String sCourseSectionId = oldUrl.substring(group2Start, group2End);
						long courseSectionId = Long.valueOf(sCourseSectionId);
						CourseSection courseSection = CourseSection.findById(courseSectionId);
							
						if(course != null && courseSection != null) {
							String newUrl = oldUrl.replaceAll("/\\d+/", "/" + course.sanitizedTitle + "/");
							newUrl = newUrl.replaceAll("/\\d+$", "/" + courseSection.sanitizedTitle);
							changedUrl.newUrl = newUrl;
							changedUrl.save();
						}
					}
				}
			}
		}
	}
}
