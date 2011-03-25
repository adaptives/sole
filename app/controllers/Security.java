package controllers;

import java.util.Set;

import models.Role;
import models.User;

public class Security extends Secure.Security{
	
	public static boolean authenticate(String username, String password) {
		User user = 
			User.find("byEmailAndPasswordHash", username, password).first();
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
