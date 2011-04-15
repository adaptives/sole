package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class DefaultStudySessionAffiliatez extends Model {
	
	String studySessionListAffiliate;
	
	@OneToMany(cascade = CascadeType.ALL)
	List<DefaultSudySessionLocationAffiliate> defaultLocationAffiliates;
	
	public DefaultStudySessionAffiliatez(String studySessionAffiliate) {
		this.studySessionListAffiliate = studySessionAffiliate;
		create();
	}
}
