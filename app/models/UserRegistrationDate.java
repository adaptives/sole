package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class UserRegistrationDate extends Model{
	@OneToOne
	public SocialUser socialUser;
	public Date registrationDate;
	
	public UserRegistrationDate(SocialUser user) {
		this.socialUser = user;
		this.registrationDate = new Date();
	}
	
	public UserRegistrationDate(SocialUser user, Date registrationDate) {
		this.socialUser = user;
		this.registrationDate = registrationDate;
	}
}
