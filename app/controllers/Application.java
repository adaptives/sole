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
	
	//------------------- SignUp and Register
	public static void signup() {
        render();
    }
	
	public static void register(@Required @Email String email, 
								@Required @MinSize(5) String password, 
								@Equals("password") String password2, 
								@Required String name) {
        if (validation.hasErrors()) {
            validation.keep();
            params.flash();
            flash.error("Please correct these errors !");
            signup();
        }
        SocialUser socialUser = new SocialUser(email);
        socialUser.save();
        socialUser.screenname = "user-" + socialUser.id;
        socialUser.save();
        
        User user = new User(email, password, name, socialUser);
        (new UserRegistrationDate(user)).save();
        try {
            if (Notifier.welcome(user)) {
                flash.success("Your account is created. Please check your emails ...");                
            } else {
            	flash.error("Oops ... (the email cannot be sent)");
            }
            Secure.login();
        } catch (Throwable e) {
            Logger.error(e, "Mail error");
            flash.error(MessageConstants.INTERNAL_ERROR);
        }
        
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