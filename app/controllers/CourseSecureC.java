package controllers;

import other.utils.LinkGenUtils;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Scope.Flash;
import play.mvc.With;
import models.Course;
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
					new SiteEvent("User " + 
								  LinkGenUtils.getUserProfileLink(user) + 
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
	
	
}
