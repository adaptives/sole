package models;

import javax.persistence.Entity;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class SocialUser extends Model implements Comparable {
	
	@Required
	public String screenname;
	
	@Email
	public String email;
	
	public SocialUser(String screenname) {
		this.screenname = screenname;
		create();
	}

	@Override
	public int compareTo(Object arg0) {
		SocialUser other = (SocialUser)arg0;
		return this.screenname.compareTo(other.screenname);
	}
}
