package models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class ApplicationStore extends Model {
	@OneToOne
	public StudySession studySession;
	
	@OneToMany
	public Set<StudySessionApplication> pendingApplications;
	
	@OneToMany
	public Set<StudySessionApplication> acceptedApplications;
	
	@OneToMany
	public Set<StudySessionApplication> rejectedApplications;
	
	@OneToMany
	public Set<StudySessionApplication> deregisteredApplications;
}
