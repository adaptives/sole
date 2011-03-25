package controllers;

import static controllers.Secure.login;
import play.*;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.mvc.*;

import java.util.*;

import notifiers.Notifier;


import models.*;

public class Application extends Controller {

	public static final org.apache.log4j.Logger cLogger = 
									Logger.log4j.getLogger(Application.class);
	
	@Before
	public static void setConnectedUser() {
		if(Security.isConnected()) {
			User user = User.findByEmail(Security.connected());
			renderArgs.put("user", user.name);
		}
	}
	
	//------------------ HomePage
	public static void index() {
		Page page = Page.find("byName", "home").first();
		render(page);
    }
	
	//------------------- SignUp and Register
	public static void signup() {
        render();
    }
	
	public static void register(@Required @Email String email, 
								@Required @MinSize(5) String password, 
								@Equals("password") String password2, 
								@Required String name) {
        if (validation.hasErrors()) {
            validation.keep();
            params.flash();
            flash.error("Please correct these errors !");
            signup();
        }
        User user = new User(email, password, name);
        try {
            if (Notifier.welcome(user)) {
                flash.success("Your account is created. Please check your emails ...");                
            } else {
            	flash.error("Oops ... (the email cannot be sent)");
            }
            Secure.login();
        } catch (Throwable e) {
            Logger.error(e, "Mail error");
            flash.error(MessageConstants.INTERNAL_ERROR);
        }
        
    }

	//-------------------------- Feedback
	public static void feedback() {
		List<Feedback> feedbacks = Feedback.findAll();
		render(feedbacks);
	}
	
	public static void createFeedback(@Required String name, 
									  @Required @Email String email, 
									  @Required String message) {
		if(validation.hasErrors()) {
			validation.keep();
			params.flash();
			flash.error("Please correct these errors");
			feedback();
		}
		Feedback feedback = new Feedback(name, email, message);
		feedback.save();
		feedback();
	}
	
	//-------------------------Study groups
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
//		StudySession studySession = StudySession.findById(studySessionId);
//		render(studySession);
		studySessionForumQuestion(studySessionId, -1);
	}
	
	public static void studySessionForumQuestion(long studySessionId, 
												 long questionId) {
		StudySession studySession = StudySession.findById(studySessionId);
		Question question = Question.findById(questionId);
		
		if(question != null) {
			System.out.println("Getting answers for question " + question);
			System.out.println(question.answers);
		}
		
		render("Application/studySessionForum.html", studySession, question);
	}
	
	public static void studySessionQuestion(long studySessionId,
										    long forumId,
											@Required String title,
											@Required String content,
											String tags) {
		Forum forum = Forum.findById(forumId);
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
	
	//--------------------------Static pages
	public static void page(String name) {
		Page page = Page.find("byName", name).first();
		render(page);
	}
}