package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.Scanner;
import java.util.regex.Pattern;

import dinolib.Giocatore;


public class ClientListener extends Server implements Runnable {
	private boolean stopThread = false;
	
	private Socket mySocket = null;
	private BufferedReader incomingData = null;
	private PrintWriter outgoingData = null;
	private boolean logged = false;
	private boolean inGame = false;
	
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
					terminateThreadOnIOException("IOException while trying to communicate with the client..");
				}
			}
			else if ( isLogged() ) {
				try {
					loggatoLeggiComandi();
				}
				catch (IOException e) {
					terminateThreadOnIOException("IOException while trying to communicate with the client..");
				}
			}
			else {
				System.out.println("Invalid status for thread, killing it..");
				stopTheThread();
			}
		}
	}
	
	/**
	 * Helper per fermare il thread, imposta stopThread a true
	 */
	public void stopTheThread () {
		stopThread = true;
	}
	
	private boolean isMioTurno () {
		if (super.nomeGiocatoreCorrente.equals(myPlayer.getNome())) return true;
		else return false;
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
	 * Due helper per impostare lo stato del login dell'utente
	 * @param status
	 */
	private void iAmLogged() {
		logged = true;
	}
	
	private void iAmNotLogged() {
		logged = false;
	}
	
	/**
	 * Helper per la generazione di un nuovo token alfanumerico
	 * @return
	 */
	private static String getNewToken() {
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
						iAmLogged();
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
				Giocatore tempPlayer = new Giocatore(tempUser, tempPwd, getNewToken()); 
				super.Giocatori.put(tempUser, tempPlayer);
				writeLineToOutput("@ok");
			}
			else writeLineToOutput("@no,@usernameOccupato");
		}
		else return;
	}
	
	/**
	 * Helper per gestire i comandi da utente loggato correttamente
	 * Dal protocollo si deduce che il token è sempre il secondo argomento di qualunque comando una volta che l'utente è loggato.
	 * Implementa il controllo del token FUORI dalle varie funzioni che implementano i comandi, di modo da risparmiare MOLTO codice.
	 * @throws IOException
	 */
	private void loggatoLeggiComandi() throws IOException {
		do {
			Scanner scanner = new Scanner(readLineFromInput());
			scanner.useDelimiter(",");
			if (readAndValidateTokenFromInput(scanner)) {
				if (scanner.hasNext()) {
					String tempInput = scanner.next();
					/* comandi fuori partita*/
					if (tempInput.equals("@creaRazza")) creaNuovaRazza(scanner);
					else if (tempInput.equals("@accessoPartita")) accediAPartita();
					else if (tempInput.equals("@uscitaPartita")) esciDallaPartita();
					else if (tempInput.equals("@listaGiocatori")) listaDeiGiocatori();
					else if (tempInput.equals("@classifica")) classifica();
					else if (tempInput.equals("@logout")) handleLogout();
					/* comandi in partita */
					else if (isInGame()) {
						/* comandi di informazione */
						if (tempInput.equals("@mappaGenerale")) sendMappaGeneale();
						else if (tempInput.equals("@listaDinosauri")) sendListaDinosauri();
						else if (tempInput.equals("@vistaLocale")) sendVistaLocale();
						else if (tempInput.equals("@statoDinosauro")) sendStatoDinosauro();
						/* comandi di azione */
						else if (tempInput.equals("@muoviDinosauro")) sendStatoDinosauro();
						else if (tempInput.equals("@cresciDinosauro")) cresciDinosauro();
						else if (tempInput.equals("@deponiUovo")) deponiUovo();
						/* comandi di turno */
						else if (tempInput.equals("@confermaTurno")) confermaTurno();
						else if (tempInput.equals("@passaTurno")) passaTurno();
						else if (tempInput.equals("@cambioTurno")) broadcastCambioTurno();
						else writeLineToOutput("@no");
					}
					else writeLineToOutput("@no");
				}
				else writeLineToOutput("@no");
			}
			else writeLineToOutput("@no,tokenNonValido");
		} while (isLogged());

	}
	
	private void passaTurno() {
		// TODO Auto-generated method stub
		
	}

	private void confermaTurno() {
		// TODO Auto-generated method stub
		
	}

	private void deponiUovo() {
		// TODO Auto-generated method stub
		
	}

	private void cresciDinosauro() {
		// TODO Auto-generated method stub
		
	}

	private void sendStatoDinosauro() {
		// TODO Auto-generated method stub
		
	}

	private void sendVistaLocale() {
		// TODO Auto-generated method stub
		
	}

	private void sendListaDinosauri() {
		// TODO Auto-generated method stub
		
	}

	private void sendMappaGeneale() {
		// TODO Auto-generated method stub
		
	}

	private boolean isInGame() {
		return inGame;
	}
		
	private void iAmInGame() {
		inserisciDinosauriNellaMappa();
		inGame = true;
	}
	
	private void iAmNotInGame() {
		rimuoviDinosauriDallaMappa();
		inGame = false;
	}

	private void classifica() {
		// TODO Auto-generated method stub
		
	}

	private void listaDeiGiocatori() {
		// TODO Auto-generated method stub
		
	}

	private void esciDallaPartita() {
		// TODO Auto-generated method stub
		
	}

	private void accediAPartita() {
		// TODO Auto-generated method stub
		
	}

	private void creaNuovaRazza(Scanner scanner) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Helper per garantire la correttezza del token che è sempre il secondo parametro dei comandi da utente loggato.
	 * @param scanner
	 * @return
	 * @throws IOException
	 */
	private boolean readAndValidateTokenFromInput (Scanner scanner) throws IOException {
		if ( scanner.hasNext() ) {
			if ( scanner.next(Pattern.compile("[^token=]")).equals(myPlayer.getToken() )) {
				return true;
			}
			else return false;
		}
		else writeLineToOutput("@no");
		return false;
	}
	
	/**
	 * Gestisce il logout dell'utente
	 */
	private void handleLogout() throws IOException {
		if (isLogged()) {
			if (isInGame()) {
				quitTheGame();
				iAmNotLogged();
				writeLineToOutput("@ok");
				return;
			}
			else {
				iAmNotLogged();
				writeLineToOutput("@ok");
				return;
			}
		}
		else writeLineToOutput("@no");
	}

	/**
	 * Helper per l'uscita dalla partita
	 */
	private void quitTheGame() {
		// TODO Auto-generated method stub
		
	}
}