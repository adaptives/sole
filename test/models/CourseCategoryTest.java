package models;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class CourseCategoryTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCreateAndRetrieve() {
		//create
		String name1 = "Programming Languages";
		CourseCategory cc1 = new CourseCategory(name1);
		cc1.save();
		String name2 = "Applied Computer Science";
		CourseCategory cc2 = new CourseCategory(name2);
		cc2.save();
		
		//retrieve
		List<CourseCategory> courseCategories = CourseCategory.findAll();
		assertEquals(2, courseCategories.size());
		assertEquals(name1, courseCategories.get(0).name);
		assertEquals(name2, courseCategories.get(1).name);
	}
	
	@Test
	public void testFindByName() {
		//create
		String name1 = "Programming Languages";
		CourseCategory cc1 = new CourseCategory(name1);
		cc1.save();
		String name2 = "Applied Computer Science";
		CourseCategory cc2 = new CourseCategory(name2);
		cc2.save();
		
		//retrieve
		CourseCategory plcc = CourseCategory.findByName(name1);
		assertEquals(name1, plcc.name);
		
		CourseCategory acscc = CourseCategory.findByName(name2);
		assertEquals(name2, acscc.name);
	}

	@Test
	public void testFindByOrderedPlacement() {
		//create
		String name1 = "Programming Languages";
		CourseCategory cc1 = new CourseCategory(name1);
		cc1.placement = 1;
		cc1.save();
		String name2 = "Applied Computer Science";
		CourseCategory cc2 = new CourseCategory(name2);
		cc2.placement = 0;
		cc2.save();
		
		//retrieve
		List<CourseCategory> courseCategories = CourseCategory.findByOrderedPlacement();
		assertEquals(2, courseCategories.size());
		assertEquals(name2, courseCategories.get(0).name);
		assertEquals(name1, courseCategories.get(1).name);
	}
	
}
