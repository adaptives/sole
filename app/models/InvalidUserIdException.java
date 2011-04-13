package models;

public class InvalidUserIdException extends Exception {
	
	public InvalidUserIdException() {
		
	}
	
	public InvalidUserIdException(String msg) {
		super(msg);
	}
}
