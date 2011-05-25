package dinolib;

@SuppressWarnings("serial")
class RaceAlreadyCreatedException extends Exception {
	public RaceAlreadyCreatedException() { }
	public RaceAlreadyCreatedException(String message) {
		super(message);
	}
}
