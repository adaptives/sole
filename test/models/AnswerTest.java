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

import other.utils.InitUtils;

import controllers.Security;

import play.test.Fixtures;
import play.test.UnitTest;


public class AnswerTest extends UnitTest {
	@Before
	public void setUp() {
		Fixtures.deleteAll();
		InitUtils.initData();
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testAddAnswers() {
		SocialUser user = SocialUser.find("byEmail", "someone@somewhere.com").first();
		Question question = new Question("New question", "New question for test", user);
		question.save();
		String answerContent = "this may not work because we are giving the question to the answer";
		Date expectedAnsweredAt = new Date();
		Answer answer = new Answer(answerContent, user, question);
		question.answers.add(answer);
		question.save();
		
		List<Question> retrievedQuestions = Question.findAll();
		Assert.assertEquals(1, retrievedQuestions.size());
		Question retrievedQuestion = retrievedQuestions.get(0);
		
		assertEquals(1, retrievedQuestion.answers.size());
		//It's ok to put the same assertEquals in the entire loop, since we are expecting only one answer
		for(Answer retrievedAnswer : question.answers) {
			assertEquals(answerContent, retrievedAnswer.content);
			assertEquals(expectedAnsweredAt.getTime(), retrievedAnswer.answeredAt.getTime(), 10);
			assertEquals(user, retrievedAnswer.author);
			assertEquals(question, retrievedAnswer.question);
		}
		
	}
	
	@Test
	public void testLatestAnswer() throws Exception {		
		List<SocialUser> socialUsers = SocialUser.findAll();
		SocialUser questionAuthor = socialUsers.get(0);
		Question question = new Question("title", "content", questionAuthor);
	    question.save();
	    Answer answer = new Answer("content 1", questionAuthor, question);
	    assertEquals("content 1", answer.getLatestRevision());
	    AnswerRevision answerRevision = new AnswerRevision("", "content 2", questionAuthor, answer);
	    answerRevision.save();	    
	    answer.save();
	    assertEquals("content 2", answer.getLatestRevision());
	}
}
