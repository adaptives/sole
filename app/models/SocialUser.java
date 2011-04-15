package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class SocialUser extends Model implements Comparable {
	
	@javax.persistence.Column(unique = true)
	@Required
	public String screenname;
	
	@javax.persistence.Column(unique = true)
	@Email
	public String email;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
	public UserProfile userProfile;
	
	public SocialUser(String email) {
		this(email, "");
	}
	
	public SocialUser(String email, String screenname) {
		this.email = email;
		this.screenname = screenname;
		this.userProfile = new UserProfile(this);
//		create();		
	}

	@Override
	public int compareTo(Object arg0) {
		SocialUser other = (SocialUser)arg0;
		return this.screenname.compareTo(other.screenname);
	}
	
	public String toString() {
		return this.screenname;
	}
}
