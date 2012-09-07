package models;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import other.utils.InitUtils;

import play.test.Fixtures;
import play.test.UnitTest;

public class CodeSnippetTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
		InitUtils.initData();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCreateAndRetrieve() throws Exception {
		//create
		List<SocialUser> users = SocialUser.findAll();
		SocialUser user = users.get(0);
		Pastebin pastebin = new Pastebin("test");
		pastebin.save();
		CodeSnippet codeSnippet = new CodeSnippet(user, pastebin, "test snippet","print \"Hello\";");
		codeSnippet.save();
		
		//retrieve
		List<CodeSnippet> codeSnippets = CodeSnippet.findAll();
		assertEquals(1, codeSnippets.size());
	}

}
