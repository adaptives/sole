package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class MessageProperties extends Model {
	
	@OneToOne
	public PrivateMessage message;
	
	public boolean isRead;
	
	public MessageProperties(PrivateMessage message) {
		this.message = message;
	}
}