package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.Comment;
import models.Course;
import models.CourseSection;
import models.Question;
import models.User;
import play.data.validation.Email;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;

public class CourseC extends Controller {
	
	@Before
	public static void setConnectedUser() {
		if(Security.isConnected()) {
			User user = User.findByEmail(Security.connected());
			renderArgs.put("user", user.name);
		}
	}
	
	public static void courses() {
		List<Course> courses = Course.findAll();
		render(courses);
	}
	
	public static void sections(long courseId) {
		Course course = Course.find("byId", courseId).first();
		List<CourseSection> courseSections = course.fetchSectionsByPlacement();
		render(course, courseSections);
	}
	
	public static void section(long courseId, long sectionId) {
		CourseSection courseSection = CourseSection.find("byId", sectionId).first();
		
		//TODO: Do we need to fetch comments and questions... I am doing this
		//because we are passing a set and the template might expect a list.
		//and also because of lazy loading
		Set<Comment> commentsSet = courseSection.comments; 
		List<Comment> comments = new ArrayList<Comment>(commentsSet);
		
		Set<Question> questionSet = courseSection.questions;
		List<Question> questions = new ArrayList<Question>(questionSet);
		
		render(courseSection, questions, comments);
	}
	
	public static void courseSectionQuestion(long courseSectionId, 
											 @Required String title,
											 @Required String content,
											 String tags) {
		CourseSection courseSection = CourseSection.findById(courseSectionId);
		User user = User.findByEmail(Security.connected());
		Question question = new Question(title, content, user);
		if(tags != null) {
			String tagArray[] = tags.split(",");
			if(tagArray != null) {
				for(String tag : tagArray) {
					question.tagWith(tag);
				}
			}			
		}
		courseSection.questions.add(question);
		courseSection.save();
		section(courseSection.course.id, courseSection.id);
	}
	
	public static void comment(long courseSectionId,
							   @Required String name, 
			  				   @Required @Email String email,
			  				   String website,
			  				   @Required String message) {
		CourseSection courseSection = CourseSection.findById(courseSectionId);
		Comment comment = new Comment(message, name, email, website);
		courseSection.comments.add(comment);
		courseSection.save();
		section(courseSection.course.id, courseSection.id);
	}
}
