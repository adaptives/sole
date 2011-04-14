package controllers;

import static controllers.Secure.login;
import play.*;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.mvc.*;

import java.util.*;

import notifiers.Notifier;


import models.*;

@With(SocialAuthC.class)
public class Application extends Controller {

	public static final org.apache.log4j.Logger cLogger = 
									Logger.log4j.getLogger(Application.class);
	
	
	//------------------ HomePage
	public static void index() {
		PageC.show("home");
    }
	
	public static void confirmRegistration(String uuid) {
        User user = User.findByRegistrationUUID(uuid);
        notFoundIfNull(user);
        user.needConfirmation = null;
        user.save();
        //log in the user
        session.put(SocialAuthC.USER, user.id);
        index();
    }
	
	//-------------------------- Feedback
	public static void feedback() {
		List<Feedback> feedbacks = Feedback.findAll();
		render(feedbacks);
	}
	
	public static void createFeedback(@Required String name, 
									  @Required @Email String email, 
									  @Required String message) {
		if(validation.hasErrors()) {
			validation.keep();
			params.flash();
			flash.error("Please correct these errors");
			feedback();
		}
		Feedback feedback = new Feedback(name, email, message);
		feedback.save();
		feedback();
	}
	
}