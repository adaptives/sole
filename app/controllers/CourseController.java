package controllers;

import java.util.List;

import models.Course;
import models.CourseSection;
import play.mvc.Controller;

public class CourseController extends Controller{
	
	public static void sections(long courseId) {
		Course course = Course.find("byId", courseId).first();
		List<CourseSection> courseSections = 
			CourseSection.find("byCourse", course).fetch();
		render(courseSections);
	}
	
	public static void section(long courseId, long sectionId) {
		CourseSection courseSection = CourseSection.find("byId", sectionId).first();
		render(courseSection);
	}
}
