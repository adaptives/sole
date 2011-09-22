package models;

import java.util.Date;
import java.util.List;

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
	
	public Date timestamp;
	
	public ActivityResponseLiked(ActivityResponse activityResponse,
								 SocialUser user) {
		
		this.activityResponse = activityResponse;
		this.user = user;
		this.timestamp = new Date();
		create();
	}
	
	//TODO: Unit Test this function
	public static List<ActivityResponseLiked> findByActivityResponse(long id) {
		String query = "select arl from ActivityResponseLiked arl join arl.activityResponse ar where ar.id = ?";
		return ActivityResponseLiked.find(query, id).fetch();
	}
}
