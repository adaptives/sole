package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class CourseCategory extends Model {
	
	public String name;
	
	public int placement;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="category")
	public List<Course> courses;
	
	public CourseCategory(String name) {
		this.name = name;
	}
	
	public static CourseCategory findByName(String name) {
		String query = "select cc from CourseCategory cc where cc.name = ?";
		CourseCategory cc = CourseCategory.find(query, name).first();
		return cc;
	}
	
	public static List<CourseCategory> findByOrderedPlacement() {
		String query = "select cc from CourseCategory cc order by cc.placement";
		return CourseCategory.find(query).fetch();
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
