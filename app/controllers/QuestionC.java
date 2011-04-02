package controllers;

import models.Question;
import models.QuestionLiked;
import models.User;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;


public class QuestionC extends Controller {
	
	private static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(QuestionC.class);
	
	@Before
	public static void setConnectedUser() {
		if(Security.isConnected()) {
			User user = User.findByEmail(Security.connected());
			renderArgs.put("user", user.name);			
		}
	}
	
	public static void like(long questionId) {
		String textToRender = "";
		if(Security.isConnected()) {
			User user = User.findByEmail(Security.connected());
			Question question = Question.findById(questionId);
			if(user != null && question != null) {
				question.like(user);
				textToRender = String.valueOf(question.likes());
			} else {
				String msg = "Could not find ";
				if(user == null) {
					msg += " user '" + user.id + "' ";
				}
				if(question == null) {
					msg += " question '" + questionId + "'";
				}
				cLogger.error(msg);
			}
		}
		renderText(textToRender);
	}
}
