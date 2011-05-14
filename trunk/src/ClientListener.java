import dinolib;

public class ClientListener extends Server implements Runnable {
	private boolean stopThread = false;
	public void run () {
		while ( ! stopThread ) {
			
		}
	}
	
	public void stopTheThread () {
		stopThread = true;
	}
}