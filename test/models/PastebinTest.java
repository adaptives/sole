package models;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class PastebinTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
		Fixtures.load("users-and-study-sessions.yml");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCreateAndRetrieve() throws Exception {
		//create
		Pastebin pastebin = new Pastebin("test");
		pastebin.save();
		
		//retrieve
		List<Pastebin> pastebins = Pastebin.findAll();
		assertEquals(1, pastebins.size());
	}

	@Test
	public void testFindByName() throws Exception {
		//create
		String name = "test";
		Pastebin pastebin = new Pastebin(name);
		pastebin.save();
		
		//test
		Pastebin retrievedPastebin = Pastebin.findByName(name);
		assertEquals(name, retrievedPastebin.name);
	}
	
	@Test
	public void testFindSnippetByUser() throws Exception {
		//create
		String name = "test";
		Pastebin pastebin = new Pastebin(name);
		pastebin.save();
		
		List<SocialUser> users = SocialUser.findAll();
		SocialUser user1 = users.get(0);
		SocialUser user2 = users.get(1);
		
		CodeSnippet codeSnippet1 = new CodeSnippet(user1, pastebin, "test snippet 1", "print");
		codeSnippet1.save();
		
		CodeSnippet codeSnippet2 = new CodeSnippet(user1, pastebin, "test snippet 1", "print");
		codeSnippet2.save();
		
		CodeSnippet codeSnippet3 = new CodeSnippet(user2, pastebin, "test snippet 1", "print");
		codeSnippet3.save();
		
		//retrieve
		Pastebin retrievedPastebin = Pastebin.findByName(name);
		
		List<CodeSnippet> codeSnippets1 = retrievedPastebin.findSnippetsByUser(user1.id);
		assertEquals(2, codeSnippets1.size());
		
		List<CodeSnippet> codeSnippets2 = retrievedPastebin.findSnippetsByUser(user2.id);
		assertEquals(1, codeSnippets2.size());
	}
}
