package controllers;

import play.*;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.mvc.*;

import java.util.*;

import notifiers.Notifier;


import models.*;

public class Application extends Controller {

	public static void index() {
		List<Course> courses = Course.findAll();
		render(courses);
    }
	
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
        User user = new User(email, password, name);
        try {
            if (Notifier.welcome(user)) {
                flash.success("Your account is created. Please check your emails ...");
                login();
            }
        } catch (Exception e) {
            Logger.error(e, "Mail error");
        }
        flash.error("Oops ... (the email cannot be sent)");
        login();
    }

	private static void login() {
		render();
	}
}