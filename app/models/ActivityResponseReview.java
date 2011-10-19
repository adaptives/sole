package models;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.codehaus.groovy.runtime.StringBufferWriter;
import org.hsqldb.lib.StringInputStream;
import org.tautua.markdownpapers.Markdown;

import other.utils.HtmlSanitizer;

import controllers.CourseC;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class ActivityResponseReview extends Model {

	public static final org.apache.log4j.Logger cLogger = Logger.log4j.getLogger(ActivityResponseReview.class);
	
	@ManyToOne
	public ActivityResponse activityResponse;
	
	@ManyToOne
	public SocialUser user;
	
	@Lob
	public String review;
	
	public Date timestamp;
	
	public ActivityResponseReview(ActivityResponse activityResponse,
								  SocialUser user,
								  String review) {
		
		this.activityResponse = activityResponse;
		this.user = user;
		this.review = review;
		this.timestamp = new Date();
		create();
	}
	
	public String processMarkdown() {
		String retVal = "";
		try {
			StringBuffer buff = new StringBuffer();
			Markdown md = new Markdown();
			Reader in = new StringReader(this.review);
			Writer out = new StringBufferWriter(buff);
			md.transform(in, out);
			retVal = buff.toString();
			retVal = HtmlSanitizer.clean(retVal); 
		} catch (Exception e) {
			String msg = "Could not parse ActivityResponseReview for markdown '" + 
						 this.review + "'"; 
			cLogger.error(msg, e);
		}
		return retVal;
	}
}
