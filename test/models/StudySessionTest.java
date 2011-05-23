package models;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.SocialUser;
import models.StudySession;
import models.StudySessionApplication;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;


public class StudySessionTest extends UnitTest {
	
	@Before
	public void setUp() {
		Fixtures.deleteAll();
		Fixtures.load("data.yml");
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testCreateAndRetrieve() throws Exception {
		//create a StudySession
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd");
		Date courseStartDate = dateFormat.parse("2011-03-04");
		Date courseEndDate = dateFormat.parse("2011-03-14");
		StudySession studySession = 
			new StudySession("Javascript101", 
							 "Javascript 101 description", 
							 courseStartDate, 
							 courseEndDate);
		studySession.applicationText = "Please create a blog post";
		studySession.save();
		
		//test retrieval
		List<StudySession> retrievedStudySessions = StudySession.findAll();
		assertEquals(2, retrievedStudySessions.size());
		StudySession retrievedStudySession = retrievedStudySessions.get(1);
		assertNotNull(retrievedStudySession);
		assertNotNull(retrievedStudySession.forum);
		assertEquals(retrievedStudySession.applicationText, "Please create a blog post");		
	}
	
	@Test
	public void testCreateAndRetrieveWithDependencies() throws Exception {
		//create a StudySession
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd");
		Date courseStartDate = dateFormat.parse("2011-03-04");
		Date courseEndDate = dateFormat.parse("2011-03-14");
		StudySession studySession = 
			new StudySession("Javascript101",
							 "Javascript 101 description", 
							 courseStartDate,
							 courseEndDate);
		studySession.applicationText = "Please create a blog post";
		SocialUser aFacilitator = SocialUser.find("select su from SocialUser su where su.screenname = ?", "testuser").first();
		studySession.facilitators.add(aFacilitator);
		
		SocialUser student = SocialUser.find("select su from SocialUser su where su.screenname = ?", "anothertestuser").first();
		SocialUser student1 = SocialUser.find("select su from SocialUser su where su.screenname = ?", "learner").first();
		SocialUser student2 = SocialUser.find("select su from SocialUser su where su.screenname = ?", "learner1").first();
		studySession.addApplication(student, "I like this course");
		studySession.addApplication(student1, "I like this course 1");
		studySession.addApplication(student2, "I like this course 2");
		studySession.save();
		
		//test retrieval
		List<StudySession> retrievedStudySessions = StudySession.findAll();
		assertEquals(2, retrievedStudySessions.size());
		StudySession retrievedStudySession = retrievedStudySessions.get(1);
		assertNotNull(retrievedStudySession);
		assertNotNull(retrievedStudySession.applicationStore);
		assertEquals(1, retrievedStudySession.facilitators.size());
		assertFalse(retrievedStudySession.isUserEnrolled(String.valueOf(student1.id)));
		assertFalse(retrievedStudySession.canEnroll(student1.id));
		assertEquals(3, retrievedStudySession.getPendingApplicants().size());
		
//		studySession.applicationStore.deregister(student.id);
//		studySession.applicationStore.save();
//		assertFalse(retrievedStudySession.isUserEnrolled(String.valueOf(student.id)));
//		assertTrue(retrievedStudySession.canEnroll(student.id));
//		assertEquals(2, retrievedStudySession.getPendingApplicants().size());
//		assertEquals(1, retrievedStudySession.applicationStore.deregisteredApplications.size());
	}
	
	@Test
	public void testDeregistrationOfPendingApplication() throws Exception {
		//create a StudySession
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd");
		Date courseStartDate = dateFormat.parse("2011-03-04");
		Date courseEndDate = dateFormat.parse("2011-03-14");
		StudySession studySession = 
			new StudySession("Javascript101",
							 "Javascript 101 description", 
							 courseStartDate,
							 courseEndDate);
		studySession.applicationText = "Please create a blog post";
		SocialUser aFacilitator = SocialUser.find("select su from SocialUser su where su.screenname = ?", "testuser").first();
		studySession.facilitators.add(aFacilitator);
		
		SocialUser student = SocialUser.find("select su from SocialUser su where su.screenname = ?", "anothertestuser").first();
		SocialUser student1 = SocialUser.find("select su from SocialUser su where su.screenname = ?", "learner").first();
		SocialUser student2 = SocialUser.find("select su from SocialUser su where su.screenname = ?", "learner1").first();
		studySession.addApplication(student, "I like this course");
		studySession.addApplication(student1, "I like this course 1");
		studySession.addApplication(student2, "I like this course 2");
		studySession.save();
		
		List<StudySession> retrievedStudySessions = StudySession.findAll();
		StudySession retrievedStudySession = retrievedStudySessions.get(1);
		
		//test deregistration
		retrievedStudySession.applicationStore.deregister(student.id, "cannot dedicate time to this course");
		//Now there should be 1 application for the user which has a status of deregistered
		List<StudySessionApplication> studySessionApplications = StudySessionApplication.find("select a from StudySessionApplication a where a.socialUser.id = ?", student.id).fetch();
		assertEquals(1, studySessionApplications.size());
		StudySessionApplication deregisteredApp = studySessionApplications.get(0);
		assertEquals(-2, deregisteredApp.currentStatus);
		assertTrue(retrievedStudySession.canEnroll(student.id));
		assertTrue(retrievedStudySession.canEnroll(String.valueOf(student.id)));
		assertFalse(retrievedStudySession.isUserEnrolled(String.valueOf(student.id)));
	}
	
	@Test
	public void testDeregistrationOfAcceptedApplication() throws Exception {
		//create a StudySession
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd");
		Date courseStartDate = dateFormat.parse("2011-03-04");
		Date courseEndDate = dateFormat.parse("2011-03-14");
		StudySession studySession = 
			new StudySession("Javascript101",
							 "Javascript 101 description", 
							 courseStartDate,
							 courseEndDate);
		studySession.applicationText = "Please create a blog post";
		SocialUser aFacilitator = SocialUser.find("select su from SocialUser su where su.screenname = ?", "testuser").first();
		studySession.facilitators.add(aFacilitator);
		
		SocialUser student = SocialUser.find("select su from SocialUser su where su.screenname = ?", "anothertestuser").first();
		SocialUser student1 = SocialUser.find("select su from SocialUser su where su.screenname = ?", "learner").first();
		SocialUser student2 = SocialUser.find("select su from SocialUser su where su.screenname = ?", "learner1").first();
		studySession.addApplication(student, "I like this course");
		studySession.addApplication(student1, "I like this course 1");
		studySession.addApplication(student2, "I like this course 2");
		studySession.save();
		
		List<StudySession> retrievedStudySessions = StudySession.findAll();
		StudySession retrievedStudySession = retrievedStudySessions.get(1);
		
		//Accept the application of student
		retrievedStudySession.acceptApplication(student.id, "Good application");
		assertTrue(retrievedStudySession.isUserEnrolled(student.id));
		
		retrievedStudySession.deregister(student.id, "deregistering");
		assertTrue(retrievedStudySession.canEnroll(student.id));
	}
	
	@Test
	public void testRetrieveYetToStartStudySessions() throws Exception {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd");
		
		//create StudySession1
		Date course1StartDate = dateFormat.parse("2011-03-04");
		Date course1EndDate = dateFormat.parse("2011-03-14");
		StudySession studySession1 = 
			new StudySession("Javascript101", 
							 "Javascript 101 description", 
							 course1StartDate, 
							 course1EndDate);
		studySession1.applicationText = "Please create a blog post";
		studySession1.save();
		
		//create StudySession2
		Date course2StartDate = dateFormat.parse("2011-03-08");
		Date course2EndDate = dateFormat.parse("2011-03-24");
		StudySession studySession2 = 
			new StudySession("Javascript201", 
							 "Javascript 201 description", 
							 course2StartDate, 
							 course2EndDate);
		studySession2.applicationText = "Please create a blog post";
		studySession2.save();
		
		//create StudySession3
		Date course3StartDate = dateFormat.parse("2011-03-28");
		Date course3EndDate = dateFormat.parse("2011-04-24");
		StudySession studySession3 = 
			new StudySession("Javascript301", 
							 "Javascript 301 description", 
							 course3StartDate, 
							 course3EndDate);
		studySession3.applicationText = "Please create a blog post";
		studySession3.save();
		
		//create StudySession4
		Date course4StartDate = dateFormat.parse("2011-04-28");
		Date course4EndDate = dateFormat.parse("2011-05-24");
		StudySession studySession4 = 
			new StudySession("Javascript401", 
							 "Javascript 401 description", 
							 course4StartDate, 
							 course4EndDate);
		studySession4.applicationText = "Please create a blog post";
		studySession4.save();
		
		List<StudySession> yetToStart1 = StudySession.getYetToStart(dateFormat.parse("2011-01-01"));
		assertEquals(5, yetToStart1.size());
		
		List<StudySession> yetToStart2 = StudySession.getYetToStart(dateFormat.parse("2011-03-10"));
		assertEquals(3, yetToStart2.size());
		
		List<StudySession> yetToStart3 = StudySession.getYetToStart(dateFormat.parse("2011-03-28"));
		assertEquals(2, yetToStart3.size());
		
		List<StudySession> yetToStart4 = StudySession.getYetToStart(dateFormat.parse("2011-05-28"));
		assertEquals(0, yetToStart4.size());
	}
	
	@Test
	public void testRetrieveAlreadyStartedStudySessions() throws Exception {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd");
		
		//create StudySession1
		Date course1StartDate = dateFormat.parse("2011-03-04");
		Date course1EndDate = dateFormat.parse("2011-03-14");
		StudySession studySession1 = 
			new StudySession("Javascript101", 
							 "Javascript 101 description", 
							 course1StartDate, 
							 course1EndDate);
		studySession1.applicationText = "Please create a blog post";
		studySession1.save();
		
		//create StudySession2
		Date course2StartDate = dateFormat.parse("2011-03-08");
		Date course2EndDate = dateFormat.parse("2011-03-24");
		StudySession studySession2 = 
			new StudySession("Javascript201", 
							 "Javascript 201 description", 
							 course2StartDate, 
							 course2EndDate);
		studySession2.applicationText = "Please create a blog post";
		studySession2.save();
		
		//create StudySession3
		Date course3StartDate = dateFormat.parse("2011-03-28");
		Date course3EndDate = dateFormat.parse("2011-04-24");
		StudySession studySession3 = 
			new StudySession("Javascript301", 
							 "Javascript 301 description", 
							 course3StartDate, 
							 course3EndDate);
		studySession3.applicationText = "Please create a blog post";
		studySession3.save();
		
		//create StudySession4
		Date course4StartDate = dateFormat.parse("2011-04-28");
		Date course4EndDate = dateFormat.parse("2011-05-24");
		StudySession studySession4 = 
			new StudySession("Javascript401", 
							 "Javascript 401 description", 
							 course4StartDate, 
							 course4EndDate);
		studySession4.applicationText = "Please create a blog post";
		studySession4.save();
		
		List<StudySession> ongoing1 = StudySession.getOngoing(dateFormat.parse("2011-01-01"));
		assertEquals(0, ongoing1.size());
		
		List<StudySession> ongoing2 = StudySession.getOngoing(dateFormat.parse("2011-03-04"));
		assertEquals(1, ongoing2.size());
		
		List<StudySession> ongoing3 = StudySession.getOngoing(dateFormat.parse("2011-03-09"));
		assertEquals(2, ongoing3.size());
		
		List<StudySession> ongoing4 = StudySession.getOngoing(dateFormat.parse("2011-03-14"));
		assertEquals(2, ongoing4.size());
		
		List<StudySession> ongoing5 = StudySession.getOngoing(dateFormat.parse("2011-03-15"));
		assertEquals(1, ongoing5.size());
		
		List<StudySession> ongoing6 = StudySession.getOngoing(dateFormat.parse("2011-05-24"));
		assertEquals(1, ongoing6.size());
		
		List<StudySession> ongoing7 = StudySession.getOngoing(dateFormat.parse("2011-05-26"));
		assertEquals(0, ongoing7.size());
	}
	
	@Test
	public void testRetrieveOverStudySessions() throws Exception {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd");
		
		//create StudySession1
		Date course1StartDate = dateFormat.parse("2011-03-04");
		Date course1EndDate = dateFormat.parse("2011-03-14");
		StudySession studySession1 = 
			new StudySession("Javascript101", 
							 "Javascript 101 description", 
							 course1StartDate, 
							 course1EndDate);
		studySession1.applicationText = "Please create a blog post";
		studySession1.save();
		
		//create StudySession2
		Date course2StartDate = dateFormat.parse("2011-03-08");
		Date course2EndDate = dateFormat.parse("2011-03-24");
		StudySession studySession2 = 
			new StudySession("Javascript201", 
							 "Javascript 201 description", 
							 course2StartDate, 
							 course2EndDate);
		studySession2.applicationText = "Please create a blog post";
		studySession2.save();
		
		//create StudySession3
		Date course3StartDate = dateFormat.parse("2011-03-28");
		Date course3EndDate = dateFormat.parse("2011-04-24");
		StudySession studySession3 = 
			new StudySession("Javascript301", 
							 "Javascript 301 description", 
							 course3StartDate, 
							 course3EndDate);
		studySession3.applicationText = "Please create a blog post";
		studySession3.save();
		
		//create StudySession4
		Date course4StartDate = dateFormat.parse("2011-04-28");
		Date course4EndDate = dateFormat.parse("2011-05-24");
		StudySession studySession4 = 
			new StudySession("Javascript401", 
							 "Javascript 401 description", 
							 course4StartDate, 
							 course4EndDate);
		studySession4.applicationText = "Please create a blog post";
		studySession4.save();
		
		List<StudySession> over1 = StudySession.getOver(dateFormat.parse("2011-01-01"));
		assertEquals(0, over1.size());
		
//		List<StudySession> ongoing2 = StudySession.getOngoing(dateFormat.parse("2011-03-04"));
//		assertEquals(1, ongoing2.size());
//		
//		List<StudySession> ongoing3 = StudySession.getOngoing(dateFormat.parse("2011-03-09"));
//		assertEquals(2, ongoing3.size());
//		
//		List<StudySession> ongoing4 = StudySession.getOngoing(dateFormat.parse("2011-03-14"));
//		assertEquals(2, ongoing4.size());
//		
//		List<StudySession> ongoing5 = StudySession.getOngoing(dateFormat.parse("2011-03-15"));
//		assertEquals(1, ongoing5.size());
//		
//		List<StudySession> ongoing6 = StudySession.getOngoing(dateFormat.parse("2011-05-24"));
//		assertEquals(1, ongoing6.size());
//		
//		List<StudySession> ongoing7 = StudySession.getOngoing(dateFormat.parse("2011-05-26"));
//		assertEquals(0, ongoing7.size());
	}
	
	@Test
	public void testRetrieveTail() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd");
		
		//create StudySession1
		Date course1StartDate = dateFormat.parse("2011-03-04");
		Date course1EndDate = dateFormat.parse("2011-03-14");
		StudySession studySession1 = 
			new StudySession("Javascript101", 
							 "Javascript 101 description", 
							 course1StartDate, 
							 course1EndDate);
		studySession1.applicationText = "Please create a blog post";
		studySession1.save();
		
		//create StudySession2
		Date course2StartDate = dateFormat.parse("2011-03-08");
		Date course2EndDate = dateFormat.parse("2011-03-24");
		StudySession studySession2 = 
			new StudySession("Javascript201", 
							 "Javascript 201 description", 
							 course2StartDate, 
							 course2EndDate);
		studySession2.applicationText = "Please create a blog post";
		studySession2.save();
		
		//create StudySession3
		Date course3StartDate = dateFormat.parse("2011-03-28");
		Date course3EndDate = dateFormat.parse("2011-04-24");
		StudySession studySession3 = 
			new StudySession("Javascript301", 
							 "Javascript 301 description", 
							 course3StartDate, 
							 course3EndDate);
		studySession3.applicationText = "Please create a blog post";
		studySession3.save();
		
		//create StudySession4
		Date course4StartDate = dateFormat.parse("2011-04-28");
		Date course4EndDate = dateFormat.parse("2011-05-24");
		StudySession studySession4 = 
			new StudySession("Javascript401", 
							 "Javascript 401 description", 
							 course4StartDate, 
							 course4EndDate);
		studySession4.applicationText = "Please create a blog post";
		studySession4.save();
		
		//test retrieving tail
		List<StudySession> studySessionsZero = StudySession.tail(0);
		assertEquals(0, studySessionsZero.size());
		
		List<StudySession> studySessionsOne = StudySession.tail(1);
		assertEquals(1, studySessionsOne.size());
		assertEquals(dateFormat.parse("2011-04-28"), studySessionsOne.get(0).startDate);
		
		List<StudySession> studySessionsFive = StudySession.tail(5);
		assertEquals(5, studySessionsFive.size());
		assertEquals(dateFormat.parse("2011-04-28"), studySessionsFive.get(0).startDate);
		assertEquals(dateFormat.parseObject("2011-03-04"), studySessionsFive.get(4).startDate);
		
		List<StudySession> studySessionsFiveButAskedForMore = StudySession.tail(10);
		assertEquals(5, studySessionsFiveButAskedForMore.size());
		assertEquals(dateFormat.parse("2011-04-28"), studySessionsFiveButAskedForMore.get(0).startDate);
		assertEquals(dateFormat.parseObject("2011-03-04"), studySessionsFiveButAskedForMore.get(4).startDate);
	}
	
	@Test
	public void testGetAcceptedUsersCount() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd");
		
		//create StudySession1
		Date course1StartDate = dateFormat.parse("2011-06-04");
		Date course1EndDate = dateFormat.parse("2011-06-14");
		StudySession studySession1 = 
			new StudySession("Javascript101", 
							 "Javascript 101 description", 
							 course1StartDate, 
							 course1EndDate);
		studySession1.applicationText = "Please create a blog post";
		studySession1.save();
		
		List<SocialUser> applicants = SocialUser.findAll();
		assertNotNull(applicants);
		Assert.assertFalse(applicants.size() == 0);
		SocialUser applicant = applicants.get(0);
		studySession1.addApplication(applicant, "applying");
		studySession1.acceptApplication(applicant.id, "Accepted");
		
		assertEquals(1, studySession1.getAcceptedUsers().size());
		assertEquals(1, studySession1.getAcceptedUsersCount());
	}
}
