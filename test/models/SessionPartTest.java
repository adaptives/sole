package models;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class SessionPartTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSanitizedTitle() throws Exception {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd");
		
		//create StudySession1
		String expectedSanitizedTitle = "first-lession";
		
		Date course1StartDate = dateFormat.parse("2011-03-04");
		Date course1EndDate = dateFormat.parse("2011-03-14");
		StudySession studySession1 = 
			new StudySession("Javascript 101", 
							 "Javascript 101 description", 
							 course1StartDate, 
							 course1EndDate);
		studySession1.applicationText = "Please create a blog post";
		studySession1.save();
		
		SessionPart sessionPart = 
			new SessionPart("First lession", 
							course1StartDate, 
							course1EndDate, 
							"this is the content", 
							studySession1);
		sessionPart.save();
		Assert.assertEquals(expectedSanitizedTitle, sessionPart.sanitizedTitle);
		
		SessionPart retrievedSessionPart = SessionPart.findBySanitizedTitle(expectedSanitizedTitle);
		Assert.assertEquals(expectedSanitizedTitle, retrievedSessionPart.sanitizedTitle);
	}
	
	@Test(expected=Exception.class)
	public void testSanitizedTitleForUniqueness() throws Exception {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd");
		
		//create StudySession1
		String expectedSanitizedTitle = "first-lession";
		
		Date course1StartDate = dateFormat.parse("2011-03-04");
		Date course1EndDate = dateFormat.parse("2011-03-14");
		StudySession studySession1 = 
			new StudySession("Javascript 101", 
							 "Javascript 101 description", 
							 course1StartDate, 
							 course1EndDate);
		studySession1.applicationText = "Please create a blog post";
		studySession1.save();
		
		SessionPart sessionPart1 = 
			new SessionPart("First lession", 
							course1StartDate, 
							course1EndDate, 
							"this is the content", 
							studySession1);
		sessionPart1.save();
		
		SessionPart sessionPart2 = 
			new SessionPart("First lession", 
							course1StartDate, 
							course1EndDate, 
							"this is the content for the second lesson", 
							studySession1);		
		sessionPart2.save();
		
	}

}
