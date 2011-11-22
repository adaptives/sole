package controllers;

import play.mvc.With;

@With(Security.class)
@Check("admin")
public class BadgeDefs extends CRUD {

}
