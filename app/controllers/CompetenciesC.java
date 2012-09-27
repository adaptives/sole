/**
 * 
 */
package controllers;

import java.util.List;

import models.Competency;
import models.Topic;
import play.mvc.Controller;

/**
 * @author pshah
 *
 */
public class CompetenciesC extends Controller {

	public static void topics() {
		List<Topic> topics = Topic.findAll();		
		render(topics);
	}
	
	public static void topic(String sanitizedTitle) {
		Topic topic = Topic.fetchBySanitizedTitle(sanitizedTitle);
		notFoundIfNull(topic, "Topic '" + sanitizedTitle + "' not found");
		render(topic);
	}
	
	public static void competencyGroup(String sanitizedTopicTitle, 
									   String sanitizedCompetencyGroupTitle) {
		render();
	}
	
	public static void competency(String sanitizedTopicTitle, 
								  String sanitizedCompetencyTitle) {
		Competency competency = Competency.fetchBySanitizedTitle(sanitizedCompetencyTitle);
		notFoundIfNull(competency);
		render(competency);
	}

}
