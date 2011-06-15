import java.util.ArrayList;
import java.util.List;

import other.utils.StringUtils;

import models.Question;
import models.SessionPart;
import models.SocialUser;
import models.StudySession;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class UpdateQuestionWithSanitizedTitle extends Job {
	
	public void doJob() {
//		List<Question> questions = Question.findAll();
//		for(Question question : questions) {
//			question.sanitizedTitle = 
//				StringUtils.replaceSpaceWithDashes(question.title);
//			question.save();
//		}
	}
}
