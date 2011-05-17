package models;

import javax.persistence.Entity;
import javax.persistence.Lob;

import play.data.validation.MaxSize;
import play.db.jpa.Model;

@Entity
public class AffiliateCode extends Model {
	
	@Lob
	@MaxSize(2048)
	public String code;
	
	public AffiliateCode(String code) {
		this.code = code;
	}
	
}
