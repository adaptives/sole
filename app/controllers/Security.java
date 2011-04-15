package controllers;

import java.util.List;
import java.util.Set;

import play.Logger;

import models.Role;
import models.SocialUser;
import models.User;

public class Security extends Secure.Security{
	
	public static final org.apache.log4j.Logger cLogger = 
		Logger.log4j.getLogger(Security.class);
	
	public static boolean authenticate(String username, String password) {
		User user = 
			User.find("byEmail", username).first();
		if(user != null && user.checkPassword(password)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean check(String profile) {
		boolean retVal = false;
		if(isConnected()) {
			//TODO: We should move roles to SocialUser
			SocialUser user = SocialUser.findById(Long.parseLong(connected()));
			if(user != null) {
				Set<Role> roles = user.roles;
				for(Role role : roles) {
					if(role.name.equals(profile)) {
						retVal = true;
						break;
					}
				}
			}

		}
		return retVal;
	}
	
	/**
     * This method is called after a successful authentication.
     * You need to override this method if you with to perform specific actions (eg. Record the time the user signed in)
     */
    static void onAuthenticated() {
    	//'username' is set in Session by Secure.authenticate
    	String username = session.get("username");
//    	System.out.println("username : '" + username + "'");
    	List<SocialUser> socialUsers = SocialUser.findAll();
    	List<User> users = User.findAll();
    	User loggedInUser = User.find("select u from User u where u.email = ?", username).first();
    	session.put(SocialAuthC.USER, loggedInUser.socialUser.id);
    }

     /**
     * This method is called after a successful sign off.
     * You need to override this method if you with to perform specific actions (eg. Record the time the user signed off)
     */
    static void onDisconnected() {
    	//Secure.logout() clears the session, so we probably do not need to do anything here
    }

    /**
     * This method returns the current connected username
     * @return
     */
    public static String connected() {
        return session.get(SocialAuthC.USER);
    }

    /**
     * Indicate if a user is currently connected
     * @return  true if the user is connected
     */
    public static boolean isConnected() {
        return session.contains(SocialAuthC.USER);
    }
}
