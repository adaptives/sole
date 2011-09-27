package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class ModeratedFeedback extends Model {
	public String name;
	public String email;
	public String message;
	public Date timestamp;
	public boolean moderated;
	
	public ModeratedFeedback(String name, String email, String message) {
		this.name = name;
		this.email = email;
		this.message = message;
		this.moderated = false;
		this.timestamp = new Date();
	}
	
	public static List<ModeratedFeedback> findModerated() {
		String query = "select f from ModeratedFeedback f where f.moderated = ?";
		return ModeratedFeedback.find(query, true).fetch();
	}
	
	public String toString() {
		String template = "'%s' : '%s' at '%s' approved %b '%s'";
		return String.format(template, 
							 this.name, 
							 this.email,
							 this.timestamp != null ? this.timestamp.toString() : "", 
							 this.moderated, 
							 this.message);
	}
}
