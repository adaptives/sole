package models;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.data.validation.URL;
import play.db.jpa.Model;

@Entity
public class ActivityResponse extends Model {
	
	@ManyToOne
	public SocialUser user;
	
	@URL
	public String responseLink;
	public String title;
	
	//TODO: Many rows do not have a timestamp... put something manually and 
	//remove the ? from the html file.
	public Date timestamp;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	public Set<Tag> tags;
	
	@ManyToOne
	public Activity activity;
	
	public ActivityResponse(SocialUser user,
							Activity activity,
							String responseLink,
							String title) {
		this.user = user;
		this.activity = activity;
		this.responseLink = responseLink;
		if(title != null && !title.trim().equals("")) {
			this.title = title;
		}
		this.timestamp = new Date();
	}
	
	@Override
	public String toString() {
		return this.user.screenname + " " + 
		       this.activity.title + " " + 		  
		       this.responseLink + "/" +
		       this.title;
	}
}
