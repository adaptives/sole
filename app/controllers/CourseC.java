package controllers;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import other.json.JsonSocialUser;

import models.ActivityResponse;
import models.Answer;
import models.CodeSnippet;
import models.Comment;
import models.Course;
import models.CourseGroup;
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
		List<CourseGroup> courseGroups = CourseGroup.findGroupsForCourse(course.id);
		render(course, courseSections, courseGroups);		
	}
	
	public static void pic(long courseId) {
		flash.keep();
		Course course = Course.find("byId", courseId).first();
		InputStream is = null;
		if(course != null && course.coursePic != null) {
			is = course.coursePic.image.get();
			if(is != null) {
				renderBinary(course.coursePic.image.get());
			}			
		} 
		if(is == null) {
			try {
				is = new FileInputStream(Play.getFile("public/images/default_course_image.gif"));
				renderBinary(is);
			} catch(Exception e) {
				cLogger.error("Could not render default user image ", e);
			}
		}		
	}
	
	public static void section(String sanitizedTitle, 
							   String courseSectionSanitizedTitle) {
		
		Course course = Course.findBySanitizedTitle(sanitizedTitle);
		CourseSection courseSection = CourseSection.findBySanitizedTitleByCouse(course, courseSectionSanitizedTitle);
		
		if(courseSection != null) {
			render("CourseC/section.html", courseSection);
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
	
	public static void sectionActivityResponseReview(String courseSanitizedTitle,
													 String sectionSanitizedTitle,
											         long activityResponseId) {
		Course course = Course.findBySanitizedTitle(courseSanitizedTitle);
		notFoundIfNull(course);
		
		CourseSection section = CourseSection.findBySanitizedTitleByCouse(course, sectionSanitizedTitle);
		notFoundIfNull(section);
		
		ActivityResponse activityResponse = ActivityResponse.findById(activityResponseId);
		notFoundIfNull(activityResponse);
		
		//TODO: Ensure that this activity response actually belongs to an activity from this course
		render(course, section, activityResponse);
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
		pages++;
		
		render(course, participants, page, size, pages);		
	}
	
	public static void participantsJson(long id) {
		Course course = Course.findById(id);
		String query = "select p from Course c join c.enrolledParticipants p where c.id = ?";
		List<SocialUser> participants = SocialUser.find(query, course.id).fetch();
		List<JsonSocialUser> jsonParticipants = new ArrayList<JsonSocialUser>();
		for(SocialUser socialUser : participants) {
			jsonParticipants.add(new JsonSocialUser(socialUser));
		}
		renderJSON(jsonParticipants);		
	}
	
	public static void embedCodeSnippet(@Required String sanitizedTitle, 
			   					   		@Required long codeSnippetId) {
		Course course = Course.findBySanitizedTitle(sanitizedTitle);
		notFoundIfNull(course);
		
		CodeSnippet codeSnippet = CodeSnippet.findById(codeSnippetId);
		if(codeSnippet == null) {
			String message = "404 Not Found";
			render("CourseC/noEmbedCodeSnippet.html", course, message);
		}
		
		if(codeSnippet.pastebin.restricted) {
			if(Security.isConnected()) {				
				if(course != null && course.isSocialUserEnrolled(Security.connected())) {
					render(course, codeSnippet);
				} else {
					render("CourseC/noEmbedCodeSnippet.html", course);
				}
			} else {
				render("CourseC/noEmbedCodeSnippet.html", course);
			}
		} else {
			render(course, codeSnippet);
		}
		
	}

}
