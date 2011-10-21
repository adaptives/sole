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
		
		PrivateMessage m1 = new PrivateMessage(from, to, title, msgBody);
		m1.save();
		
		PrivateMessage m2 = new PrivateMessage(from, to, title, msgBody);
		m2.save();
	}

}