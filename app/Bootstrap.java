import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
		if(User.count() == 0) {
			Fixtures.load("users-and-study-sessions.yml");
			Fixtures.load("pages.yml");
			Fixtures.load("diycourses.yml");
		}
	}
}
