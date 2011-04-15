package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class DefaultSudySessionLocationAffiliate extends Model {
	
	@javax.persistence.Column(unique = true)
	public int location;
	
	public String affiliateCode;
	
	public DefaultSudySessionLocationAffiliate(int location, 
											   String affiliateCode) {
		this.location = location;
		this.affiliateCode = affiliateCode;
	}
}
