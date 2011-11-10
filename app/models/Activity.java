package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import play.Logger;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Activity extends Model implements Comparable {
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(Activity.class);
	
	public String title;
	
	@Column(nullable=true)
	public Integer placement;
	
	@Required
	@Lob
	@MaxSize(20000)
	public String content;
	
	@OneToMany(mappedBy="activity", cascade=CascadeType.ALL)
	@OrderBy("timestamp DESC")
	public Set<ActivityResponse> activityResponses;
	
	public Activity(String title, String content) {
		this(title, content, 0);
	}
	
	public Activity(String title, String content, int placement) {
		this.title = title;
		this.placement = placement;
		this.content = content;
		this.activityResponses = new TreeSet<ActivityResponse>();
		create();
	}
	
	public boolean hasResponded(String sUserId) {
		boolean retVal = false;
		try {
			long userId = Long.parseLong(sUserId);
			String query = "select count(distinct ar) from Activity a join a.activityResponses ar where a.id = ? and ar.user.id = ?";
			if (ActivityResponse.count(query, this.id, userId) > 0) {
				retVal = true;
			}			
		} catch(Exception e) {
			cLogger.warn("Recieved incorrect userId '" + sUserId + "'");
		}
		return retVal;
	}
	
	public List<ActivityResponse> getResponsesByUser(String sUserId) {
		List<ActivityResponse> retVal = new ArrayList<ActivityResponse>();
		try {
			long userId = Long.parseLong(sUserId);
			String query = "select ar from Activity a join a.activityResponses ar where a.id = ? and ar.user.id = ?";
			List<ActivityResponse> activityResponses = ActivityResponse.find(query, this.id, userId).fetch();
			if(activityResponses != null) {
				retVal = activityResponses;
			}
		} catch(Exception e) {
			cLogger.warn("Recieved incorrect userId '" + sUserId + "'");
		}
		return retVal;
	}
	
	public List<ActivityResponse> getAllResponses() {
		List<ActivityResponse> activityResponses;
		String query = "select ar from Activity a join a.activityResponses ar where a.id = ?";
		activityResponses = Activity.find(query, this.id).fetch();
		if(activityResponses == null) {
			activityResponses = new ArrayList<ActivityResponse>();
		}
		return activityResponses;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.id) + " " + this.title;
	}

	@Override
	public int compareTo(Object arg0) {
		Activity other = (Activity)arg0;
		return this.placement.compareTo(other.placement);
	}

}
