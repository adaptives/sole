package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class LastLogin extends Model{

	public User user;
	public Date loginTime;
	
	public LastLogin(User user) {
		this.user = user;
		this.loginTime = new Date();
	}
	
	public LastLogin(User user, Date loginTime) {
		this.user = user;
		this.loginTime = loginTime;
	}
}
