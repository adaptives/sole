package models;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.Logger;
import play.data.validation.URL;
import play.db.jpa.Model;

@Entity
public class ActivityResponse extends Model {
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(ActivityResponse.class);
	
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
	
	public int likes() {
		String query = "select count(distinct arl) from ActivityResponseLiked arl where arl.activityResponse.id = ?";
		return (int)ActivityResponseLiked.count(query, this.id);
	}
	
	public void like(SocialUser user) {
		//Users cannot upvote their own activityResponse and they cannot upvote any 
		//activityResponse more than once 
		if(this.user.id != user.id) {
			if(!hasLiked(user)) {
				new ActivityResponseLiked(this, user);
			} else {
				//TODO Rate check ?
				cLogger.warn(user.id + "'" + " has already liked activity '" + this.id);
			}			
		} else {
			//TODO Rate check ?
			cLogger.warn(user.id + "'" + " cannot like own activity '" + this.id);
		}
	}
	
	public boolean hasLiked(SocialUser user) {
		String query = "select count(distinct arl) from ActivityResponseLiked arl where arl.activityResponse = ? and arl.user=?";
		if(ActivityResponseLiked.count(query, this, user) == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public String toString() {
		return this.user.screenname + " " + 
		       this.activity.title + " " + 		  
		       this.responseLink + "/" +
		       this.title;
	}
}
