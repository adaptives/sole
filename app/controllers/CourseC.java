package controllers;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Answer;
import models.Comment;
import models.Course;
import models.CourseSection;
import models.Forum;
import models.Question;
import models.SocialUser;
import models.User;
import models.UserProfile;
import play.Logger;
import play.Play;
import play.data.validation.Email;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import play.mvc.Scope.Session;

@With(SocialAuthC.class)
public class CourseC extends Controller {
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(CourseC.class);
	
	public static void list() {
		List<Course> courses = Course.findAll();
		render(courses);
	}
	
	public static void course(String sanitizedTitle) {
		Course course = Course.findBySanitizedTitle(sanitizedTitle);
		notFoundIfNull(course);		
		List<CourseSection> courseSections = course.fetchSectionsByPlacement();
		render(course, courseSections);		
	}
	
	public static void pic(long courseId) {
		Course course = Course.find("byId", courseId).first();
		if(course.coursePic != null) {
			renderBinary(course.coursePic.image.get());
		} else {
			try {
				InputStream is = new FileInputStream(Play.getFile("public/images/default_course_image.gif"));
				renderBinary(is);
			} catch(Exception e) {
				cLogger.error("Could not render default user image ", e);
			}
		}		
	}
	
	public static void section(String sanitizedTitle, 
							   String courseSectionSanitizedTitle) {
		List<String> tabIds = new ArrayList<String>();
		tabIds.add("questions");
		tabIds.add("selected-question");
		tabIds.add("comments");
		List<String> tabNames = new ArrayList<String>();
		tabNames.add("Questions");
		tabNames.add("Selected Question");
		tabNames.add("Comments");
		
		Course course = Course.findBySanitizedTitle(sanitizedTitle);
		CourseSection courseSection = CourseSection.findBySanitizedTitleByCouse(course, courseSectionSanitizedTitle);
		
		if(courseSection != null) {
			render("CourseC/section.html", courseSection, tabIds, tabNames);
		} else {
			//TODO: We should actually render the course with this error message
			flash.error("Sorry we could not find the section");
			render("emptypage.html");
		}
	}
	
	public static void question(long sectionId, long questionId) {
		List<String> tabIds = new ArrayList<String>();
		tabIds.add("questions");
		tabIds.add("selected-question");
		tabIds.add("comments");
		List<String> tabNames = new ArrayList<String>();
		tabNames.add("Questions");
		tabNames.add("Selected Question");
		tabNames.add("Comments");
		
		CourseSection courseSection = CourseSection.findById(sectionId);
		if(courseSection != null) {
			Question question = Question.findById(questionId);
			render("CourseC/section.html", courseSection, question, tabIds, tabNames);
		} else {
			//TODO: We should actually render the course with this error message
			flash.error("Sorry we could not find the section");
			render("emptypage.html");
		}
	}
	
//	public static void addQuestion(long sectionId, 
//								   @Required String title,
//								   @Required String content,
//								   String tags) {
//		if(validation.hasErrors()) {
//			validation.keep();
//			params.flash();
//			flash.error("Please correct these errors");
//			section(sectionId);
//		}
//		SocialUser user = SocialUser.findById(Long.parseLong(Security.connected()));
//		CourseSection courseSection = CourseSection.findById(sectionId);		
//		Question question = new Question(title, content, user);
//		if(tags != null) {
//			String tagArray[] = tags.split(",");
//			if(tagArray != null) {
//				for(String tag : tagArray) {
//					question.tagWith(tag);
//				}
//			}			
//		}
//		courseSection.questions.add(question);
//		courseSection.save();
//		section(courseSection.id);
//	}
	
	public static void postAnswer(long sectionId,
								  long questionId,
								  String answerContent) {
		Question question = Question.findById(questionId);
		SocialUser user = SocialUser.findById(Long.parseLong(Security.connected()));
		Answer answer = new Answer(answerContent, user, question);
		question.answers.add(answer);
		question.save();
		question(sectionId, questionId);
	}
	
	public static void comment(long courseId,
							   long courseSectionId,
							   @Required String name, 
			  				   @Required @Email String email,
			  				   String website,
			  				   @Required String message) {
		CourseSection courseSection = CourseSection.findById(courseSectionId);
		Comment comment = new Comment(message, name, email, website);
		courseSection.comments.add(comment);
		courseSection.save();
		section(courseSection.course.sanitizedTitle, courseSection.sanitizedTitle);
	}
	
	public static void forum(String sanitizedTitle) {
		Course course = Course.findBySanitizedTitle(sanitizedTitle);
		notFoundIfNull(course);
		render(course);
	}
	
	public static void forumQuestion(String sanitizedTitle, long questionId) {
		Course course = Course.findBySanitizedTitle(sanitizedTitle);
		Question question = Question.findById(questionId);
		render(course, question);
	}
	
	public static void sectionActivityResponses(String courseSanitizedTitle, String courseSectionSanitizedTitle) {
		Course course = Course.findBySanitizedTitle(courseSanitizedTitle);
		notFoundIfNull(course);
		
		CourseSection courseSection = CourseSection.findBySanitizedTitleByCouse(course, courseSectionSanitizedTitle);
		notFoundIfNull(courseSection);
		
		render(course, courseSection);
	}
	
	public static void activityResponses(String courseSanitizedTitle) {
		Course course = Course.findBySanitizedTitle(courseSanitizedTitle);
		notFoundIfNull(course);
		
		render(course);
	}
	
	public static void participants(String sanitizedTitle, long page, long size) {
		//sensible defaults
		if(size < 1) {
			size = 24;
		}
		if(page < 1) {
			page = 1;
		}
		Course course = Course.findBySanitizedTitle(sanitizedTitle);
		notFoundIfNull(course);
		String query = "select p from Course c join c.enrolledParticipants p where c.id = ?";
		List<SocialUser> participants = SocialUser.find(query, course.id).fetch((int)page, (int)size);
		long count = course.getEnrolledUsersCount();
		int pages = (int)(count/size);
		if(count % size > 0) {
			pages++;
		}
		
		render(course, participants, page, size, pages);
	}	

}
