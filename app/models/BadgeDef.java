package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class BadgeDef extends Model {
	
	public String name;
	
	public String title;
	
	public String description;
	
	public boolean multiIssue;
	
	@ManyToOne
	public BadgeType type;
	
	@OneToOne
	public Pic badgeIcon;
	
	public BadgeDef(String name) {
		this.name = name;
	}
}
