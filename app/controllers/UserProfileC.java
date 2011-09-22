package controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import other.utils.TwitterUtils;

import models.Answer;
import models.Course;
import models.CourseSection;
import models.Forum;
import models.Pic;
import models.Question;
import models.SocialUser;
import models.StudySession;
import models.TwitterUser;
import models.User;
import models.UserProfile;
import play.Logger;
import play.Play;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.libs.Codec;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;
import play.mvc.With;
import play.mvc.Router.ActionDefinition;
import play.server.Server;

@With(SocialAuthC.class)
public class UserProfileC extends Controller {
	
	public static org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(UserProfileC.class);
	
	public static class PageRange {
		public long from;
		public long to;
	}
	
	// NOTE: The order of the two overloaded list() methods is very important
	// The order matches the order in which the url's are mentioned in the 
	// routes file
	
	public static void list(long page, long size) {
		if(page < 1) {
			flash.error("Incorrect value for page '" + page + "' . The value must be equal or greater than 1");
			render("emptypage.html");
		}
		if(size < 1) {
			flash.error("Incorrect value for size '" + size + "' . The value must be equal or greater than 1");
			render("emptypage.html");
		}
		List<SocialUser> allUsers = 
			SocialUser.
				find("select su from SocialUser su order by su.screenname").
					fetch((int)page, (int)size);
		long count = SocialUser.count();
		
		int pages = (int)(count/size);
		pages++;
		
		render("UserProfileC/list.html", allUsers, page, size, pages);
	}
	
	public static void list() {
		List<SocialUser> allUsers = 
			SocialUser.find("select su from SocialUser su order by su.screenname").fetch();		
		render(allUsers);
	}
	
	public static void show(long userId) {
		
		
		List<String> tabIds = new ArrayList<String>();
		List<String> tabNames = new ArrayList<String>();
		
		if(Security.isConnected()) {
			SocialUser connectedUser = SocialUser.findById(Long.parseLong(Security.connected()));
			if(connectedUser.id == userId) {
				tabIds.add("settings");
				tabNames.add("Settings");
			}			
		}
		tabIds.add("merit");
		tabIds.add("courses");
		tabIds.add("study-sessions");
		tabIds.add("challenges-taken");
		
				
		tabNames.add("Merit");
		tabNames.add("DIY Courses");
		tabNames.add("Study Sessions");
		tabNames.add("Challenges Taken");
		
		
		
		UserProfile userProfile = getUserProfileFromSocialUserId(userId);
		
		if(userProfile != null) {
			//Get a list of questions asked by the user in DIY courses
			
			List<Course> coursesEnrolled = Course.find("select c from Course c join c.enrolledParticipants ep where ep.id = ?", userId).fetch();
			List<Course> coursesCompleted = Course.find("select c from Course c join c.completedParticipants cp where cp.id = ?", userId).fetch();		
			List diyQuestions = CourseSection.find("select distinct q, cs.id from CourseSection cs join cs.questions as q where q.author.id = ?", userId).fetch();
			List diyAnswers = StudySession.find("select q, cs.id from CourseSection cs join cs.questions as q join q.answers as a where a.author.id = ?", userId).fetch();
			
			//TODO: Closed / private study session questions should not be included here
			
			List studySessionQuestions = 
				StudySession.find("select distinct q, ss.sanitizedTitle from StudySession ss join ss.forum.questions as q where q.author.id = ?", userId).fetch();
			List studySessionAnswers = 
				StudySession.find("select q, ss.sanitizedTitle from StudySession ss join ss.forum.questions as q join q.answers as a where a.author.id = ?", userId).fetch();
			List<StudySession> studySessionsParticipated = 
				StudySession.find("select ss from StudySession ss join ss.applicationStore aps join aps.applications a where a.socialUser.id = ? and a.currentStatus = 1", userId).fetch();
	 
			render(userProfile, 
				   coursesEnrolled, 
				   coursesCompleted, 
				   diyQuestions, 
				   diyAnswers, 
				   studySessionsParticipated, 
				   studySessionQuestions, 
				   studySessionAnswers, 
				   tabIds, 
				   tabNames);
		} else {
			flash.error("We could not find the requested user");
			render("emptypage.html");
		}
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
			User.find("byEmailAndPasswordHash", username, Codec.hexMD5(oldPassword)).first();
		if(user != null) {
			if(newPassword.equals(newPassword2)) {
				user.passwordHash = Codec.hexMD5(newPassword);
				user.save();
			} else {
				flash.error("Sorry, the fields 'New Password' and 'Retype New Password' did not match.");				
			}
			show(user.socialUser.id);
		} else {
			flash.error("Sorry, the oldPassword did not match, please  again.");
			User userByUsername = User.findByEmail(username);
			show(userByUsername.socialUser.id);
		}
	}
	
	public static void pic(long userId) {
		flash.keep();
		UserProfile userProfile = UserProfile.find("select distinct upr from UserProfile upr where upr.user.id = ?", userId).first();
		InputStream is = null;
		if(userProfile != null && userProfile.profilePic != null) {
			is = userProfile.profilePic.image.get();
			if(is != null) {
				renderBinary(is);
			}
		} 
		if(is == null) {
			try {				
				if(userProfile.user.screenname != null && userProfile.user.screenname.startsWith("http://twitter.com/")) {
					is = TwitterUtils.getStreamToTwitterPic(userProfile.user.screenname);
				}
				if(is == null) {
					is = new FileInputStream(Play.getFile("public/images/default_user_image.png"));
				}
				renderBinary(is);
			} catch(Exception e) {
				cLogger.error("Could not render default user image ", e);
			}
		}		
	}
	
	public static void picTag(long userId) {
		flash.keep();
		String imageTag = "<img src=\"%s\" />";
		String picTag = String.format(imageTag, "/public/images/default_user_image.png");
		
		UserProfile userProfile = UserProfile.find("select distinct upr from UserProfile upr where upr.user.id = ?", userId).first();
		if(userProfile.profilePic != null) {
			Map actionArgs = new HashMap();
			actionArgs.put("userId", userId);			
			ActionDefinition imageDef = Router.reverse("UserProfileC.pic", actionArgs);
			picTag = String.format(imageTag, imageDef.url);
		} else {
			String twitterScreenname = TwitterUser.getTwitterUsernameForSocialUser(userId);
			if(twitterScreenname != "") {
				picTag = String.format(imageTag, getTwitterPicUrl(twitterScreenname));
			}			
		}
		renderText(picTag);
	}
	
	public static void update(long userId, 
							  String aboutMyself, 
							  String location, 
							  Blob profilePicBlob) {
		
		UserProfile userProfile = UserProfile.find("select distinct upr from UserProfile upr where upr.user.id = ?", userId).first();
		if(profilePicBlob != null) {
			//TODO: Delete the old profile pic
			userProfile.profilePic = new Pic(profilePicBlob).save();
		}
		userProfile.aboutMyself = aboutMyself;
		userProfile.location = location;
		userProfile.save();
		
		show(userId);
	}
	
	private static UserProfile getUserProfileFromSocialUserId(long userId) {
		String sql = "select distinct upr from UserProfile upr where upr.user.id = ?";
		return UserProfile.find(sql, userId).first();
	}
	
	private static String getTwitterPicUrl(String twitterScreenname) {
		return String.format("http://api.twitter.com/1/users/profile_image?screen_name=%s&size=normal", 
 		                     twitterScreenname.substring("http://twitter.com/".length()));
	}
	
}
