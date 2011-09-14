package controllers;

import java.util.List;

import other.utils.LinkGenUtils;
import play.Logger;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.Scope.Flash;
import play.mvc.With;
import models.Activity;
import models.ActivityResponse;
import models.Answer;
import models.CodeSnippet;
import models.Course;
import models.DIYCourseEvent;
import models.Forum;
import models.Pastebin;
import models.Question;
import models.SiteEvent;
import models.SocialUser;

@With({Secure.class, SocialAuthC.class})
public class CourseSecureC extends Controller {
	
	public static final org.apache.log4j.Logger cLogger = 
									Logger.log4j.getLogger(CourseSecureC.class);
	
	public static void enroll(long courseId) {
		
		Course course = Course.findById(courseId);
		
		if(course != null) {
			String sUserId = Security.connected();
			if(sUserId != null) {
				long userId = Long.parseLong(sUserId);
				SocialUser user = SocialUser.findById(userId);
				if(!course.enrolledParticipants.contains(user)) {
					course.enrolledParticipants.add(user);
					course.save();
					new SiteEvent(LinkGenUtils.getUserProfileLink(user) + 
								  " enrolled in " + 
								  LinkGenUtils.getDIYCourseLink(course));
				}				
			}
			else {
				//control should never come here, because this is a secure action
			}
		} else {
			cLogger.error("Could not find course '" + courseId + "' " +
						  "while trying to enroll a user");
			flash.error(MessageConstants.INTERNAL_ERROR);
		}
		CourseC.course(course.sanitizedTitle);
	}
	
	//TODO: Implement
	public static void markCompleted(long courseId) {
		
	}
	
	public static void postQuestion(long courseId, 
									long forumId,
									@Required String title, 
									@Required String content, 
									String tags) {

		SocialUser user = SocialUser.findById(Long.parseLong(Security
				.connected()));
		Course course = Course.findById(courseId);
		
		Forum forum = Forum.findById(forumId);
		if(user != null && course != null && forum != null) {
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
			saveIfNotNull(DIYCourseEvent.buildFromQuestion(course, user, question));
			CourseC.forum(course.sanitizedTitle);
		} else {
			//TODO: What do we do here?
		}
		
	}
	
	public static void postAnswer(long courseId,
			  					  long forumId,
			  					  long questionId,
			  					  String answerContent) {
		
		Course course = Course.findById(courseId);
		Question question = Question.findById(questionId);
		SocialUser user = SocialUser.findById(Long.parseLong(Security.connected()));
		Answer answer = new Answer(answerContent, user, question);
		question.answers.add(answer);
		question.save();
		saveIfNotNull(DIYCourseEvent.buildFromAnswer(course, user, answer));
		CourseC.forumQuestion(course.sanitizedTitle, questionId);
	}
	
	public static int postActivityResponse(long activityId,
										   String activityResponse, 
										   String title) {
		
		Activity activity = Activity.findById(activityId);
		if (activityResponse != null && !activityResponse.equals("")) {
			String sUserId = Security.connected();
			long userId = Long.parseLong(sUserId);
			SocialUser user = SocialUser.findById(userId);
			if (user != null) {
				ActivityResponse activityResponseObj = new ActivityResponse(
						user, activity, activityResponse, title);
				activityResponseObj.save();
				saveIfNotNull(DIYCourseEvent.buildFromActivity(user, activityResponseObj));
			}
		}

		return activity.activityResponses.size();
	}
	
	public static void pastebin(@Required String sanitizedTitle) {
		String sUserId = Security.connected();
		long userId = Long.parseLong(sUserId);
		SocialUser user = SocialUser.findById(userId);
		
		Course course = Course.findBySanitizedTitle(sanitizedTitle);
		notFoundIfNull(course);
		
		Pastebin pastebin = Pastebin.findByName(course.sanitizedTitle);
		notFoundIfNull(pastebin);
		
		List<CodeSnippet> codeSnippets = pastebin.findSnippetsByUser(user.id);
		render(course, codeSnippets);
	}
	
	public static void postCodeSnippet(@Required long courseId, 
			                           @Required String title,
									   @Required String code) {
		Course course = Course.findById(courseId);
		notFoundIfNull(course);
		
		if(validation.hasErrors()) {
			params.flash();
			validation.keep();
		} else {
			String sUserId = Security.connected();
			long userId = Long.parseLong(sUserId);
			SocialUser user = SocialUser.findById(userId);
			
			Pastebin pastebin = Pastebin.findByName(course.sanitizedTitle);
			notFoundIfNull(pastebin);
			
			CodeSnippet codeSnippet = new CodeSnippet(user, pastebin, title, code);
			codeSnippet.save();
		}
		
		pastebin(course.sanitizedTitle);
	}
	
	public static void codeSnippet(@Required String sanitizedTitle, 
								   @Required long codeSnippetId) {
		
		String sUserId = Security.connected();
		long userId = Long.parseLong(sUserId);
		SocialUser user = SocialUser.findById(userId);
		
		Course course = Course.findBySanitizedTitle(sanitizedTitle);
		notFoundIfNull(course);
		
		CodeSnippet codeSnippet = CodeSnippet.findById(codeSnippetId);
		notFoundIfNull(codeSnippet);
		
		//TODO: This code will have to change when we strongly link pastebins with courses
		//as opposed to linking them via their name
		if(codeSnippet.pastebin.restricted) {
			if(codeSnippet.user.id == user.id || 
					(course.isSocialUserEnrolled(sUserId) && codeSnippet.pastebin.name .equals(sanitizedTitle))) {
				render(course, codeSnippet);
			} else {
				flash.error("You can access a code snippet in this course only if it is your's or if you are enrolled in the course");
				pastebin(course.sanitizedTitle);
			}
		} else {
			render(course, codeSnippet);
		}		
	}
	
	public static void editCodeSnippet(@Required String sanitizedTitle,
								       @Required long codeSnippetId) {

		String sUserId = Security.connected();
		long userId = Long.parseLong(sUserId);
		SocialUser user = SocialUser.findById(userId);

		Course course = Course.findBySanitizedTitle(sanitizedTitle);
		notFoundIfNull(course);

		CodeSnippet codeSnippet = CodeSnippet.findById(codeSnippetId);
		notFoundIfNull(codeSnippet);

		if (codeSnippet.user.id == user.id) {
			render(course, codeSnippet);
		} else {
			flash.error("You can only edit your own code snippets");
			pastebin(sanitizedTitle);
		}
	}
	
	public static void deleteCodeSnippet(@Required String sanitizedTitle,
									     @Required long codeSnippetId) {

		String sUserId = Security.connected();
		long userId = Long.parseLong(sUserId);
		SocialUser user = SocialUser.findById(userId);

		Course course = Course.findBySanitizedTitle(sanitizedTitle);
		notFoundIfNull(course);

		CodeSnippet codeSnippet = CodeSnippet.findById(codeSnippetId);
		notFoundIfNull(codeSnippet);

		if (codeSnippet.user.id == user.id) {
			codeSnippet.delete();			
		} else {
			flash.error("You can only delete your own code snippets");
		}
		
		pastebin(sanitizedTitle);
	}
	
	public static void postEditCodeSnippet(@Required long courseId,
			                               @Required long codeSnippetId,
									       @Required String title, 
									       @Required String code) {

		String sUserId = Security.connected();
		long userId = Long.parseLong(sUserId);
		SocialUser user = SocialUser.findById(userId);

		Course course = Course.findById(courseId);
		notFoundIfNull(course);

		Pastebin pastebin = Pastebin.findByName(course.sanitizedTitle);
		notFoundIfNull(pastebin);

		CodeSnippet codeSnippet = CodeSnippet.findById(codeSnippetId);
		if(codeSnippet.user.id == user.id) {
			if(validation.hasErrors()) {
				params.flash();
				validation.keep();
				editCodeSnippet(course.sanitizedTitle, codeSnippetId);
			} else {
				codeSnippet.title = title;
				codeSnippet.code = code;
				codeSnippet.save();
				codeSnippet(course.sanitizedTitle, codeSnippetId);
			}
		} else {
			flash.error("You do not own the code snippet");
			pastebin(course.sanitizedTitle);
		}
	}

	private static void saveIfNotNull(DIYCourseEvent event) {
		if(event != null) {
			event.save();
		}
	}

}
