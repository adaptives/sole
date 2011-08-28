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
	
	public void like(SocialUser user) {
		//Users cannot 'like' their own question and they cannot upvote any 
		//question more than once 
		if(this.user.id != user.id) {
			if(!hasLiked(user)) {
				ActivityResponseLiked arl = new ActivityResponseLiked(this,user);
				arl.save();
			} else {
				//TODO Rate check ?
				//cLogger.warn(user.id + "'" + " has already liked question '" + this.id);
			}			
		} else {
			//TODO Rate check ?
			//cLogger.warn(user.id + "'" + " cannot like own question '" + this.id);
		}
	}
	
	public int likes() {
		String query = "select count(distinct arl) from ActivityResponseLiked arl where arl.activityResponse.id = ?";
		return (int)ActivityResponseLiked.count(query, this.id);
	}
	
	private boolean hasLiked(SocialUser user) {
		String query = "select count(distinct arl) from ActivityResponseLiked arl where arl.activityResponse.id = ? and arl.user.id = ?";
		return (ActivityResponseLiked.count(query, this.id, user.id) > 0);
	}
	
	@Override
	public String toString() {
		return this.user.screenname + " " + 
		       this.activity.title + " " + 		  
		       this.responseLink + "/" +
		       this.title;
	}
}
