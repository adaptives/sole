package models;

import javax.persistence.Entity;

import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Pic extends Model {

	public Blob image;
	
	public Pic(Blob image) {
		this.image = image;
	}
}
