package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.Scanner;


public class ClientListener extends Server implements Runnable {
	private boolean stopThread = false;
	
	private Socket mySocket = null;
	private BufferedReader incomingData = null;
	private PrintWriter outgoingData = null;
	private boolean logged = false;
	
	public ClientListener(Socket socket) throws IOException {
		mySocket = socket;
		try {
			incomingData = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			outgoingData = new PrintWriter(mySocket.getOutputStream(), true);
		}
		catch (IOException e) {
			terminateThreadOnIOException("Cannot initialize input/output streams!");
		}
	}

	public void run () {
		while ( ! stopThread ) {
			/**
			 * 1. Controlla se il giocatore che vuole loggarsi esiste
			 * 2. Se esiste loggalo e mandagli un token
			 * 3. Se non esiste crealo
			 */
			if ( ! logged ) {
				handleLogin();
			}
			else {
				
			}
		}
	}
	
	public void stopTheThread () {
		stopThread = true;
	}
	
	private void isMioTurno () {
	//	if (super.nomeGiocatoreCorrente == super.Giocatori.)
	}
	
	private void handleLogin () {
		String inLine;
		try {
			inLine = readLineFromInput();
		} catch (IOException e) {
			terminateThreadOnIOException("Unable to read data from input stream!");
			return;
		}
		Scanner scanner = new Scanner(inLine);
		scanner.useDelimiter(","); 
		if (scanner.next() == "@login") {
			
		}
	}
	
	private String readLineFromInput () throws IOException {
		return incomingData.readLine();
	}
	
	private void terminateThreadOnIOException (String cause) {
		System.out.println(cause);
		System.out.println("Terminating current thread!");
		stopTheThread();
	}
}