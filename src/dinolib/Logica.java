package dinolib;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import dinolib.MieLogicExceptions;

public class Logica {
	/**
	 * Definisce staticamente il lato della mappa.
	 * @uml.property name="LATO_MAPPA"
	 */
	private final int lato_MAPPA = 40;
	/**
	 * Definisce il riferimento alla mappa.
	 * @uml.property name="rifMappa"
	 */
	private Mappa rifMappa;
	/**
	 * Definisce la lista dei giocatori.
	 * @uml.property name="Giocatori"
	 */
	private Hashtable<String, Giocatore> Giocatori;
	/**
	 * Definisce la stringa che contiene il nome del giocatore che in questo momento ha il turno.
	 * @uml.property name="Giocatori"
	 */
	private String nomeGiocatoreCorrente;
	/**
	 * Definisce una variabile che assicura che qualcuno sta giocando.
	 * @uml.property name="qualcunoStaGiocando"
	 */
	protected boolean qualcunoStaGiocando = false;
	/**
	 * Definisce il generatore di numeri casuali.
	 * @uml.property name="rnd"
	 */
	Random rnd = new Random();
	
	public Logica () {
		try {
			caricaPartitaDaFile();
		}
		catch (FileNotFoundException e) {
			System.out.println("No save files found, creating a new map..");
			creaNuovaMappa();
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException while trying to open save files. Exiting..");
			System.exit(-1);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("ClassNotFoundException while trying to read save files. Exiting..");
			System.exit(-1);
		}
	}

	/**
	 * Se i file di salvataggio esistono li carica, altrimenti assume primo avvio
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	private void caricaPartitaDaFile() throws IOException, ClassNotFoundException, FileNotFoundException {
		/**
		 * Carica file di mappa, se esiste deve esistere anche il file dei giocatori.
		 * In caso il primo o l'altro non esistano l'eccezione viene gestita e passata al chiamante, che quindi assume un primo avvios
		 */
		caricaFileDiMappa("mappa.dat");
		caricaFileGiocatori("giocatori.dat");
	}
	/**
	 * Implementa il caricamento del file di mappa, se esiste
	 * Se non esiste lancia FileNotFoundException
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private void caricaFileDiMappa(String nomefile) throws IOException, ClassNotFoundException, FileNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(nomefile)));
		rifMappa = (Mappa) ois.readObject();
	}
	/**
	 * Implementa il caricamento del file giocatori se esiste
	 * Se non esiste lancia FileNotFoundException
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	private void caricaFileGiocatori(String nomefile) throws IOException, ClassNotFoundException, FileNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(nomefile)));
		Giocatori = (Hashtable<String, Giocatore>) ois.readObject();
	}
	
	/**
	 * Helper per la creazione di una mappa nuova
	 */
	private void creaNuovaMappa() {
		rifMappa = new Mappa(lato_MAPPA);
	}
	
	/* Seguono un mare di helper comuni a tutti i thread e spostabili nella main class.*/
	/**
	 * Helper per verificare l'esistenza di un utente.
	 * @param userToLookFor
	 * @return
	 */
	private boolean userExists(String userToLookFor) {
		if (Giocatori.containsKey(userToLookFor)) return true;
		else return false;
	}
	
	/**
	 * Gestisce la rimozione di tutti i dinosauri dalla mappa.	
	 */
	private void rimuoviDinosauriDallaMappa(Giocatore curPlayer) {
		/* Usa iteratore per iterare tutti i dinosauri dell'utente e impostarli sulla mappa */
		Iterator<String> itIdDinosauri = myPlayer.getItIdDinosauri();
		while (itIdDinosauri.hasNext()) {
			String idCorrente = itIdDinosauri.next();
			Dinosauro tempDinosauro = myPlayer.getDinosauro(idCorrente);
			rimuoviDinosauroDallaCella(tempDinosauro.getX(), tempDinosauro.getY());
		}
	}

	/**
	 * Quando l'utente esegue il login aggiunge i dinosauri alla mappa.
	 */
	private void inserisciDinosauriNellaMappa() {
		/* Usa iteratore per iterare tutti i dinosauri dell'utente e impostarli sulla mappa */
		Iterator<String> itIdDinosauri = myPlayer.getItIdDinosauri();
		while (itIdDinosauri.hasNext()) {
			String idCorrente = itIdDinosauri.next();
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
				} while (i < rifMappa.getLatoDellaMappa());
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

	/* Due helper per impostare lo stato dell'utente, sta giocando o no? */
	/**
	 * Spawn dei dinosauri e imposta lo stato su "in partita".
	 */
	private void iAmInGame() {
		inserisciDinosauriNellaMappa();
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
						String newToken = CommonUtils.getNewToken();
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
			if (scanner.hasNext()) {
				String comando = scanner.next();
				if (readAndValidateTokenFromInput(scanner, myPlayer)) {
					if (scanner.hasNext()) {
						/* comandi fuori partita*/
						if (comando.equals("@creaRazza")) creaNuovaRazza(scanner);
						else if (comando.equals("@accessoPartita")) accediAPartita();
						else if (comando.equals("@uscitaPartita")) esciDallaPartita();
						else if (comando.equals("@listaGiocatori")) listaDeiGiocatori();
						else if (comando.equals("@classifica")) classifica();
						else if (comando.equals("@logout")) handleLogout();
						/* comandi in partita */
						else if (isInGame()) {
							/* comandi di informazione */
							if (comando.equals("@mappaGenerale")) sendMappaGenerale();
							else if (comando.equals("@listaDinosauri")) sendListaDinosauri();
							else if (comando.equals("@vistaLocale")) sendVistaLocale();
							else if (comando.equals("@statoDinosauro")) sendStatoDinosauro(scanner);
							if (isMioTurno(super.nomeGiocatoreCorrente, myPlayer)) {
								/* comandi di azione */
								if (comando.equals("@muoviDinosauro")) muoviDinosauro();
								else if (comando.equals("@cresciDinosauro")) cresciDinosauro(scanner);
								else if (comando.equals("@deponiUovo")) deponiUovo(scanner);
								/* comandi di turno */
								else if (comando.equals("@confermaTurno")) confermaTurno();
								else if (comando.equals("@passaTurno")) passaTurno();
								else writeLineToOutput("@no");
							}
							else writeLineToOutput("@no,@nonIlTuoTurno");
						}
						else writeLineToOutput("@no,@nonInPartita");
					}
					else writeLineToOutput("@no");
				}
				else writeLineToOutput("@no,tokenNonValido");
			}
			else writeLineToOutput("@no");
		} while (isLogged());
	}
	
	/**
	 * Crea una nuova razza di dinosauri per l'utente.
	 * @param scanner
	 * @throws IOException
	 */
	private void creaNuovaRazza(Scanner scanner) throws IOException {
		if (scanner.hasNext() && !existsRazza(myPlayer)) {
			String nomeRazza = scanner.next(Pattern.compile("[^nome=]"));
			if (scanner.hasNext()) {
				String tipoRazza = scanner.next(Pattern.compile("[^tipo=]"));
				if (tipoRazza.equals("c") || tipoRazza.equals("e")) {
					if (tipoRazza == "c") {
						do {
							int x = CommonUtils.getNewRandomIntValueOnMyMap(rifMappa.getLatoDellaMappa());
							int y = CommonUtils.getNewRandomIntValueOnMyMap(rifMappa.getLatoDellaMappa());
							if (rifMappa.isLibera(x,y)) {
								myPlayer.creaNuovaRazzaDiDinosauri(nomeRazza, new Carnivoro(x,y));
								break;
							}
						} while (true);
					}
					else if (tipoRazza == "e") {
						do {
							int x = CommonUtils.getNewRandomIntValueOnMyMap(rifMappa.getLatoDellaMappa());
							int y = CommonUtils.getNewRandomIntValueOnMyMap(rifMappa.getLatoDellaMappa());
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
		if (existsRazza(myPlayer)) {
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
		Iterator<String> itNomiGiocatori = Giocatori.keySet().iterator();
		String buffer = "@ok,";
		if (itNomiGiocatori.hasNext()) {
			while (itNomiGiocatori.hasNext()) {
				buffer = buffer + "," + itNomiGiocatori.next();
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
		if (existsRazza(myPlayer)) {
			String buffer = "@ok";
			Iterator<String> itListaIds = myPlayer.getItIdDinosauri();
			while (itListaIds.hasNext()) {
				buffer = buffer + "," + itListaIds.next();
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

	private void muoviDinosauro() {
		// TODO Auto-generated method stub

	}

	/**
	 * Prova a inserire un dinosauro appena nato. Questo metodo viene usato da deponiUovo.
	 * @return
	 */
	private boolean trySpawnOfAnEgg(int x, int y, Dinosauro newDinosauro, String newIdDinosauro) {
		int i = 1;
		do {
			if (tryNearestSpawn(x, y, i, newIdDinosauro, newDinosauro)) {
				myPlayer.aggiungiDinosauroAllaRazza(newDinosauro, newIdDinosauro);
				return true;
			}
			else i++;
		} while (i<rifMappa.getLatoDellaMappa());
		return false;
	}

	/**
	 * Gestisce il comando che depone l'uovo.
	 * @param scanner
	 * @throws IOException
	 */
	private void deponiUovo(Scanner scanner) throws IOException {
		if (myPlayer.getNumeroDinosauri() < 5) {
			String idDinosauro = scanner.next(Pattern.compile("[^idDino=]"));
			Dinosauro curDinosauro = myPlayer.getDinosauro(idDinosauro);
			if (curDinosauro.haAbbastanzaEnergiaPerDeporreUnUovo()){
				int x = curDinosauro.getX();
				int y = curDinosauro.getY();
				String newIdDinosauro = CommonUtils.getNewToken();
				if (curDinosauro.getTipoRazza().equals("Carnivoro")) {
					Dinosauro newDinosauro = new Carnivoro(x, y);
					newDinosauro.nonSonoUsabile();
					if (trySpawnOfAnEgg(x, y, newDinosauro, newIdDinosauro)) {
						writeLineToOutput("@ok" + "," + newIdDinosauro);
					}
					else writeLineToOutput("@no");
				}
				else if (curDinosauro.getTipoRazza().equals("Erbivoro")) {
					Dinosauro newDinosauro = new Erbivoro(x, y);
					newDinosauro.nonSonoUsabile();
					if (trySpawnOfAnEgg(x, y, newDinosauro, newIdDinosauro)) {
						writeLineToOutput("@ok" + "," + newIdDinosauro);
					}
					else writeLineToOutput("@no");
				}
				else writeLineToOutput("@no");
			}
			else writeLineToOutput("@no,@mortePerInedia");
		}
		else writeLineToOutput("@no,@raggiuntoNumeroMaxDinosauri");
	}

	/**
	 * Chiama la funzione di crescita del dinosauro.
	 */
	private void cresciDinosauro(Scanner scanner) throws IOException {
		String idDinosauro = scanner.next(Pattern.compile("[^idDino=]"));
		if (myPlayer.existsDinosauro(idDinosauro)) {
			Dinosauro tempDinosauro = myPlayer.getDinosauro(idDinosauro);
			if (!tempDinosauro.isAtDimensioneMax()) {
				if (tempDinosauro.haAbbastanzaEnergiaPerCrescere()) {
					tempDinosauro.cresci();
					writeLineToOutput("ok");
				}
				else writeLineToOutput("@no,@mortePerInedia");
			}
			else writeLineToOutput("@no,@raggiuntaDimensioneMax");
		}
		else writeLineToOutput("@no,@idNonValido");
	}

	/**
	 * Invia lo stato del dinosauro richiesto all'utente.
	 * @param scanner
	 * @throws IOException
	 */
	private void sendStatoDinosauro(Scanner scanner) throws IOException {
		String idDinosauro = scanner.next(Pattern.compile("[^idDino=]"));
		if (myPlayer.existsDinosauro(idDinosauro)) {
			Dinosauro tempDinosauro = myPlayer.getDinosauro(idDinosauro);
			String buffer = "@statoDinosauro";
			buffer = buffer + "," +
			myPlayer.getNome() + "," +
			myPlayer.getNomeRazzaDinosauri() + "," +
			myPlayer.getTipoRazza().toLowerCase().charAt(0) + "," +
			"{" + tempDinosauro.getX() + "," + tempDinosauro.getY() + "}" +
			tempDinosauro.getDimensione() + "," +
			tempDinosauro.getEnergiaAttuale() + "," +
			tempDinosauro.getTurnoDiVita();
			writeLineToOutput(buffer);
		}
		else if (!myPlayer.existsDinosauro(idDinosauro)) {
			Iterator<Giocatore> itSuiGiocatori = Giocatori.values().iterator();
			while (itSuiGiocatori.hasNext()) {
				Giocatore tempGiocatore = itSuiGiocatori.next();
				Iterator<String> itSuIdDinosauri = tempGiocatore.getItIdDinosauri();
				while (itSuIdDinosauri.hasNext()) {
					if (itSuIdDinosauri.next() == idDinosauro) {
						Dinosauro tempDinosauro = tempGiocatore.getDinosauro(idDinosauro);
						String buffer = "@statoDinosauro";
						buffer = buffer + "," +
						tempGiocatore.getNome() + "," +
						tempGiocatore.getNomeRazzaDinosauri() + "," +
						tempGiocatore.getTipoRazza().toLowerCase().charAt(0) + "," +
						"{" + tempDinosauro.getX() + "," + tempDinosauro.getY() + "}" +
						tempDinosauro.getDimensione();
						writeLineToOutput(buffer);
					}
				}
			}
		}
		else writeLineToOutput("@no,@idNonValido");
	}

	private void sendVistaLocale() {
		// TODO Auto-generated method stub

	}

	private void sendMappaGenerale() {
		// TODO Auto-generated method stub

	}

	private void classifica() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Se esiste almeno un dinosauro significa che la razza esiste già
	 * @return
	 */
	private static boolean existsRazza(Giocatore curPlayer) {
		if ((curPlayer.getNumeroDinosauri() > 0) && (curPlayer.getNomeRazzaDinosauri() != null)) return true;
		else return false;
	}
	
	/**
	 * Helper per verificare che sia il turno del giocatore che chiama la funzione. 
	 * @return
	 */
	private static boolean isMioTurno (String nomeDelGiocatoreCorrente, Giocatore curPlayer) {
		if (nomeDelGiocatoreCorrente.equals(curPlayer.getNome())) return true;
		else return false;
	}
	
	/**
	 * Helper per garantire la correttezza del token che è sempre il secondo parametro dei comandi da utente loggato.
	 * @param scanner
	 * @return
	 * @throws IOException
	 */
	private static boolean readAndValidateTokenFromInput (Scanner scanner, Giocatore curPlayer) throws IOException {
		if ( scanner.hasNext() ) {
			if ( scanner.next(Pattern.compile("[^token=]")).equals(curPlayer.getTokenUnivoco() )) {
				return true;
			}
			else return false;
		}
		return false;
	}
}