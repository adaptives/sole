import groovy.lang.Closure;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.FeedC;
import controllers.Security;

import models.AffiliateCode;
import models.KeyValueData;
import models.SidebarWidget;
import models.SocialUser;
import models.StudySession;
import models.StudySessionAffiliateSpace;
import models.StudySessionMeta;

import play.mvc.Router;
import play.mvc.Router.ActionDefinition;
import play.mvc.Scope.Session;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;


public class SoleTags extends FastTags {
	
	public static void _studySessionMetadata(Map<?, ?> args, 
								Closure body, 
								PrintWriter out, 
								ExecutableTemplate template, 
								int fromLine) {
		
		StudySession studySession = (StudySession)args.get("ss");
		StudySessionMeta studySessionMeta = (StudySessionMeta)args.get("ssm");		
		if(studySession == null) {
			return;
		}
		if(studySessionMeta == null) {
			return;
		}
		out.print("<span class=\"course-status\">");
		if(studySessionMeta.canceled) {
			out.println("<span style=\"color: #E02A02; weight: bold; font-size: 1.2em;\">This course has been cancelled </span>");
		} else if(studySessionMeta.locked) {
			out.println("<span style=\"color: #E03008; weight: bold; font-size: 1.2em;\">This course is closed for further participation </span>");	
		} else if(studySessionMeta.enrollmentClosed) {
			out.print("<span style=\"color: #E03008; weight: bold; font-size: 1.2em;\">This course is is full and is not accepting new applications </span>");
		} else {
			//out.print("<span style=\"color: #0090CE; weight: bold; font-size: 1.2em;\">This Course is accepting new applications </span>");
		}
		out.print("</span>");
		
		out.print("<div class=\"course-status\">");
		
		if(Security.isConnected()) {
			String userId = Security.connected();
			long lUserId = Long.parseLong(userId);
			out.print("<div>");
			if(studySession.isFacilitator(lUserId)) {
				out.println("<span style=\"color: #038E01; weight: bold; font-size: 1.2em;\"> You are a facilitator in this course. </span>");
			} else if(studySession.applicationStore.isUserApplicationAccepted(lUserId)) {
				out.print("<span style=\"color: #038E01; weight: bold; font-size: 1.2em;\">You are enrolled in this course.</span> ");
			} else if(studySession.applicationStore.isUserApplicationPending(lUserId)) {
				out.print("<span style=\"color: #8AD153; weight: bold; font-size: 1.2em;\">Your applcation is pending approval. You will see a Twitter mention in your timeline from @diycs once you are approved. You can also come back here to check. Since we do not ask for your email id, we cannot generate emails when someone is approved.</span>");
			}
			out.print("</div>");
		}
		
		
		
		out.print("</div>");
	}
	
	public static void _apply(Map<?, ?> args, 
							  Closure body,
							  PrintWriter out, 
							  ExecutableTemplate template, 
							  int fromLine) {

		ActionDefinition actionDefinition = null;
		StudySession studySession = (StudySession) args.get("ss");
		StudySessionMeta studySessionMeta = (StudySessionMeta) args.get("ssm");		
		if (studySession == null) {
			return;
		}
		if (studySessionMeta == null) {
			return;
		}
		if (studySessionMeta.canceled) {
			return;
		}
		if (studySessionMeta.locked) {
			return;
		}
		if (studySessionMeta.enrollmentClosed) {
			return;
		}		
		if(Security.isConnected()) {
			String userId = Security.connected();
			long lUserId = Long.parseLong(userId);
			if (studySession.canEnroll(lUserId)) {
				actionDefinition = play.mvc.Router.reverse("StudySessionSecureC.apply");
				String htmlFormBegin = "<form method=\"GET\" action=\"" + actionDefinition.url + "\">";
				String hiddenParam = String.format("<input type=\"hidden\" name=\"id\" value=\"%s\" />", studySession.id);
				String htmlButton = "<div style=\"padding-top:5px;\"><div style=\"padding-top:5px;\"><input class=\"promotional submit button\" value=\"Apply\" type=\"submit\"> </div></div>";
				String htmlFormEnd = "</form>";
				out.println(htmlFormBegin + hiddenParam + htmlButton + htmlFormEnd);
				
				return;
			}
			
			if(studySession.applicationStore.isUserApplicationAccepted(lUserId) || studySession.applicationStore.isUserApplicationPending(lUserId)) {
				actionDefinition = play.mvc.Router.reverse("StudySessionSecureC.deregister");

				String htmlFormBegin = String.format("<form method=\"GET\" action=\"%s\">", actionDefinition.url);
				String hiddenParam = String.format("<input type=\"hidden\" name=\"id\" value=\"%s\" />", studySession.id);
				String htmlButton = "<div style=\"padding-top:5px;\"><div style=\"padding-top:5px;\"><input class=\"promotionalred submitred buttonred\" value=\"Deregister\" type=\"submit\"> </div></div>";
				String htmlFormEnd = "</form>";

				out.println(htmlFormBegin + hiddenParam + htmlButton + htmlFormEnd);
				
				return;
			}
		} else {
			Map actionArgs = new HashMap();
			ActionDefinition actionDef = 
								Router.reverse("Secure.login", actionArgs);
			out.print(" Please <a href=\"" + actionDef.url + "\">login</a> to enroll in this course.");
		}
	}
	
	public static void _keyValueData(Map<?, ?> args, 
			  						 Closure body,
			  						 PrintWriter out, 
			  						 ExecutableTemplate template, 
			  						 int fromLine) {
		String key = (String)args.get("k");
		String val = KeyValueData.findValue(key);
		if(val != null) {
			out.println(val);
		}
	}
	
	public static void _socialButtons(Map<?, ?> args, 
				 					  Closure body,
				 					  PrintWriter out, 
				 					  ExecutableTemplate template, 
				 					  int fromLine) {
		String blogFeedUrl = KeyValueData.findValue(FeedC.BLOG_FEED_URL);
		String twitterUrl = KeyValueData.findValue(FeedC.TWITTER_URL);
		String fbUrl = KeyValueData.findValue(FeedC.FACEBOOK_URL);
		
		if(blogFeedUrl != null && blogFeedUrl != "") {
			out.print("<span style=\"margin-right:5px;\">");
			out.print("<a href=\"" + blogFeedUrl + "\" target=\"_blank\">");
			out.print("<img src=\"/public/images/blog_feed_logo.jpg\" />");
			out.print("</a>");
			out.print("</span>");
		}
		if(twitterUrl != null && twitterUrl != "") {
			out.print("<span style=\"margin-right:5px;\">");
			out.print("<a href=\"" + twitterUrl + "\" target=\"_blank\">");
			out.print("<img src=\"/public/images/twitter_logo.jpg\" />");
			out.print("</a>");
			out.print("</span>");
		}
		if(fbUrl != null && fbUrl != "") {
			//TODO: Proper style and CSS
			out.print("<span style=\"margin-right:5px;\">");
			out.print("<a href=\"" + fbUrl + "\">");
			out.print("<img src=\"/public/images/facebook_logo.jpg\" />");
			out.print("</a>");
			out.print("</span>");
		}
	}
	
	public static void _recentlyPubSS(Map<?, ?> args, Closure body,
									  PrintWriter out, 
									  ExecutableTemplate template, 
									  int fromLine) {
		String feedUrl = KeyValueData.findValue(FeedC.RECENTLY_PUB_SS);

		if (feedUrl != null && feedUrl != "") {
			out.print("<span style=\"margin-right:5px;\">");
			out.print("<a href=\"" + feedUrl + "\">");
			out.print("<img src=\"/public/images/blog_feed_logo.jpg\" /> " + "Please subscribe to this feed to be notified when new study groups are published");
			out.print("</a>");
			out.print("</span>");
		}
	}
	
	public static void _sidebarWidgets(Map<?, ?> args, 
									   Closure body,
			  						   PrintWriter out, 
			  						   ExecutableTemplate template, 
			  						   int fromLine) {
		List<SidebarWidget> sidebarWidgets = SidebarWidget.findAll(); 
			//SidebarWidget.find("select sw from SidebarWidget sw order by sw.position").fetch();
		for(SidebarWidget sidebarWidget : sidebarWidgets) {
			out.println("<div class=\"sidebar-widget\">");
			out.println("<div class=\"sidebar-widget-title\">");
			out.println(sidebarWidget.displayTitle);
			out.println("</div>");
			out.println(sidebarWidget.content);
			out.println("</div>");
		}
	}
	
	public static void _studySessionAffiliatez(Map<?, ?> args, 
			   								   Closure body,
			   								   PrintWriter out, 
			   								   ExecutableTemplate template, 
			   								   int fromLine) {
		Map<Integer, String> flowElementStarts = new HashMap<Integer, String>();
		flowElementStarts.put(1, "<div class=\"study-session-affiliate-v\">");
		flowElementStarts.put(2, "<span class=\"study-session-affiliate-h\">");
		Map<Integer, String> flowElementEnds = new HashMap<Integer, String>();
		flowElementEnds.put(1, "</div>");
		flowElementEnds.put(2, "</span>");
		 
		int location = (Integer)args.get("location"); 
		int flow = (Integer)args.get("flow");
		
		StudySession studySession = (StudySession)args.get("studySession");
		StudySessionAffiliateSpace ssas = 
			StudySessionAffiliateSpace.
				findByStudySessionAndLocation(studySession.id, location);
		
		if(ssas != null) {
			out.print("<div class=\"study-session-affiliates\">");
			for(AffiliateCode code : ssas.affiliateCodes) {
				out.print(flowElementStarts.get(flow));
				out.println(code.code);
				out.print(flowElementEnds.get(flow));
			}
			out.println("</div>");
		}
	}
	
}
