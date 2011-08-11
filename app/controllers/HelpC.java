package controllers;

import models.Page;
import play.mvc.Controller;

public class HelpC extends Controller {
	
	public static void index() {
		Page page = Page.find("select p from Page p where p.name = ?", "help").first();
		render("PageC/show.html", page);
	}
}
