import static org.junit.Assert.*;

import models.Role;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;


public class RoleTest extends UnitTest{

	@Before
	public void setUp() {
		Fixtures.deleteAll();
	}
	
	@After
	public void tearDown() {
		
	}
	
}
