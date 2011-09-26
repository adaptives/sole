package controllers;

import play.mvc.With;
import controllers.CRUD;
import controllers.Check;
import controllers.Secure;

@With(Secure.class)
@Check("admin")
public class ModeratedFeedbacks extends CRUD {

}
