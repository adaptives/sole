package controllers;

import play.mvc.Controller;

public class CatchAllC extends Controller {
	
	public static void index() {
		render("errors/404.html");
	}
}
