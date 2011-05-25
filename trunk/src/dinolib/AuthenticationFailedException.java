package dinolib;

@SuppressWarnings("serial")
class AuthenticationFailedException extends Exception {
	public AuthenticationFailedException() { }
	public AuthenticationFailedException(String message) {
		super(message);
	}
}
