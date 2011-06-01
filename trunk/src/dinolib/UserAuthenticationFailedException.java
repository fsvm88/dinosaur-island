package dinolib;

public
class UserAuthenticationFailedException extends Exception {
	public UserAuthenticationFailedException() { }
	public UserAuthenticationFailedException(String message) {
		super(message);
	}
}
