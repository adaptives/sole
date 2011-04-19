package other.utils;

import java.util.HashMap;
import java.util.Map;

import models.Course;
import models.SocialUser;

import play.mvc.Router;
import play.mvc.Router.ActionDefinition;

public class LinkGenUtils {
	
	public static String getUserProfileLink(SocialUser user) {
		Map actionArgs = new HashMap();
		actionArgs.put("userId", user.id);
		ActionDefinition actionDef = 
							Router.reverse("UserProfileC.show", actionArgs);
		return "<a href=\"" + actionDef.url + "\">" +  user.screenname + "</a>";
	}
	
	public static String getDIYCourseLink(Course course) {
		Map actionArgs = new HashMap();
		actionArgs.put("courseId", course.id);
		ActionDefinition actionDef = 
							Router.reverse("CourseC.course", actionArgs);
		return "<a href=\"" + actionDef.url + "\">" +  course.title + "</a>";
	}
}
