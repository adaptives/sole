package controllers;

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
import models.StudySessionMeta;
import models.User;
import play.Logger;
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
		StudySessionMeta studySessionMeta = StudySessionMeta.forStudySession(studySession.id);
		render(studySession, studySessionMeta);
	}
	
	/**
	 * Checks if the user is eligible to apply for the course and redirects
	 * the user to the application page
	 * @param id
	 */
	public static void apply(long id) {
		
		if(Security.isConnected()) {			
			String userId = Security.connected();			
			SocialUser connectedUser = SocialUser.findById(Long.parseLong(userId));
			if(connectedUser != null) {
				StudySession studySession = StudySession.findById(id);
				if(studySession != null && 
				   !studySession.applicationStore.isUserApplicationAccepted(connectedUser.id) &&
				   !studySession.facilitators.contains(connectedUser)) {					
					render(studySession);
				} else {					
					flash.error(MessageConstants.INTERNAL_ERROR);
				}
			} else {
				flash.error(MessageConstants.INTERNAL_ERROR);
			}
			//TODO: Could we have just rendered the view and passed id to it?
			//studySession(id);
			//We are redirecting to the original URL... TODO: Make this a utility method
			String url = flash.get("url");
	        if(url == null) {
	            url = "/";
	        }
	        redirect(url);
		} else {
			flash.put("url", request.method == "GET" ? request.url : "/");
			try {
				Secure.login();
			} catch(Throwable t) {
				flash.error(MessageConstants.INTERNAL_ERROR);
				cLogger.error("Could not redirect user to the login " +
							  "page before registering for a studySession", t);
				studySession(id);
			}
		}
	}
	
	public static void application(long id, String application) {
		if(Security.isConnected()) {			
			String userId = Security.connected();			
			SocialUser connectedUser = SocialUser.findById(Long.parseLong(userId));
			if(connectedUser != null) {
				StudySession studySession = StudySession.findById(id);
				if(studySession != null && 
				   !studySession.applicationStore.isUserApplicationAccepted(connectedUser.id) &&
				   !studySession.facilitators.contains(connectedUser)) {

					studySession.addApplication(connectedUser, application);
					studySession.save();
				} else {					
					flash.error(MessageConstants.INTERNAL_ERROR);
				}
			} else {
				flash.error(MessageConstants.INTERNAL_ERROR);
			}
			//TODO: Could we have just rendered the view and passed id to it?
			//studySession(id);
			//We are redirecting to the original URL... TODO: Make this a utility method
			String url = flash.get("url");
	        if(url == null) {
	            studySession(id);
	        }
	        redirect(url);
		} else {
			flash.put("url", request.method == "GET" ? request.url : "/");
			try {
				Secure.login();
			} catch(Throwable t) {
				flash.error(MessageConstants.INTERNAL_ERROR);
				cLogger.error("Could not redirect user to the login " +
							  "page before registering for a studySession", t);
				studySession(id);
			}
		}
	}
	
	public static void deregister(long id) {
		long studySessionId = id;
		render(studySessionId);
	}
	
	//TODO: This url should be called only via a POST request
	public static void deregistration(long id, String cause) {
		if(Security.isConnected()) {						
			String userId = Security.connected();			
			SocialUser connectedUser = SocialUser.findById(Long.parseLong(userId));
			if(connectedUser != null) {
				StudySession studySession = StudySession.findById(id);
				if(studySession != null) {
					studySession.deregister(connectedUser.id, cause);
					//TODO: We should need only one, which one?
					studySession.applicationStore.save();
					studySession.save();
				} else {
					cLogger.warn("StudySession is null '" + id + "'");
					flash.error(MessageConstants.INTERNAL_ERROR);
				}
			} else {
				cLogger.error("could not get user object for username '" + userId + "'");				
				flash.error(MessageConstants.INTERNAL_ERROR);
			}
			//TODO: Could we have just rendered the view and passed id to it?
			studySession(id);
		} else {
			cLogger.error("A user is not logged in. This should never happen for this action");
			flash.error(MessageConstants.INTERNAL_ERROR);
		}
	}
	
	public static void sessionPart(long studySessionId, long id) {
		SessionPart sessionPart = SessionPart.findById(id);
		render(studySessionId, sessionPart);
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
		Question question = Question.findById(questionId);
		
		render("StudySessionC/forum.html", 
			   studySession, 
			   question, 
			   tabIds, 
			   tabNames);
	}
	
	public static void sessionPartActivityResponses(long sessionPartId) {
		SessionPart sessionPart = SessionPart.findById(sessionPartId);
		render(sessionPart);
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
