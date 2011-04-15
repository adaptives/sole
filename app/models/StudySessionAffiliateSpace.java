package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class StudySessionAffiliateSpace extends Model {
	
	public int location;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	public List<AffiliateCode> affiliateCodes;
	
	@ManyToOne
	public StudySession studySession;
	
	public StudySessionAffiliateSpace(int location) {
		this.location = location;
		create();
	}
}
