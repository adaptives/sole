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
        
        List<String> duplicateErrors = noDuplicates(email, name);
        	
        if(duplicateErrors.size() == 0) {
        	SocialUser socialUser = new SocialUser(email);
            socialUser.save();
            socialUser.screenname = name;
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
        } else {            
            validation.keep();
            params.flash();
            String errorMsg = "Please correct these errors - ";
            for(String dupError : duplicateErrors) {
            	errorMsg += dupError + ", ";
            }
            flash.error(errorMsg);
            signup();
        }
        
    }

	private static List<String> noDuplicates(String email, 
											 String name) {
		
		//TODO: Can we use an annotation for ensuring uniqueness of the email field
        //Ensure that the email is unique
		List<String> retVal = new ArrayList<String>();
		
		if(User.findByEmail(email) != null) {
			retVal.add("User with email already exists");
		}
        
		
		List<SocialUser> existingUsersWithEmail = 
        	SocialUser.find("select u from SocialUser u where u.email = ?", email).fetch();
        if(existingUsersWithEmail.size() > 0) {
        	retVal.add("SocialUser with email already exists"); 
        }
        
        List<User> usersByName= User.find("select u from User u where u.name = ?", name).fetch();
        if(usersByName.size() > 0) {
        	retVal.add("User with name already exists");
        }
        
        List<SocialUser> existingUsersWithName = 
        	SocialUser.find("select u from SocialUser u where u.screenname = ?", name).fetch();
        if(existingUsersWithEmail.size() > 0) {
        	retVal.add("SocialUser with name already exists");
        }
        
        return retVal;
	}

}
