package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Role extends Model {
	public String name;
	private String test;
	
	public Role(String name) {
		this.name = name;
	}
	
	public String getTest() {
		return this.test;
	}
	
	public void setTest(String test) {
		this.test = test;
	}
}
