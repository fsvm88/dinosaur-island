package dinolib;

public
class InvalidTokenException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8870177665981407595L;
	public InvalidTokenException() { }
	public InvalidTokenException(String message) {
		super(message);
	}
}
