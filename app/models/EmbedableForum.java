package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class EmbedableForum extends Model {
	
	public String title;
	public String description;
	public String linkId;
	public String embedCode;
	
	public EmbedableForum(String title,
						  String description,
						  String linkId,
						  String embedCode) {
		this.title = title;
		this.description = description;
		this.linkId = linkId;
		this.embedCode = embedCode;
	}
}
