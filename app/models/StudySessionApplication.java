package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class StudySessionApplication extends Model {
	
	public SocialUser socialUser;
	public StudySession studySession;
	public String application;
	
	public StudySessionApplication(SocialUser socialUser,
								   StudySession studySession,
								   String application) {
		this.socialUser = socialUser;
		this.studySession = studySession;
		this.application = application;
		create();
	}
	
}
