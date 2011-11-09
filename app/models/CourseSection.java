package models;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import other.utils.StringUtils;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class CourseSection extends Model {
	
	@Required
	public String title;
	
	public String sanitizedTitle;
	
	public int placement;
	
	@Lob
	@MaxSize(20000)
	public String content;

	@Required
	@ManyToOne
	public Course course;
	
	@ManyToMany(cascade=CascadeType.ALL)
	public Set<SocialUser> understoodParticipants;
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("id")
	public Set<Activity> activities;
	
	public CourseSection(Course course, String title, String content) {
		this.course = course;
		this.title = title;
		this.sanitizedTitle = StringUtils.replaceSpaceWithDashes(this.title);
		this.content = content;
		this.activities = new TreeSet<Activity>();
	}
	
	public CourseSection previous() {
		CourseSection courseSection = null;
		int pPlacement = this.placement - 1;
		if(pPlacement >= 0) {
			String query = "select cs from CourseSection cs where cs.course.id = ? and cs.placement = ?";
			courseSection = CourseSection.find(query, this.course.id, pPlacement).first();
		}
		return courseSection;
	}
	
	public CourseSection next() {
		CourseSection courseSection = null;
		int pPlacement = this.placement + 1;
		String query = "select cs from CourseSection cs where cs.course.id = ? and cs.placement = ?";
		courseSection = CourseSection.find(query, this.course.id, pPlacement).first();
		return courseSection;
	}
	
	public static CourseSection findBySanitizedTitleByCouse(Course course, 
															String sanitizedTitle) {
		
		String query = "select cs from CourseSection cs where cs.course.id = ? and cs.sanitizedTitle = ?";
		CourseSection courseSection = 
			CourseSection.find(query, course.id, sanitizedTitle).first();
		return courseSection;
	}
	
	public static CourseSection findCourseSectionForActivity(Activity activity) {
		String query = "select cs from CourseSection cs join cs.activities a where a.id = ?";
		CourseSection cs = CourseSection.find(query, activity.id).first();
		return cs;
	}
	
	public List<Activity> fetchActivitiesByPlacement() {
		String query = "select a from CourseSection cs join cs.activities a where cs.id = ? order by a.placement";
		return CourseSection.find(query, this.id).fetch();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseSection other = (CourseSection) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (course == null) {
			if (other.course != null)
				return false;
		} else if (!course.equals(other.course))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.title;
	}	
}

