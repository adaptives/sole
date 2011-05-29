package controllers;

import java.util.List;

import models.Answer;
import models.BookGroup;
import models.Forum;
import models.Question;
import models.SocialUser;
import play.data.validation.Required;
import play.mvc.Controller;

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
		
		render(bookGroups, page, size, pages);
	}
	
	public static void show(long id) {
		BookGroup bookGroup = BookGroup.findById(id);
		render(bookGroup);
	}
	
	public static void forum(long id) {
		BookGroup bookGroup = BookGroup.findById(id);
		render(bookGroup);
	}
	
	public static void forumQuestion(long bookGroupId, long questionId) {
		BookGroup bookGroup = BookGroup.findById(bookGroupId);
		Question question = Question.findById(questionId);
		render(bookGroup, question);
	}
	
	public static void members(long id) {
		BookGroup bookGroup = BookGroup.findById(id);
		render(bookGroup);
	}
}
