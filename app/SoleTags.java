import groovy.lang.Closure;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.FeedC;
import controllers.Security;

import models.KeyValueData;
import models.SidebarWidget;
import models.SocialUser;

import play.mvc.Router;
import play.mvc.Router.ActionDefinition;
import play.mvc.Scope.Session;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;


public class SoleTags extends FastTags {
	
	
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
	
}
