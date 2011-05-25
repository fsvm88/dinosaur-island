package dinolib;

@SuppressWarnings("serial")
class InvalidTokenException extends Exception {
	public InvalidTokenException() { }
	public InvalidTokenException(String message) {
		super(message);
	}
}
