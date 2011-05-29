package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class BookLink extends Model {
	public BookLinkProvider provider;
	public String code;
}
