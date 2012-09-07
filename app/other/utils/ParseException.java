package other.utils;

public class ParseException extends Exception {

	public ParseException() {
		super();
	}
	
	public ParseException(String msg) {
		super(msg);
	}
	
	public ParseException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public ParseException(Throwable t) {
		super(t);
	}
}
