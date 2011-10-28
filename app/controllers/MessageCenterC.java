package controllers;

import models.MessageCenter;
import models.PrivateMessage;
import models.SocialUser;
import other.json.JsonMessage;
import play.mvc.Controller;
import play.mvc.With;

@With({Secure.class, SocialAuthC.class})
public class MessageCenterC extends Controller {
	
	public static void inbox(long page, long size) {
		SocialUser user = 
			SocialUser.findById(Long.parseLong(Security.connected()));
		
		MessageCenter messageCenter = MessageCenter.findByUserId(user.id);
		int pages = calculatePages(messageCenter.inbox.size(), (int)size);
		
		render(messageCenter, page, size, pages);
	}
	
	public static void inboxMessage(long messageId) {
		//TODO: Make sure this message belongs to the logged in user
		SocialUser user = 
			SocialUser.findById(Long.parseLong(Security.connected()));
		
		MessageCenter messageCenter = MessageCenter.findByUserId(user.id);
		
		PrivateMessage message = messageCenter.findInboxMessage(messageId);
		if(message != null) {
			JsonMessage jsonMessage = JsonMessage.build(message);
			message.messageProperties.isRead = true;
			message.save();
			renderJSON(jsonMessage);
		} else {
			renderJSON("error");
		}		
	}
	
	private static int calculatePages(int count, int size) {
		if(count == 0 || size == 0) {
			return 1;
		}
		
		int pages = (int)(count/size);
		if(count % size > 0) {
			pages++;
		}
		return pages;
	}
}