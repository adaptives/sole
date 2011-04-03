package models;

import javax.persistence.Entity;
import javax.persistence.Lob;

import play.db.jpa.Model;

@Entity
public class TwitterRequestToken extends Model {
	@Lob
	public byte[] requestToken;
	
	public TwitterRequestToken(byte[] requestToken) {
		this.requestToken = requestToken;
		create();
	}
}
