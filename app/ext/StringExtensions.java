package ext;

import play.templates.JavaExtensions;

public class StringExtensions extends JavaExtensions {
	public static String sp2nbsp(Object obj) {
		return obj.toString().replaceAll(" ", "&nbsp;");
	}
}
