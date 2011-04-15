package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Role extends Model {
	
	public String name;
	
	public Role(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
