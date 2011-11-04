package models;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class CourseGroupTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCreateAndRetrieve() throws Exception {
		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
		javaCourse.save();
		CourseGroup javaGroup1 = new CourseGroup("Java grp 2011-11", javaCourse);
		javaGroup1.save();
		
		Course javascriptCourse = new Course("Introduction to Javascript", "Javascript Programming Description"); 
		javascriptCourse.save();
		CourseGroup javascriptGroup1 = new CourseGroup("Javascript grp 2011-11", javascriptCourse);
		javascriptGroup1.save();
		
		List<CourseGroup> courseGroups = CourseGroup.findAll();
		assertEquals(2, courseGroups.size());
	}
	
	@Test
	public void testFindGroupsForCourse() throws Exception {
		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
		javaCourse.save();
		CourseGroup javaGroup1 = new CourseGroup("Java grp 2011-10", javaCourse);
		javaGroup1.save();
		CourseGroup javaGroup2 = new CourseGroup("Java grp 2011-11", javaCourse);
		javaGroup2.save();
		CourseGroup javaGroup3 = new CourseGroup("Java grp 2011-12", javaCourse);
		javaGroup3.save();
		
		Course javascriptCourse = new Course("Introduction to Javascript", "Javascript Programming Description"); 
		javascriptCourse.save();
		CourseGroup javascriptGroup1 = new CourseGroup("Javascript grp 2011-11", javascriptCourse);
		javascriptGroup1.save();
		
		List<CourseGroup> javaCourseGroups = CourseGroup.findGroupsForCourse(javaCourse.id);
		assertEquals(3, javaCourseGroups.size());
		
		List<CourseGroup> javascriptCourseGroups = CourseGroup.findGroupsForCourse(javascriptCourse.id);
		assertEquals(1, javascriptCourseGroups.size());		
	}
	
	@Test
	public void testFindByTitle() throws Exception {
		Course javaCourse = new Course("Introduction to Java", "Java Programming Description"); 
		javaCourse.save();
		CourseGroup javaGroup1 = new CourseGroup("Java grp 2011-10", javaCourse);
		javaGroup1.save();
		CourseGroup javaGroup2 = new CourseGroup("Java grp 2011-11", javaCourse);
		javaGroup2.save();
		CourseGroup javaGroup3 = new CourseGroup("Java grp 2011-12", javaCourse);
		javaGroup3.save();
		
		Course javascriptCourse = new Course("Introduction to Javascript", "Javascript Programming Description"); 
		javascriptCourse.save();
		CourseGroup javascriptGroup1 = new CourseGroup("Javascript grp 2011-11", javascriptCourse);
		javascriptGroup1.save();
		
		CourseGroup retrievedJavaGroup1 = CourseGroup.findBySanitizedTitle("Java grp 2011-10");
		assertEquals(javaGroup1.id, retrievedJavaGroup1.id);
		
		CourseGroup nonexistentGroup = CourseGroup.findBySanitizedTitle("");
		assertNull(nonexistentGroup);
	}

}
