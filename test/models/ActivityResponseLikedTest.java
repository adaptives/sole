package models;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class ActivityResponseLikedTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
		Fixtures.load("users-and-study-sessions.yml");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSaveAndRetrieve() {
		//create
		List<SocialUser> users = SocialUser.findAll(); 
		Activity activity = new Activity("Test activity", "Test Activity Content");
		
		ActivityResponse activityResponse1 = new ActivityResponse(users.get(0), 
																  activity, 
																  "http://diycomputerscience.com", 
																  "My first activity response");
		activityResponse1.save();
		activityResponse1.like(users.get(1));
		
		ActivityResponse activityResponse2 = new ActivityResponse(users.get(1), 
				  												  activity, 
				  												  "http://diycomputerscience.com", 
				  												  "My first activity response");
		activityResponse2.save();
		activityResponse2.like(users.get(0));
		activityResponse2.like(users.get(2));
		
		//retrieve
		List<ActivityResponseLiked> arls = ActivityResponseLiked.findAll();
		assertEquals(3, arls.size());
		
		assertEquals(1, activityResponse1.likes());
		assertEquals(2, activityResponse2.likes());		
	}
	
	@Test
	public void testLikeBySameUser() {
		//create
		List<SocialUser> users = SocialUser.findAll(); 
		Activity activity = new Activity("Test activity", "Test Activity Content");
		
		ActivityResponse activityResponse1 = new ActivityResponse(users.get(0), 
																  activity, 
																  "http://diycomputerscience.com", 
																  "My first activity response");
		activityResponse1.save();
		activityResponse1.like(users.get(0));
		
		//retrieve
		assertEquals(0, activityResponse1.likes());
	}
	
	@Test
	public void testMultipleLikesBySameUser() {
		//create
		List<SocialUser> users = SocialUser.findAll(); 
		Activity activity = new Activity("Test activity", "Test Activity Content");		
		
		ActivityResponse activityResponse1 = new ActivityResponse(users.get(0), 
																  activity, 
																  "http://diycomputerscience.com", 
																  "My first activity response");
		activityResponse1.save();
		activityResponse1.like(users.get(1));
		activityResponse1.like(users.get(1));
		activityResponse1.like(users.get(1));
		
		//retrieve
		assertEquals(1, activityResponse1.likes());
	}
	
}
