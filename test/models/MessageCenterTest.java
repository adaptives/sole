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

public class MessageCenterTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
		Fixtures.load("data.yml");
	}

	@After
	public void tearDown() throws Exception {
	
	}
	
	@Test
	public void testCreateAndRetrieve() {
		//create
		List<SocialUser> socialUsers = SocialUser.findAll();
		SocialUser user1 = socialUsers.get(0);
		MessageCenter messageCenter = new MessageCenter(user1);
		messageCenter.save();
		
		//retrieve
		List<MessageCenter> retrievedMessageCenters = MessageCenter.findAll();
		assertEquals(1, retrievedMessageCenters.size());
		MessageCenter retrievedMessageCenter = retrievedMessageCenters.get(0);
		assertEquals(user1.id, retrievedMessageCenter.owner.id);
		assertNotNull(retrievedMessageCenter.inbox);
	}
	
	@Test
	public void testFindByUserId() {
		//create
		List<SocialUser> socialUsers = SocialUser.findAll();
		SocialUser user1 = socialUsers.get(0);
		SocialUser user2 = socialUsers.get(1);
		
		MessageCenter messageCenter1 = new MessageCenter(user1);
		messageCenter1.save();
		
		MessageCenter messageCenter2 = new MessageCenter(user2);
		messageCenter2.save();
		
		//retrieve
		MessageCenter retrievedMessageCenter = MessageCenter.findByUserId(user2.id);
		assertEquals(user2.id, retrievedMessageCenter.owner.id);
	}
	
//	@Test
//	public void testFindInboxMessage() throws Exception {
//		//create
//		List<SocialUser> socialUsers = SocialUser.findAll();
//		SocialUser user1 = socialUsers.get(0);
//		SocialUser user2 = socialUsers.get(1);
//		
//		MessageCenter messageCenter1 = new MessageCenter(user1);
//		messageCenter1.save();
//		
//		MessageCenter messageCenter2 = new MessageCenter(user2);
//		messageCenter2.save();
//		
//		PrivateMessage message1 = createTestMessage(user1, user2);
//		message1.save();
//		PrivateMessage message2 = createTestMessage(user2, user1);
//		message2.save();
//		MessageCenter.sendMessage(message1);
//		MessageCenter.sendMessage(message2);
//		
//		//verify
//		assertNull(messageCenter1.findInboxMessage(message1.id));
//		assertNotNull(messageCenter1.findInboxMessage(message2.id));
//		
//		assertNull(messageCenter2.findInboxMessage(message2.id));
//		assertNotNull(messageCenter2.findInboxMessage(message1.id));
//	}

	private PrivateMessage createTestMessage(SocialUser fromUser, SocialUser toUser) {		
		PrivateMessage message = new PrivateMessage(fromUser, toUser, "test title", "test message");
		return message;
	}
}