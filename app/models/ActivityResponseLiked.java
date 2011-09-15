package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class ActivityResponseLiked extends Model {
	
	@Required
	@ManyToOne
	public ActivityResponse activityResponse;
	
	@Required
	@ManyToOne
	public SocialUser user;
	
	public ActivityResponseLiked(ActivityResponse activityResponse,
								 SocialUser user) {
		
		this.activityResponse = activityResponse;
		this.user = user;
		create();
	}
}
