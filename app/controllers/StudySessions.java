package controllers;

import java.util.List;

import play.mvc.Controller;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
public class StudySessions extends CRUD {
	
}
