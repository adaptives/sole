package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Answer;
import models.Forum;
import models.Question;
import models.SessionPart;
import models.StudySession;
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
	
	public static void currentStudySessions() {
		List<StudySession> studySessions = StudySession.findAll();
		render(studySessions);
	}
	
	public static void studySession(long id) {
		StudySession studySession = StudySession.findById(id); 
		render(studySession);
	}
	
	public static void registerInStudySession(long id) {
		if(Security.isConnected()) {			
			String username = Security.connected();			
			User connectedUser = User.findByEmail(username);
			if(connectedUser != null) {
				System.out.println("user is connected " + username);
				StudySession studySession = StudySession.findById(id);
				if(studySession != null) {
					System.out.println("adding participant to studySession");
					studySession.participants.add(connectedUser);
					studySession.save();
				} else {
					
					flash.error(MessageConstants.INTERNAL_ERROR);
				}
			} else {
				System.out.println("could not get user object for username '" + username + "'");
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
	
	public static void deregisterFromStudySession(long id) {
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
	
	public static void sessionPart(long sessionPartId) {
		SessionPart sessionPart = SessionPart.findById(sessionPartId);
		render(sessionPart);
	}
	
	public static void studySessionForum(long studySessionId) {
		studySessionForumQuestion(studySessionId, -1);
	}
	
	public static void studySessionForumQuestion(long studySessionId, 
												 long questionId) {
		List<String> tabIds = new ArrayList<String>();
		tabIds.add("questions");
		tabIds.add("selected-question");
		List<String> tabNames = new ArrayList<String>();
		tabNames.add("Questions");
		tabNames.add("Selected Question");
		
		StudySession studySession = StudySession.findById(studySessionId);
		Question question = Question.findById(questionId);
		
		render("StudySessionC/studySessionForum.html", 
			   studySession, 
			   question, 
			   tabIds, 
			   tabNames);
	}
	
	public static void studySessionQuestion(long studySessionId,
										    long forumId,
											@Required String title,
											@Required String content,
											String tags) {
		Forum forum = Forum.findById(forumId);
		List<Forum> allForums = Forum.findAll();
		Question question = new Question(title, 
										 content, 
										 User.findByEmail(Security.connected()));
		forum.questions.add(question);
		forum.save();
		studySessionForum(studySessionId);
	}
	
	public static void studySessionAnswer(long studySessionId, 
										  long studySessionForumId, 
										  long questionId,
										  String answerContent) {
		Question question = Question.findById(questionId);
		Answer answer = new Answer(answerContent, question);
		question.answers.add(answer);
		question.save();
		studySessionForumQuestion(studySessionId, questionId);
	}

}
