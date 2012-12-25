package controllers;

import java.text.SimpleDateFormat;
import java.util.List;

import models.BlogPost;
import models.SocialUser;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;
import play.Logger;

@With(SocialAuthC.class)
public class BlogPostC extends Controller {
	
	public static final org.apache.log4j.Logger cLogger = 
										Logger.log4j.getLogger(BlogPostC.class);
	public static final int DEFAULT_PAGE = 1;
	public static final int DEFAULT_PAGE_SIZE = 4;
	
	public static void index() {
		list(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
	}
	
	public static void list(long page, long size) {
		
		if(page < 1) {
			flash.error("Incorrect value for page '" + page + "' . The value must be equal or greater than 1");
			render("emptypage.html");
		}
		if(size < 1) {
			flash.error("Incorrect value for size '" + size + "' . The value must be equal or greater than 1");
			render("emptypage.html");
		}
		// TODO: Query should be moved to the model class BlogPost.java
		String query = "select b from BlogPost b order by b.postedAt desc";
		List<BlogPost> allBlogs = BlogPost.find(query).fetch((int)page, (int)size);
		long count = BlogPost.count();
		int pages = (int)(count/size);
		pages++;		
		render("BlogPostC/list.html", allBlogs, page, size, pages);
	}
	
	public static  void show(String year, 
							 String month, 
							 String day, 
							 String sanitizedTitle) {
		
		BlogPost blogPost = BlogPost.findBySanitizedTitleAndDate(year, month, day, sanitizedTitle);
		notFoundIfNull(blogPost);
		render(blogPost);
	}
	
	public static void form(BlogPost blogPost) {
		if(Security.isConnected() && Security.check("admin")) {
			render(blogPost);
		}
		else {
			renderText("You are not authorized to view this page");
		}
	}
	
	public static void save(@Required String title, 
							@Required String content, 
							String tags) {		
		
		if(!(Security.isConnected() && Security.check("admin"))) {
			renderText("Sorry you do not have authorization to view this page");
		}
		SocialUser author = SocialUser.findById(Long.parseLong(Security.connected()));
		BlogPost blogPost = new BlogPost(author, title, content);
		if(validation.hasErrors()) {
			validation.keep();
			params.flash();
			flash.error("Please correct these errors");
			render("@form", blogPost);
		}
		blogPost.save();
		list(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
	}
	
}