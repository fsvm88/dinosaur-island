package dinolib;

public
class AuthenticationFailedException extends Exception {
	public AuthenticationFailedException() { }
	public AuthenticationFailedException(String message) {
		super(message);
	}
}
