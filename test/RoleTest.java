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
	
	@Test
	public void testRolePrivateField() {
		Role role = new Role("admin");
		role.setTest("abc");
		role.save();
		
		Role retrievedRole = Role.find("byTest", "abc").first();
		assertNotNull(retrievedRole);
	}
}
