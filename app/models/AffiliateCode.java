package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class AffiliateCode extends Model {
	
	public String code;
	
	public AffiliateCode(String code) {
		this.code = code;
	}
	
}
