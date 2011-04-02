package controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Answer;
import models.CourseSection;
import models.Forum;
import models.ProfilePic;
import models.Question;
import models.StudySession;
import models.User;
import models.UserProfile;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.mvc.Before;
import play.mvc.Controller;
import play.server.Server;

public class UserProfileC extends Controller {
	
	@Before
	public static void setConnectedUser() {
		if(Security.isConnected()) {
			User user = User.findByEmail(Security.connected());
			renderArgs.put("user", user.name);
		}
	}
	
	public static void list() {
		List<User> allUsers = User.findAll();
		render(allUsers);
	}
	
	public static void show(long userId) {
		
		
		List<String> tabIds = new ArrayList<String>();
		List<String> tabNames = new ArrayList<String>();
		
		if(Security.isConnected()) {			
			User connectedUser = User.findByEmail(Security.connected());
			if(connectedUser.id == userId) {
				tabIds.add("settings");
				tabNames.add("Settings");
			}			
		}
		tabIds.add("merit");
		tabIds.add("questions-asked");
		tabIds.add("answers-provided");
		tabIds.add("challenges-taken");
		
				
		tabNames.add("Merit");
		tabNames.add("Questions Asked");
		tabNames.add("Answers Provided");
		tabNames.add("Challenges Taken");
		
		
		
		UserProfile userProfile = getUserProfileFromUserId(userId);
		
		//Get a list of questions asked by the user in DIY courses
		//TODO: 
		List diyQuestions = CourseSection.find("select distinct q, cs.id from CourseSection cs join cs.questions as q where q.author.id = ?", userId).fetch();
		
		//Get a list of questions asked by the user in StudySessions
		//TODO: Closed / private study session questions should not be included here
		List studySessionQuestions = StudySession.find("select distinct q, ss.id from StudySession ss join ss.forum.questions as q where q.author.id = ?", userId).fetch();
		
		//Get a list of answers provided by the user in DIY courses
		List diyAnswers = StudySession.find("select q, cs.id from CourseSection cs join cs.questions as q join q.answers as a where a.author.id = ?", userId).fetch();
		
		//Get a list of answers provided by the user in Study Sessions
		List studySessionAnswers = 
			StudySession.find("select q, ss.id from StudySession ss join ss.forum.questions as q join q.answers as a where a.author.id = ?", userId).fetch();
		
		
		render(userProfile, diyQuestions, studySessionQuestions, diyAnswers, studySessionAnswers, tabIds, tabNames);
	}
	
	public static void change(String username, 
							  String oldPassword, 
							  @Required @MinSize(5) String newPassword, 
							  @Required @MinSize(5) String newPassword2) {
		if (validation.hasErrors()) {
            validation.keep();
            params.flash();
            flash.error("Please correct these errors !");
            User userByUsername = User.findByEmail(username);
			show(userByUsername.id);
        }
		User user = 
			User.find("byEmailAndPasswordHash", username, oldPassword).first();
		if(user != null) {
			if(newPassword.equals(newPassword2)) {
				user.passwordHash = newPassword;
				user.save();
			} else {
				flash.error("Sorry, the fields 'New Password' and 'Retype New Password' did not match.");				
			}
			show(user.id);
		} else {
			flash.error("Sorry, the oldPassword did not match, please  again.");
			User userByUsername = User.findByEmail(username);
			show(userByUsername.id);
		}
	}
	
	public static void pic(long userId) {
		UserProfile userProfile = UserProfile.find("select distinct upr from UserProfile upr where upr.user.id = ?", userId).first();
		renderBinary(userProfile.profilePic.image.get());
	}
	
	public static void update(long userId, 
							  String aboutMyself, 
							  String location, 
							  Blob profilePicBlob) {
		
		UserProfile userProfile = UserProfile.find("select distinct upr from UserProfile upr where upr.user.id = 1").first();
		if(profilePicBlob != null) {
			//TODO: Delete the old profile pic
			userProfile.profilePic = new ProfilePic(profilePicBlob).save();
		}
		userProfile.aboutMyself = aboutMyself;
		userProfile.location = location;
		userProfile.save();
		
		show(userId);
	}
	
	private static UserProfile getUserProfileFromUserId(long userId) {
		String sql = "select distinct upr from UserProfile upr where upr.user.id = ?";
		return UserProfile.find(sql, userId).first();
	}
}
