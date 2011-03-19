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
		
		CourseSection retrievedSection1 = CourseSection.find("byCourse", javaCourse).first();
		assertEquals(section1, retrievedSection1);
	}
	

}
