package models;
import org.junit.*;
import java.util.*;

import play.test.*;
import models.*;

public class CourseSectionTest extends UnitTest {

	@Before
	public void setUp() {
		Fixtures.deleteAll();
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testInsertAndRetrieveCourseSection() {
		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
		javaCourse.save();
		
		CourseSection section1 = new CourseSection(javaCourse, "section 1", "description of section 1");
		section1.save();
		CourseSection section2 = new CourseSection(javaCourse, "section 2", "description of section 2");
		section2.save();
		CourseSection section3 = new CourseSection(javaCourse, "section 3", "description of section 3");
		section3.save();
		
		List<CourseSection> courseSections = CourseSection.findAll();
		assertEquals(3, courseSections.size());
		
		CourseSection retrievedSection1 = CourseSection.findBySanitizedTitleByCouse(javaCourse, "section-1");
		assertEquals(section1, retrievedSection1);
	}
	
	@Test
	public void testRetrieveCourseSectionsBYPlacement() throws Exception {
		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
		javaCourse = javaCourse.save();
		
		CourseSection section1 = new CourseSection(javaCourse, "section 1", "description of section 1");
		section1.placement = 1;
		section1.save();
		
		CourseSection section2 = new CourseSection(javaCourse, "section 2", "description of section 2");
		section2.placement = 0;
		section2.save();
		
		CourseSection section3 = new CourseSection(javaCourse, "section 3", "description of section 3");
		section3.placement = 2;
		section3.save();
		
		List<CourseSection> courseSections = javaCourse.fetchSectionsByPlacement();
		assertEquals(3, courseSections.size());
		
		assertEquals("section 2", courseSections.get(0).title);
	}
	
	@Test
	public void testActivities() throws Exception {
		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
		javaCourse = javaCourse.save();
		
		CourseSection section1 = new CourseSection(javaCourse, "section 1", "description of section 1");
		section1.placement = 1;
		section1.save();
		
		CourseSection section2 = new CourseSection(javaCourse, "section 2", "description of section 2");
		section2.placement = 0;
		section2.save();
		
		CourseSection section3 = new CourseSection(javaCourse, "section 3", "description of section 3");
		section3.placement = 2;
		section3.save();
		
		Activity activity = new Activity("Activity 1", "Content for activity 1");
		section1.activities.add(activity);
		section1.save();
		
		//verify
		Course retrievedCourse = Course.findById(javaCourse.id);
		List<CourseSection> retrievedCourseSections = retrievedCourse.fetchSectionsByPlacement(); 
		assertEquals(1, retrievedCourseSections.get(1).activities.size());
	}

}
