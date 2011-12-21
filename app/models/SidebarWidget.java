package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;

import play.db.jpa.Model;

@Entity
public class SidebarWidget extends Model {
	
	public String title;
	public String displayTitle;
	public String context;
	@Lob
	public String content;
	public int location;
	
	public SidebarWidget(String title, String displayTitle, String content) {
		this.title = title;
		this.displayTitle = displayTitle;
		this.content = content;
	}
	
	public static List<SidebarWidget> findByContext(String context) {
		if(context == null) {
			context = "";
		}
		String query = "select sw from SidebarWidget sw where sw.context = ? order by location ASC";
		return SidebarWidget.find(query, context).fetch();
	}
	
	@Override
	public String toString() {
		return this.title;
	}
}
