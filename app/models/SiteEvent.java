package models;

import java.util.Date;
import java.util.List;

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
	
	public static List<SiteEvent> tail(int count) {
		String query = "select se from SiteEvent se order by se.timestamp desc"; 
		return SiteEvent.find(query).fetch(count);
	}
}
