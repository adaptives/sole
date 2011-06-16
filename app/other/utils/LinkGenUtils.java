package other.utils;

import java.util.HashMap;
import java.util.Map;

import models.Course;
import models.Question;
import models.SocialUser;
import models.StudySession;

import play.mvc.Router;
import play.mvc.Router.ActionDefinition;

public class LinkGenUtils {
	
	public static String getUserProfileLink(SocialUser user) {
		Map actionArgs = new HashMap();
		actionArgs.put("userId", user.id);
		ActionDefinition actionDef = 
							Router.reverse("UserProfileC.show", actionArgs);
		
		ActionDefinition imageDef = Router.reverse("UserProfileC.pic", actionArgs);
		String img = String.format("<img src=\"%s\" />", imageDef.url);
		String screenname = user.screenname;
		String userLink = "<a href=\"" + actionDef.url + "\"> %s </a>";
		return String.format(userLink, img) + String.format(userLink, screenname);
	}
	
	public static String getDIYCourseLink(Course course) {
		Map actionArgs = new HashMap();
		actionArgs.put("courseId", course.id);
		ActionDefinition actionDef = 
							Router.reverse("CourseC.course", actionArgs);
		return "<a href=\"" + actionDef.url + "\">" +  course.title + "</a>";
	}
	
	public static String getStudyGroupQuestionLink(long studySessionId, 
												   Question question) {
		StudySession studySession = StudySession.findById(studySessionId);
		Map actionArgs = new HashMap();
		actionArgs.put("sanitizedTitle", studySession.sanitizedTitle);
		actionArgs.put("questionId", question.id);
		actionArgs.put("sanitizedQuestionTitle", question.sanitizedTitle);
		ActionDefinition actionDef = 
							Router.reverse("StudySessionC.forumQuestion", actionArgs);
		return "<a href=\"" + actionDef.url + "\">" +  question.title + "</a>";
	}
	
	public static String getViewAllResponsesLink(long studySessionId, 
												 long sessionPartId) {
		Map actionArgs = new HashMap();
		actionArgs.put("studySessionId", studySessionId);
		actionArgs.put("sessionPartId", sessionPartId);
		ActionDefinition actionDef = 
							Router.reverse("StudySessionC.sessionPartActivityResponses", actionArgs);
		return "<a href=\"" + actionDef.url + "\">" +  "response" + "</a>";
	}
	
	public static String getStudySessionLink(long studySessionId) {
		Map actionArgs = new HashMap();
		actionArgs.put("id", studySessionId);
		ActionDefinition actionDef = 
							Router.reverse("StudySessionC.studySession", actionArgs);
		return actionDef.url;
	}
}
