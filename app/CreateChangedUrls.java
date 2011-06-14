import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import other.utils.StringUtils;

import models.ChangedUrl;
import models.Question;
import models.SessionPart;
import models.SocialUser;
import models.StudySession;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.mvc.Router;
import play.test.Fixtures;

@OnApplicationStart
public class CreateChangedUrls extends Job {
	
	public void doJob() {
		List<StudySession> studySessions = StudySession.findAll();		
		
		for(StudySession studySession : studySessions) {
			// Link to show the study session
			Map<String, Object> showStudySessionArgs = new HashMap<String, Object>();
			showStudySessionArgs.put("id", studySession.id);
			new ChangedUrl(Router.reverse("StudySessionC.studySession", showStudySessionArgs).url, "");	
			
			// Link to show participants
			Map<String, Object> showStudySessionParticipantsArgs = new HashMap<String, Object>();
			showStudySessionParticipantsArgs.put("studySessionId", studySession.id);
			new ChangedUrl(Router.reverse("StudySessionC.participants", showStudySessionParticipantsArgs).url, "");
			
			// Link to show resources
//			Map<String, Object> showStudySessionResourcesArgs = new HashMap<String, Object>();
//			showStudySessionResourcesArgs.put("studySessionId", studySession.id);
//			System.out.println(Router.reverse("StudySessionC.resources", showStudySessionResourcesArgs).url);
			
			// Link to show discussion forum
			Map<String, Object> showStudySessionDiscussioArgs = new HashMap<String, Object>();
			showStudySessionDiscussioArgs.put("studySessionId", studySession.id);
			new ChangedUrl(Router.reverse("StudySessionC.forum", showStudySessionDiscussioArgs).url, "");
			
			// Link to show question
			for(Question question : studySession.forum.questions) {
				Map<String, Object> showStudySessionDiscussionQuestionArgs = new HashMap<String, Object>();
				showStudySessionDiscussionQuestionArgs.put("studySessionId", studySession.id);
				showStudySessionDiscussionQuestionArgs.put("questionId", question.id);
				new ChangedUrl(Router.reverse("StudySessionC.forumQuestion", showStudySessionDiscussionQuestionArgs).url, "");
			}
			
			
			for(SessionPart sessionPart : studySession.sessionParts) {
				// Link to show a session part
				Map<String, Object> showSessionPartArgs = new HashMap<String, Object>();
				showSessionPartArgs.put("studySessionId", studySession.id);
				showSessionPartArgs.put("id", sessionPart.id);
				new ChangedUrl(Router.reverse("StudySessionC.sessionPart", showSessionPartArgs).url, "");
				
				// View all activity responses of a session sessionPartActivityResponses(long studySessionId, long sessionPartId) {
				Map<String, Object> showSessionPartActivityResponsesArgs = new HashMap<String, Object>();
				showSessionPartActivityResponsesArgs.put("studySessionId", studySession.id);
				showSessionPartActivityResponsesArgs.put("sessionPartId", sessionPart.id);
				new ChangedUrl(Router.reverse("StudySessionC.sessionPartActivityResponses", showSessionPartActivityResponsesArgs).url, "");
			}
			
		}
	}
}
