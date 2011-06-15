package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import play.db.jpa.Model;

@Entity
public class Page extends Model {
	@Column(unique=true, nullable=false)
	public String name;
	
	public String title;
	
	@Lob
	public String content;
	
	public Page(String name, String title, String content) {
		this.name = name;
		this.title = title;
		this.content = content;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
