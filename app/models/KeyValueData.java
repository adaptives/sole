package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class KeyValueData extends Model {
	public String k;
	public String v;
	
	public KeyValueData(String key, String value) {
		this.k = key;
		this.v = value;
		create();
	}
	
	public static String findValue(String key) {
		return findValue(key, null);
	}
	
	public static String findValue(String key, String defaultVal) {
		String retVal = defaultVal;
		KeyValueData keyValueData = KeyValueData.find("select kvd from KeyValueData kvd where kvd.k = ?", key).first();
		if(keyValueData != null) {
			retVal = keyValueData.v;
		}
		return retVal;
	}
	
	@Override
	public String toString() {
		return k;
	}
}
