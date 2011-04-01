package models;

import javax.persistence.Entity;

import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class ProfilePic extends Model {

	public Blob image;
	
	public ProfilePic(Blob image) {
		this.image = image;
	}
}
