package controllers;

import models.Answer;
import models.BookGroup;
import models.Forum;
import models.Question;
import models.SocialUser;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;

@With({Secure.class, SocialAuthC.class})
public class BookGroupSecureC extends Controller {
	
	public static void postQuestion(long bookGroupId, 
									long forumId,
									@Required String title, 
									@Required String content, 
									String tags) {
		flash.keep("url");
		String sUserId = Security.connected();
		SocialUser user = SocialUser.findById(Long.parseLong(sUserId));

		Forum forum = Forum.findById(forumId);
		Question question = new Question(title, content, user);
		if (tags != null) {
			String tagArray[] = tags.split(",");
			if (tagArray != null) {
				for (String tag : tagArray) {
					question.tagWith(tag);
				}
			}
		}

		forum.questions.add(question);
		forum.save();
		
		try {			
	        String url = flash.get("url");
	        if(url == null) {
	            url = "/";
	        }
	        redirect(url);		    
		} catch(Throwable t) {
			render("errors/500.html", t);
		}
	}

	public static void postAnswer(long bookGroupId, 
								  long forumId,
								  long questionId, 
								  String answerContent) {

		Question question = Question.findById(questionId);
		SocialUser user = SocialUser.findById(Long.parseLong(Security
				.connected()));
		Answer answer = new Answer(answerContent, user, question);
		question.answers.add(answer);
		question.save();
		BookGroup bookGroup = BookGroup.findById(bookGroupId);
		BookGroupC.forumQuestion(bookGroup.sanitizedTitle, questionId, question.sanitizedTitle);
	}
	
	public static void createBookGroup() {
		
	}

}
