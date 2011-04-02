package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class UserRegistrationDate extends Model{
	@OneToOne
	public User user;
	public Date registrationDate;
	
	public UserRegistrationDate(User user) {
		this.user = user;
		this.registrationDate = new Date();
	}
	
	public UserRegistrationDate(User user, Date registrationDate) {
		this.user = user;
		this.registrationDate = registrationDate;
	}
}
