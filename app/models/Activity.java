package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Activity extends Model {
	String content;
}
