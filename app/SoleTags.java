import groovy.lang.Closure;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import controllers.Security;

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
		
		StudySession studySession = (StudySession)args.get("ss");
		StudySessionMeta studySessionMeta = (StudySessionMeta)args.get("ssm");		
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
		else {
			out.print("This Course is open for enrollment. ");
		}
		if(Security.isConnected()) {
			String userId = Security.connected();
			long lUserId = Long.parseLong(userId);
			if(studySession.canEnroll(lUserId)) {
				// We do not do anything. The _apply tag will display the link to enroll
			} else if(studySession.applicationStore.isUserApplicationPending(lUserId)) {
				out.print("Applcation pending approval. ");
			} else if(studySession.applicationStore.isUserApplicationAccepted(lUserId)) {
				out.print("You are enrolled in this course. ");
			} else if(studySession.isFacilitator(lUserId)) {
				out.println("You are a facilitator in this course. ");
			}
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
		if(Security.isConnected()) {
			String userId = Security.connected();
			long lUserId = Long.parseLong(userId);
			if (studySession.canEnroll(lUserId)) {
				Map<String, Object> methodArgs = new HashMap<String, Object>();
				methodArgs.put("id", studySession.id);
				actionDefinition = play.mvc.Router.reverse("StudySessionC.apply",
						methodArgs);
				String htmlLinkTemnplate = "<span class=\"%s\" ><a href=\"%s\">%s</a></span>";

				out.print(String.format(htmlLinkTemnplate,
						"study-session-enrollment", actionDefinition.url, "Apply"));
				return;
			}
			
			if(studySession.applicationStore.isUserApplicationAccepted(lUserId) || studySession.applicationStore.isUserApplicationPending(lUserId)) {
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
}
