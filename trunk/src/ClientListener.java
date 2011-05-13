import dinolib;

public class ClientListener implements Runnable {
	private boolean stopThread = false;
	public void run () {
		while ( ! stopThread ) {
			
		}
	}
	
	public void stopTheThread () {
		stopThread = true;
	}
}