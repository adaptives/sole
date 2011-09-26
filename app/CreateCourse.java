import java.util.List;

import models.Course;
import models.CourseCategory;
import models.CourseSection;
import models.Feedback;
import models.ModeratedFeedback;
import models.Pastebin;
import models.SocialUser;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class CreateCourse extends Job {

	private String bannedWords[] = {"viagra", "cialis", "weight", "drug", "is it yours too", "rx", "news.com", "sleep", "virility", "hot"};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void doJob() {
		List<Feedback> feedbacks = Feedback.findAll();
		for(Feedback feedback : feedbacks) {
			boolean copy = true;
			for(String bannedWord : bannedWords) {
				if(feedback.message != null) {
					String capsMessage = feedback.message.toUpperCase();
					if(capsMessage.contains(bannedWord.toUpperCase())) {
						copy = false;
					}
				}
			}
			if(copy) {
				ModeratedFeedback mFeedback = new ModeratedFeedback(feedback.name, feedback.email, feedback.message);
				mFeedback.timestamp = null;
				mFeedback.save();
			}
		}
	}
}
