package other.utils;

import java.io.InputStream;
import java.net.URL;

import play.Logger;

import twitter4j.ProfileImage;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class TwitterUtils {
	
	public static final org.apache.log4j.Logger cLogger = 
									Logger.log4j.getLogger(TwitterUtils.class);
	
	public static InputStream getStreamToTwitterPic(String screenName) {
		String TWITTER_PREFIX = "http://twitter.com/";
		if(screenName.startsWith(TWITTER_PREFIX)) {
			screenName = screenName.substring(TWITTER_PREFIX.length());
		}
		try {
			Twitter twitter = (new TwitterFactory()).getInstance();			
			URL url = new URL(twitter.getProfileImage(screenName, 
													  ProfileImage.MINI).getURL());
			return url.openStream();
		} catch(Exception e) {
			cLogger.error("Could not get Twitter image ", e);
		}
		return null;
	}
	
}
