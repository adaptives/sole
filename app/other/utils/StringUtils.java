package other.utils;

public class StringUtils {
	
	public static String replaceSpaceWithDashes(String s) {						
		s = s.replace(' ', '-');
		s = s.replaceAll("[^a-zA-Z0-9-_\\-]", "");
		s = s.toLowerCase();
		return s;
	}
}
