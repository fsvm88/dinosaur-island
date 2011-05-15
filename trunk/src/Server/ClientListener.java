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
				try {
					allowLoginOrCreation();
				}
				catch (IOException e) {
					terminateThreadOnIOException("IOException while trying to communicate with the client, killing the thread..");
				}
			}
			else {
				
			}
		}
	}
	
	/**
	 * Helper per fermare il thread, imposta stopThread a true
	 */
	public void stopTheThread () {
		stopThread = true;
	}
	
	private void isMioTurno () {
	//	if (super.nomeGiocatoreCorrente == super.Giocatori.)
	}
	
	/**
	 * Helper per ricevere comandi dal client
	 * @return
	 * @throws IOException
	 */
	private String readLineFromInput () throws IOException {
		return incomingData.readLine();
	}
	
	/**
	 * Helper per rispondere ai comandi del client
	 * @param toSend
	 * @throws IOException
	 */
	private void writeLineToOutput (String toSend) throws IOException {
		outgoingData.println(toSend);
	}
	
	/**
	 * Termina il thread su eccezione:
	 * dà notifica con motivazione e imposta il parametro per lo stop 
	 * @param cause
	 */
	private void terminateThreadOnIOException (String cause) {
		System.out.println(cause);
		System.out.println("Killing this thread!");
		stopTheThread();
	}
	
	/**
	 * Helper per verificare l'esistenza di un utente
	 * @param userToLookFor
	 * @return
	 */
	private boolean userExists(String userToLookFor) {
		if (super.Giocatori.containsKey(userToLookFor)) return true;
		else return false;
	}
	
	/**
	 * Helper per verificare la correttezza della password
	 * @param curGamer
	 * @param suppliedPassword
	 * @return
	 */
	private boolean passwordIsValid(Giocatore curGamer, String suppliedPassword) {
		if ( curGamer.getPassword().equals(suppliedPassword) ) return true;
		else return false;
	}
	
	/**
	 * Helper per verificare se l'utente è loggato
	 * @return
	 */
	private boolean isLogged() {
		return logged;
	}
	
	/**
	 * Helper per impostare lo stato dell'utente
	 * @param status
	 */
	private void setLogged(boolean status) {
		logged = status;
	}
	
	/**
	 * Helper per la generazione di un nuovo token alfanumerico
	 * @return
	 */
	private String getNewToken() {
		return Long.toString(Double.doubleToLongBits(Math.random()));
	}
	
	/**
	 * Funzione per la gestione di login o creazione utente.
	 * Se l'utente non esiste la logica principale richiama sempre questa funzione in attesa che l'utente esegua il login o si registri
	 * @throws IOException
	 */
	private void allowLoginOrCreation () throws IOException {
		String inLine = readLineFromInput();
		Scanner scanner = new Scanner(inLine);
		scanner.useDelimiter(","); 
		if (scanner.hasNext() && (scanner.next() == "@login")) {
			gestisciLogin(scanner);
			return;
		}
		else if (scanner.hasNext() && (scanner.next() == "@creaUtente")) {
			gestisciCreazioneUtente(scanner);
			return;
		}
		else {
			writeLineToOutput("@no");
		}
	}
	
	/**
	 * Gestisce il login dell'utente
	 * @param scanner
	 * @throws IOException
	 */
	private void gestisciLogin(Scanner scanner) throws IOException {
		if ( scanner.hasNext() ){
			String tempUser = scanner.next(Pattern.compile("[^user=]"));
			if ( userExists(tempUser) ) {
				myPlayer = super.Giocatori.get(tempUser);
				if ( scanner.hasNext() ) {
					String tempPwd = scanner.next(Pattern.compile("[^pass=]"));
					if ( passwordIsValid(myPlayer, tempPwd) ) {
						setLogged(true);
						String newToken = getNewToken();
						writeLineToOutput("@ok," + newToken);
						myPlayer.setToken(newToken);
					}
					else writeLineToOutput("@no,@autenticazioneFallita");
				}
			}
			else writeLineToOutput("@no,@autenticazioneFallita");
		}
		else return;
	}
	
	/**
	 * Gestisce la registrazione di un utente
	 * @param scanner
	 * @throws IOException
	 */
	private void gestisciCreazioneUtente(Scanner scanner) throws IOException {
		if ( scanner.hasNext() ){
			String tempUser = scanner.next(Pattern.compile("[^user=]"));
			if ( ! userExists(tempUser) ) {
				String tempPwd = scanner.next(Pattern.compile("[^pass=]"));
				myPlayer = new Giocatore(tempUser, tempPwd, getNewToken());
				super.Giocatori.put(tempUser, myPlayer);
				writeLineToOutput("@ok");
			}
			else writeLineToOutput("@no,@usernameOccupato");
		}
		else return;
	}
}