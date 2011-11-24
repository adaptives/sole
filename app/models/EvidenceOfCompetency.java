package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class EvidenceOfCompetency extends Model {
	
	public String note;
	
	public EvidenceOfCompetency(String note) {
		this.note = note;
	}
}
