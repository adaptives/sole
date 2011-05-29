package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class BookCategory extends Model {
	@javax.persistence.Column(unique = true)
	public String categoryName;
	
	public BookCategory(String categotyName) {
		this.categoryName = categotyName;
		create();
	}
	
	public static BookCategory findByCategotyName(String categoryName) {
		BookCategory bookCategory = 
			BookCategory.find("select bc from BookCategory bc where bc.categoryName = ?", categoryName).first();
		return bookCategory;
	}
}
