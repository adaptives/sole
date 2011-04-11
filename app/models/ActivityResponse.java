package models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class ActivityResponse extends Model {
	
	@ManyToOne
	public SocialUser user;
	
	public String responseLink;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	public Set<Tag> tags;
	
	@ManyToOne
	public Activity activity;
	
	public ActivityResponse(SocialUser user,
							Activity activity, 
							String responseLink) {
		this.user = user;
		this.activity = activity;
		this.responseLink = responseLink;
	}
}
