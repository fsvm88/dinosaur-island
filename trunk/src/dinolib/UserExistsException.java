package dinolib;

@SuppressWarnings("serial")
class UserExistsException extends Exception {
	public UserExistsException() { }
	public UserExistsException(String message)
	{
		super(message);
	}
}
