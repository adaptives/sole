package models;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import play.db.jpa.Model;

@Entity
public class Forum extends Model {
	
	public String title;
	public String description;
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("askedAt DESC")
	public Set<Question> questions;
	
	public Forum(String title,
				 String description) {
		this.title = title;
		this.description = description;
		this.questions = new TreeSet<Question>();
		create();
	}
	
	public List<Question> fetchQuestionsByPage(int length, int page) {
		String query = "select q from Forum f join f.questions q where f.id = ? order by q.askedAt DESC";
		return Forum.find(query, this.id).fetch(page, length);
	}
	
	@Override
	public String toString() {
		return this.title;
	}
}
