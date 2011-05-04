package models;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class StudySessionEventTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
		Fixtures.load("data.yml");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testStudySessionEventTail() throws Exception {
		List<StudySession> studySessions = StudySession.findAll();
		StudySession studySession = studySessions.get(0);
		
		List<StudySessionEvent> tailEvents = StudySessionEvent.tail(studySession.id, 1, 40);
		assertNotNull(tailEvents);
		assertEquals(0, tailEvents.size());
		
		//add a StudySession event and test
		StudySessionEvent sse1 = new StudySessionEvent(studySession, "event title", "event 1");
		tailEvents = StudySessionEvent.tail(studySession.id, 1, 40);
		assertNotNull(tailEvents);
		assertEquals(1, tailEvents.size());
		StudySessionEvent retrievedEvent = tailEvents.get(0);
		assertEquals("event title", retrievedEvent.title);
		assertEquals("event 1", retrievedEvent.text);
		
		tailEvents = StudySessionEvent.tail(studySession.id, 2, 40);
		assertEquals(0, tailEvents.size());
		
		//add two more event and test
		StudySessionEvent sse2 = new StudySessionEvent(studySession, "event title", "event 2");
		StudySessionEvent sse3 = new StudySessionEvent(studySession, "event title", "event 3");
		tailEvents = StudySessionEvent.tail(studySession.id, 1, 40);
		assertNotNull(tailEvents);
		assertEquals(3, tailEvents.size());
		
		//add events so there are more than 40 and test again
		for(int i = 0; i < 50 ; i++) {
			StudySessionEvent sse = new StudySessionEvent(studySession, "event title", "more events " + i);
		}
		tailEvents = StudySessionEvent.tail(studySession.id, 1, 40);
		assertNotNull(tailEvents);
		assertEquals(40, tailEvents.size());
		tailEvents = StudySessionEvent.tail(studySession.id, 2, 40);
		assertTrue(tailEvents.size() > 0);
		tailEvents = StudySessionEvent.tail(studySession.id, 3, 40);
		assertEquals(0, tailEvents.size());
	}

}
