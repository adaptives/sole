package other.utils;

import java.util.HashMap;
import java.util.Map;

import models.Course;
import models.Question;
import models.SocialUser;

import play.mvc.Router;
import play.mvc.Router.ActionDefinition;

public class LinkGenUtils {
	
	public static String getUserProfileLink(SocialUser user) {
		Map actionArgs = new HashMap();
		actionArgs.put("userId", user.id);
		ActionDefinition actionDef = 
							Router.reverse("UserProfileC.show", actionArgs);
		
		//ActionDefinition imageDef = Router.reverse("UserProfileC.pic", actionArgs);
		//String img = String.format("<img width=\"20\" height=\"20\" src=\"%s\" />", imageDef.url);
		String img = String.format("<span id=\"%s\" class=\"user-image-small\">", user.id);
		String screenname = user.screenname;
		String userLink = "<a href=\"" + actionDef.url + "\"> %s </a>";
		return String.format(userLink, img) + String.format(userLink, screenname);
	}
	
	public static String getDIYCourseLink(Course course) {
		Map actionArgs = new HashMap();
		actionArgs.put("sanitizedTitle", course.sanitizedTitle);
		ActionDefinition actionDef = 
							Router.reverse("CourseC.course", actionArgs);
		return "<a href=\"" + actionDef.url + "\">" +  course.title + "</a>";
	}
	
	public static String getStudySessionLink(long studySessionId) {
		Map actionArgs = new HashMap();
		actionArgs.put("id", studySessionId);
		ActionDefinition actionDef = 
							Router.reverse("StudySessionC.studySession", actionArgs);
		return actionDef.url;
	}
}
