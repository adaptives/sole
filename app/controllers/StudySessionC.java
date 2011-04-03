package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Answer;
import models.Forum;
import models.Question;
import models.SessionPart;
import models.StudySession;
import models.StudySessionMeta;
import models.User;
import play.Logger;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;

public class StudySessionC extends Controller {
	
	public static final org.apache.log4j.Logger cLogger = 
								Logger.log4j.getLogger(StudySessionC.class);
	
	@Before
	public static void setConnectedUser() {
		if(Security.isConnected()) {
			User user = User.findByEmail(Security.connected());
			renderArgs.put("user", user.name);
		}
	}
	
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
		render(studySession);
	}
	
	public static void register(long id) {
		if(Security.isConnected()) {			
			String username = Security.connected();			
			User connectedUser = User.findByEmail(username);
			if(connectedUser != null) {
				StudySession studySession = StudySession.findById(id);
				if(studySession != null) {
					studySession.participants.add(connectedUser);
					studySession.save();
				} else {
					
					flash.error(MessageConstants.INTERNAL_ERROR);
				}
			} else {
				flash.error(MessageConstants.INTERNAL_ERROR);
			}
			//TODO: Could we have just rendered the view and passed id to it?
			studySession(id);
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
			String username = Security.connected();			
			User connectedUser = User.findByEmail(username);
			if(connectedUser != null) {
				StudySession studySession = StudySession.findById(id);
				if(studySession != null) {
					studySession.participants.remove(connectedUser);
					studySession.save();
				} else {
					
					flash.error(MessageConstants.INTERNAL_ERROR);
				}
			} else {
				cLogger.error("could not get user object for username '" + username + "'");				
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
		Forum forum = Forum.findById(forumId);
		List<Forum> allForums = Forum.findAll();
		Question question = new Question(title, 
										 content, 
										 User.findByEmail(Security.connected()));
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
	}
	
	public static void postAnswer(long studySessionId, 
								  long forumId, 
								  long questionId,
								  String answerContent) {
		Question question = Question.findById(questionId);
		User user = User.findByEmail(Security.connected());
		Answer answer = new Answer(answerContent, user, question);
		question.answers.add(answer);
		question.save();
		forumQuestion(studySessionId, questionId);
	}

}
