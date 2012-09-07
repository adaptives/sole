package models;

import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public final class LevelTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
    }

    @Test
    public void testCreateAndFindByTitle() {
        Level l = new Level("Level I", "Level I description");
        l.placement = 4;
        l.save();

        Level found = Level.findByTitle("Level I");
        Assert.assertNotNull(found);
        Assert.assertEquals("Level I", found.title);
        Assert.assertEquals("Level I description", found.description);
        Assert.assertNotNull(found.placement);
        Assert.assertEquals(4, found.placement.intValue());
    }

    @Test(expected = PersistenceException.class)
    public void testCreateUnsuccessfullWithoutRequiredTitle() {
        Level l = new Level(null, "Level I description");
        l.save();
    }

    @Test(expected = PersistenceException.class)
    public void testCreateUnsuccessfullWithoutRequiredPlacement() {
        Level l = new Level("Level I", "Level I description");
        l.placement = null;
        l.save();
    }
}
