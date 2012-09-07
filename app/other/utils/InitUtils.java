package other.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import models.Activity;
import models.Competency;
import models.CompetencyGroup;
import models.Course;
import models.CourseCategory;
import models.CourseSection;
import models.KeyValueData;
import models.Level;
import models.Page;
import models.Role;
import models.SiteEvent;
import models.SocialUser;
import models.Topic;
import models.User;

public class InitUtils {
	public static void initData() {
		createRoles();
		createUsers();
		createPages();
		createKVData();
		createSiteEvents();
		createCourse();
		createLevels();
		createTopic("default-data.csv");
	}

	private static void createRoles() {
		Role learnerRole = new Role("learner");
		learnerRole.save();
		Role adminRole = new Role("admin");
		adminRole.save();
	}

	private static void createUsers() {
		// testuser
		SocialUser testUser = new SocialUser("testuser@somewhere.com",
				"testuser");
		Role adminRole = Role.find("select r from Role r where r.name = ?",
				"admin").first();
		testUser.roles.add(adminRole);
		testUser.save();
		User uTestUser = new User("testuser@somewhere.com", "secret",
				"Test User", testUser);
		uTestUser.save();

		// anothertestuser
		SocialUser anotherTestUser = new SocialUser(
				"anothertestuser@somewhere.com", "anothertestuser");
		anotherTestUser.save();
		User uAnotherTestUser = new User("anothertestuser@somewhere.com",
				"secret", "anothertestuser", anotherTestUser);
		uAnotherTestUser.save();

		// learner
		SocialUser learner = new SocialUser("learner@somewhere.com", "learner");
		learner.save();
		User uLearner = new User("learner@somewhere.com", "secret", "learner",
				learner);
		uLearner.save();
	}

	private static void createCourse() {
		CourseCategory cat = new CourseCategory("courses");
		cat.save();

		Course course = new Course("Play Framework", "Play framework course");
		course.category = cat;
		course.save();
		CourseSection section = new CourseSection(course, "introduction",
				"Introductory section");
		Activity activity = new Activity("Blog", "Please write a blog post");
		section.activities.add(activity);
		section.save();
	}

	private static void createSiteEvents() {
		String text = "User <a href=\"http://localhost:9000/users/15\">http://twitter.com/bitsandwaves</a> ENROLLED in DIY Course <a href=\"http://localhost:9000/courses/course/2\">Structure And Interpretation Of Computer Programs</a>";
		SiteEvent se1 = new SiteEvent(text);
	}

	private static void createKVData() {
		KeyValueData kvData = new KeyValueData("recently_pub_ss",
				"http://feeds.feedburner.com/DIYComputerScienceRecentlyPublishedStudySessions");
	}

	private static void createPages() {
		// home
		String homePageName = "home";
		String homePageTitle = "Home";
		String homePageContent = "Test homepage";
		Page homePage = new Page(homePageName, homePageTitle, homePageContent);
		homePage.save();

		// tos
		String tosPageName = "tos";
		String tosPageTitle = "Terms Of Service";
		String tosPageContent = "<h1 class=\"title\">Terms of Use</h1>"
				+ "\n"
				+ "<div>"
				+ "\n"
				+ "<p>All Users, Students and Course Organisers participating in diycomputerscience.com and visitors to websites hosted under the diycomputerscience.com domain acknowledge and agree to the following terms of use:</p>"
				+ "\n"
				+ "<h2> 1. Acceptance</h2>"
				+ "\n"
				+ "<ol>"
				+ "\n"
				+ "<li>These Terms of Use shall apply to any use by you of the diycomputerscience.com website and participation in diycomputerscience.com.</li>"
				+ "\n"
				+ "<li>By accessing and using this Website you agree to be legally bound by these Terms of Use in full and without limitation. If you do not wish to be bound by these terms of use in full, do not use this website.</li>"
				+ "\n"
				+ "<li>&quot;We&quot; means diycomputerscience.com means the organization maintaining diycomputerscience.com, the team, the advisory board and affiliates of diycomputerscience.com. &quot;You&quot; means any user of the website, student and/or tutor of any P2PU course.</li>"
				+ "\n"
				+ "<li>Throughout the Website there may be additional rules or specific terms and conditions relating diycomputerscience.com (&quot;Special Conditions&quot;). In the event of any inconsistency between these Terms of Use and the Special Conditions then the Special Conditions shall prevail.</li>"
				+ "\n"
				+ "<li>We may change these Terms of Use and any Special Conditions from time to time by posting changes online. Substantive changes will also be announced through the standard mechanisms through which diycomputerscience.com communicates with the community. It is your responsibility to familiarise yourself with these Terms of Use and any Special Conditions each time you access this Website. For the avoidance of doubt your use of this Website after changes are posted shall constitute your acceptance of these Terms of Use and any Special Conditions as modified.</li>"
				+ "\n"
				+ "<li>Only people who are over the age of majority in their jurisdiction ( which is typically 18 years old , but may be different in your jurisdiction) and fully competent to enter into the terms, conditions, obligations, affirmations, representations and warranties set out in these Terms of Use and to abide by these Terms of Use may register an account and use the Related Services.</li>"
				+ "\n"
				+ "</ol>"
				+ "\n"
				+ "<h2>2. Terms of Use</h2>"
				+ "\n"
				+ "<ol>"
				+ "\n"
				+ "<li>You acknowledge that diycomputerscience.com is not an educational institution and has no affiliation or association with any educational institutions. diycomputerscience.com courses are not accredited or a recognised course of study in any country.</li>"
				+ "\n"
				+ "<li>You acknowledge and agree that diycomputerscience.com is being provided freely and no kind of agreement or contract is created between diycomputerscience.com and you and the owners or users of this site, the owners of the servers upon which it is housed, individual tutors and students of diycomputerscience.com or our advisors and administrators or anyone else connected with this project subject to your claims against them directly.</li>"
				+ "\n"
				+ "<li>You acknowledge and agree that your participation in diycomputerscience.com courses is on a voluntary basis.</li>"
				+ "\n"
				+ "<li>We do not guarantee enrolment into any of its courses or the continued offering of any of its courses.</li>"
				+ "\n"
				+ "<li>We reserve the right to limit the number of enrolments in each course for any reason.</li>"
				+ "\n"
				+ "<li>We reserve the right to terminate your participation in the course for any reason.</li>"
				+ "\n"
				+ "<li>We reserves the right to change the commencement and end dates and times of any course for any reason.</li>"
				+ "\n"
				+ "<li>We reserve the right to cancel a course for any reason.</li>"
				+ "\n"
				+ "</ol>"
				+ "\n"
				+ "<h2>3. Copyright</h2>"
				+ "\n"
				+ "<ol>"
				+ "\n"
				+ "<li>All copyright, trade marks and other intellectual property rights in this Website (including the design, arrangement look and feel) and all material or content supplied as part of this Website shall remain at all times our property or that of our licensors.</li>"
				+ "\n"
				+ "<li>Certain material may be downloaded or used by you under a Creative Common Licence. Other material may be made available under different Special Conditions. It is your sole responsibility to check the copyright and licensing status (including any Special Conditions) applying to the material you view, download or otherwise access from this Website and to abide by any such Special Conditions in full.</li>"
				+ "\n"
				+ "<li>We prohibit you from uploading, posting or otherwise transmitting on the Website any materials that violate another party&#39;s image, intellectual property, privacy or any other rights.</li>"
				+ "\n"
				+ "<li>All text, images or other works in which you hold the copyright, by submitting it to diycomputerscience.com , you agree to license it under the <a href=\"http://creativecommons.org/licenses/by-sa/3.0/\" rel=\"nofollow\">creative commons license 3.0 license</a>.</li>"
				+ "\n"
				+ "</ol>"
				+ "\n"
				+ "<h2>4. Disclaimer</h2>"
				+ "\n"
				+ "<ol>"
				+ "\n"
				+ "<li>We make no warranty or representation in relation to the quality, qualifications or performance of any of its diycomputerscience.com services, content, courses, tutors, advisors, administrators or volunteers.</li>"
				+ "\n"
				+ "<li>We will try to ensure that all information provided by us in connection with the diycomputerscience.com and/or the Website is accurate at the time of inclusion. However, there may be errors, omissions or inaccuracies in respect of which we exclude all liability. We make no representation or warranty about the information included on this Website and/or associated communications or information. You shall be solely responsible for any decisions based on the information contained in this Website.</li>"
				+ "\n"
				+ "<li>We will try to ensure that the website is maintained in a fully operating condition. We are not responsible for the results of any defects that exist in this Website. You should not assume that this Website is error free or that it will be suitable for the particular purposes that you have in mind when using it. We reserve the right to make changes to the Website and services may be supplemented, modified or withdrawn.</li>"
				+ "\n"
				+ "<li>Information provided on the website does not constitute professional advice and should not be relied upon without taking independent advice. We disclaim all liability and responsibility arising from any reliance placed on such materials by you.</li>"
				+ "\n"
				+ "<li>The Website and the diycomputerscience.com courses are provided on an <i>as is</i> and on an <i>as available</i> basis without any representation or endorsement being made and without warranty of any kind, including without limitation to the implied warranties of satisfactory quality, fitness for particular purpose, non-infringement, compatibility, security and accuracy.</li>"
				+ "\n"
				+ "<li>In addition to the above you agree that we will not be liable for any damages including but not limited to, indirect or consequential damages, errors or omissions or any damages arising from use, loss of use data or profits, arising out of or in connection with the use of the Website and/or participation in any of the diycomputerscience.com courses or associated activities.</li>"
				+ "\n" + "</ol>" + "\n" + "</div>" + "\n";
		Page tosPage = new Page(tosPageName, tosPageTitle, tosPageContent);
		tosPage.save();

		String helpPageName = "help";
		String helpPageTitle = "Help";
		String helpPageContent = "<div style=\"font-size: 1.2em; line-height:130%; color: #A9B2C1;\">"
				+ "\n"
				+ "<div class=\"vspacediv\">This site is inspired by the concept of Massive Open Online Courses (MOOC) and peer enabled learning. Briefly a MOOC is a massive open online course which can have anywhere from a hundred to thousands of participants. A MOOC typically does not have a teacher who is a sage on the stage, but rather facilitators who are more like peers, who facilitate the learning of other participants. A long time back I had read a definition of a teacher, on the Internet. <blockquote>It said A teacher is also a student who is further ahead on the road.</blockquote></div>"
				+ "\n"
				+ "<div class=\"vspacediv\">We are all teachers and we are all students. Since one of the best ways to learn is by teaching, we foster learning communities where participants learn and help others learn at the same time. And if you should need help there are always mentors, facilitators, and friendly community members to help you.</div>"
				+ "\n"
				+ "At the moment this site supports 2 types of courses."
				+ "\n"
				+ "<ol style=\"list-style-type:square; margin-left:15px;\">"
				+ "\n"
				+ "<li style=\"font-weight: bold;\">DIY Courses</li>"
				+ "\n"
				+ "<li style=\"font-weight: bold;\">Study Sessions</li>"
				+ "\n"
				+ "</ol>"
				+ "\n"
				+ "<h1>DIY Courses</h1>"
				+ "\n"
				+ "<div>A DIY course consists of learning material in the form of videos, audio, and text. Along with the learning material, these courses will also have 'questions to ponder', and homework assignments. These coures also contain forums for collaboration.</div>"
				+ "\n"
				+ "<div class=\"vspacediv\">Learners can learn at their own pace. Here is how you would study in a typical DIY course.</div>"
				+ "\n"
				+ "<ul style=\"list-style-type:circle; margin-left: 15px;\">"
				+ "\n"
				+ "<li>Watch the videos or read the learning material</li>"
				+ "\n"
				+ "<li>Make a note of things you do not understand</li>"
				+ "\n"
				+ "<li>Clear your doubts on the course forums (while you are on the forums, also try and answer other people's questions if you can)</li>"
				+ "\n"
				+ "<li>Write one or more blog posts describing what you learned (reflections, gotchas, facts, surprises, explanations, etc). Feel free to use mindmaps, and pictures where you can. It will help you learn the material better, and will also assist in later recall.</li>"
				+ "\n"
				+ "<li>Do the assignments and upload them to any public open source repository. If you do not wish to upload them to an open source repository, you can also paste your code in blog posts.</li>"
				+ "\n"
				+ "<li>Repeat this process for all the material in the course</li>"
				+ "\n"
				+ "</ul>"
				+ "\n"
				+ "<h1>Study Session</h1>"
				+ "\n"
				+ "<div>A study session is more organized than a DIY course and is more appropriate for learners who prefer to work with schedules and deadlines. Each study session will run for a fixed duration of time, and will focus on a specific topic, such as 'Javascript 101', 'TDD with JUnit', 'Introduction to GIT', etc.</div>"
				+ "\n"
				+ "<div class=\"vspacediv\">Most study sessions will be facilitated by one or many facilitators. Study sessions may optionally have a maximum limit of participants.</div>"
				+ "\n"
				+ "<div class=\"vspacediv\">If you find yourself interested in a study session which is still open for enrollment, then you can enroll yourself in that session. Some study sessions might have a sign-up task, which you have to complete to get accepted in the study session. Once you get accepted you can participate in the session.</div>"
				+ "\n"
				+ "<div class=\"vspacediv\">A typical study session will be divided into weeks or days. We will call these time units for convenience. Each time unit will have some material to study, and some homework. Participants can study the material at their own leasure, ask questions on the forums to clear their doubts, and do the homework on their blogs or by pushing code to open source repositories.</div>"
				+ "\n" + "</div>" + "\n";
		Page helpPage = new Page(helpPageName, helpPageTitle, helpPageContent);
		helpPage.save();

		// pp
		String ppPageName = "pp";
		String ppPageTitle = "Privacy Policy";
		String ppPageContent = "Under Construction";
		Page ppPage = new Page(ppPageName, ppPageTitle, ppPageContent);
		ppPage.save();

		// faq
		String faqPageName = "faq";
		String faqPageTitle = "Frequently Asked Questions";
		String faqPageContent = "Frequently Asked Questions";
		Page faqPage = new Page(faqPageName, faqPageTitle, faqPageContent);
		faqPage.save();
	}

	private static void createLevels() {
		Level level1 = new Level("Level I", "Level I Description");
		level1.placement = 1;
		level1.save();
		Level level2 = new Level("Level II", "Level II Description");
		level2.placement = 2;
		level2.save();
		Level level3 = new Level("Level III", "Level III Description");
		level3.placement = 3;
		level3.save();
	}

	private static void createTopic(String fName) {
		List<Level> levels = Level.findAll();
		ClassLoader loader = InitUtils.class.getClassLoader();
		InputStream is = loader.getResourceAsStream(fName);
		// create string from file
		StringBuffer sBuff = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sBuff.append(line + "\n");
			}

			String csvString = sBuff.toString();
			Topic topic = DataUtils.parseCSV("Core Java", 0, csvString);
			topic.levels.add(levels.get(0));
			topic.levels.add(levels.get(1));
			topic.levels.add(levels.get(2));
			topic.save();
		} catch (IOException ioe) {
			//cLogger.error("caught IOException while parsing default data ", ioe);
		} catch (ParseException pe) {
			//cLogger.error("caught ParseException while parsing default data ", pe);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ioe) {
				}
			}
		}
	}

}
