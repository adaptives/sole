package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import other.utils.LinkGenUtils;

import models.Activity;
import models.ActivityResponse;
import models.Answer;
import models.Forum;
import models.InvalidUserIdException;
import models.Question;
import models.SessionPart;
import models.SocialUser;
import models.StudySession;
import models.StudySessionApplication;
import models.StudySessionEvent;
import play.Logger;
import play.data.validation.Required;
import play.data.validation.URL;
import play.mvc.Controller;
import play.mvc.Scope.Session;
import play.mvc.With;

@With({Secure.class, SocialAuthC.class})
public class StudySessionSecureC extends Controller {
	
	public static org.apache.log4j.Logger cLogger = 
							Logger.log4j.getLogger(StudySessionSecureC.class);
	
	private enum QuestionActivityType {
		QUESTION,
		ANSWER;
	}
	
	public static void studySessionForm() {
		if(Security.check("admin")) {
			render();
		} else {
			renderText("You are not authorized to perform this action !");
		}
	}
	
	public static void create(String title,
						      String description,
							  String startDate,
							  String endDate,
							  String applicationText,
							  String sessionPartCount) {
		if(Security.check("admin")) {
			SimpleDateFormat dateFormat = new SimpleDateFormat();
			dateFormat.applyPattern("yyyy-MM-dd");
			try {
				StudySession studySession = new StudySession(title, 
														 	 description, 
														 	 dateFormat.parse(startDate),
														 	 dateFormat.parse(endDate));
				studySession.applicationText = applicationText;
				studySession.save();
				
				
				int iSessionPartCount = Integer.parseInt(sessionPartCount);
				for(int i = 0; i < iSessionPartCount; i++) {
					SessionPart sessionPart = 
						new SessionPart(title + " - session part " + i, 
										dateFormat.parse(startDate),
							 	 		dateFormat.parse(endDate),
										"please add content", 
										studySession);
					sessionPart.save();
				}
			} catch(ParseException pe) {
				cLogger.error("Could not create StudySession", pe);
				flash.error("Please fix errors");
				redirect("/StudySessionSecureC/studySessionForm.html");
			}
			redirect("/admin/studysessions");
		} else {
			renderText("You are not authorized to perform this action !");
		}
	}
	
	public static void createSessionPart(long studySessionId,
										 String title,
		      							 String content,
		      							 String startDate,
		      							 String endDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd");
		try {
			StudySession studySession = StudySession.findById(studySessionId);
			SessionPart sessionPart = new SessionPart(title, dateFormat.parse(startDate), dateFormat.parse(endDate), content, studySession);
			studySession.sessionParts.add(sessionPart);
			studySession.save();
		} catch(ParseException pe) {
			cLogger.error("Could not create SessionPart", pe);
			flash.error("Please fix errors");
			redirect("/admin/sessionparts/new");
		}
		redirect("/admin/sessionparts");
	}
	
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
			createEventForQuestionActivity(user, studySessionId, question, QuestionActivityType.QUESTION);
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
			createEventForQuestionActivity(user, studySessionId, question, QuestionActivityType.ANSWER);
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
	
	/**
	 * Checks if the user is eligible to apply for the course and redirects
	 * the user to the application page
	 * @param id
	 */
	public static void apply(long id) {
		
		if(Security.isConnected()) {			
			String userId = Security.connected();			
			SocialUser connectedUser = SocialUser.findById(Long.parseLong(userId));
			if(connectedUser != null) {
				StudySession studySession = StudySession.findById(id);
				if(studySession != null) {
					if(studySession.facilitators.contains(connectedUser)) {
						flash.error("You cannot enroll for this course because you are already a facilitator");
					} else if(studySession.canEnroll(connectedUser.id)) {
						render(studySession);
					} else {
						flash.error("Sorry you cannot enroll for this course right now");
					}
				} else {					
					cLogger.error("Could not find studySession '" + id + "'");
					flash.error(MessageConstants.INTERNAL_ERROR);
				}
			} else {
				//TOOD: Generate a random number to associate with this error
				cLogger.error("Could not find socialUser with id '" + userId + "'");
				flash.error(MessageConstants.INTERNAL_ERROR);
			}
			//TODO: Could we have just rendered the view and passed id to it?
			//studySession(id);
			//We are redirecting to the original URL... TODO: Make this a utility method
			String url = flash.get("url");
	        if(url == null) {
	            url = "/";
	        }
	        redirect(url);
		} else {
			flash.put("url", request.method == "GET" ? request.url : "/");
			try {
				Secure.login();
			} catch(Throwable t) {
				flash.error(MessageConstants.INTERNAL_ERROR);
				cLogger.error("Could not redirect user to the login " +
							  "page before registering for a studySession", t);
				StudySessionC.studySession(id);
			}
		}
	}
	
	public static void application(long id, String application) {
		if(Security.isConnected()) {			
			String userId = Security.connected();			
			SocialUser connectedUser = SocialUser.findById(Long.parseLong(userId));
			if(connectedUser != null) {
				StudySession studySession = StudySession.findById(id);
				if(studySession != null) {
					if(studySession.facilitators.contains(connectedUser)) {
						flash.error("You cannot enroll for this course because you are already a facilitator");
					} else if(studySession.canEnroll(connectedUser.id)) {
						studySession.addApplication(connectedUser, application);
						studySession.save();
					} else {
						flash.error("Sorry you cannot enroll for this course right now");
					}
				} else {					
					flash.error(MessageConstants.INTERNAL_ERROR);
				}
			} else {
				flash.error(MessageConstants.INTERNAL_ERROR);
			}
			//TODO: Could we have just rendered the view and passed id to it?
			//studySession(id);
			//We are redirecting to the original URL... TODO: Make this a utility method
			String url = flash.get("url");
	        if(url == null) {
	        	StudySessionC.studySession(id);
	        }
	        redirect(url);
		} else {
			flash.put("url", request.method == "GET" ? request.url : "/");
			try {
				Secure.login();
			} catch(Throwable t) {
				flash.error(MessageConstants.INTERNAL_ERROR);
				cLogger.error("Could not redirect user to the login " +
							  "page before registering for a studySession", t);
				StudySessionC.studySession(id);
			}
		}
	}
	
	public static void deregister(long id) {
		long studySessionId = id;
		render(studySessionId);
	}
	
	//TODO: This url should be called only via a POST request
	public static void deregistration(long id, String cause) {
		if(Security.isConnected()) {						
			String userId = Security.connected();			
			SocialUser connectedUser = SocialUser.findById(Long.parseLong(userId));
			if(connectedUser != null) {
				StudySession studySession = StudySession.findById(id);
				if(studySession != null) {
					studySession.deregister(connectedUser.id, cause);
					//TODO: We should need only one, which one?
					studySession.applicationStore.save();
					studySession.save();
				} else {
					cLogger.warn("StudySession is null '" + id + "'");
					flash.error(MessageConstants.INTERNAL_ERROR);
				}
			} else {
				cLogger.error("could not get user object for username '" + userId + "'");				
				flash.error(MessageConstants.INTERNAL_ERROR);
			}
			//TODO: Could we have just rendered the view and passed id to it?
			StudySessionC.studySession(id);
		} else {
			cLogger.error("A user is not logged in. This should never happen for this action");
			flash.error(MessageConstants.INTERNAL_ERROR);
		}
	}
	
	public static void manageParticipants(long studySessionId) {
		if(canFacilitate(studySessionId)) {
			StudySession studySession = StudySession.findById(studySessionId);
			List<StudySessionApplication> pendingApplications = studySession.getPendingApplications();
			render(studySessionId, pendingApplications);
		}		
	}
	
	public static int postActivityResponse(long activityId, 
										   String activityResponse) {
		Activity activity = Activity.findById(activityId);
		if(activityResponse != null && !activityResponse.equals("")) {
			String sUserId = Security.connected();
			long userId = Long.parseLong(sUserId);
			SocialUser user = SocialUser.findById(userId);
			if(user != null) {
				ActivityResponse activityResponseObj = 
					new ActivityResponse(user, activity, activityResponse);
				activityResponseObj.save();
				createEventForActivityResponse(user, activity, activityResponse);
			}			
		}
		
		return activity.activityResponses.size();
	}

	private static boolean canFacilitate(long studySessionId) {
		if(Security.isConnected()) {
			String userId = Session.current().get(SocialAuthC.USER);
			StudySession studySession = StudySession.findById(studySessionId);
			try {
				return studySession.isFacilitator(userId) || Security.check("admin");
			} catch(InvalidUserIdException iue) {
				//TODO: Should this be an internal error?
				cLogger.warn("incorrect userId obtained from database ", iue);
				return false;
			}
		}		
		return false;
	}
	
	private static boolean isParticipant(long studySessionId) {
		if(Security.isConnected()) {
			String userId = Session.current().get(SocialAuthC.USER);
			StudySession studySession = StudySession.findById(studySessionId);
			if(studySession != null && userId != null) {
				try {
				return studySession.isFacilitator(userId) || 
					   studySession.isUserEnrolled(userId);
				} catch(InvalidUserIdException iue) {
					//TODO: Should this be an internal error?
					cLogger.warn("incorrect userId obtained from database ", iue);
					return false;
				}
			}
		}
		return false;
	}
	
	private static void createEventForQuestionActivity(SocialUser user,
												  	   long studySessionId, 
												  	   Question question,
												  	   QuestionActivityType questionActivityType) {
		
		if(user == null) {
			throw new NullPointerException("user cannot be null");
		}
		
		if(studySessionId < 1) {
			throw new IllegalArgumentException("Incorrect studySessionId");
		}
		
		if(question == null) {
			throw new NullPointerException("question cannot be null");
		}
		
		if(questionActivityType == null) {
			throw new NullPointerException("questionActivityType cannot be null");
		}
		
		String questionLink = LinkGenUtils.getStudyGroupQuestionLink(
				studySessionId, question);

		String userLink = LinkGenUtils.getUserProfileLink(user);

		String text = "";
		String title = "";
		if(questionActivityType == QuestionActivityType.QUESTION) {
			text += userLink + " posted a new question : " + questionLink;
			title += "New forum question";
		} else if(questionActivityType == QuestionActivityType.ANSWER) {
			text += userLink + " posted a new answer for question : " + questionLink;
			title += "New forum answer";
		} 
		
		StudySession studySession = StudySession.findById(studySessionId);

		if (studySession != null) {
			new StudySessionEvent(studySession, title, text);
		} else {
			String msg = "Could not create StudySessionEvent for new question : user '"
					+ user
					+ "' question '"
					+ question
					+ "' studySessionId '"
					+ studySessionId + "'";
			cLogger.error(msg);
		}

	}
	
	private static void createEventForActivityResponse(SocialUser user,
													   Activity activity, 
													   String activityResponse) {
		
		if(user == null) {
			throw new NullPointerException("user cannot be null");
		}
		
		if(activity == null) {
			throw new NullPointerException("activity cannot be null");
		}
		
		if(activityResponse == null) {
			throw new NullPointerException("activityResponse cannot be null");
		}
		
		String userLink = LinkGenUtils.getUserProfileLink(user);
		//StudySession studySession = activity.findStudySession();
		SessionPart sessionPart = activity.findSessionPart();
		if(sessionPart != null) {
			String text = userLink + " has posted a " + LinkGenUtils.getViewAllResponsesLink(sessionPart.studySession.id, sessionPart.id) + " to the activity '" + activity.title + "'";
			String title = "New activity response";
			new StudySessionEvent(sessionPart.studySession, title, text);
		} else {
			cLogger.error("Could not find StudySession for activity '" + activity.id + "'");
		}
		
	}
}
