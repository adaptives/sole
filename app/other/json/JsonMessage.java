package other.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import models.PrivateMessage;
import models.SocialUser;

public class JsonMessage {

	public final long from;
	public final long to;
	public final Date timestamp;
	public final String subject;
	public final String body;
	
	public static JsonMessage build(PrivateMessage message) {
		long from = message.from.id;
		long to = message.to.id;
		Date timestamp = message.timestamp;
		String subject = message.subject;
		String body = message.body;
		JsonMessage jsonMessage = new JsonMessage(from, to, timestamp, subject, body);
		
		return jsonMessage;
	}
	
	private JsonMessage(long from, 
						long to,
						Date timestamp,
						String title,
						String body) {
		this.from = from;
		this.to = to;
		this.timestamp = timestamp;
		this.subject = title;
		this.body = body;
	}
}