package ext;

import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import models.ActivityResponseReview;

import org.codehaus.groovy.runtime.StringBufferWriter;
import org.tautua.markdownpapers.Markdown;

import other.utils.HtmlSanitizer;
import play.Logger;
import play.templates.JavaExtensions;

public class StringExtensions extends JavaExtensions {
	
	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(StringExtensions.class);
	
	public static String sp2nbsp(Object obj) {
		return obj.toString().replaceAll(" ", "&nbsp;");
	}
	
	public static String makeCode(Object obj) {
		String str = (String)obj;
		String codeString = "<pre><code>" + str + "</code></pre>";
		return codeString;
	}
	
	public static String md(Object obj) {
		String retVal = obj.toString();
		try {
			StringBuffer buff = new StringBuffer();
			Markdown md = new Markdown();
			Reader in = new StringReader(obj.toString());
			Writer out = new StringBufferWriter(buff);
			md.transform(in, out);
			retVal = buff.toString();
			retVal = HtmlSanitizer.clean(retVal); 
		} catch (Exception e) {
			String msg = "Could not parse for markdown '" + obj.toString() + "'"; 
			cLogger.error(msg, e);
		}
		return retVal;
	}
	
	public static String sanitize(Object obj) {
		String retVal = obj.toString();
		try {
			retVal = HtmlSanitizer.clean(retVal); 
		} catch (Exception e) {
			String msg = "Could not sanitize '" + obj.toString() + "'"; 
			cLogger.error(msg, e);
		}
		return retVal;
	}
}
