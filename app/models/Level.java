package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Level extends Model implements Comparable {
	
	//TODO: Add database constraint to ensure that this cannot be set to null
	@Required
    @Column(nullable=false)
	public String title;
	
	public String description;

	@Column(nullable = false)
	public Integer placement;
	
	public Level(String title,
				 String description) {
		this.title = title;
		this.description = description;
		this.placement = 0;
	}
	
	public static Level findByTitle(String title) {
		String query = "select l from Level l where l.title = ?";
		return Level.find(query, title).first();		
	}

	@Override
	public int compareTo(Object o) {
		Level other = (Level)o;
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
