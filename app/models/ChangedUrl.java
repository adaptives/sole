package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class ChangedUrl extends Model {

	@Column(unique = true)
	public String oldUrl;
	
	public String newUrl;
	
	public ChangedUrl(String oldUrl, String newUrl) {
		this.oldUrl = oldUrl;
		this.newUrl = newUrl;
		create();
	}
	
	public static String findNewUrl(String oldUrl) {
		String retVal = null;
		String query = "select cu from ChangedUrl cu where cu.oldUrl = ?";
		ChangedUrl changedUrl =  ChangedUrl.find(query, oldUrl).first();
		if(changedUrl != null) {
			retVal = changedUrl.newUrl;
		}
		return retVal;
	}
	
	public String toString() {
		return this.oldUrl;
	}
}
