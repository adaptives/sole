package models;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import models.Answer;
import models.Question;
import models.SocialUser;
import models.User;

import org.apache.ivy.core.search.RevisionEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controllers.Security;

import play.test.Fixtures;
import play.test.UnitTest;


public class QuestionTest extends UnitTest {
	@Before
	public void setUp() {
		Fixtures.deleteAll();
		Fixtures.load("users-and-study-sessions.yml");
		Fixtures.load("pages.yml");
		Fixtures.load("diycourses.yml");
		Fixtures.load("kvdata.yml");
		Fixtures.load("site-events.yml");
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testAddQuestion() throws Exception {
		SocialUser user = SocialUser.find("byEmail", "someone@somewhere.com").first();
		Date expectedAskedAt = new Date();
		Question question = new Question("New question", "New question for test", user);
		question.save();
		
		List<Question> retrievedQuestions = Question.findAll();
		Assert.assertEquals(1, retrievedQuestions.size());
		Question retrievedQuestion = retrievedQuestions.get(0);
		Assert.assertEquals("New question", retrievedQuestion.title);
		Assert.assertEquals("new-question", retrievedQuestion.sanitizedTitle);
		Assert.assertEquals("New question for test", retrievedQuestion.content);
		Assert.assertEquals(user, retrievedQuestion.author);
		Assert.assertEquals(0, retrievedQuestion.answers.size());
		Assert.assertEquals(expectedAskedAt.getTime(), retrievedQuestion.askedAt.getTime(), 10);
	}
	
	@Test
	public void testGetLatestRevision() throws Exception {
		SocialUser user = SocialUser.find("byEmail", "someone@somewhere.com").first();
		Question question = new Question("New question", "New question for test", user);
		question.save();
		assertEquals("New question for test", question.getLatestRevision());
		
		QuestionRevision questionRevision = new QuestionRevision("testing", "new content", user, question);
		questionRevision.save();
		question.latestRevision = questionRevision;
		question.save();
		assertEquals("new content", question.getLatestRevision());
	}
	
	@Test
	public void testCanEdit() throws Exception {
		List<SocialUser> users = SocialUser.findAll();
		SocialUser user = users.get(0);
		SocialUser user1 = users.get(1);
		
		Question question = new Question("New question", "New question for test", user);
		question.save();
		
		assertTrue(question.canEdit(String.valueOf(user.id)));
		assertFalse(question.canEdit(String.valueOf(user1.id)));
	}
}
