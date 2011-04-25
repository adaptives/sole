package controllers;

import java.util.ArrayList;
import java.util.List;

import models.SocialUser;
import models.User;
import models.UserRegistrationDate;
import notifiers.Notifier;
import play.Logger;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.Validation.ValidationResult;
import play.mvc.Controller;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
public class RegisterC extends Controller {

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
        
        try {
        	SocialUser socialUser = new SocialUser(email, name);
        	socialUser.screenname = name;            
            socialUser.save();                                
            
            User user = new User(email, password, name, socialUser);
            user.save();
            
            (new UserRegistrationDate(socialUser)).save();
            try {
                if (Notifier.welcome(user)) {
                    flash.success("Your account is created. Please check your emails ...");                
                } else {
                	flash.error("Oops ... (the email cannot be sent)");
                }
                AdminC.index();
            } catch (Throwable e) {
                Logger.error(e, "Mail error");
                flash.error(MessageConstants.INTERNAL_ERROR);
            }
        } catch(Exception e) {            
            validation.keep();
            params.flash();
            String errorMsg = "Please correct these errors - " + e.getMessage();
            flash.error(errorMsg);
            signup();
        }
        
    }

}
