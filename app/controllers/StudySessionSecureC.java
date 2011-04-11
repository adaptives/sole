package controllers;

import java.util.List;

import models.Answer;
import models.Forum;
import models.Question;
import models.SocialUser;
import models.StudySession;
import models.StudySessionApplication;
import play.Logger;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.Scope.Session;
import play.mvc.With;

@With(Secure.class)
public class StudySessionSecureC extends Controller {
	
	public static org.apache.log4j.Logger cLogger = 
							Logger.log4j.getLogger(StudySessionSecureC.class);
	
	public static void postQuestion(long studySessionId, long forumId,
			@Required String title, @Required String content, String tags) {
		
		if (isParticipant(studySessionId)) {
			SocialUser user = 
				SocialUser.findById(Long.parseLong(Security.connected()));
			
			Forum forum = Forum.findById(forumId);
			Question question = new Question(title, content, user);
			if (tags != null) {
				String tagArray[] = tags.split(",");
				if (tagArray != null) {
					for (String tag : tagArray) {
						question.tagWith(tag);
					}
				}
			}

			forum.questions.add(question);
			forum.save();
			StudySessionC.forum(studySessionId);
		} else {
			cLogger.info("user '" + Session.current().get(SocialAuthC.USER)
					+ "' must be enrolled in StudySession '" + studySessionId
					+ "' before posting questions");
		}

	}
	
	
	public static void postAnswer(long studySessionId, 
								  long forumId, 
								  long questionId,
								  String answerContent) {
		Question question = Question.findById(questionId);
		SocialUser user = SocialUser.findById(Long.parseLong(Security.connected()));
		if(user != null) {
			Answer answer = new Answer(answerContent, user, question);
			question.answers.add(answer);
			question.save();
			StudySessionC.forumQuestion(studySessionId, questionId);
		} else {
			cLogger.info("user '" + user.id + "' must be signed in before " +
						 "posting answers");
		}
	}
	
	public static void makeFacilitator(long studySessionId, long userId) {
		if(canFacilitate(studySessionId)) {
			StudySession studySession = StudySession.findById(studySessionId);
			studySession.addFacilitator(userId);
			studySession.save();
			StudySessionC.participants(studySessionId);
		} else {
			cLogger.warn("Unauthorized attempt to maeFacilitator by '" + 
						  userId + "' for course '" + studySessionId + "'");
		}
	}
	
	public static void acceptApplication(long studySessionId,
										 long applicationId, 
										 String comment) {

		if(canFacilitate(studySessionId)) {
			StudySessionApplication studySessionApplication = 
									StudySessionApplication.findById(applicationId);
			studySessionApplication.changeStatus(1, comment);
			studySessionApplication.save();
			manageParticipants(studySessionId);
		}
	}
	
	public static void rejectApplication(long studySessionId, 
			 							 long applicationId, 
			 							 String comment) {

		if(canFacilitate(studySessionId)) {
			StudySessionApplication studySessionApplication = 
				StudySessionApplication.findById(applicationId);
			studySessionApplication.changeStatus(-1, comment);
			studySessionApplication.save();
			manageParticipants(studySessionId);
		}
		
	}
	
	public static void manageParticipants(long studySessionId) {
		if(canFacilitate(studySessionId)) {
			StudySession studySession = StudySession.findById(studySessionId);
			List<StudySessionApplication> pendingApplications = studySession.getPendingApplications();
			render(studySessionId, pendingApplications);
		}		
	}

	private static boolean canFacilitate(long studySessionId) {
		if(Security.isConnected()) {
			String userId = Session.current().get(SocialAuthC.USER);
			StudySession studySession = StudySession.findById(studySessionId);
			return studySession.isFacilitator(userId) || Security.check("admin");
		}		
		return false;
	}
	
	private static boolean isParticipant(long studySessionId) {
		if(Security.isConnected()) {
			String userId = Session.current().get(SocialAuthC.USER);
			StudySession studySession = StudySession.findById(studySessionId);
			if(studySession != null && userId != null) {
				return studySession.isFacilitator(userId) || 
					   studySession.isUserEnrolled(userId);
			}			
		}		
		return false;
	}
}
