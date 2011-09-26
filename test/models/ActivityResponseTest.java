package models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class ActivityResponseTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
		Fixtures.load("users-and-study-sessions.yml");
		ActivityTest.createCourseWithActivities();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCreateAndRetrieve()  throws Exception {
		List<SocialUser> users = SocialUser.findAll();
		Assert.assertNotSame(0, users.size());
		List<Course> courses = Course.findAll();
		Course course = courses.get(0);
		List<CourseSection> sections = course.fetchSectionsByPlacement();
		CourseSection theSection = sections.get(0);
		Set<Activity> activities = theSection.activities;
		Activity theActivity = (new ArrayList<Activity>(activities)).get(0);
		SocialUser user1 = users.get(0);
		ActivityResponse activityResponse = new ActivityResponse(user1, 
																 theActivity, 
																 "title", 
																 "http://diycomputerscience.com");
		activityResponse.save();
		
		List<ActivityResponse> retrievedActivityResponses = ActivityResponse.findAll();
		assertEquals(1, retrievedActivityResponses.size());
	}
	
	@Test
	public void testDelete() throws Exception {
		List<SocialUser> users = SocialUser.findAll();
		Assert.assertNotSame(0, users.size());
		List<Course> courses = Course.findAll();
		Course course = courses.get(0);
		List<CourseSection> sections = course.fetchSectionsByPlacement();
		CourseSection theSection = sections.get(0);
		Set<Activity> activities = theSection.activities;
		Activity theActivity = (new ArrayList<Activity>(activities)).get(0);
		SocialUser user1 = users.get(0);
		ActivityResponse activityResponse = new ActivityResponse(user1, 
																 theActivity, 
																 "title", 
																 "http://diycomputerscience.com");
		activityResponse.save();
		
		List<ActivityResponse> retrievedActivityResponses = ActivityResponse.findAll();
		assertEquals(1, retrievedActivityResponses.size());
		
		ActivityResponse retrieveActivityResponse = retrievedActivityResponses.get(0);
		ActivityResponse deletedActivityResponse = retrieveActivityResponse.delete();
		assertNotNull(deletedActivityResponse);
		assertEquals(0, ActivityResponse.findAll().size());
	}

}
