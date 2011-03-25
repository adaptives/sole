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
}
