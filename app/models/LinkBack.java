package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class LinkBack extends Model {
	public String link;
}
