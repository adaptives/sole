import groovy.lang.Closure;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import models.SocialUser;
import models.StudySession;
import models.StudySessionMeta;

import play.mvc.Router.ActionDefinition;
import play.mvc.Scope.Session;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;


public class SoleTags extends FastTags {
	
	public static void _studySessionMetadata(Map<?, ?> args, 
								Closure body, 
								PrintWriter out, 
								ExecutableTemplate template, 
								int fromLine) {
		
		ActionDefinition actionDefinition = null;
		StudySession studySession = (StudySession)args.get("ss");
		StudySessionMeta studySessionMeta = (StudySessionMeta)args.get("ssm");
		String userId = Session.current().get("user");
		if(studySession == null) {
			return;
		}
		if(studySessionMeta == null) {
			return;
		}
		out.print("<span class=\"course-status\">");
		if(studySessionMeta.canceled) {
			out.print("This course has been cancelled");
		}
		else if(studySessionMeta.locked) {
			out.println("This course is closed for further participation");
			
		}
		else if(studySessionMeta.enrollmentClosed) {
			out.print("This course is full but you can follow along without formally participating");
		}
		else if(studySession.canEnroll(userId)) {
			out.print("This Course is open for enrollment");
		}
		else if(studySession.isUserApplicationPending(Long.parseLong(userId))) {
			out.print("Applcation pending approval");
		}
		else if(studySession.isUserEnrolled(Long.parseLong(userId))) {
			out.print("You are enrolled in this course");
		}
		out.print("</span>");
	}
	
	public static void _apply(Map<?, ?> args, 
							  Closure body,
							  PrintWriter out, 
							  ExecutableTemplate template, 
							  int fromLine) {

		ActionDefinition actionDefinition = null;
		StudySession studySession = (StudySession) args.get("ss");
		StudySessionMeta studySessionMeta = (StudySessionMeta) args.get("ssm");
		String userId = Session.current().get("user");
		if (studySession == null) {
			return;
		}
		if (studySessionMeta == null) {
			return;
		}
		if (studySessionMeta.canceled) {
			return;
		}
		if (studySessionMeta.locked) {
			return;
		}
		if (studySessionMeta.enrollmentClosed) {
			return;
		}
		if (studySession.canEnroll(userId)) {
			Map<String, Object> methodArgs = new HashMap<String, Object>();
			methodArgs.put("id", studySession.id);
			actionDefinition = play.mvc.Router.reverse("StudySessionC.apply",
					methodArgs);
			String htmlLinkTemnplate = "<span class=\"%s\" ><a href=\"%s\">%s</a></span>";

			out.print(String.format(htmlLinkTemnplate,
					"study-session-enrollment", actionDefinition.url, "Apply"));
			return;
		}
		
		if(studySession.isUserEnrolled(Long.parseLong(userId)) || studySession.isUserApplicationPending(Long.parseLong(userId))) {
			Map<String, Object> methodArgs = new HashMap<String, Object>();
			methodArgs.put("id", studySession.id);
			actionDefinition = play.mvc.Router.reverse("StudySessionC.deregister",
					methodArgs);
			String htmlLinkTemnplate = "<span class=\"%s\" ><a href=\"%s\">%s</a></span>";

			out.print(String.format(htmlLinkTemnplate,
					"study-session-enrollment", actionDefinition.url, "Deregister"));
			return;
		}
	}

}
