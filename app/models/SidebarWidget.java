package models;

import javax.persistence.Entity;
import javax.persistence.Lob;

import play.db.jpa.Model;

@Entity
public class SidebarWidget extends Model {
	
	public String title;
	public String displayTitle;
	@Lob
	public String content;
	public int location;
	
	public SidebarWidget(String title, String displayTitle, String content) {
		this.title = title;
		this.displayTitle = displayTitle;
		this.content = content;
	}
	
	@Override
	public String toString() {
		return this.title;
	}
}
