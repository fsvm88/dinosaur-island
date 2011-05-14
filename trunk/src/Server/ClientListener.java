package Server;
import java.net.Socket;


public class ClientListener extends Server implements Runnable {
	private boolean stopThread = false;
	public ClientListener(Socket accept) {
		// TODO Auto-generated constructor stub
	}

	public void run () {
		while ( ! stopThread ) {
			
		}
	}
	
	public void stopTheThread () {
		stopThread = true;
	}
}