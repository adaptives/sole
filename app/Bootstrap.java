import models.User;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
		if(Play.mode == Play.Mode.DEV) {
			Fixtures.load("users-and-study-sessions.yml");
			Fixtures.load("pages.yml");
			Fixtures.load("diycourses.yml");
			Fixtures.load("kvdata.yml");
			Fixtures.load("site-events.yml");
		}

	}
}
