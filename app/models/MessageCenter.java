package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class MessageCenter extends Model {
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(MessageCenter.class);

	@OneToOne
	public SocialUser owner;
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("timestamp DESC")
	public Set<PrivateMessage> inbox;
	
	public MessageCenter(SocialUser owner) {
		this.owner = owner;
		this.inbox = new TreeSet<PrivateMessage>();
	}

	public static MessageCenter findByUserId(Long id) {
		String query = "select mc from MessageCenter mc where mc.owner.id = ?";
		MessageCenter messageCenter = MessageCenter.find(query, id).first();
		return messageCenter;
	}
	
	public PrivateMessage findInboxMessage(long messageId) {
		String query = "select m from MessageCenter mc join mc.inbox as m where mc.id = ? and m.id = ?";
		PrivateMessage message = find(query, this.id, messageId).first();
		return message;
	}
	
	public static String messageCount(String userId) {
		String retVal = "";
		try {
			long lUserId = Long.valueOf(userId);
			String query = "select count(distinct m) from MessageCenter mc join mc.inbox m where m.messageProperties.isRead = ? and mc.owner.id = ?";
			long count = MessageCenter.count(query, false, lUserId);
			retVal = count != 0 ? "*" : "";
		} catch(Exception e) {
			cLogger.error("The user is incorrect '" + userId + "'");
		}
		return retVal;
	}
		
	@Override
	public String toString() {
		return this.owner.screenname;
	}
}