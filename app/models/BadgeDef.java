package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class BadgeDef extends Model {
	
	public String name;
	
	public String title;
	
	public String description;
	
	public boolean multiIssue;
	
	@OneToOne
	public Pic badgeIcon;
	
	public BadgeDef(String name) {
		this.name = name;
	}
}
