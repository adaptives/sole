package models;

import java.util.List;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Feedback extends Model {
	public String name;
	public String email;
	public String message;
	
	public Feedback(String name, String email, String message) {
		this.name = name;
		this.email = email;
		this.message = message;
		create();
	}
	
}
