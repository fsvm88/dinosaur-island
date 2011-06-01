package dinolib;

@SuppressWarnings("serial")
public
class InvalidTokenException extends Exception {
	public InvalidTokenException() { }
	public InvalidTokenException(String message) {
		super(message);
	}
}
