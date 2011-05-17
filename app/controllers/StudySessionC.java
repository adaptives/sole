package controllers;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Activity;
import models.ActivityResponse;
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
	
	public static class ActivityResponseComparator implements Comparator {

		@Override
		public int compare(Object o1, Object o2) {
			ActivityResponse a1 = (ActivityResponse)o1;
			ActivityResponse a2 = (ActivityResponse)o2;
			if(a1.timestamp != null && a2.timestamp != null) {
				return (int)(a1.timestamp.getTime() - a2.timestamp.getTime());
			} else if(a1.timestamp == null && a2.timestamp != null) {
				return -1;
			} else if(a1.timestamp != null && a2.timestamp == null) {
				return 1;
			} else {
				return 0;
			}
		}
		
	}
	
	public static void currentlist() {
		Date now = new Date();
		
		//Get list of study sessions which are yet to start
		List<StudySession> yetToStartList = StudySession.getYetToStart(now);
		Map<StudySession, StudySessionMeta> yetToStart = 
								new HashMap<StudySession, StudySessionMeta>();
		for(StudySession studySession : yetToStartList) {
			yetToStart.put(studySession, 
										StudySessionMeta.
											forStudySession(studySession.id));
		}
		
		//Get list of study sessions which have started
		List<StudySession> ongoingList = StudySession.getOngoing(now);
		Map<StudySession, StudySessionMeta> ongoing = 
								new HashMap<StudySession, StudySessionMeta>();
		for(StudySession studySession : ongoingList) {
			ongoing.put(studySession, 
						StudySessionMeta.
						forStudySession(studySession.id));
		}
		
		//Get list of study sessions which are over (but not yet closed)
		List<StudySession> overList = StudySession.getOver(now);
		Map<StudySession, StudySessionMeta> over = 
								new HashMap<StudySession, StudySessionMeta>();
		for(StudySession studySession : overList) {
			over.put(studySession, 
					 StudySessionMeta.
					 forStudySession(studySession.id));
		}
		
		render(yetToStart, 
			   ongoing, 
			   over, 
			   getDefaultStudySessionAffiliatez());
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
	
	public static void archives() {
		render();
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
