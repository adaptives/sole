package controllers;

import java.util.List;

import other.utils.AuthUtils;
import other.utils.LinkGenUtils;
import play.Logger;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.Scope.Flash;
import play.mvc.With;
import models.Activity;
import models.ActivityResponse;
import models.ActivityResponseLiked;
import models.ActivityResponseReview;
import models.Answer;
import models.CodeSnippet;
import models.Course;
import models.CourseSection;
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
				saveIfNotNull(DIYCourseEvent.buildFromActivityResponse(user, activityResponseObj));
			}
		}

		return activity.activityResponses.size();
	}
	
	public static void postActivityResponseReview(long courseId,
			                                      long sectionId,
												  long activityResponseId, 
												  String review) {
		String sUserId = Security.connected();
		long userId = Long.parseLong(sUserId);
		SocialUser user = SocialUser.findById(userId);
		
		Course course = Course.findById(courseId);
		notFoundIfNull(course);
		
		CourseSection section = CourseSection.findById(sectionId);
		notFoundIfNull(sectionId);
		
		ActivityResponse activityResponse = ActivityResponse.findById(activityResponseId);
		notFoundIfNull(activityResponse);
		
		//TODO: Ensure that this activity belongs to this course
		
		ActivityResponseReview activityResponseReview = 
			new ActivityResponseReview(activityResponse, user, review);
		
		saveIfNotNull(DIYCourseEvent.buildFromActivityResponseReview(user, activityResponseReview));
		
		CourseC.sectionActivityResponseReview(course.sanitizedTitle, 
											  section.sanitizedTitle, 
											  activityResponseId);
	}
	
	public static void voteActivityResponse(String courseSanitizedTitle, 
										    String sectionSanitizedTitle, 
										    long activityResponseId, 
										    Boolean ajax) {
		
		SocialUser user = AuthUtils.getSocialUser();
		
		ActivityResponse activityResponse = 
								ActivityResponse.findById(activityResponseId);
		
		Course course = null;
		CourseSection section = null;
		
		if(ajax) {
			String responseMsg = "-1";
			//We do not want to make any further database calls if this upvote is not legal
			if(activityResponse != null && activityResponse.user.id != user.id && !activityResponse.hasLiked(user)) {
				course = Course.findBySanitizedTitle(courseSanitizedTitle);						
				if(course != null) {
					section = CourseSection.
									findBySanitizedTitleByCouse(course, 
					        									sectionSanitizedTitle);
					if(section != null) {
						ActivityResponseLiked activityResponseLiked = 
								new ActivityResponseLiked(activityResponse, user);								
						responseMsg = String.valueOf(activityResponse.likes());
					}
				}
			}				
			renderText(responseMsg);
		} else {
			notFoundIfNull(activityResponse);
			course = Course.findBySanitizedTitle(courseSanitizedTitle);
			notFoundIfNull(course);				
			section = CourseSection.findBySanitizedTitleByCouse(course, 
				        										sectionSanitizedTitle);
			notFoundIfNull(section);
			if(activityResponse.user.id != user.id && !activityResponse.hasLiked(user)) {				
				ActivityResponseLiked activityResponseLiked = 
					new ActivityResponseLiked(activityResponse, user);
			}
			CourseC.sectionActivityResponseReview(course.sanitizedTitle, 
					  							  section.sanitizedTitle, 
					  							  activityResponseId);
		}
	}
	
	public static void deleteActivityResponse(long activityResponseId,
											  String courseSanitizedTitle,
											  String sectionSanitizedTitle) {
		ActivityResponse deletedAr = null;
		ActivityResponse activityResponse = ActivityResponse.findById(activityResponseId);
		notFoundIfNull(activityResponse);
		SocialUser user = AuthUtils.getSocialUser();
		if(activityResponse.user.id == user.id) {
			//TODO: Try firing just one query to delete 
			List<ActivityResponseLiked> arls = ActivityResponseLiked.findByActivityResponse(activityResponseId);
			for(ActivityResponseLiked arl : arls) {
				arl.delete();
			}
			deletedAr = activityResponse.delete();
			
			if(deletedAr != null) {
				cLogger.info("Deleted Activity Response '" + activityResponseId + "'");
				String msg = "Activity response successfully deleted";
				flash.success(msg);
				//TODO: We are assuming that we will only get a delete request from a section activity response... what if we got one from a course activity response ?
				CourseC.sectionActivityResponses(courseSanitizedTitle, sectionSanitizedTitle);
			} else {			
				cLogger.warn("Could not delete activity response '" + activityResponseId + "'");
				String msg = "Activity response could not be deleted";
				flash.error(msg);
				CourseC.sectionActivityResponseReview(courseSanitizedTitle, sectionSanitizedTitle, activityResponseId);
			}
		} else {
			cLogger.warn("User " + user.id + " attempted to delete activity response '" + activityResponseId + "' which they do not own");
			String msg = "You cannot delete someone else's activity response";
			flash.error(msg);
			CourseC.sectionActivityResponseReview(courseSanitizedTitle, sectionSanitizedTitle, activityResponseId);
		}
				
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
		render(course, pastebin, codeSnippets);
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
