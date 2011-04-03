package models;

import javax.persistence.Entity;

import play.data.validation.Email;
import play.db.jpa.Model;

@Entity
public class SocialUser extends Model {
	
	public String screenname;
	
	@Email
	public String email;
	
	public SocialUser(String screenname) {
		this.screenname = screenname;
		create();
	}
}
