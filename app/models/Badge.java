package models;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class Badge extends Model {
	@ManyToOne
	public SocialUser awardee;
	
	@ManyToMany
	public List<SocialUser> awardedBy;
	
	@ManyToOne
	public BadgeDef badgeDef;
	
	public Date timestamp;
	
	public String note;
	
	@OneToOne
	public EvidenceOfCompetency evidence;
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(Badge.class);
	
	public Badge(BadgeDef badgeDef,
				 SocialUser awardee,
			     List<SocialUser> awardedBy,
			     String note) {
		this.badgeDef = badgeDef;
		this.awardee = awardee;
		this.awardedBy = awardedBy;
		this.note = note;
		this.timestamp = new Date();
	}
	
	public static List<Badge> fetchBadgesForSocialUser(String sUserId) {
		List<Badge> badges = new ArrayList<Badge>();
		try {
			long userId = Long.parseLong(sUserId);
			String query = "select b from Badge b join b.awardee a where a.id = ?";
			badges = Badge.find(query, userId).fetch();
		} catch(Exception pe) {
			cLogger.error("Invalid userId '" + sUserId + "'");
		}
		return badges;
	}

	
}
