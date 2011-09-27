package other.utils;

import java.io.InputStream;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

import play.vfs.VirtualFile;

public class HtmlSanitizer {

	public static String clean(String text) throws PolicyException, ScanException {
		VirtualFile antisamyPolicy = VirtualFile. fromRelativePath("/conf/antisamy-policy.xml");
		InputStream is = antisamyPolicy.inputstream();
		
		Policy policy = Policy.getInstance(is);
		AntiSamy as = new AntiSamy();
		CleanResults cleanResults = as.scan(text, policy);
		return cleanResults.getCleanHTML();
	}
}
