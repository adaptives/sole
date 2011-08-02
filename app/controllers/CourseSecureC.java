package controllers;

import other.utils.LinkGenUtils;
import play.Logger;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.Scope.Flash;
import play.mvc.With;
import models.Activity;
import models.ActivityResponse;
import models.Answer;
import models.Course;
import models.DIYCourseEvent;
import models.Forum;
import models.Question;
import models.SiteEvent;
import models.SocialUser;

//TODO: Add SocialAuthC.class to @With
@With(Secure.class)
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
								  " enrolled in DIY Course " + 
								  LinkGenUtils.getDIYCourseLink(course));
				}				
			}
			else {
				
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

	private static void saveIfNotNull(DIYCourseEvent event) {
		if(event != null) {
			event.save();
		}
	}

}
