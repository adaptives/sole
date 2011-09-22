package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class ActivityResponseReview extends Model {

	@ManyToOne
	public ActivityResponse activityResponse;
	
	@ManyToOne
	public SocialUser user;
	
	@Lob
	public String review;
	
	public Date timestamp;
	
	public ActivityResponseReview(ActivityResponse activityResponse,
								  SocialUser user,
								  String review) {
		
		this.activityResponse = activityResponse;
		this.user = user;
		this.review = review;
		this.timestamp = new Date();
		create();
	}
}
