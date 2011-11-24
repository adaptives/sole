package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import other.dob.UserCourseProgress;
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
	
	public List<UserCourseProgress> fetchSortedUsers() {
		Comparator userComp = new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				UserCourseProgress user1 = (UserCourseProgress)o1;
				UserCourseProgress user2 = (UserCourseProgress)o2;
				double diff =
					user2.progress - user1.progress;
				if(diff > 0.0) {
					return 1;
				} else if(diff < 0.0) {
					return -1;
				} else {
					return 0;
				}
			}
			
		};
		List<UserCourseProgress> lUsers = new ArrayList<UserCourseProgress>();
		for(SocialUser user : this.users) {
			lUsers.add(new UserCourseProgress(user, this.course.getActivityCompletionStatus(String.valueOf(user.id))));
		}
		Collections.sort(lUsers, userComp);
		return lUsers;
	}
	
	@Override
	public String toString() {
		return this.title;
	}
}
