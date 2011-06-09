package dinolib.Exceptions;

public
class UserExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2649488050337107030L;
	public UserExistsException() { }
	public UserExistsException(String message)
	{
		super(message);
	}
}
