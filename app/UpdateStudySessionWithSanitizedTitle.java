import java.util.ArrayList;
import java.util.List;

import other.utils.StringUtils;

import models.SocialUser;
import models.StudySession;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class UpdateStudySessionWithSanitizedTitle extends Job {
	
	public void doJob() {
		List<StudySession> studySessions = StudySession.findAll();
		for(StudySession studySession : studySessions) {
			if(studySession.sanitizedTitle == null) {
				studySession.sanitizedTitle = 
					StringUtils.replaceSpaceWithDashes(studySession.title);
				studySession.save();
			}
		}
	}
}
