package models;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import other.utils.StringUtils;

import play.db.jpa.Model;

@Entity
public class CourseGroup extends Model {
	
	public String title;
	
	public String sanitizedTitle;
	
	@ManyToOne
	public Course course;
	
	@ManyToMany
	public Set<SocialUser> users;
	
	public CourseGroup(String title, Course course) {
		this.title = title;
		this.sanitizedTitle = StringUtils.replaceSpaceWithDashes(this.title);
		this.course = course;
		this.users = new TreeSet<SocialUser>();
	}
	
	public static List<CourseGroup> findGroupsForCourse(long courseId) {
		String query = "select cg from CourseGroup cg where cg.course.id = ?";
		List<CourseGroup> courseGroups = CourseGroup.find(query, courseId).fetch();
		return courseGroups;
	}
	
	public static CourseGroup findBySanitizedTitle(String title) {
		String query = "select cg from CourseGroup cg where cg.sanitizedTitle = ?";
		return CourseGroup.find(query, title).first();
	}
	
	@Override
	public String toString() {
		return this.title;
	}
}
