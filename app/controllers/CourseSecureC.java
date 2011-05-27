package controllers;

import other.utils.LinkGenUtils;
import play.Logger;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.Scope.Flash;
import play.mvc.With;
import models.Answer;
import models.Course;
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
		CourseC.course(courseId);
	}
	
	//TODO: Implement
	public static void markCompleted(long courseId) {
		
	}
	
	public static void postQuestion(long courseId, long forumId,
			@Required String title, @Required String content, String tags) {

		SocialUser user = SocialUser.findById(Long.parseLong(Security
				.connected()));

		Forum forum = Forum.findById(forumId);
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
		CourseC.forum(courseId);
	}
	
	public static void postAnswer(long courseId,
			  					  long forumId,
			  					  long questionId,
			  					  String answerContent) {
		
		Question question = Question.findById(questionId);
		SocialUser user = SocialUser.findById(Long.parseLong(Security.connected()));
		Answer answer = new Answer(answerContent, user, question);
		question.answers.add(answer);
		question.save();
		CourseC.forumQuestion(courseId, questionId);
	}
	
}
