package dinolib;

@SuppressWarnings("serial")
class RaceNameExistsException extends Exception {
	public RaceNameExistsException() { }
	public RaceNameExistsException(String message) {
		super(message);
	}
}
