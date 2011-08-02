package models;
import org.junit.*;

import java.util.*;

import play.test.*;
import models.*;

public class CourseTest extends UnitTest {

	@Before
	public void setUp() {
		Fixtures.deleteAll();		
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
		Fixtures.load("users-and-study-sessions.yml");
		Fixtures.load("diycourses.yml");
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
	public void testActivities() throws Exception {
		//create
		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
		javaCourse.save();
		Activity activity = new Activity("Activity 1", "Content for activity 1");
		javaCourse.activities.add(activity);
		javaCourse.save();
		
		//verify
		Course retrievedCourse = Course.findById(javaCourse.id); 
		assertEquals(1, retrievedCourse.activities.size());
	}
}
