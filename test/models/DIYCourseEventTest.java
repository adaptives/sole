package models;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class DIYCourseEventTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
		Fixtures.load("users-and-study-sessions.yml");
		Fixtures.load("diycourses.yml");
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testBuildFromQuestion() throws Exception {
		//create Course
		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
		javaCourse.save();
		
		SocialUser user = (SocialUser)SocialUser.findAll().get(0);
		
		Question question = new Question("Question title", "Question content", user);
		javaCourse.forum.questions.add(question);
		javaCourse.save();
		
		DIYCourseEvent event = DIYCourseEvent.buildFromQuestion(javaCourse, user, question);
		String expectedEventString = 
			"<span id=\"%s\" class=\"user-image-small\"></span><a href=\"/users/%d\">testuser</a> asked a new question '<a href=\"/courses/course/introduction-to-java/question/%s\">Question title</a>'";
		assertEquals(String.format(expectedEventString, user.id, user.id, question.id), event.render());
		
	}
	
	@Test
	public void testBuildFromAnswer() throws Exception {
		//create Course
		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
		javaCourse.save();
		
		SocialUser user0 = (SocialUser)SocialUser.findAll().get(0);
		SocialUser user1 = (SocialUser)SocialUser.findAll().get(1);
		
		Question question = new Question("Question title", "Question content", user0);
		javaCourse.forum.questions.add(question);
		javaCourse.save();
		
		Answer answer = new Answer("This is my answer", user1, question);
		question.answers.add(answer);
		question.save();
		
		DIYCourseEvent event = DIYCourseEvent.buildFromAnswer(javaCourse, user1, answer);
		
		String expectedEventString = 
			"<span id=\"%s\" class=\"user-image-small\"></span><a href=\"/users/%s\">anothertestuser</a> provided an answer for question '<a href=\"/courses/course/introduction-to-java/question/%s\">Question title</a>'";
		assertEquals(String.format(expectedEventString, user1.id, user1.id, question.id), event.render());
		
	}
	
	@Test
	public void testBuildFromCourseActivityResponse() throws Exception {
		//create Course
		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
		javaCourse.save();
		
		SocialUser user0 = (SocialUser)SocialUser.findAll().get(0);
		SocialUser user1 = (SocialUser)SocialUser.findAll().get(1);
		
		Activity activity = new Activity("activity title", "activity content");
		javaCourse.activities.add(activity);
		javaCourse.save();
		
		ActivityResponse activityResponse = new ActivityResponse(user1, activity, "http://diycomputerscience.com", "Peer Learning Environemnt for Computer Science");
		
		DIYCourseEvent event = DIYCourseEvent.buildFromActivityResponse(user1, activityResponse);
		
		String expectedEventString = 
			"<span id=\"%s\" class=\"user-image-small\"></span><a href=\"/users/%s\">anothertestuser</a> submitted an activity response for activity '<a href=\"/courses/course/introduction-to-java/activity/responses#%s\">activity title</a>'";
		assertEquals(String.format(expectedEventString, user1.id, user1.id, activity.id), event.render());
		
	}
	
	@Test
	public void testBuildFromCourseSectionActivityResponse() throws Exception {
		//create Course
		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
		javaCourse.save();
		CourseSection section1 = new CourseSection(javaCourse, "section 1", "description of section 1");
		section1.save();
		
		SocialUser user0 = (SocialUser)SocialUser.findAll().get(0);
		SocialUser user1 = (SocialUser)SocialUser.findAll().get(1);
		
		Activity activity = new Activity("activity title", "activity content");
		section1.activities.add(activity);
		section1.save();
		
		ActivityResponse activityResponse = new ActivityResponse(user1, activity, "http://diycomputerscience.com", "Peer Learning Environemnt for Computer Science");
		
		DIYCourseEvent event = DIYCourseEvent.buildFromActivityResponse(user1, activityResponse);
		
		String expectedEventString = 
			"<span id=\"%s\" class=\"user-image-small\"></span><a href=\"/users/%s\">anothertestuser</a> submitted an activity response for activity '<a href=\"/courses/course/introduction-to-java/section/section-1/activity/responses#%s\">activity title</a>'";
		assertEquals(String.format(expectedEventString, user1.id, user1.id, activity.id), event.render());
		
	}

}
