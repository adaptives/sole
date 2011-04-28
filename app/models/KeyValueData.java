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
	
	public static String findValue(String key) {
		return findValue(key, null);
	}
	
	public static String findValue(String key, String defaultVal) {
		String retVal = defaultVal;
		KeyValueData keyValueData = KeyValueData.find("select kvd from KeyValueData kvd where kvd.key = ?", key).first();
		if(keyValueData != null) {
			retVal = keyValueData.value;
		}
		return retVal;
	}
}
