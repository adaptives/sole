package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Author extends Model implements Comparable{

	public String firstName;
	public String middleName;
	public String lastName;
	public String url;
	
	public Author(String firstName, 
				  String middleName, 
				  String lastName, 
				  String url) {
		
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.url = url;
		create();
	}

	@Override
	public int compareTo(Object o) {
		Author other = (Author)o;
		return (int)(this.id - other.id);
	}
	
	@Override
	public String toString() {
		String retVal = "";
		if(this.firstName != null) {
			retVal += this.firstName;
		}
		if(this.middleName != null) {
			retVal += " " + this.middleName;
		}
		if(this.lastName != null) {
			retVal += " " + this.lastName;
		}
		if(this.url != null) {
			retVal += "[" + this.url + "]";
		}
		return retVal;
	}
}
