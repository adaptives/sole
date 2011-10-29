package other.utils;

import controllers.CourseSecureC;
import models.ActivityResponseReview;
import models.Course;
import models.CourseSection;
import models.DIYCourseEvent;
import models.MessageCenter;
import models.PrivateMessage;
import models.Question;
import models.SocialUser;
import play.Logger;
import play.Play;

public class MessagingUtils {
	
	public static final org.apache.log4j.Logger cLogger = 
									Logger.log4j.getLogger(MessagingUtils.class);
	
	public static void generateMessageForQuestionAnswered(Question question,
														   Course course) {		
		
		SocialUser from = getMessageFrom();
		SocialUser to = question.author;
		String title = "There is a new answer to your question '"
				+ question.title + "'";
		String content = "Your question '" + question.title
				+ "' has a new answer "
				+ DIYCourseEvent.getQuestionURL(course, question);
		// PrivateMessage.send(from, to, title, content);
		PrivateMessage message = new PrivateMessage(from, to, title, content);
		createMessage(message);
	}
	
	public static void generateActivityResponseReviewMessage(Course course,
															 CourseSection section,
															 ActivityResponseReview activityResponseReview) {
		SocialUser from = getMessageFrom();
		SocialUser to = activityResponseReview.activityResponse.user;
		String title = "There is a new review for your activity response '"
				+ activityResponseReview.activityResponse.title + "'";
		String content = "There is a new review for your activity response '" + 
						 activityResponseReview.activityResponse.title + 
						 "'. Click on this link to view the review - " + 
						 DIYCourseEvent.getActivityReviewURL(course, section, activityResponseReview); 
				
		// PrivateMessage.send(from, to, title, content);
		PrivateMessage message = new PrivateMessage(from, to, title, content);
		createMessage(message);
	}
	
	private static SocialUser getMessageFrom() {
		String messagingFrom = Play.configuration.getProperty("messaging.from");
		long lMessagingFrom = (long) 1;
		try {
			lMessagingFrom = Long.valueOf(messagingFrom);
		} catch (Exception e) {
			cLogger.warn("Could not find value for messaging.from in the configuration file. Using the default value of 17");
		}
		SocialUser from = SocialUser.findById(lMessagingFrom);
		return from;
	}
	
	private static void createMessage(PrivateMessage message) {
		message.save();
		MessageCenter messageCenter = MessageCenter.findByUserId(message.to.id);
		messageCenter.inbox.add(message);
		messageCenter.save();
	}
}
