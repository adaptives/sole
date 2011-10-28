package models;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class PrivateMessageTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
		Fixtures.load("data.yml");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCreateAndRetrieve() throws Exception {
		//Create
		List<SocialUser> users = SocialUser.findAll();
		SocialUser from = users.get(0);
		SocialUser to = users.get(1);
		String title = "message title";
		String msgBody = "message body";
		PrivateMessage m = new PrivateMessage(from, to, title, msgBody);
		m.save();
		
		//retrieve
		List<PrivateMessage> retrievedMessages = PrivateMessage.findAll();
		assertEquals(1, retrievedMessages.size());
		PrivateMessage retrievedMessage = retrievedMessages.get(0);
		assertEquals(from.id, retrievedMessage.from.id);
		assertEquals(title, retrievedMessage.subject);
		assertEquals(msgBody, retrievedMessage.body);
		assertNotNull(retrievedMessage.messageProperties);
		assertEquals(retrievedMessage.id, retrievedMessage.messageProperties.message.id);
		assertFalse(retrievedMessage.messageProperties.isRead);
	}
	
	@Test
	public void testCreateMultipleMessagesWithSameUsers() throws Exception {
		//Create
		List<SocialUser> users = SocialUser.findAll();
		SocialUser from = users.get(0);
		SocialUser to = users.get(1); 
		String title = "message title";
		String msgBody = "message body";
		
		PrivateMessage m1 = new PrivateMessage(from, to, title+"1", msgBody+"1");
		m1.save();
		
		PrivateMessage m2 = new PrivateMessage(from, to, title+"2", msgBody+"2");
		m2.save();
		
		//retrieve
		List<PrivateMessage> retrievedMessages = PrivateMessage.findAll();
		assertEquals(2, retrievedMessages.size());
		
		PrivateMessage retrievedMessage1 = retrievedMessages.get(0);
		assertEquals(from.id, retrievedMessage1.from.id);
		assertEquals(title+"1", retrievedMessage1.subject);
		assertEquals(msgBody+"1", retrievedMessage1.body);
		assertNotNull(retrievedMessage1.messageProperties);
		assertEquals(retrievedMessage1.id, retrievedMessage1.messageProperties.message.id);
		assertFalse(retrievedMessage1.messageProperties.isRead);
		
		PrivateMessage retrievedMessage2 = retrievedMessages.get(1);
		assertEquals(from.id, retrievedMessage2.from.id);
		assertEquals(title+"2", retrievedMessage2.subject);
		assertEquals(msgBody+"2", retrievedMessage2.body);
		assertNotNull(retrievedMessage2.messageProperties);
		assertEquals(retrievedMessage2.id, retrievedMessage2.messageProperties.message.id);
		assertFalse(retrievedMessage2.messageProperties.isRead);
	}

}
