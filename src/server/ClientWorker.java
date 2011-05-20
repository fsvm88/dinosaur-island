package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.regex.Pattern;

import dinolib.Carnivoro;
import dinolib.Dinosauro;
import dinolib.Erbivoro;
import dinolib.Giocatore;

/** 
 * Implementa l'ascoltatore per i client. Ascolta i comandi e gestisce le giuste risposte.
 */
public class ClientWorker extends Server implements Runnable {
	/**
	 * Variabile per fermare il thread.
	 * @uml.property name="stopThread"
	 */
	private boolean stopThread = false;
	/**
	 * Istanzia riferimento al socket.
	 * @uml.property name="mySocket"
	 */
	private Socket mySocket = null;
	/**
	 * Istanzia riferimento al buffer per la ricezione dei dati.
	 * @uml.property name="BufferedReader"
	 */
	private BufferedReader incomingData = null;
	/**
	 * Istanzia riferimento al buffer per l'invio dei dati.
	 * @uml.property name="outgoingData"
	 */
	private PrintWriter outgoingData = null;
	/**
	 * Variabile per gestire il login degli utenti.
	 * @uml.property name="logged"
	 */
	private boolean logged = false;
	/**
	 * Variabile per gestire il fatto che l'utente è in partita.
	 * @uml.property name="inGame"
	 */
	private boolean inGame = false;
	/**
	 * Istanzia riferimento al giocatore.
	 * @uml.property name="myPlayer"
	 */
	Giocatore myPlayer = null;

	/**
	 * Costruttore pubblico e unico costruttore definito per ClientWorker.
	 * @param socket
	 * @throws IOException
	 */
	public ClientWorker(Socket socket) throws IOException {
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
	 * Helper per fermare il thread, imposta stopThread a true.
	 */
	public void stopTheThread () {
		stopThread = true;
	}

	/** 
	 * Termina il thread su eccezione:
	 * dà notifica con motivazione e imposta il parametro per lo stop.
	 * @param cause 
	 */
	private void terminateThreadOnIOException (String cause) {
		System.out.println(cause);
		System.out.println("Killing this thread!");
		stopTheThread();
	}

	/**
	 * Helper per verificare se l'utente è loggato.
	 * @return
	 */
	private boolean isLogged() {
		return logged;
	}

	/* Due helper per impostare lo stato del login dell'utente. */
	/**
	 * Imposta lo stato su loggato.
	 */
	private void iAmLogged() {
		logged = true;
	}

	/**
	 * Imposta lo stato su falso.
	 */
	private void iAmNotLogged() {
		logged = false;
	}

	/**
	 * Ritorna il valore di inGame, dice se l'utente sta giocando.
	 */
	private boolean isInGame() {
		return inGame;
	}

	/* Seguono un mare di helper comuni a tutti i thread e spostabili nella main class.*/

	/**
	 * Helper per la generazione di un nuovo token alfanumerico.
	 * @return
	 */
	protected static String getNewToken() {
		return Long.toString(Double.doubleToLongBits(Math.random()));
	}

	/**
	 * Helper per verificare l'esistenza di un utente.
	 * @param userToLookFor
	 * @return
	 */
	protected boolean userExists(String userToLookFor) {
		if (Giocatori.containsKey(userToLookFor)) return true;
		else return false;
	}

	/**
	 * Helper per verificare che sia il turno del giocatore che chiama la funzione. 
	 * @return
	 */
	protected boolean isMioTurno (Giocatore curPlayer) {
		if (nomeGiocatoreCorrente.equals(curPlayer.getNome())) return true;
		else return false;
	}

	/**
	 * Gestisce la rimozione di un singolo dinosauro da una cella.
	 */
	public void rimuoviDinosauroDallaCella(int x, int y) {
		rifMappa.rimuoviIlDinosauroDallaCella(x, y);
	}

	/**
	 * Gestisce la rimozione di tutti i dinosauri dalla mappa.	
	 */
	protected void rimuoviDinosauriDallaMappa(Giocatore curPlayer) {
		/* Usa enumerazione per iterare tutti i dinosauri dell'utente e impostarli sulla mappa */
		Enumeration<String> enumerazioneSugliID = myPlayer.getEnumerazioneDegliIdDeiDinosauri();
		while (enumerazioneSugliID.hasMoreElements()) {
			String idCorrente = enumerazioneSugliID.nextElement();
			Dinosauro tempDinosauro = myPlayer.getDinosauro(idCorrente);
			rimuoviDinosauroDallaCella(tempDinosauro.getX(), tempDinosauro.getY());
		}
	}

	/**
	 * Quando l'utente esegue il login aggiunge i dinosauri alla mappa.
	 */
	protected void inserisciDinosauriNellaMappa(Giocatore curPlayer) {
		/* Usa enumerazione per iterare tutti i dinosauri dell'utente e impostarli sulla mappa */
		Enumeration<String> enumerazioneSugliID = myPlayer.getEnumerazioneDegliIdDeiDinosauri();
		while (enumerazioneSugliID.hasMoreElements()) {
			String idCorrente = enumerazioneSugliID.nextElement();
			Dinosauro tempDinosauro = myPlayer.getDinosauro(idCorrente);
			int x = tempDinosauro.getX(), y = tempDinosauro.getY();
			if (tryActualSpawn(x, y, idCorrente)) return;
			else {
				int i = 1;
				do {
					if (tryNearestSpawn(x, y, i, idCorrente, tempDinosauro)) {
						return;
					}
					else i++;
				} while (true);
			}
		}
	}

	/**
	 * Controlla che la Cella sia libera e prova a inserire il dinosauro nella posizione richiesta.
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean tryActualSpawn(int x, int y, String idDinosauro) {
		if (rifMappa.isLibera(x, y)) {
			rifMappa.spawnDinosauro(x, y, idDinosauro, rifMappa.getCella(x, y));
			return true;
		}
		return false;
	}

	/**
	 * Prova a inserire il dinosauro nella Cella più vicina.
	 * Viene chiamata quando la cella salvata in memoria è già occupata.
	 * @param x
	 * @param y
	 * @param maxDistance
	 * @param tempDinosauro
	 * @return
	 */
	private boolean tryNearestSpawn(int x, int y, int maxDistance, String idDinosauro, Dinosauro tempDinosauro) {
		int i = -1;
		int j = -1;
		do {
			for (i = -maxDistance; i < (maxDistance+1); i++) {
				if (tryActualSpawn(i+x, j+y, idDinosauro)) {
					tempDinosauro.setXY(i+x, j+y);
					return true;
				}
			}
			j++;
		} while (j<maxDistance && (0<=(i+x)) &&
				((i+x)<rifMappa.getLatoDellaMappa()) &&
				(0<=(j+y)) &&
				((j+y)<rifMappa.getLatoDellaMappa()) );
		return false;
	}

	/**
	 * Ritorna un valore casuale 0<rnd.nextInt()<LATO_DELLA_MAPPA
	 * @return
	 */
	protected int getNewRandomIntValueOnMyMap() {
		return rnd.nextInt(rifMappa.getLatoDellaMappa());
	}

	/**
	 * Se esiste almeno un dinosauro significa che la razza esiste già
	 * @return
	 */
	protected boolean existsRazza() {
		if ((myPlayer.getNumeroDinosauri() > 0) && (myPlayer.getNomeRazzaDinosauri() != null)) return true;
		else return false;
	}

	/* Due helper per impostare lo stato dell'utente, sta giocando o no? */
	/**
	 * Spawn dei dinosauri e imposta lo stato su "in partita".
	 */
	private void iAmInGame() {
		inserisciDinosauriNellaMappa(myPlayer);
		inGame = true;
	}

	/**
	 * Imposta lo stato su "non in partita".
	 */
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
					if ( myPlayer.passwordIsValid(tempPwd) ) {
						iAmLogged();
						String newToken = getNewToken();
						writeLineToOutput("@ok," + newToken);
						myPlayer.setTokenUnivoco(newToken);
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
				Giocatore tempPlayer = new Giocatore(tempUser, tempPwd); 
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
							else writeLineToOutput("@no");
						}
						else writeLineToOutput("@no,@nonIlTuoTurno");
					}
					else writeLineToOutput("@no.@nonInPartita");
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
			if ( scanner.next(Pattern.compile("[^token=]")).equals(myPlayer.getTokenUnivoco() )) {
				return true;
			}
			else return false;
		}
		else writeLineToOutput("@no");
		return false;
	}

	/**
	 * Crea una nuova razza di dinosauri per l'utente.
	 * @param scanner
	 * @throws IOException
	 */
	private void creaNuovaRazza(Scanner scanner) throws IOException {
		if (scanner.hasNext() && !existsRazza()) {
			String nomeRazza = scanner.next(Pattern.compile("[^nome=]"));
			if (scanner.hasNext()) {
				String tipoRazza = scanner.next(Pattern.compile("[^tipo=]"));
				if (tipoRazza.equals("c") || tipoRazza.equals("e")) {
					if (tipoRazza == "c") {
						int x = getNewRandomIntValueOnMyMap();
						int y = getNewRandomIntValueOnMyMap();
						do {
							if (rifMappa.isLibera(x,y)) {
								myPlayer.creaNuovaRazzaDiDinosauri(nomeRazza, new Carnivoro(x,y));
								break;
							}
						} while (true);
					}
					else if (tipoRazza == "e") {
						do {
							int x = getNewRandomIntValueOnMyMap();
							int y = getNewRandomIntValueOnMyMap();
							if (rifMappa.isLibera(x,y)) {
								myPlayer.creaNuovaRazzaDiDinosauri(nomeRazza, new Erbivoro(x,y));
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
		if (existsRazza() && existsRazza()) {
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
		Enumeration<String> enumerazioneSuiGiocatori = Giocatori.keys();
		String buffer = "@ok,";
		if (enumerazioneSuiGiocatori.hasMoreElements()) {
			while (enumerazioneSuiGiocatori.hasMoreElements()) {
				buffer = buffer + "," + enumerazioneSuiGiocatori.nextElement();
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

	/**
	 * Spedisce la lista dei dinosauri all'utente.
	 * @throws IOException
	 */
	private void sendListaDinosauri() throws IOException {
		if (existsRazza()) {
			String buffer = "@ok";
			Enumeration<String> enumerazioneListaIds = myPlayer.getEnumerazioneDegliIdDeiDinosauri();
			while (enumerazioneListaIds.hasMoreElements()) {
				buffer = buffer + "," + enumerazioneListaIds.nextElement();
			}
			writeLineToOutput(buffer);
		}
		else writeLineToOutput("@no,@nonInPartita");
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

	private void sendMappaGeneale() {
		// TODO Auto-generated method stub

	}

	private void classifica() {
		// TODO Auto-generated method stub

	}
}