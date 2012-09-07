package models;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceException;

import other.utils.StringUtils;

import play.data.validation.Required;
import play.db.jpa.JPABase;
import play.db.jpa.Model;

@Entity
public class Competency extends Model implements Comparable {
		
	@Required
	@Column(nullable=false)
    public String title;       
    
	@Column(nullable=false)
    public String sanitizedTitle;
    
    @Lob
    public String description;
    
    @Lob
    public String resources;
    
    @Column(nullable=false)
    public Integer placement;
    
    //TODO: Set database constraints to ensure that this is not nullable
    //TODO: Verify that this level is indeed present in the topic to which this competenct belongs
    @Required
    @ManyToOne
    public Level level;

    @Required
    @ManyToOne
    //TODO: How do we make this required
    public CompetencyGroup competencyGroup;
    
    //TODO: If a Competency has been specified as a prerequsite for this
    //Competency, then the preerquisite must belong to the same CompetencyGroup
    @OneToMany
    public Set<Competency> prerequisites;
    

    public Competency(String title,
    				  String description,  
    				  CompetencyGroup competencyGroup,
    				  Level level,
    				  String resources) {
        super();
        if(title == null) {
        	throw new NullPointerException("attribute 'topic' cannot be null");
        }
        this.title = title;
        this.sanitizedTitle = StringUtils.replaceSpaceWithDashes(this.title);
        this.description = description;
        this.competencyGroup = competencyGroup;
        this.level = level;
        this.resources = resources;
        this.placement = 0;
        this.prerequisites = new TreeSet<Competency>();
    }
    
    public static Competency fetchBySanitizedTitle(String sanitizedTitle) {
    	String query = "select c from Competency c where c.sanitizedTitle = ?";
    	return Competency.find(query, sanitizedTitle).first();
    }

    @Override
	public int compareTo(Object o) {
		Competency other = (Competency)o;
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
    public <T extends JPABase> T save() {
        // Should not add itself as pre-requisite.
        for (Competency c : prerequisites) {
            if (c.id == null) {
                throw new PersistenceException("Attempt to add a pre-requisite Competency which has not been saved.");
            } else if (this.id != null && this.id == c.id) {
                throw new PersistenceException("A Competency cannot have itself as pre-requisite");
            }
        }

        return super.save();
    }

    @Override
    public String toString() {
    	return this.id + " : " + this.title;
    }

    public static Competency findByTitle(String title) {
        String query = "select c from Competency c where c.title = ?";
        return Competency.find(query, title).first();
    }
}
