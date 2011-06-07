package dinolib;

public
class UserAuthenticationFailedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -471774499920060950L;
	public UserAuthenticationFailedException() { }
	public UserAuthenticationFailedException(String message) {
		super(message);
	}
}
