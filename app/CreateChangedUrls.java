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
//		List<StudySession> studySessions = StudySession.findAll();		
//		
//		for(StudySession studySession : studySessions) {
//			
//			// Link to show question
//			for(Question question : studySession.forum.questions) {
//				Map<String, Object> showStudySessionDiscussionQuestionArgs = new HashMap<String, Object>();
//				showStudySessionDiscussionQuestionArgs.put("sanitizedTitle", studySession.sanitizedTitle);
//				showStudySessionDiscussionQuestionArgs.put("questionId", question.id);
//				showStudySessionDiscussionQuestionArgs.put("sanitizedQuestionTitle", question.sanitizedTitle);
//				String newForumQuestionUrl = Router.reverse("StudySessionC.forumQuestion", showStudySessionDiscussionQuestionArgs).url;
//				String oldForumQuestionUrl = "/studysessions/studysession/" + studySession.id + "/forum/question/" + question.id;
//				ChangedUrl forumQuestionChangedUrl = ChangedUrl.find("select cu from ChangedUrl cu where cu.oldUrl = ?", oldForumQuestionUrl).first();
//				forumQuestionChangedUrl.newUrl = newForumQuestionUrl;
//				forumQuestionChangedUrl.save();
//			}
//			
//			for(SessionPart sessionPart : studySession.sessionParts) {
//				//sessionPart(String sanitizedTitle, String sessionPartSanitizedTitle)
//				
//				// Link to show a session part
//				Map<String, Object> showSessionPartArgs = new HashMap<String, Object>();
//				showSessionPartArgs.put("sanitizedTitle", studySession.sanitizedTitle);
//				showSessionPartArgs.put("sessionPartSanitizedTitle", sessionPart.sanitizedTitle);
//				String newSessionPartUrl = Router.reverse("StudySessionC.sessionPart", showSessionPartArgs).url;
//				String oldSessionPartUrl = "/studysessions/studysession/" + studySession.id + "/sessionpart/" + sessionPart.id;
//				ChangedUrl sessionPartChangedUrl = ChangedUrl.find("select cu from ChangedUrl cu where cu.oldUrl = ?", oldSessionPartUrl).first();
//				sessionPartChangedUrl.newUrl = newSessionPartUrl;
//				sessionPartChangedUrl.save();
//				
//				// View all activity responses of a session sessionPartActivityResponses(String sanitizedTitle, String sessionPartSanitizedTitle)
//				Map<String, Object> showSessionPartActivityResponsesArgs = new HashMap<String, Object>();
//				showSessionPartActivityResponsesArgs.put("sanitizedTitle", studySession.sanitizedTitle);
//				showSessionPartActivityResponsesArgs.put("sessionPartSanitizedTitle", sessionPart.sanitizedTitle);
//				String newSessionPartActivityResponseUrl = Router.reverse("StudySessionC.sessionPartActivityResponses", showSessionPartActivityResponsesArgs).url;
//				String oldSessionPartActivityResponseUrl = "/studysessions/studysession/" + studySession.id + "/sessionpart/" + sessionPart.id + "/activity/responses";
//				ChangedUrl sessionPartActivityResponseChangedUrl = ChangedUrl.find("select cu from ChangedUrl cu where cu.oldUrl = ?", oldSessionPartActivityResponseUrl).first();
//				sessionPartActivityResponseChangedUrl.newUrl = newSessionPartActivityResponseUrl;
//				sessionPartActivityResponseChangedUrl.save();
//			}
//			
//		}
	}
}
