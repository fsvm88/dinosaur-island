package dinolib;

public
class NonAutenticatoException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7461525136601804452L;
	public NonAutenticatoException() { }
	public NonAutenticatoException(String message) {
		super(message);
	}
}
