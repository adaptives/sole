package models;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;

import play.Logger;
import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class SocialUser extends Model implements Comparable {
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(SocialUser.class);
	
	@javax.persistence.Column(unique = true)
	@Required
	public String screenname;
	
	@Email
	public String email;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
    public Set<Role> roles;
    
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
	public UserProfile userProfile;
	
	public SocialUser(String email) {
		this(email, "");
	}
	
	public SocialUser(String email, String screenname) {
		this.email = email;
		this.screenname = screenname;
		this.userProfile = new UserProfile(this);
		this.roles = new TreeSet<Role>();
		Role role = Role.find("select r from Role r where r.name = ?", "learner").first();
		if(role != null) {
			this.roles.add(role);
		} else {
			cLogger.warn("Could not find a role for learner");
		}
		//create();		
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
