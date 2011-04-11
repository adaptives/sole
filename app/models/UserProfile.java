package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class UserProfile extends Model {
	
	public String aboutMyself;
	
	public String location;
	
	@OneToMany
	public Set<Question> questionsAsked;
	
	@OneToMany
	public Set<Answer> answersProvided;
	
	@OneToOne
	public Pic profilePic;
	
	public int points;
	
	@Required
	@OneToOne
	public SocialUser user;
	
	public UserProfile(SocialUser user) {
		this.user = user;
		this.questionsAsked = new HashSet<Question>();
		this.answersProvided = new HashSet<Answer>();
	}
}
