package models;

import java.util.List;

import javax.persistence.PersistenceException;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class TopicTest extends UnitTest {
    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
    }

    //Test the main success scenario
    @Test
    public void testCreate() {
    	//create levels
    	Level level1 = new Level("Level I", "Level I Description");
    	level1.placement = 1;
    	level1.save();
    	Level level2 = new Level("Level II", "Level II Description");
    	level2.placement = 2;
    	level2.save();
    	Level level3 = new Level("Level III", "Level III Description");
    	level3.placement = 3;
    	level3.save();
    	
    	//create a Topic which we will use as a pre-requisite
    	Topic binary = new Topic("binary", "binary desc", "binary resources");
    	binary.levels.add(level1);
    	binary.levels.add(level2);
    	binary.levels.add(level3);
    	binary.save();
    	
    	//create Topic
    	String topicTitle = "Core CS";
    	String topicDesc = "Core CS Description";
    	String topicResources = "Core CS Resources";
    	Topic topic = new Topic(topicTitle, 
    							topicDesc, 
    							topicResources);
    	topic.levels.add(level1);
    	topic.levels.add(level2);
    	topic.levels.add(level3);
    	topic.prerequisites.add(binary);
    	topic.save();
    	
    	List<Topic> retreivedTopics = Topic.findAll();
    	assertEquals(2, retreivedTopics.size());
    	Topic retreivedTopic = retreivedTopics.get(1);
    	assertEquals(topicTitle, retreivedTopic.title);
    	assertEquals(topicDesc, retreivedTopic.description);
    	assertEquals(topicResources, retreivedTopic.resources);
    	assertEquals(3, retreivedTopic.levels.size());
    	assertTrue(retreivedTopic.levels.contains(level1));
    	assertTrue(retreivedTopic.levels.contains(level2));
    	assertTrue(retreivedTopic.levels.contains(level3));
    	assertEquals(1, retreivedTopic.prerequisites.size());
    	assertEquals(0, retreivedTopic.competencyGroups.size());
    	assertEquals(new Integer(0), retreivedTopic.placement);
    }
    
    @Test
    public void testCreateWithoutPrerequisites() {
    	//create levels
    	Level level1 = new Level("Level I", "Level I Description");
    	level1.placement = 1;
    	level1.save();
    	Level level2 = new Level("Level II", "Level II Description");
    	level2.placement = 2;
    	level2.save();
    	Level level3 = new Level("Level III", "Level III Description");
    	level3.placement = 3;
    	level3.save();
    	
    	//create Topic
    	String topicTitle = "Core CS";
    	String topicDesc = "Core CS Description";
    	String topicResources = "Core CS Resources";
    	Topic topic = new Topic(topicTitle, 
    							topicDesc, 
    							topicResources);
    	topic.levels.add(level1);
    	topic.levels.add(level2);
    	topic.levels.add(level3);
    	topic.save();
    	
    	List<Topic> retreivedTopics = Topic.findAll();
    	assertEquals(1, retreivedTopics.size());
    	Topic retreivedTopic = retreivedTopics.get(0);
    	assertEquals(0, retreivedTopic.prerequisites.size());
    }
    
    @Test(expected=NullPointerException.class)
    public void testCreateUnsuccessfullWithoutRequiredTitle() {
    	//create levels
    	Level level1 = new Level("Level I", "Level I Description");
    	level1.save();
    	Level level2 = new Level("Level II", "Level II Description");
    	level2.save();
    	Level level3 = new Level("Level III", "Level III Description");
    	level3.save();
    	
    	Topic topic = new Topic(null, 
    							"Core CS Description", 
    							"Core CS Resources");
    	topic.levels.add(level1);
    	topic.levels.add(level2);
    	topic.levels.add(level3);
    	
    	topic.save();
    }
    
    @Test(expected=PersistenceException.class)
    public void testCreateUnsuccessfullWithoutRequiredPlacement() {
    	//create levels
    	Level level1 = new Level("Level I", "Level I Description");
    	level1.save();
    	Level level2 = new Level("Level II", "Level II Description");
    	level2.save();
    	Level level3 = new Level("Level III", "Level III Description");
    	level3.save();
    	
    	Topic topic = new Topic("Core CS", 
    							"Core CS Description", 
    							"Core CS Resources");
    	topic.placement = null;
    	topic.levels.add(level1);
    	topic.levels.add(level2);
    	topic.levels.add(level3);
    	
    	topic.save();
    }
    
    @Test(expected=PersistenceException.class)
    public void testCreateUnsuccessfullWithoutRequiredLevels() {
    	Topic topic = new Topic("Core CS", 
    							"Core CS Description", 
    							"Core CS Resources");
    	topic.save();
    }
    
    @Test
    public void testCompetencyGroupsOrderBy() {
    	//create levels
    	Level level1 = new Level("Level I", "Level I Description");
    	level1.save();
    	Level level2 = new Level("Level II", "Level II Description");
    	level2.save();
    	Level level3 = new Level("Level III", "Level III Description");
    	level3.save();
    	
    	//create Topic
    	String topicTitle = "Data Structures";
    	String topicDesc = "Data Structures Description";
    	String topicResources = "Data Structures Resources";
    	Topic topic = new Topic(topicTitle, 
    							topicDesc, 
    							topicResources);
    	topic.levels.add(level1);
    	topic.levels.add(level2);
    	topic.levels.add(level3);    	
    	
    	//save some competencyGroups (the order in I have saved them is important
    	//We want placement 2 to be saved before placement 1
    	String cg0Title = "Basic Data Structures";
    	String cg0Description = "Basic Data Structures description";
    	String cg0Resources = "Basic Data Structures resources";
    	CompetencyGroup cg0 = new CompetencyGroup(cg0Title, cg0Description, cg0Resources);
    	cg0.placement = 0;
    	cg0.topic = topic;    	    	
    	    	
    	String cg2Title = "Adv Data Structures";
    	String cg2Description = "Adv Data Structures description";
    	String cg2Resources = "Adv Data Structures resources";
    	CompetencyGroup cg2 = new CompetencyGroup(cg2Title, cg2Description, cg2Resources);
    	cg2.placement = 2;
    	cg2.topic = topic;    	
    	
    	String cg1Title = "Intermediate Data Structures";
    	String cg1Description = "Intermediate Data Structures description";
    	String cg1Resources = "Intermediate Data Structures resources";
    	CompetencyGroup cg1 = new CompetencyGroup(cg1Title, cg1Description, cg1Resources);
    	cg1.placement = 1;
    	cg1.topic = topic;    	

    	topic.competencyGroups.add(cg0);
    	topic.competencyGroups.add(cg1);
    	topic.competencyGroups.add(cg2);
    	topic.save();
    	
    	//fetch the Topic and verify order of CompetencyGroups
    	List<Topic> retreivedTopics = Topic.findAll();
    	assertEquals(1, retreivedTopics.size());
    	Topic retreivedTopic = retreivedTopics.get(0);
    	assertEquals(3, retreivedTopic.competencyGroups.size());
    	int count = 0;
    	for(CompetencyGroup competencyGroup : retreivedTopic.competencyGroups) {    		
    		switch(count) {
    			case 0:
    				assertEquals(cg0Title, competencyGroup.title);
    				break;
    				
    			case 1:
    				assertEquals(cg1Title, competencyGroup.title);
    				break;
    			
    			case 2:
    				assertEquals(cg2Title, competencyGroup.title);
    				break;
    		}
    		count++;
    	}
    }
    
    @Test(expected=PersistenceException.class)
    public void testVerifyUnsuccessfullCreationWithTopicAsItsOwnPrerequisite() {
    	//create levels
    	Level level1 = new Level("Level I", "Level I Description");
    	level1.placement = 1;
    	level1.save();
    	Level level2 = new Level("Level II", "Level II Description");
    	level2.placement = 2;
    	level2.save();
    	Level level3 = new Level("Level III", "Level III Description");
    	level3.placement = 3;
    	level3.save();
    	
    	//create a Topic which we will use as a pre-requisite
    	Topic binary = new Topic("binary", "binary desc", "binary resources");
    	binary.levels.add(level1);
    	binary.levels.add(level2);
    	binary.levels.add(level3);
    	binary.save();
    	
    	Topic retreivedTopic = Topic.find("select t from Topic t where t.title = ?", "binary").first();
    	assertNotNull(retreivedTopic);
    	retreivedTopic.prerequisites.add(retreivedTopic);
    	retreivedTopic.save();    	
    }
    
    @Test
    public void testFindAll() {
        
    }
}
