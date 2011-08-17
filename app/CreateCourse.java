import models.Course;
import models.CourseCategory;
import models.CourseSection;
import models.SocialUser;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class CreateCourse extends Job {

	public void doJob() {
		
		CourseCategory category = CourseCategory.findByName("Applied Computer Science");
		
		Course c = new Course("The Elements of Computing Systems", "Description");
		c.category = category;
		c.save();
		
		CourseSection cs1 = new CourseSection(c, "Designing elementary logic gates from a NAND gate using Hardware Description Language", "Content");
		cs1.placement = 0;
		cs1.save();
		
		CourseSection cs2 = new CourseSection(c, "Building and ALU (Arithmatic Logic Unit)", "Content");
		cs2.placement = 1;
		cs2.save();
		
		CourseSection cs3 = new CourseSection(c, "Designing Registers, RAM, etc", "Content");
		cs3.placement = 2;
		cs3.save();
		
		CourseSection cs4 = new CourseSection(c, "Designing an assembly language", "Content");
		cs4.placement = 3;
		cs4.save();
		
		CourseSection cs5 = new CourseSection(c, "Building the entire computer (in simulation) with the ALU, Registers, RAM, and the assembly language", "Content");
		cs5.placement = 4;
		cs5.save();
		
		CourseSection cs6 = new CourseSection(c, "Building an Assembler for the assembly language we created", "Content");
		cs6.placement = 5;
		cs6.save();
		
		CourseSection cs7 = new CourseSection(c, "Building a Virtual Machine (similar to the JVM - though much smaller in scope)", "Content");
		cs7.placement = 6;
		cs7.save();
		
		CourseSection cs8 = new CourseSection(c, "Introducing a high level, object oriented programming language", "Content");
		cs8.placement = 7;
		cs8.save();
		
		CourseSection cs9 = new CourseSection(c, "Writing a compiler for the high level language", "Content");
		cs9.placement = 8;
		cs9.save();
		
		CourseSection cs10 = new CourseSection(c, "Writing an Operating System for our computer, using the high level language we created", "Content");
		cs10.placement = 9;
		cs10.save();
		
		CourseSection cs11 = new CourseSection(c, "Some more fun (discussing improvements, and future directions)", "Content");
		cs11.placement = 10;
		cs11.save();
		
	}
}
