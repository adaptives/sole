package models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

public class BadgeDefTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCreateAndRetrieve() throws Exception {
		String type = "certification";
		BadgeType badgeType = new BadgeType(type);
		badgeType.save();
		
		String badgeName = "Java 101";
		String badgeDescription = "Java 101 badge";
		String badgeTitle = "Java 101";
		
		BadgeDef badgeDef = new BadgeDef(badgeName);
		badgeDef.description = badgeDescription;
		badgeDef.title = badgeTitle;
		badgeDef.type = badgeType;
		badgeDef.save();
		
		List<BadgeDef> retrievedBadgeDefs = BadgeDef.findAll();
		assertEquals(1, retrievedBadgeDefs.size());
		BadgeDef retrievedBadgeDef = retrievedBadgeDefs.get(0);
		assertEquals(badgeName, retrievedBadgeDef.name);
		assertEquals(badgeTitle, retrievedBadgeDef.title);
		assertEquals(badgeDescription, retrievedBadgeDef.description);
		assertEquals(type, retrievedBadgeDef.type.type);
	}
	
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
		List<SocialUser> awardedBy = new ArrayList<SocialUser>();
		awardedBy.add(socialUsers.get(0));
		
		Badge java101Badge1 = new Badge(java101BadgeDef, socialUsers.get(1), awardedBy, "good work");
		java101Badge1.save();
		
		Badge python101Badge1 = new Badge(python101BadgeDef, socialUsers.get(1), awardedBy, "good work");
		python101Badge1.save();
		
		//List<Badge> badgesForUser = 
		
	}

}
