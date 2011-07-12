package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Answer;
import models.BookGroup;
import models.Forum;
import models.Question;
import models.SocialUser;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;
import play.mvc.Router.ActionDefinition;

@With(SocialAuthC.class)
public class BookGroupC extends Controller {
	
	public static void list(long page, long size) {
		List<BookGroup> bookGroups = 
			BookGroup.find("select bg from BookGroup bg").fetch((int)page, (int)size);
		
		long count = BookGroup.count();
		int pages = (int)(count/size);
		if(count % size > 0) {
			pages++;
		}
		
		if(pages == 0) {
			pages++;
		}
		
		render(bookGroups, page, size, pages);
	}
	
	public static void show(String sanitizedTitle) {		
		BookGroup bookGroup = BookGroup.findBySanitizedTitle(sanitizedTitle);		
		render(bookGroup);
	}
	
	public static void forum(String sanitizedTitle) {
		String originalDestination = request.method == "GET" ? request.url : "/bookgroups"; 
		flash.put("url", originalDestination);
		BookGroup bookGroup = BookGroup.findBySanitizedTitle(sanitizedTitle);
		render(bookGroup);
	}
	
	public static void forumQuestion(String sanitizedTitle, 
									 long questionId, 
									 String sanitizedQuestionTitle) {
		
		BookGroup bookGroup = BookGroup.findBySanitizedTitle(sanitizedTitle);
		Question question = Question.findById(questionId);
		
		String originalDestination = flash.get("url");
		if(originalDestination == null || originalDestination.equals("")) {
			Map<String, Object> methodArgs = new HashMap<String, Object>();
			methodArgs.put("sanitizedTitle", sanitizedTitle);
			ActionDefinition actionDefinition = 
				play.mvc.Router.reverse("BookGroupC.show", methodArgs);
			originalDestination = actionDefinition.url;
		}
		render(bookGroup, question, originalDestination);
	}
	
	public static void members(long id) {
		BookGroup bookGroup = BookGroup.findById(id);
		render(bookGroup);
	}
}
