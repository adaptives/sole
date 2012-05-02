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
import models.Badge;
import models.Course;
import models.CourseSection;
import models.Forum;
import models.Pic;
import models.Question;
import models.SocialUser;
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
	
	public static void show(long userId) {
				
		UserProfile userProfile = getUserProfileFromSocialUserId(userId);
		notFoundIfNull(userProfile);
		
		List<Badge> badges = Badge.fetchBadgesForSocialUser(String.valueOf(userId)); 
		List<Course> coursesEnrolled = Course.find("select c from Course c join c.enrolledParticipants ep where ep.id = ?", userId).fetch();
		List<Course> coursesCompleted = Course.find("select c from Course c join c.completedParticipants cp where cp.id = ?", userId).fetch();		
		List diyQuestions = Course.find("select distinct q, c.sanitizedTitle from Course c join c.forum as f join f.questions q where q.author.id = ?", userId).fetch();
		List diyAnswers = Course.find("select distinct a, c.sanitizedTitle from Course c join c.forum as f join f.questions q join q.answers a where a.author.id = ?", userId).fetch();
		
		render(userProfile,
			   badges,
			   coursesEnrolled, 
			   coursesCompleted, 
			   diyQuestions, 
			   diyAnswers);
		
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
	
	public static void badgeIcon(long badgeId) {
		flash.keep();
		Badge badge = Badge.findById(badgeId);
		notFoundIfNull(badge);
		InputStream is = null;
		if(badge.badgeDef.badgeIcon != null) {
			is = badge.badgeDef.badgeIcon.image.get();
			if(is != null) {
				renderBinary(is);
			}			
		} 
		if(is == null) {
			try {
				is = new FileInputStream(Play.getFile("public/images/default_course_image.gif"));
				renderBinary(is);
			} catch(Exception e) {
				cLogger.error("Could not render default user image ", e);
			}
		}
	}
	
	public static void badgeDetails(long userId, long badgeId) {
		SocialUser user = SocialUser.findById(userId);
		notFoundIfNull(user);
		UserProfile userProfile = getUserProfileFromSocialUserId(userId);
		notFoundIfNull(userProfile);
		Badge badge = Badge.findById(badgeId);
		notFoundIfNull(badge);
		if(badge.awardee.id == user.id) {
			render(userProfile, badge);
		} else {
			error("Badge does not belong to user");
		}
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
