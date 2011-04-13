package controllers;

import models.SocialUser;
import models.User;
import models.UserRegistrationDate;
import notifiers.Notifier;
import play.Logger;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.MinSize;
import play.data.validation.Required;
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
            AdminC.index();
        } catch (Throwable e) {
            Logger.error(e, "Mail error");
            flash.error(MessageConstants.INTERNAL_ERROR);
        }
        
    }


}
