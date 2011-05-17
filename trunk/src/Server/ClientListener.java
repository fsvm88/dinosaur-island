package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Pattern;

import dinolib.*;

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
	
	/* Quattro helper molto generici di cui due per l'IO per il client e due per fermare il thread. */
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
	 * Helper per fermare il thread, imposta stopThread a true
	 */
	public void stopTheThread () {
		stopThread = true;
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
	 * Helper per verificare se l'utente è loggato
	 * @return
	 */
	private boolean isLogged() {
		return logged;
	}
	
	/* Due helper per impostare lo stato del login dell'utente. */
	private void iAmLogged() {
		logged = true;
	}
	
	private void iAmNotLogged() {
		logged = false;
	}
	
	/**
	 * Ritorna il valore di inGame, che dice se l'utente sta giocando.
	 */
	private boolean isInGame() {
		return inGame;
	}
	
	/* Due helper per impostare lo stato dell'utente, sta giocando o no? */
	private void iAmInGame() {
		inserisciDinosauriNellaMappa(myPlayer);
		inGame = true;
	}
	
	private void iAmNotInGame() {
		inGame = false;
	}
	
	/* Tre metodi (uno principale, due helper) per il login o la creazione dell'utente. */
	
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
	
	/* Funzione principale per gestire i comandi di un utente loggato correttamente e a seguire tutti i metodi per i comandi individuali. */
	
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
						if (isMioTurno(myPlayer)) {
							/* comandi di azione */
							if (tempInput.equals("@muoviDinosauro")) sendStatoDinosauro();
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
				else writeLineToOutput("@no");
			}
			else writeLineToOutput("@no,tokenNonValido");
		} while (isLogged());
	}
	
	/* Helper per verificare l'integrità del token */
	
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
	
	private void creaNuovaRazza(Scanner scanner) throws IOException {
		if (scanner.hasNext() && !existsRazza(myPlayer)) {
			String nomeRazza = scanner.next(Pattern.compile("[^nome=]"));
			if (scanner.hasNext() && (!existsNomeRazza(myPlayer)) ) {
				String tipoRazza = scanner.next(Pattern.compile("[^tipo=]"));
				if (tipoRazza.equals("c") || tipoRazza.equals("e")) {
					if (tipoRazza == "c") {
						myPlayer.setNomeRazzaDinosauro(nomeRazza);
						int x = getNewRandomIntValueOnMyMap();
						int y = getNewRandomIntValueOnMyMap();
						do {
							if (rifMappa.isLibera(x,y)) {
								myPlayer.aggiungiDinosauro(new Carnivoro(x,y));
								rifMappa.spawnDinosauro(x, y);
								break;
							}
						} while (true);
					}
					else if (tipoRazza == "e") {
						myPlayer.setNomeRazzaDinosauro(nomeRazza);
						do {
							int x = getNewRandomIntValueOnMyMap();
							int y = getNewRandomIntValueOnMyMap();
							if (rifMappa.isLibera(x,y)) {
								myPlayer.aggiungiDinosauro(new Erbivoro(x,y));
								rifMappa.spawnDinosauro(x, y);
								break;
							}
						} while (true);
					}
					else writeLineToOutput("@no");
				}
				else writeLineToOutput("@no");
			}
			else writeLineToOutput("@no,@nomeRazzaOccupato");
		}
		else writeLineToOutput("@no,@razzaGiaCreata");
	}
	
	/**
	 * Comando per l'accesso alla partita. Verifica che l'utente abbia creato una razza di dinosauri e gli abbia dato un nome.
	 */
	private void accediAPartita() {
		if (existsRazza(myPlayer) && existsNomeRazza(myPlayer)) {
			iAmInGame();
		}
	}
	
	/**
	 * Helper/Comando per l'uscita dalla partita: rimuove i dinosauri e imposta lo stato di "fuori dal gioco"
	 */
	private void esciDallaPartita() {
		rimuoviDinosauriDallaMappa(myPlayer);
		iAmNotInGame();
	}
	
	/**
	 * Comando per restituire la lista dei giocatori al client.
	 * @throws IOException 
	 */
	private void listaDeiGiocatori() throws IOException {
		Iterator<Entry<String, Giocatore>> iteratoreSuListaGiocatori = super.Giocatori.entrySet().iterator();
		String buffer = "@ok,";
		if (iteratoreSuListaGiocatori.hasNext()) {
			while (iteratoreSuListaGiocatori.hasNext()) {
				buffer = buffer + iteratoreSuListaGiocatori.next();
			}
			writeLineToOutput(buffer);
			return;
		}
		else writeLineToOutput("@no");
	}
	
	/**
	 * Comando per gestire il logout dell'utente
	 */
	private void handleLogout() throws IOException {
		if (isLogged()) {
			if (isInGame()) esciDallaPartita();
			iAmNotLogged();
			writeLineToOutput("@ok");
			return;
		}
		else writeLineToOutput("@no");
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
	
	private void classifica() {
		// TODO Auto-generated method stub
		
	}
}