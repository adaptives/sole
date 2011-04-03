import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import models.TwitterRequestToken;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;


public class TwitterRequestTokenTest extends UnitTest {
	
	@Before
	public void setUp() {
		Fixtures.deleteAll();
	}
	
	public void tearDown() {
		
	}
	
	@Test
	public void testRequestTokenPersistence() throws Exception {
		Date now = new Date();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(now);
		oos.close();
		TwitterRequestToken trt = new TwitterRequestToken(baos.toByteArray());
		trt.save();
		long id = trt.id;
		
		TwitterRequestToken retrievedTrt = TwitterRequestToken.findById(id);
		ByteArrayInputStream bais = new ByteArrayInputStream(retrievedTrt.requestToken);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Date retrievedDate = (Date)ois.readObject();
		assertEquals(now, retrievedDate);
	}
}
