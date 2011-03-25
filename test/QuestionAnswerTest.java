import static org.junit.Assert.*;

import models.Answer;
import models.Question;
import models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;


public class QuestionAnswerTest extends UnitTest {
	@Before
	public void setUp() {
		Fixtures.deleteAll();
		Fixtures.load("initial-data.yml");
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testAddAnswers() {
		Question question = new Question("New question", "New question for test", User.findByEmail("learner@somewhere.com"));
		question.save();
		String answerContent = "this may not work because we are giving the question to the answer";
		Answer answer = new Answer(answerContent, question);
		question.answers.add(answer);
		question.save();
		assertEquals(1, question.answers.size());
		
		for(Answer retrievedAnswer : question.answers) {
			assertEquals(answerContent, retrievedAnswer.content);
		}
		
	}
}
