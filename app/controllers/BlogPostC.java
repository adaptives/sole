package controllers;

import java.text.SimpleDateFormat;
import java.util.List;

import models.BlogPost;
import models.SocialUser;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;

@With(SocialAuthC.class)
public class BlogPostC extends Controller {
	
	public static void index() {
		list();
	}
	
	public static void list() {
		String query = "select b from BlogPost b order by b.postedAt desc";
		List<BlogPost> blogPosts = BlogPost.find(query).fetch(1, 10);
		render(blogPosts);
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
		list();		
	}
	
}
