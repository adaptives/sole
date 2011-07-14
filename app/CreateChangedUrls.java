import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import other.utils.StringUtils;

import models.Activity;
import models.ChangedUrl;
import models.Course;
import models.CourseSection;
import models.KeyValueData;
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
	private String chapters[] = {
			"LPTHW Exercise 0: The Setup",
			"LPTHW Exercise 1: A Good First Program", 
			"LPTHW Exercise 2: Comments And Pound Characters",
			"LPTHW Exercise 3: Numbers And Math",
			"LPTHW Exercise 4: Variables And Names", 
			"LPTHW Exercise 5: More Variables And Printing", 
			"LPTHW Exercise 6: Strings And Text", 
			"LPTHW Exercise 7: More Printing", 
			"LPTHW Exercise 8: Printing, Printing", 
			"LPTHW Exercise 9: Printing, Printing, Printing", 
			"LPTHW Exercise 10: What Was That?", 
			"LPTHW Exercise 11: Asking Questions", 
			"LPTHW Exercise 12: Prompting People", 
			"LPTHW Exercise 13: Parameters, Unpacking, Variables",  
			"LPTHW Exercise 14: Prompting And Passing", 
			"LPTHW Exercise 15: Reading Files", 
			"LPTHW Exercise 16: Reading And Writing Files",
			"LPTHW Exercise 17: More Files", 
			"LPTHW Exercise 18: Names, Variables, Code, Functions", 
			"LPTHW Exercise 19: Functions And Variables", 
			"LPTHW Exercise 20: Functions And Files", 
			"LPTHW Exercise 21: Functions Can Return Something", 
			"LPTHW Exercise 22: What Do You Know So Far?", 
			"LPTHW Exercise 23: Read Some Code", 
			"LPTHW Exercise 24: More Practice", 
			"LPTHW Exercise 25: Even More Practice", 
			"LPTHW Exercise 26: Congratulations, Take A Test!", 
			"LPTHW Exercise 27: Memorizing Logic", 
			"LPTHW Exercise 28: Boolean Practice", 
			"LPTHW Exercise 29: What If", 
			"LPTHW Exercise 30: Else And If", 
			"LPTHW Exercise 31: Making Decisions", 
			"LPTHW Exercise 32: Loops And Lists", 
			"LPTHW Exercise 33: While Loops", 
			"LPTHW Exercise 34: Accessing Elements Of Lists", 
			"LPTHW Exercise 35: Branches and Functions", 
			"LPTHW Exercise 36: Designing and Debugging", 
			"LPTHW Exercise 37: Symbol Review", 
			"LPTHW Exercise 38: Reading Code", 
			"LPTHW Exercise 39: Doing Things To Lists", 
			"LPTHW Exercise 40: Dictionaries, Oh Lovely Dictionaries", 
			"LPTHW Exercise 41: Gothons From Planet Percal #25", 
			"LPTHW Exercise 42: Gothons Are Getting Classy", 
			"LPTHW Exercise 43: You Make A Game", 
			"LPTHW Exercise 44: Evaluating Your Game", 
			"LPTHW Exercise 45: Is-A, Has-A, Objects, and Classes", 
			"LPTHW Exercise 46: A Project Skeleton", 
			"LPTHW Exercise 47: Automated Testing", 
			"LPTHW Exercise 48: Advanced User Input", 
			"LPTHW Exercise 49: Making Sentences",
			"LPTHW Exercise 50: Your First Website",
			"LPTHW Exercise 51: Getting Input From A Browser",
			"LPTHW Exercise 52: The Start Of Your Web Game"
			};
	public void doJob() {
		for(String chapter : this.chapters) {
			String contents = "Please submit links to your blog posts related to this activity";
			Activity activity = new Activity("Activity for - " + chapter, contents);
			activity.save();
		}
	}
}
