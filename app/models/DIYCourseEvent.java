package models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MethodNotSupportedException;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import controllers.Activitys;
import controllers.CourseC;

import play.Logger;
import play.db.jpa.Model;
import play.mvc.Router;
import play.mvc.Router.ActionDefinition;

@Entity
public class DIYCourseEvent extends Model {
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(DIYCourseEvent.class);
	
	@ManyToOne
	public Course course;
	
	@ManyToOne
	public SocialUser originator;
	
	public String title;
	
	public String text;
	
	public Date timestamp;
	
	public DIYCourseEvent(Course course, 
						  SocialUser originator,
						  String title,
						  String text) {
		this.course = course;
		this.originator = originator;
		this.title = title;
		this.text = text;
		this.timestamp = new Date();
	}
	
	public static DIYCourseEvent buildFromQuestion(Course course,
						  						   SocialUser originator,
						  						   Question question) {
		
		String title = "New question '" + question.title + "'";
		String text = "asked a new question '" + getQuestionURL(course, question) + "'";
		
		return new DIYCourseEvent(course, originator, title, text);
	}
	
	public static DIYCourseEvent buildFromAnswer(Course course,
			  			  						 SocialUser originator,
			  			  						 Answer answer) {
		
		String title = "New answer for question '" + answer.question.title + "'";
		String text = "provided an answer for question '" + getQuestionURL(course, answer.question) + "'";
		
		return new DIYCourseEvent(course, originator, title, text);
	}
	
	public static DIYCourseEvent buildFromActivityResponse(SocialUser originator,
			  			  						   ActivityResponse activityResponse) {
		
		DIYCourseEvent event = null;
		Course course = null;
		String title = "New activity response for activity '" + activityResponse.activity.title + "'";
		String text = "";
		
		CourseSection courseSectionForActivity = CourseSection.findCourseSectionForActivity(activityResponse.activity);
		if(courseSectionForActivity != null) {
			course = courseSectionForActivity.course;
			text = "submitted an activity response for activity '" + 
					getViewAllActivitiesURL(course, courseSectionForActivity, activityResponse) + "'";
		}
		if(course != null) {
			event = new DIYCourseEvent(course, originator, title, text); 
		}
		return event;
	}
	
	public static DIYCourseEvent buildFromActivityResponseReview(SocialUser originator, 
																 ActivityResponseReview review) {

		DIYCourseEvent event = null;
		Course course = null;
		String title = "New review for activity '" + review.activityResponse.activity.title + "'";
		String text = "";

		CourseSection courseSectionForActivity = 
			CourseSection.findCourseSectionForActivity(review.activityResponse.activity);
		if (courseSectionForActivity != null) {
			course = courseSectionForActivity.course;
			text = "reviewed a response submitted for activity '"
					+ getActivityReviewURL(course, courseSectionForActivity, review) + "'";
		}
		if (course != null) {
			event = new DIYCourseEvent(course, originator, title, text);
		}
		return event;
	}
	
	public static List<DIYCourseEvent> tailByCourse(Course course, int page, int pageSize) {
		String query = "select diyce from DIYCourseEvent diyce where diyce.course.id = ? order by diyce.timestamp desc";		
		return DIYCourseEvent.find(query, course.id).fetch(page, pageSize);
	}

	public String render() {
		StringBuffer buff = new StringBuffer();
		buff.append(getUserImagePlaceholder());
		buff.append(getUserProfileLink(this.originator));
		buff.append(" " + text);
		return buff.toString();
	}
	
	public String renderToFeed() {
		StringBuffer buff = new StringBuffer();
		buff.append(getUserProfileLink(this.originator));
		buff.append(" " + text);
		return buff.toString();
	}
	
	private Object getUserImagePlaceholder() {
		String placeholder = "<span id=\"%s\" class=\"user-image-small\"></span>";
		return String.format(placeholder, this.originator.id);
	}

	public String toString() {
		return this.title;
	}
	
	private static String getUserProfileLink(SocialUser originator) {
		Map showUserActionArgs = new HashMap();
		showUserActionArgs.put("userId", originator.id);
		ActionDefinition showUserActionDef = 
						Router.reverse("UserProfileC.show", showUserActionArgs);
		String template = "<a href=\"%s\">%s</a>";
		return String.format(template, showUserActionDef.url, originator.screenname);
	}
	
	//TODO: Move this method to a utility class
	public static String getQuestionURL(Course course, Question question) {
		Map showQuestionActionArgs = new HashMap();
		showQuestionActionArgs.put("sanitizedTitle", course.sanitizedTitle);
		showQuestionActionArgs.put("questionId", question.id);
		ActionDefinition showQuestionActionDef = 
						Router.reverse("CourseC.forumQuestion", showQuestionActionArgs);
		String template = "<a href=\"%s\">%s</a>";
		return String.format(template, showQuestionActionDef.url, question.title);		
	}
	
	private static String getViewAllActivitiesURL(Course course, CourseSection courseSection, ActivityResponse activityResponse) {
		Map showQuestionActionArgs = new HashMap();
		showQuestionActionArgs.put("courseSanitizedTitle", course.sanitizedTitle);
		showQuestionActionArgs.put("courseSectionSanitizedTitle", courseSection.sanitizedTitle);
		ActionDefinition showQuestionActionDef = 
						Router.reverse("CourseC.sectionActivityResponses", showQuestionActionArgs);
		String template = "<a href=\"%s\">%s</a>";
		return String.format(template, showQuestionActionDef.url + "#" + activityResponse.activity.id, activityResponse.activity.title);
	}
	
	private static String getViewAllActivitiesURL(Course course, 
												  ActivityResponse activityResponse) {
		
		//public static void activityResponses(String courseSanitizedTitle) {
		Map showQuestionActionArgs = new HashMap();
		showQuestionActionArgs.put("courseSanitizedTitle", course.sanitizedTitle);
		ActionDefinition showQuestionActionDef = 
						Router.reverse("CourseC.activityResponses", showQuestionActionArgs);
		String template = "<a href=\"%s\">%s</a>";
		return String.format(template, showQuestionActionDef.url + "#" + activityResponse.activity.id, activityResponse.activity.title);
	}
	
	public static String getActivityReviewURL(Course course, 
											  CourseSection courseSection, 
											  ActivityResponseReview review) {
		Map args = new HashMap();
		args.put("courseSanitizedTitle", course.sanitizedTitle);
		args.put("sectionSanitizedTitle", courseSection.sanitizedTitle);
		args.put("activityResponseId", review.activityResponse.id);
		String url = Router.reverse("CourseC.sectionActivityResponseReview", args).url;
		String template = "<a href=\"%s\">%s</a>";
		return String.format(template, url, review.activityResponse.activity.title);
	}
	
	public static String getActivityReviewURL(Course course,
											  CourseSection courseSection, 
											  ActivityResponse activityResponse) {
		Map args = new HashMap();
		args.put("courseSanitizedTitle", course.sanitizedTitle);
		args.put("sectionSanitizedTitle", courseSection.sanitizedTitle);
		args.put("activityResponseId", activityResponse.id);
		String url = Router.reverse("CourseC.sectionActivityResponseReview",
				args).url;
		return url;
	}
	
	//TODO: Implement this method
	private static String getActivityReviewURL(Course course,
											   ActivityResponseReview review) {
		if(true) {
			throw new RuntimeException("Method not supported");
		}
		return null;
	}

}
