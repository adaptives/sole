package controllers;

import ext.StringExtensions;
import play.mvc.Controller;


public class MarkdownC extends Controller {

	public static void processMarkdown(String text) {
		String markedDown = StringExtensions.md(text);
		String sanitized = StringExtensions.sanitize(markedDown);
		renderText(sanitized);
	}
}
