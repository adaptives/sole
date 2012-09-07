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

public class ActivityTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testCreateAndRetrieve()  throws Exception {
		createCourse();
		List<Activity> preActivities = Activity.findAll();
		
		List<SocialUser> users = SocialUser.findAll();
		Assert.assertNotSame(0, users.size());
		List<Course> courses = Course.findAll();
		Course course = courses.get(0);
		List<CourseSection> sections = course.fetchSectionsByPlacement();
		CourseSection theSection = sections.get(0);
		
		String title = "test activity";
		String content = "test activity content";
		Activity activity = new Activity(title, content);
		theSection.activities.add(activity);
		theSection.save();
		
		List<Activity> allActivities = Activity.findAll();
		assertEquals(preActivities.size() + 1, allActivities.size());
		
		//TODO: I am not sure if this is a good idea... will we always get the activity at this location ?
		Activity retrievedActivity = allActivities.get(preActivities.size());
		assertEquals(title, retrievedActivity.title);
		assertEquals(content, retrievedActivity.content);
	}
	
	@Test
	public void testHasResponded() throws Exception {
		createCourseWithActivities();
		List<SocialUser> socialUsers = SocialUser.findAll();
		Course course = Course.findBySanitizedTitle("play-framework");
		CourseSection section = CourseSection.findBySanitizedTitleByCouse(course, "introduction");
		Activity activity = section.activities.iterator().next();
		ActivityResponse activityResponse = new ActivityResponse(socialUsers.get(0), activity, "title", "http://diycomputerscience.com");
		activityResponse.save();
		
		assertTrue(activity.hasResponded(String.valueOf(socialUsers.get(0).id)));
		assertFalse(activity.hasResponded(String.valueOf(socialUsers.get(1).id)));
	}
	
	@Test
	public void testGetAllResponses() {
		createCourseWithActivities();
		List<SocialUser> socialUsers = SocialUser.findAll();
		Course course = Course.findBySanitizedTitle("play-framework");
		CourseSection section = CourseSection.findBySanitizedTitleByCouse(course, "introduction");
		
		Activity activity = section.activities.iterator().next();
		
		ActivityResponse activityResponse00 = new ActivityResponse(socialUsers.get(0), activity, "title", "http://diycomputerscience.com");
		activityResponse00.save();
		
		ActivityResponse activityResponse01 = new ActivityResponse(socialUsers.get(0), activity, "title 1", "http://diycomputerscience.com");
		activityResponse01.save();
		
		List<ActivityResponse> activityResponses = activity.getAllResponses();
		assertEquals(2, activityResponses.size());		
	}
	
	@Test
	public void testGetResponsesByUser() {
		createCourseWithActivities();
		List<SocialUser> socialUsers = SocialUser.findAll();
		Course course = Course.findBySanitizedTitle("play-framework");
		CourseSection section = CourseSection.findBySanitizedTitleByCouse(course, "introduction");
		
		Activity activity = section.activities.iterator().next();
		
		ActivityResponse activityResponse00 = new ActivityResponse(socialUsers.get(0), activity, "title", "http://diycomputerscience.com");
		activityResponse00.save();
		
		ActivityResponse activityResponse01 = new ActivityResponse(socialUsers.get(0), activity, "title 1", "http://diycomputerscience.com");
		activityResponse01.save();
		
		
		List<ActivityResponse> activityResponsesByUser0 = activity.getResponsesByUser(String.valueOf(socialUsers.get(0).id));
		assertEquals(2, activityResponsesByUser0.size());
		
		List<ActivityResponse> activityResponsesByUser1 = activity.getResponsesByUser(String.valueOf(socialUsers.get(1).id));
		assertEquals(0, activityResponsesByUser1.size());		
	}
	
	public static void createCourse() {
		CourseCategory cat = new CourseCategory("courses");
		cat.save();
		
		Course course = new Course("Play Framework", "Play framework course");
		course.category = cat;
		course.save();
		CourseSection section = new CourseSection(course, "introduction", "Introductory section");
		section.save();
	}
	
	public static void createCourseWithActivities() {
		CourseCategory cat = new CourseCategory("courses");
		cat.save();
		
		Course course = new Course("Play Framework", "Play framework course");
		course.category = cat;
		course.save();
		CourseSection section = new CourseSection(course, "introduction", "Introductory section");
		Activity activity = new Activity("Blog", "Please write a blog post");
		section.activities.add(activity);
		section.save();
	}

}
