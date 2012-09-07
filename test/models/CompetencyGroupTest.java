package models;

import java.util.Set;

import javax.persistence.PersistenceException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public final class CompetencyGroupTest extends UnitTest {
    private Topic java;
    private Level level1;
    private Level level2;
    private Level level3;

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        level1 = new Level("Level I", "Level I description");
        level2 = new Level("Level II", "Level II description");
        level3 = new Level("Level III", "Level III description");
        level1.save();
        level2.save();
        level3.save();

        java = new Topic("Java", "Java programming language.", "Sample resources");
        java.levels.add(level1);
        java.levels.add(level2);
        java.levels.add(level3);
        java.save();
    }

    @Test
    public void testCreateAndFindByTitle() {
        CompetencyGroup collections = new CompetencyGroup("Collections", "Java Collections API", "Collection resources");
        collections.topic = java;
        collections.save();

        Competency c1 = new Competency("Collections API", "Collections API Basics", collections, level1, "c1 resources");
        Competency c2 = new Competency("List", "Understanding  Lits", collections, level2, "c2 resources");
        Competency c3 = new Competency("Sets", "Understanding Sets", collections, level3, "c3 resources");

        collections.competencies.add(c1);
        collections.competencies.add(c2);
        collections.competencies.add(c3);

        collections.save();

        CompetencyGroup found = CompetencyGroup.findByTitle("Collections");
        Assert.assertNotNull(found);
        Assert.assertEquals("Collections", found.title);
        Assert.assertEquals("Java Collections API", found.description);
        Assert.assertEquals("Collection resources", found.resources);
        Assert.assertNotNull(found.topic);
        Assert.assertEquals("Java", found.topic.title);
        Assert.assertNotNull(found.competencies);
        Assert.assertEquals(3, found.competencies.size());

        Assert.assertTrue(containsCompetencyWithTitle("Collections API", found.competencies));
        Assert.assertTrue(containsCompetencyWithTitle("List", found.competencies));
        Assert.assertTrue(containsCompetencyWithTitle("Sets", found.competencies));
    }

    private boolean containsCompetencyWithTitle(String title, Set<Competency> competencies) {
        for (Competency c : competencies) {
            if (title.equals(c.title)) {
                return true;
            }
        }

        return false;
    }

    @Test(expected = PersistenceException.class)
    public void testCreateUnsuccessfullWithoutRequiredTitle() {
        CompetencyGroup cg = new CompetencyGroup("Collections", "Java Collections API", "Collection resources");
        cg.topic = java;
        cg.title = null;
        cg.save();
    }

    @Test(expected = PersistenceException.class)
    public void testCreateUnsuccessfullWithoutRequiredSanitizedTitle() {
        CompetencyGroup cg = new CompetencyGroup("Collections", "Java Collections API", "Collection resources");
        cg.topic = java;
        cg.sanitizedTitle = null;
        cg.save();
    }

    @Test(expected = PersistenceException.class)
    public void testCreateUnsuccessfullWithoutRequiredPlacement() {
        CompetencyGroup cg = new CompetencyGroup("Collections", "Java Collections API", "Collection resources");
        cg.topic = java;
        cg.placement = null;
        cg.save();
    }

    @Test(expected = PersistenceException.class)
    public void testCreateUnsuccessfullWithoutRequiredTopic() {
        CompetencyGroup cg = new CompetencyGroup("Collections", "Java Collections API", "Collection resources");
        // Already null, this is simply for clarity.
        cg.topic = null;
        cg.save();
    }

    @Test(expected = PersistenceException.class)
    public void testShouldNotAllowItselfAsPreRequisite() {
        CompetencyGroup cg = null;

        try {
            cg = new CompetencyGroup("Collections", "Java Collections API", "Collection resources");
            cg.topic = java;
            cg.save();
        } catch (PersistenceException pe) {
            // Ensuring that we are able to save successfully first and
            // exception originating from this is not marking the test as
            // passed.
            pe.printStackTrace();
            Assert.fail("Not expecting the save to fail here. Failure Cause: " + pe.getMessage());
        }

        cg.prereqisites.add(cg);
        cg.save();
    }

    @Test(expected = PersistenceException.class)
    public void testShouldNotAllowPreRequisiteHavingDifferentTopic() {
        CompetencyGroup cg = null;
        CompetencyGroup p = null;

        try {
            cg = new CompetencyGroup("Collections", "Java Collections API", "Collection resources");
            cg.topic = java;
            cg.save();

            Topic html = new Topic("HTML", "Hyper Text Markup Language", "HTML resources");
            html.levels.add(level1);
            html.levels.add(level2);
            html.levels.add(level3);
            html.save();

            p = new CompetencyGroup("Motivation", "Motivation behind HTML", "HTML Motivation resources");
            p.topic = html;
            p.save();
        } catch (PersistenceException pe) {
            // Ensuring that we are able to save successfully first and
            // exception originating from this is not marking the test as
            // passed.
            pe.printStackTrace();
            Assert.fail("Not expecting the save to fail here. Failure Cause: "+ pe.getMessage());
        }

        // Adding a pre-requisite with a different topic.
        cg.prereqisites.add(p);
        cg.save();
    }
}
