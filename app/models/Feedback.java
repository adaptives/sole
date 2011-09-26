package models;

import java.util.List;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Feedback extends Model {
	public String name;
	public String email;
	public String message;
	public boolean moderated;
	
	public Feedback(String name, String email, String message) {
		this.name = name;
		this.email = email;
		this.message = message;
		this.moderated = false;
		create();
	}
	
	public List<Feedback> findModerated() {
		String query = "select f from Feedback f where f.moderated = ?";
		return Feedback.find(query, true).fetch();
	}
}
