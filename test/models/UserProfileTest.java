package models;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class UserProfileTest extends UnitTest {
	
	@Before
	public void setUp() {
		Fixtures.deleteAll();
		Fixtures.load("data.yml");
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testUpdatingExistingUserProfile() {
		SocialUser socialUser = SocialUser.find("byScreenname", "student").first();
		
		UserProfile retrievedUserProfile1 = UserProfile.find("select up from UserProfile up where up.user.id = ?", socialUser.id).first();
		long retrievedUserProfile1Id = retrievedUserProfile1.id;
		assertNotNull(retrievedUserProfile1);
		retrievedUserProfile1.aboutMyself = "New About Myself";
		retrievedUserProfile1.save();
		
		UserProfile retrievedUserProfile2 = UserProfile.findById(retrievedUserProfile1Id);
		assertNotNull(retrievedUserProfile2);
		assertEquals("New About Myself", retrievedUserProfile2.aboutMyself);
		
		//TODO: Test updating the profilePic
	}
	
	@Test
	public void testInsertingUserProfile() {
		SocialUser socialUser = new SocialUser("test@testemail.com", "testscreenname1");
		socialUser.save();
		
		UserProfile userProfile = new UserProfile(socialUser);
		userProfile.save();
		
		UserProfile retrievedUserProfile = 
			UserProfile.
				find("select up from UserProfile up where up.user.id = ?", socialUser.id).first();
		assertNotNull(retrievedUserProfile);
	}
}
