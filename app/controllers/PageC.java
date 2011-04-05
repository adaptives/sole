package controllers;

import models.Page;
import models.SocialUser;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(SocialAuthC.class)
public class PageC extends Controller {
	
	public static void show(String name) {
		Page page = Page.find("byName", name).first();
		render(page);
	}
}
