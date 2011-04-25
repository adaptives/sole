package models;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class UserRegistrationDateTest extends UnitTest {
	
	@Before
	public void setUp() {
		Fixtures.deleteAll();
		Fixtures.load("data.yml");
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testUserRegistrationDate() {
		SocialUser socialUser = (SocialUser)SocialUser.findAll().get(0);
		Date now = new Date();
		UserRegistrationDate userRegistrationDate = 
							new UserRegistrationDate(socialUser, now);
		userRegistrationDate.save();
		UserRegistrationDate retrievedURD = 
			UserRegistrationDate.
				find("select urd from UserRegistrationDate urd where urd.socialUser.id = ?", socialUser.id).first();
		assertNotNull(retrievedURD);
		assertEquals(now, retrievedURD.registrationDate);
	}
	
	
}
