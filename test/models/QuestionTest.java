package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import other.utils.InitUtils;

import play.jobs.Job;
import play.test.Fixtures;
import play.test.UnitTest;


public class QuestionTest extends UnitTest {
	@Before
	public void setUp() {
		Fixtures.deleteAll();
		InitUtils.initData();
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testAddQuestion() throws Exception {
		List<SocialUser> users = SocialUser.findAll();
		SocialUser user = users.get(0);
		assertNotNull(user);
		
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
	public void testFetchLatestRevision() throws Exception {
		List<SocialUser> users = SocialUser.findAll();
		SocialUser user = users.get(0);
		assertNotNull(user);
		
		Question question = new Question("New question", "New question for test", user);
		question.save();
		assertEquals("New question for test", question.fetchLatestRevision());
		
		QuestionRevision questionRevision = new QuestionRevision("testing", "new content", user, question);
		questionRevision.save();
		
		assertEquals("new content", question.fetchLatestRevision());
	}
	
	@Test
	public void testFetchLatestTags() throws Exception {
		List<SocialUser> users = SocialUser.findAll();
		SocialUser user = users.get(0);
		assertNotNull(user);
		
		Question question = new Question("New question", "New question for test", user);
		question.save();
		assertEquals("New question for test", question.fetchLatestRevision());
		
		QuestionRevision questionRevision = new QuestionRevision("testing", "new content", user, question);
		List<String> newTags = new ArrayList<String>();
		newTags.add("new_tag1");
		newTags.add("new_tag2");
		for(String newTag : newTags) {
			questionRevision.tagWith(newTag);
		}		
		questionRevision.save();
		
		Set<Tag> retreivedTags = question.fetchLatestTags();
		assertEquals(2, retreivedTags.size());
		for(Tag tag : retreivedTags) {
			assertTrue(newTags.contains(tag.name));
			newTags.remove(tag.name);
		}
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
