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
		Topic topic2 = new Topic("Python", "Python", "Python");
		topics.add(topic2);
		Topic topic3 = new Topic("Ruby", "Ruby", "Ruby");
		topics.add(topic3);
		Topic topic4 = new Topic("Perl", "Perl", "Perl");
		topics.add(topic4);
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
