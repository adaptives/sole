package controllers;

import java.util.TreeSet;

import models.CourseSection;
import models.Question;
import models.Tag;
import play.mvc.Controller;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
public class QuestionSecureC extends Controller {
	
	public static void delete(long id) {
		Question question = Question.findById(id);
		
		CourseSection courseSection = CourseSection.find("select cs from CourseSection cs join cs.questions as q where q.id = ?", id).first();
		courseSection.questions.remove(question);
		courseSection.save();
		
		question.tags = new TreeSet<Tag>();
		question.delete();
	}
	
}
