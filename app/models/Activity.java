package models;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Activity extends Model {
	
	@Required
	@Lob
	@MaxSize(20000)
	public String content;
	
	@OneToMany(mappedBy="activity", cascade=CascadeType.ALL)
	public Set<ActivityResponse> activityResponses;
	
	public Activity(String content) {
		this.content = content;
		this.activityResponses = new TreeSet<ActivityResponse>();
		create();
	}
	
	@Override
	public String toString() {
		String retVal = String.valueOf(this.id) + " ";
		if(this.content != null) {
			if(this.content.length() <= 100) {
				retVal += this.content;
			} else {
				retVal += this.content.substring(0, 50);
			}			
		}
		return retVal;
	}
}
