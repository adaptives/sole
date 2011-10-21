package models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class MessageCenter extends Model {

	@OneToOne
	public SocialUser owner;
	
	@ManyToMany
	@JoinTable(name="MessageCenter_Inbox")
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
	
	public List<PrivateMessage> getMessages(int count) {
		return null;
	}
}