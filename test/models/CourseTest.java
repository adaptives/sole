package models;
import org.junit.*;

import other.utils.InitUtils;

import java.util.*;

import play.test.*;
import models.*;

public class CourseTest extends UnitTest {

	@Before
	public void setUp() {
		Fixtures.deleteAll();
		InitUtils.initData();
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testInsertAndRetrieve() {
		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
		javaCourse.save();
		Course javascriptCourse = new Course("Introduction to Javascript", "Javascript Programming Description"); 
		javascriptCourse.save();
		Course pythonCourse = new Course("Introduction to Python", "Python Programming Description"); 
		pythonCourse.save();
		
		List<Course> courses = Course.findAll();
		assertEquals(3, courses.size());
		
		Course retrievedJavaCourse = Course.find("byTitle", "Introduction to Java").first();
		assertEquals(javaCourse, retrievedJavaCourse);
		assertEquals("introduction-to-java", javaCourse.sanitizedTitle);
	}
	
	@Test
	public void testAddQuestion() throws Exception {
		//create Course
		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
		javaCourse.save();
		
		SocialUser user = (SocialUser)SocialUser.findAll().get(0);
		
		Question question = new Question("Question title", "Question content", user);
		javaCourse.forum.questions.add(question);
		javaCourse.save();
		
		Course retrievedCourse = Course.findById(javaCourse.id);
		assertEquals(1, retrievedCourse.forum.questions.size());
	}
	
	@Test
	public void testGetQuestionsAskedBySocialUser() throws Exception {
		
		List<SocialUser> socialUsers = SocialUser.findAll();
		
		//create Course
		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
		javaCourse.save();
		
		Question javaQuestion1 = new Question("title 1", "content 1", socialUsers.get(0));
		javaCourse.forum.questions.add(javaQuestion1);
		javaCourse.save();
		
		assertEquals(1, javaCourse.getQuestionsAskedBySocialUser(socialUsers.get(0).id).size());
		assertEquals(0, javaCourse.getQuestionsAskedBySocialUser(socialUsers.get(1).id).size());
	}
	
	@Test
	public void testGetAnswersGivenBySocialUser() throws Exception {
		
		List<SocialUser> socialUsers = SocialUser.findAll();
		
		//create Course
		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
		javaCourse.save();
		
		Question javaQuestion1 = new Question("title 1", "content 1", socialUsers.get(0));
		javaCourse.forum.questions.add(javaQuestion1);
		javaCourse.save();
		
		Answer javaQuestion1Answer1 = new Answer("this is the answer", socialUsers.get(0), javaQuestion1);
		javaQuestion1.answers.add(javaQuestion1Answer1);
		javaQuestion1.save();
		
		assertEquals(1, javaCourse.getAnswersGivenBySocialUser(socialUsers.get(0).id).size());
		assertEquals(0, javaCourse.getAnswersGivenBySocialUser(socialUsers.get(1).id).size());
	}
	
	
	
//	@Test
//	public void testHasCompleted() throws Exception {
//		Fixtures.load("users-and-study-sessions.yml");
//		
//		List<SocialUser> socialUsers = SocialUser.findAll();
//		
//		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
//		javaCourse.save();
//		
//		CourseSection cs1 = new CourseSection(javaCourse, "section 1", "section 1 content");
//		Activity cs1a1 = new Activity("Activity 1", "Content for activity 1");
//		Activity cs1a2 = new Activity("Activity 2", "Content for activity 2");		
//		Activity cs1a3 = new Activity("Activity 3", "Content for activity 3");
//		cs1.activities.add(cs1a1);
//		cs1.activities.add(cs1a2);
//		cs1.activities.add(cs1a3);
//		cs1.save();
//		
//		CourseSection cs2 = new CourseSection(javaCourse, "section 2", "section 2 content");		
//		Activity cs2a1 = new Activity("Activity 4", "Content for activity 4");
//		cs2.activities.add(cs2a1);
//		cs2.save();
//		
//		CourseSection cs3 = new CourseSection(javaCourse, "section 3", "section 3 content");
//		cs3.save();
//		
//		assertFalse(javaCourse.hasCompleted(String.valueOf(socialUsers.get(0))));
//	}
}
