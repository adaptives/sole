import static org.junit.Assert.*;

import models.Answer;
import models.Question;
import models.SocialUser;
import models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controllers.Security;

import play.test.Fixtures;
import play.test.UnitTest;


public class QuestionAnswerTest extends UnitTest {
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
	public void testAddAnswers() {
		SocialUser user = SocialUser.find("byEmail", "someone@somewhere.com").first();
		Question question = new Question("New question", "New question for test", user);
		question.save();
		String answerContent = "this may not work because we are giving the question to the answer";
		Answer answer = new Answer(answerContent, user, question);
		question.answers.add(answer);
		question.save();
		assertEquals(1, question.answers.size());
		
		for(Answer retrievedAnswer : question.answers) {
			assertEquals(answerContent, retrievedAnswer.content);
		}
		
	}
}
