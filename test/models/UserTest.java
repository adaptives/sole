package models;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import play.libs.Codec;
import play.test.Fixtures;
import play.test.UnitTest;

public class UserTest extends UnitTest {
	@Before
	public void setUp() {
		Fixtures.deleteAll();
		Fixtures.load("data.yml");
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testRetrievingAndUpdatingExistingUser() {
		User user = User.findByEmail("learner@somewhere.com");
		assertNotNull(user);
		assertNotNull(user.socialUser);
		user.name = "New Name";
		user.passwordHash = "newpasswordhash";
		user.save();
		
		User retrievedUser = User.findByEmail("learner@somewhere.com");
		assertNotNull(retrievedUser);
		
		assertEquals("New Name", retrievedUser.name);
		assertEquals("newpasswordhash", retrievedUser.passwordHash);
	}
	
	@Test
	public void testInsertingUser() {
		SocialUser socialUser = 
			new SocialUser("test@testemail.com", "testscreenname1");		
		socialUser.save();
		
		User user = new User("test@testemail.com", 
							 "pwd", 
							 "test name", 
							 socialUser);
		
		user.save();
		
		User retrievedUser = User.findByEmail("test@testemail.com");
		assertNotNull(retrievedUser);
		assertEquals(socialUser, retrievedUser.socialUser);
		assertEquals("test name", retrievedUser.name);
		assertEquals(Codec.hexMD5("pwd"), retrievedUser.passwordHash);
		assertEquals("test@testemail.com", retrievedUser.email);
		Assert.assertNotNull(retrievedUser.needConfirmation);
	}

}
