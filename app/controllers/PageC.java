package controllers;

import models.Page;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;

public class PageC extends Controller {
	
	@Before
	public static void setConnectedUser() {
		if(Security.isConnected()) {
			User user = User.findByEmail(Security.connected());
			renderArgs.put("user", user.name);
		}
	}
	
	public static void show(String name) {
		Page page = Page.find("byName", name).first();
		render(page);
	}
}
