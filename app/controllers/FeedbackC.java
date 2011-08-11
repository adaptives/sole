package controllers;

import java.util.List;

import models.Feedback;
import play.data.validation.Email;
import play.data.validation.Required;
import play.mvc.Controller;

public class FeedbackC extends Controller {

	public static void index() {
		List<Feedback> feedbacks = Feedback.findAll();
		render(feedbacks);
	}

	public static void createFeedback(@Required String name,
			@Required @Email String email, @Required String message) {
		if (validation.hasErrors()) {
			validation.keep();
			params.flash();
			flash.error("Please correct these errors");
			index();
		}
		Feedback feedback = new Feedback(name, email, message);
		feedback.save();
		index();
	}
}
