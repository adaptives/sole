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
		String java101BadgeTitle = "Java 101 title";
		
		BadgeDef java101BadgeDef = new BadgeDef(java101BadgeName);
		java101BadgeDef.description = java101BadgeDescription;
		java101BadgeDef.title = java101BadgeTitle;
		java101BadgeDef.save();
		
		//Python 101 Badge Def
		String python101BadgeName = "Python 101";
		String python101BadgeDescription = "Python 101 badge";
		String python101BadgeTitle = "Python 101 title";
		
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
		String evidenceNote = "classroom test";
		EvidenceOfCompetency evidence = new EvidenceOfCompetency(evidenceNote);
		evidence.save();
		java101Badge1.evidence = evidence;
		java101Badge1.save();
		
		Badge python101Badge1 = new Badge(python101BadgeDef, socialUsers.get(1), awardedBy, "good work");
		python101Badge1.save();
		
		List<Badge> retreivedBadgesForUser = Badge.fetchBadgesForSocialUser(String.valueOf(socialUsers.get(1).id)); 
		assertEquals(2, retreivedBadgesForUser.size());
		
		Badge retreivedBadgeJava101 = retreivedBadgesForUser.get(0);
		assertEquals(java101BadgeName, retreivedBadgeJava101.badgeDef.name);
		assertEquals(evidenceNote, retreivedBadgeJava101.evidence.note);
	}

}
