package dinolib.Exceptions;

public
class InvalidIDException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 894721173221622029L;
	public InvalidIDException() { }
	public InvalidIDException(String message) {
		super(message);
	}
}
