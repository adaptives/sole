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
		String expectedEventString = "<a href=\"/users/4\">testuser</a> asked a new question '<a href=\"/courses/course/introduction-to-java/question/1\">Question title</a>'";
		assertEquals(expectedEventString, event.render());
		
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
		
		String expectedEventString = "<a href=\"/users/8\">anothertestuser</a> provided an answer for question '<a href=\"/courses/course/introduction-to-java/question/2\">Question title</a>'";
		assertEquals(expectedEventString, event.render());
		
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
		
		DIYCourseEvent event = DIYCourseEvent.buildFromActivity(user1, activityResponse);
		
		String expectedEventString = "<a href=\"/users/11\">anothertestuser</a> submitted an activity response for activity '<a href=\"/courses/course/introduction-to-java/activity/responses\">activity title</a>'";
		assertEquals(expectedEventString, event.render());
		
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
		
		DIYCourseEvent event = DIYCourseEvent.buildFromActivity(user1, activityResponse);
		
		String expectedEventString = "<a href=\"/users/14\">anothertestuser</a> submitted an activity response for activity '<a href=\"/coursec/sectionactivityresponses?courseSectionSanitizedTitle=section-1&courseSanitizedTitle=introduction-to-java\">activity title</a>'";
		assertEquals(expectedEventString, event.render());
		
	}

}
