package models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

public class BadgeTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testFetchBadgesForSocialUser() {
		//Java 101 Badge Def
		String java101BadgeName = "Java 101";
		String java101BadgeDescription = "Java 101 badge";
		String java101BadgeTitle = "Java 101";
		
		BadgeDef java101BadgeDef = new BadgeDef(java101BadgeName);
		java101BadgeDef.description = java101BadgeDescription;
		java101BadgeDef.title = java101BadgeTitle;
		java101BadgeDef.save();
		
		//Python 101 Badge Def
		String python101BadgeName = "Java 101";
		String python101BadgeDescription = "Java 101 badge";
		String python101BadgeTitle = "Java 101";
		
		BadgeDef python101BadgeDef = new BadgeDef(python101BadgeName);
		python101BadgeDef.description = python101BadgeDescription;
		python101BadgeDef.title = python101BadgeTitle;
		python101BadgeDef.save();
		
		//Award the badges
		List<SocialUser> socialUsers = SocialUser.findAll();
		assertTrue(socialUsers.size() > 1);
		List<SocialUser> awardedBy = new ArrayList<SocialUser>();
		awardedBy.add(socialUsers.get(0));
		
		Badge java101Badge1 = new Badge(java101BadgeDef, socialUsers.get(1), awardedBy, "good work");
		java101Badge1.save();
		
		Badge python101Badge1 = new Badge(python101BadgeDef, socialUsers.get(1), awardedBy, "good work");
		python101Badge1.save();
		
		List<Badge> badgesForUser = Badge.fetchBadgesForSocialUser(String.valueOf(socialUsers.get(1).id)); 
		assertEquals(2, badgesForUser.size());
	}

}
