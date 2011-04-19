package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;

import play.db.jpa.Model;

@Entity
public class SiteEvent extends Model {
	
	@Lob
	public String text;
	public Date timestamp;
	
	public SiteEvent(String text) {
		this.text = text;
		this.timestamp = new Date();
		 create();
	}
}
