package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Forum extends Model {
	
	public String title;
	public String description;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<Question> questions;
	
	public Forum(String title,
				 String description) {
		this.title = title;
		this.description = description;
		create();
	}
}
