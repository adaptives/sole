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
		flash.keep();
		//PageC.show("home");
		Page page = Page.find("byName", "home").first();
		render(page);
    }
	
	public static void confirmRegistration(String uuid) {
        User user = User.findByRegistrationUUID(uuid);
        if(user != null) {
            user.needConfirmation = null;
            user.save();
            //log in the user
            session.put(SocialAuthC.USER, user.id);
            flash.success("Your email has been confirmed. Welcome to the DIYCOMPUTERSCIENCE community.");
        } else {
        	flash.error("We could not confirm your email. This may happen if you have already confirmed this email earlier, or if the confirmation url is incorrect. Please try logging in with your email and password. If you are unable to login then please contact the site administrator.");
        }
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