package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class BookLinkProvider extends Model {
	public String name;
}
