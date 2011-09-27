package controllers;

import java.util.Date;
import java.util.List;
import java.util.Random;

import other.utils.HtmlSanitizer;

import models.Feedback;
import models.ModeratedFeedback;
import play.Logger;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.Required;
import play.mvc.Controller;

public class FeedbackC extends Controller {
	
	public static final org.apache.log4j.Logger cLogger = 
								Logger.log4j.getLogger(FeedbackC.class);

	public static void index() {
		List<ModeratedFeedback> feedbacks = ModeratedFeedback.findModerated();
		render(feedbacks);
	}

	public static void createFeedback(@Required String name,
									  @Required @Email String email, 
									  @Required String message,
									  String cans,
									  @Equals(value="cans", message="must.be.human") String cansval) {
		if (validation.hasErrors()) {
			validation.keep();
			params.flash();
			flash.error("Please correct these errors");
			index();
		}
		try {
			String cleanMessage = HtmlSanitizer.clean(message);
			ModeratedFeedback feedback = new ModeratedFeedback(name, email, cleanMessage);
			feedback.save();
			flash.success("Thank you for your feedback, it will be displayed as soon as it is approved");
			index();
		} catch(Exception e) {
			cLogger.error("Could not sanitize HTML input '" + e.getMessage() + "'");
			render("errors/500.html", "Sorry, an internal error has occured. It has been reported to the administrator");
		}
	}
}
