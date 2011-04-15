package controllers;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import models.KeyValueData;
import models.SocialUser;
import models.TwitterRequestToken;
import models.TwitterUser;

import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;


public class SocialAuthC extends Controller {
	
	public static final String TWITTER_REQUEST_TOKEN = "TWITTER_REQUEST_TOKEN";
	public static final String USER = "user";
	public static final String TWITTER_CALLBACK_URL = "http://127.0.0.1/callbacks/auth/twitter";
	public static final String TWITTER_CONSUMER_KEY = "tk";
	public static final String TWITTER_CONSUMER_SECRET = "ts";
	public static final org.apache.log4j.Logger cLogger = 
									Logger.log4j.getLogger(SocialAuthC.class);
	
	@Before
	public static void setConnectedUser() {
		if(Security.isConnected()) {
//			User user = User.findByEmail(Security.connected());
//			renderArgs.put("user", user.name);
			String userId = Security.connected();
			if(userId != null) {
				SocialUser socialUser = SocialUser.findById(Long.parseLong(userId));
				renderArgs.put("screenname", socialUser.screenname);
			}			
		}
	}
	
	public static void login() {
		render();
	}
	
	public static void authWithTwitter() {
		try {			
	        Twitter twitter = new TwitterFactory().getInstance();
	        setTwitterKeys(twitter);					
	        RequestToken requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
	        byte[] serRequestToken = serializeRequestToken(requestToken);
	        TwitterRequestToken trt = new TwitterRequestToken(serRequestToken);
	        trt.save();
	        session.put(TWITTER_REQUEST_TOKEN, trt.id);
	        redirect(requestToken.getAuthenticationURL());
		} catch(Exception e) {
			//TODO: What do we do?
			cLogger.error("Could not perform pre twitter login steps", e);
		}
	}

	public static void twitterCallback() {
		try {
			RequestToken requestToken = getRequestToken();
			Twitter twitter = new TwitterFactory().getInstance();
		    setTwitterKeys(twitter);
			AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, getVerifier());
			String screenName = accessToken.getScreenName();
			
			TwitterUser twitterUser = TwitterUser.find("byUsername", screenName).first();
			
			if(twitterUser == null) {
				SocialUser socialUser = new SocialUser("http://twitter.com/" + screenName);
				socialUser.save();
				socialUser.screenname = String.valueOf(socialUser.id);
				twitterUser = new TwitterUser(screenName, socialUser);
				twitterUser.accessToken = serializeAccessToken(accessToken);
				twitterUser.save();
				session.put("user", socialUser.id);
			} else {
				twitterUser.accessToken = serializeAccessToken(accessToken);
				twitterUser.save();
				session.put(SocialAuthC.USER, twitterUser.socialUser.id);
			}
			
		} catch(Exception e) {
			String msg = "Caught Exception while handling authentication " +
						 "callback from Twitter";
			cLogger.error(msg, e);
		}
	}

	private static void setTwitterKeys(Twitter twitter) {
		twitter.setOAuthConsumer(KeyValueData.findValue(TWITTER_CONSUMER_KEY), 
								 KeyValueData.findValue(TWITTER_CONSUMER_SECRET));
	}
	
	private static byte[] serializeAccessToken(AccessToken accessToken) 
		throws IOException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(accessToken);
		oos.close();
		return baos.toByteArray();
	}

	private static String getVerifier() {
		//TODO: Is this the right way to get request parameters?
		String verifier = request.params.get("oauth_verifier");
		return verifier;
	}

	private static RequestToken getRequestToken() throws IOException, ClassNotFoundException {
		long requestTokenId = Long.parseLong(session.get(TWITTER_REQUEST_TOKEN));
		TwitterRequestToken twitterRequestToken = TwitterRequestToken.findById(requestTokenId);
		byte serRequestToken[] = twitterRequestToken.requestToken;
		ByteArrayInputStream bais = new ByteArrayInputStream(serRequestToken);
		ObjectInputStream ois = new ObjectInputStream(bais);
		RequestToken requestToken = (RequestToken)ois.readObject();
		twitterRequestToken.delete();
		return requestToken;
	}

	private static byte[] serializeRequestToken(RequestToken requestToken) 
		throws IOException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(requestToken);
		oos.close();
		return baos.toByteArray();
	}

}
