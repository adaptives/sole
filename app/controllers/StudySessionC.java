package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Answer;
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
		render(studySessions);
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
	
	public static void deregister(long id) {
		if(Security.isConnected()) {			
			String userId = Security.connected();			
			SocialUser connectedUser = SocialUser.findById(Long.parseLong(userId));
			if(connectedUser != null) {
				StudySession studySession = StudySession.findById(id);
				if(studySession != null) {
					//studySession.applicationStore.deregister(connectedUser.id);
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
	
	public static void sessionPart(long id) {
		SessionPart sessionPart = SessionPart.findById(id);
		render(sessionPart);
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
	
	public static void postQuestion(long studySessionId,
									long forumId,
									@Required String title,
									@Required String content,
									String tags) {
		
		SocialUser user = SocialUser.findById(Long.parseLong(Security.connected()));
		StudySession studySession = StudySession.findById(studySessionId);
		if(studySession != null && 
		   (studySession.applicationStore.isUserApplicationAccepted(user.id) || studySession.facilitators.contains(user))) {
		
			Forum forum = Forum.findById(forumId);
			Question question = new Question(title, 
											 content, 
											 user);
			if(tags != null) {
				String tagArray[] = tags.split(",");
				if(tagArray != null) {
					for(String tag : tagArray) {
						question.tagWith(tag);
					}
				}
			}
			
			forum.questions.add(question);
			forum.save();
			forum(studySessionId);
		} else {
			cLogger.info("user '" + user.id + "' must be enrolled in StudySession '" + 
						 studySessionId + "' before posting questions");
		}
		
	}
	
	//TODO: Annotate this method to ensure that a user is logged in
	public static void postAnswer(long studySessionId, 
								  long forumId, 
								  long questionId,
								  String answerContent) {
		Question question = Question.findById(questionId);
		SocialUser user = SocialUser.findById(Long.parseLong(Security.connected()));
		if(user != null) {
			Answer answer = new Answer(answerContent, user, question);
			question.answers.add(answer);
			question.save();
			forumQuestion(studySessionId, questionId);
		} else {
			cLogger.info("user '" + user.id + "' must be signed in before " +
						 "posting answers");
		}
	}

}
