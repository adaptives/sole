package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class KeyValueData extends Model {
	public String key;
	public String value;
	
	public KeyValueData(String key, String value) {
		this.key = key;
		this.value = value;
		create();
	}
}
