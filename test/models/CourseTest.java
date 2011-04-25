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
	}
}
