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
import play.mvc.Router.ActionDefinition;

public class BookGroupC extends Controller {
	
	public static void list(long page, long size) {
		List<BookGroup> bookGroups = 
			BookGroup.find("select bg from BookGroup bg").
				fetch((int)page, (int)size);
		
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
	
	public static void show(String sanitizedTitle, long page, long size) {
		flash.put("url", request.method == "GET" ? request.url : "/blog");
		if(!(page > 0) || !(size > 0)) {
			show(sanitizedTitle, 1, 25);
		}
		
		BookGroup bookGroup = BookGroup.findBySanitizedTitle(sanitizedTitle);		
		render(bookGroup, page, size);
	}
	
	public static void forumQuestion(String sanitizedTitle, long questionId) {
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
