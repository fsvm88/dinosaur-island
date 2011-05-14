package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import dinolib.Giocatore;


public class ClientListener extends Server implements Runnable {
	private boolean stopThread = false;
	
	private Socket mySocket = null;
	private BufferedReader incomingData = null;
	private PrintWriter outgoingData = null;
	private boolean logged = false;
	
	Giocatore myPlayer = null;
	
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
			 */
			if ( ! isLogged() ) {
				allowLoginOrCreation();
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
	
	private void allowLoginOrCreation () {
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
			String tempUser = scanner.next(Pattern.compile("[^user=]"));
			if ( userExists(tempUser) ) {
				myPlayer = super.Giocatori.get(tempUser);
				String tempPwd = scanner.next(Pattern.compile("[^pass=]"));
				if ( passwordIsValid(myPlayer, tempPwd) ) {
					setLogged(true);
					try {
						writeLineToOutput("@ok," + myPlayer.getToken());
					} catch (IOException e) {
						terminateThreadOnIOException("Unable to write data to output stream!");
						return;
					}
				}
				else {
					try {
						writeLineToOutput("@no,@autenticazioneFallita");
					} catch (IOException e) {
						terminateThreadOnIOException("Unable to write data to output stream!");
						return;
					}
				}
			}
			else {
				try {
					writeLineToOutput("@no,@autenticazioneFallita");
				} catch (IOException e) {
					terminateThreadOnIOException("Unable to write data to output stream!");
					return;
				}
			}
		}
		else if (scanner.next() == "@creaUtente") {
			String tempUser = scanner.next(Pattern.compile("[^user=]"));
			if ( ! userExists(tempUser) ) {
				String tempPwd = scanner.next(Pattern.compile("[^pass=]"));
				myPlayer = new Giocatore(tempUser, tempPwd, getNewToken());
				super.Giocatori.put(tempUser, myPlayer);
				try {
					writeLineToOutput("@ok," + myPlayer.getToken());
				}
				catch (IOException e) {
					terminateThreadOnIOException("Unable to write data to output stream!");
					return;
				}
			}
			else {
				try {
					writeLineToOutput("@no,@autenticazioneFallita");
				}
				catch (IOException e) {
					terminateThreadOnIOException("Unable to write data to output stream!");
					return;
				}
			}
		}
		else {
			try {
				writeLineToOutput("@no,@autenticazioneFallita");
			}
			catch (IOException e) {
			terminateThreadOnIOException("Unable to write data to output stream!");
					return;
				}
			}
		}
	
	private String readLineFromInput () throws IOException {
		return incomingData.readLine();
	}
	
	private void writeLineToOutput (String toSend) throws IOException {
		outgoingData.println(toSend);
	}
	
	private void terminateThreadOnIOException (String cause) {
		System.out.println(cause);
		System.out.println("Terminating current thread!");
		stopTheThread();
	}
	
	private boolean userExists(String userToLookFor) {
		if (super.Giocatori.containsKey(userToLookFor)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean passwordIsValid(Giocatore curGamer, String suppliedPassword) {
		if ( curGamer.getPassword().equals(suppliedPassword) ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean isLogged() {
		return logged;
	}
	
	private void setLogged(boolean status) {
		logged = status;
	}
	
	private String getNewToken() {
		String myNewToken = null;
		
		return myNewToken;
	}
}