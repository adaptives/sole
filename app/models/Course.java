package models;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import other.utils.StringUtils;

import play.Logger;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Course extends Model {
	
	@Required
	public String title;
	
	public String sanitizedTitle;
	
	@Lob
	@MaxSize(10000)
	public String description;

	@ManyToOne
	public CourseCategory category;
	
	@OneToOne
	public Pic coursePic;
	
	@OneToMany(cascade=CascadeType.ALL)
	public Set<Activity> activities;
	
	@OneToOne(cascade=CascadeType.ALL)
	public Forum forum;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="COURSE_ENROLLED_PARTICIPANTS")
	public Set<SocialUser> enrolledParticipants;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="COURSE_COMPLETED_PARTICIPANTS")
	public Set<SocialUser> completedParticipants;
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(Course.class);
	
	
	public Course(String title, String description) {
		this.title = title;
		this.sanitizedTitle = StringUtils.replaceSpaceWithDashes(this.title);
		this.description = description;
		this.activities = new TreeSet<Activity>();
		this.forum = new Forum(this.title, 
							   "Forum for discussing all things relared to " + this.title);
		this.enrolledParticipants = new TreeSet<SocialUser>();
		this.completedParticipants = new TreeSet<SocialUser>();
		create();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
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
		Course other = (Course) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
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
	
	public List<CourseSection> fetchSectionsByPlacement() {
		return CourseSection.
					find("course = ? order by placement asc", this).fetch();
	}
	
	public static Course findBySanitizedTitle(String sanitizedTitle) {
		Course course = Course.find("select c from Course c where c.sanitizedTitle = ?", sanitizedTitle).first();
		return course;
	}
	
	public List<Question> getQuestionsAskedBySocialUser(long userId) {
		List<Question> questions = CourseSection.find("select q from CourseSection cs join cs.questions q where cs.course.id = ? and q.author.id = ?", this.id, userId).fetch();
		return questions;
	}
	
	public List<Answer> getAnswersGivenBySocialUser(long userId) {
		List<Answer> answers = CourseSection.find("select a from CourseSection cs join cs.questions q join q.answers a where cs.course.id = ? and a.author.id = ?", this.id, userId).fetch();
		return answers;
	}
	
	public boolean isSocialUserEnrolled(String userId) {
		boolean retVal = false;
		if(userId != null) {
			try {
				long lUserId = Long.parseLong(userId);
				List<SocialUser> socialUsers = Course.find("select ep from Course c join c.enrolledParticipants ep where c.id = ? and ep.id = ?", this.id, lUserId).fetch();
				if(socialUsers.size() > 0) {
					retVal = true;
				}
				//TODO: Use specific EXception
			} catch(Exception e) {
				cLogger.error("Could not convert string into long for SocialUser.ID");
			}
		}
		return retVal;
	}
	
	public long getEnrolledUsersCount() {
		return this.enrolledParticipants.size();
	}
	
	public List<DIYCourseEvent> getUpdates() {
		return DIYCourseEvent.tailByCourse(this, 1, 100);
	}
	
	public static Course findCourseForActivity(Activity activity) {
		String query = "select c from Course c join c.activities a where a.id = ?";
		Course course = Course.find(query, activity.id).first();
		return course;
	}
}
