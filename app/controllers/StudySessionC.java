package controllers;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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

	public static void studySession(String sanitizedTitle) {
		StudySession studySession = StudySession.findBySanitizedTitle(sanitizedTitle);
		notFoundIfNull(studySession);
		StudySessionMeta studySessionMeta = StudySessionMeta.forStudySession(studySession.id);
		render(studySession, studySessionMeta);
	}
	
	public static void sessionPart(String sanitizedTitle, String sessionPartSanitizedTitle) {
		StudySession studySession = StudySession.findBySanitizedTitle(sanitizedTitle);
		notFoundIfNull(studySession);
		SessionPart sessionPart = SessionPart.findBySanitizedTitle(sessionPartSanitizedTitle);
		notFoundIfNull(sessionPart);
		render(studySession, sessionPart);
	}
	
	public static void participants(String sanitizedTitle) {
		StudySession studySession = StudySession.findBySanitizedTitle(sanitizedTitle);
		List<SocialUser> participants = studySession.getAcceptedUsers();
		render(studySession, participants);
	}
	
	public static void forum(String sanitizedTitle) {
		StudySession studySession = StudySession.findBySanitizedTitle(sanitizedTitle);
		render(studySession);
	}
	
	public static void forumQuestion(String sanitizedTitle, 
									 long questionId,
									 String sanitizedQuestionTitle) {
		StudySession studySession = StudySession.findBySanitizedTitle(sanitizedTitle);
		notFoundIfNull(studySession);
		Question question = Question.findById(questionId);
		notFoundIfNull(question);
		render(studySession, question);
	}

	public static void sessionPartActivityResponses(String sanitizedTitle, String sessionPartSanitizedTitle) {
		SessionPart sessionPart = SessionPart.findBySanitizedTitle(sessionPartSanitizedTitle);
		notFoundIfNull(sessionPart);
		render(sessionPart);
	}
	
	public static void resources(String sanitizedTitle) {
		StudySession studySession = StudySession.findBySanitizedTitle(sanitizedTitle);
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
