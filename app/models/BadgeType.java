package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class BadgeType extends Model {
	
	public String type;
	
	public BadgeType(String type) {
		this.type = type;
	}
}
