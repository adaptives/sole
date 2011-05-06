package controllers;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Answer;
import models.DefaultStudySessionAffiliatez;
import models.Forum;
import models.Question;
import models.SessionPart;
import models.SocialUser;
import models.StudySession;
import models.StudySessionApplication;
import models.StudySessionEvent;
import models.StudySessionMeta;
import models.User;
import play.Logger;
import play.Play;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(SocialAuthC.class)
public class StudySessionC extends Controller {
	
	public static final org.apache.log4j.Logger cLogger = 
								Logger.log4j.getLogger(StudySessionC.class);
	
	public static void currentlist() {
		List<StudySession> studySessionList = StudySession.findAll();
		Map<StudySession, StudySessionMeta> studySessions = 
								new HashMap<StudySession, StudySessionMeta>();
		for(StudySession studySession : studySessionList) {
			studySessions.put(studySession, StudySessionMeta.forStudySession(studySession.id));
		}
		
		render(studySessions, getDefaultStudySessionAffiliatez());
	}

	public static void studySession(long id) {
		StudySession studySession = StudySession.findById(id);
		if(studySession != null) {
			StudySessionMeta studySessionMeta = StudySessionMeta.forStudySession(studySession.id);
			render(studySession, studySessionMeta);
		} else {
			flash.error("Sorry we could not find the Study Group");
			render("emptypage.html");
		}
	}
	
	public static void sessionPart(long studySessionId, long id) {
		SessionPart sessionPart = SessionPart.findById(id);
		if(sessionPart != null) { 
			render(studySessionId, 
				   sessionPart);
		} else {
			flash.error("Sorry we could not find the Study Group Section");
			render("emptypage.html");
		}
	}
	
	public static void participants(long studySessionId) {
		StudySession studySession = StudySession.findById(studySessionId);
		List<SocialUser> participants = studySession.getAcceptedUsers();		
		render(studySession, participants);
	}
	
	public static void forum(long studySessionId) {
		forumQuestion(studySessionId, -1);
	}
	
	public static void forumQuestion(long studySessionId, 
												 long questionId) {
		List<String> tabIds = new ArrayList<String>();
		tabIds.add("questions");
		tabIds.add("selected-question");
		List<String> tabNames = new ArrayList<String>();
		tabNames.add("Questions");
		tabNames.add("Selected Question");
		
		StudySession studySession = StudySession.findById(studySessionId);
		if(studySession != null) {
			Question question = Question.findById(questionId);
			
			render("StudySessionC/forum.html", 
				   studySession, 
				   question, 
				   tabIds, 
				   tabNames);
		} else {
			flash.error("Sorry we could not find the specified Forum");
			render("emptypage.html");
		}
		
	}
	
	public static void sessionPartActivityResponses(long studySessionId, long sessionPartId) {
		SessionPart sessionPart = SessionPart.findById(sessionPartId);
		if(sessionPart != null) {
			render(sessionPart);
		} else {
			flash.error("Sorry we could not find the Study Group Section");
			render("emptypage.html");
		}
	}
	
	public static void resources(long studySessionId) {
		StudySession studySession = StudySession.findById(studySessionId);
		render(studySession);
	}
	
	public static void pic(long studySessionId) {
		StudySession studySession = StudySession.findById(studySessionId);
		if(studySession.studySessionPic != null) {
			renderBinary(studySession.studySessionPic.image.get());
		} else {
			try {
				InputStream is = new FileInputStream(Play.getFile("public/images/default_study_session.gif"));
				renderBinary(is);
			} catch(Exception e) {
				cLogger.error("Could not render default user image ", e);
			}
		}
	}
	
	private static Object getDefaultStudySessionAffiliatez() {
		List<DefaultStudySessionAffiliatez> defaultStudySessionLocationAffiliates =
			DefaultStudySessionAffiliatez.findAll();
		DefaultStudySessionAffiliatez defaultAffilatez = null;
		if(defaultStudySessionLocationAffiliates.size() > 0) {
			defaultAffilatez = defaultStudySessionLocationAffiliates.get(0);
		}
		return defaultAffilatez;
	}
	
}
