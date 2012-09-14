package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceException;

import other.utils.StringUtils;

import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.JPABase;
import play.db.jpa.Model;

@Entity
public class Topic extends Model implements Comparable {
    
	@Required
	@Column(nullable=false)
	public String title;
    
	//TODO: Test
	@Column(nullable=false)
	public String sanitizedTitle;
	
	@URL
	public String imageUrl;
		
	@Lob
	public String description;
    
	@Column(nullable=false)
	public Integer placement;

    //TODO: order by placement
    @OneToMany(cascade=CascadeType.ALL, mappedBy="topic")
    @OrderBy("placement")
    public Set<CompetencyGroup> competencyGroups;
    
    @ManyToMany
    @JoinTable(name="PREREQUISITEOF_ID")
    public Set<Topic> prerequisiteOf;
    
    @ManyToMany(mappedBy="prerequisiteOf")
    public Set<Topic> prerequisites;
    
    @Lob
    public String resources;
    
    @ManyToMany    
    public Set<Level> levels;

    public Topic(String title, 
    			 String description,
    			 String resources) {
        super();
        if(title == null) {
        	throw new NullPointerException("title cannot be null");
        }
        this.title = title;
        this.sanitizedTitle = StringUtils.replaceSpaceWithDashes(this.title);
        this.description = description;
        this.resources = resources;
        this.placement = 0;
        this.competencyGroups = new TreeSet<CompetencyGroup>();
        this.prerequisites = new TreeSet<Topic>();
        this.prerequisiteOf = new TreeSet<Topic>();
        this.levels = new TreeSet<Level>();
    }
    
    public static Topic fetchBySanitizedTitle(String sanitizedTitle) {
    	String query = "select t from Topic t where t.sanitizedTitle = ?";
    	return Topic.find(query, sanitizedTitle).first();
    }

    @Override
    public <T extends JPABase> T save() {
    	//A topic must have at least one Level
    	if(this.levels.size() == 0) {
    		String msg = "Topic does not have any Level objects associated with it";
    		throw new PersistenceException(msg);
    	}
    	//A Topic should not have itself as a pre-requisite
    	for(Topic topic : this.prerequisites) {
    		if(topic.id == null) {
    			String msg = "Attempt to add as a pre-requisite a Topic which has not yet been saved";
    			throw new PersistenceException(msg);
    		} else if(this.id != null) { //updating a topic with new pre-erquisites ?
    			if(topic.id.equals(this.id)) {
    				String msg = "A Topic cannot have itself as a pre-requisite";
    				throw new PersistenceException(msg);
    			}
    		}
    	}
    	return super.save();
    }
    
    @Override
	public int compareTo(Object o) {
		Topic other = (Topic)o;
		//we do not want to return a 0 if both the placement values are the same
		//because that will make the TreeSet think these are equal objects and
		//one of them will not be added. If placements are equal then we 
		//compare by title
		if(this.placement == other.placement) {
			return this.title.compareTo(other.title);
		} else {
			return this.placement - other.placement;
		}
	}
    
    @Override
    public String toString() {
    	return this.id + " : " + this.title;
    }
}
