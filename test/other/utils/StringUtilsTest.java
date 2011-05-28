package other.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

public class StringUtilsTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testReplaceSpaceWithDashes() {
		String s1 = "This is a simple title";
		String expectedS1 = "this-is-a-simple-title";
		assertEquals(expectedS1, StringUtils.replaceSpaceWithDashes(s1));
		
		String s2 = "This is a simple string with 1 number";
		String expectedS2 = "this-is-a-simple-string-with-1-number";
		assertEquals(expectedS2, StringUtils.replaceSpaceWithDashes(s2));
		
		String s3 = "This is a simple string with 1 number and a non a'phanumeric. character";
		String expectedS3 = "this-is-a-simple-string-with-1-number-and-a-non-aphanumeric-character";
		assertEquals(expectedS3, StringUtils.replaceSpaceWithDashes(s3));
	}

}
