package models;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class ChangedUrlTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAll();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testFindNewUrl() throws Exception {
		Map<String, String> urlMapping = new HashMap<String, String>();
		urlMapping.put("http://diycomputerscience.com/blog/post/5", "http://diycomputerscience.com/blog/post/2011/05/29/interview-with-a-self-taught-programmer");
		urlMapping.put("http://diycomputerscience.com/blog/post/4", "http://diycomputerscience.com/blog/post/2011/05/05/flipping-the-classroom");
		urlMapping.put("http://diycomputerscience.com/blog/post/3", "http://diycomputerscience.com/blog/post/2011/05/03/what-is-an-online-study-group-and-how-to-participate-in-one");
		urlMapping.put("http://diycomputerscience.com/blog/post/2", "http://diycomputerscience.com/blog/post/2011/04/29/what-i-learned-by-facilitating-an-online-course-on-Javascript-101");
		urlMapping.put("http://diycomputerscience.com/blog/post/1", "http://diycomputerscience.com/blog/post/2011/04/28/hello-world");
		
		Set<String> keySet = urlMapping.keySet();
		for(String key : keySet) {
			String val = urlMapping.get(key);
			ChangedUrl curl1 = new ChangedUrl(key, val);
		}
		
		for(String key : keySet) {
			assertEquals(urlMapping.get(key), ChangedUrl.findNewUrl(key));
		}
		
		assertNull(ChangedUrl.findNewUrl("some-random-string"));
	}

}
