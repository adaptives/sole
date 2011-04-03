package controllers;

import java.util.Set;

import play.Logger;

import models.Role;
import models.User;

public class Security extends Secure.Security{
	
	public static final org.apache.log4j.Logger cLogger = 
		Logger.log4j.getLogger(Security.class);
	
	public static boolean authenticate(String username, String password) {
		User user = 
			User.find("byEmailAndPasswordHash", username, password).first();
		if(user != null) {
			Logger.info("authentication successfull '" + username + "'");
		} else {
			Logger.info("authentication failed '" + username + "'");
		}
		return user != null;
	}
	
	public static boolean check(String profile) {
		User user = User.findByEmail(connected());
		Set<Role> roles = user.roles;
		for(Role role : roles) {
			if(role.name.equals(profile)) {
				return true;
			}
		}
		return false;
	}
	
	/**
     * This method is called after a successful authentication.
     * You need to override this method if you with to perform specific actions (eg. Record the time the user signed in)
     */
    static void onAuthenticated() {
    	//username
    	User loggedInUser = User.findByEmail(session.get("username")) ;
    	session.put(SocialAuthC.USER, loggedInUser.socialUser.id);
    }

     /**
     * This method is called after a successful sign off.
     * You need to override this method if you with to perform specific actions (eg. Record the time the user signed off)
     */
    static void onDisconnected() {
    	//Secure.logout() clears the session, so we probably do not need to do anything here
    }

}
