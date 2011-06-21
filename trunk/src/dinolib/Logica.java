package dinolib;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import dinolib.Exceptions.*;
import dinolib.Razza.Carnivoro;
import dinolib.Razza.Dinosauro;
import dinolib.Razza.Erbivoro;
import dinolib.Mappa.Cella;
import dinolib.Mappa.Coord;
import dinolib.Mappa.Mappa;

public class Logica implements Runnable {
	/* Tutte le variabili statiche/definitive e non modificabili */
	/**
	 * Definisce definitivamente il numero massimo di giocatori ammessi in partita.
	 * @uml.property  name="NUMERO_MASSIMO_GIOCATORI_INGAME"
	 */
	private final int numero_MASSIMO_GIOCATORI_INGAME = 8;
	/**
	 * Definisce definitivamente il lato della mappa.
	 * @uml.property  name="LATO_MAPPA"
	 */
	private final int lato_MAPPA = 40;
	/**
	 * Definisce definitivamente il tempo da attendere senza la conferma di turno.
	 * @uml.property  name="SLEEP_CONFERMA_TURNO"
	 */
	private final int sleep_CONFERMA_TURNO = 30;
	/**
	 * Definisce definitivamente il tempo da dedicare ad ogni giocatore quando questo conferma il turno.
	 * @uml.property  name="SLEEP_TEMPO_TURNO"
	 */
	private final int sleep_TEMPO_TURNO = 120;
	/**
	 * Definisce definitivamente il tempo da dedicare ad ogni giocatore quando questo conferma il turno.
	 * @uml.property  name="NOME_FILE_MAPPA"
	 */
	private final String nome_FILE_MAPPA = "mappa.dat";
	/**
	 * Definisce definitivamente il tempo da dedicare ad ogni giocatore quando questo conferma il turno.
	 * @uml.property  name="NOME_FILE_GIOCATORI"
	 */
	private final String nome_FILE_GIOCATORI = "giocatori.dat";

	/* Tutte le variabili istanziabili */
	/**
	 * Istanzia il riferimento alla mappa.
	 * @uml.property  name="rifMappa"
	 */
	private Mappa rifMappa = null;
	/**
	 * Istanzia il riferimento alla collezione dei giocatori.
	 * @uml.property name="playerManager"
	 */
	private PlayerManager pMan = null;
	/**
	 * Istanzia il riferimento alla collezione dei giocatori connessi e dei loro token.
	 * @uml.property name="connectionManager"
	 */
	private ConnectionManager cMan = null;
	/**
	 * Contiene tutti i giocatori correntemente connessi alla partita.
	 * @uml.property  name="playersQueue"
	 */
	private RRScheduler rrsched = null;
	/**
	 * Definisce la stringa che contiene il nome del giocatore che in questo momento ha il turno.
	 * @uml.property  name="Giocatori"
	 */
	private String tokenGiocatoreCorrente = null;
	/**
	 * Contiene una variabile che dice se la logica sta funzionando.
	 * @uml.property  name="logicaIsRunning"
	 */
	private boolean logicaIsRunning = false;
	/**
	 * Variabile che dice se il turno del giocatore è stato confermato.
	 * @uml.property  name="turnoConfermato"
	 */
	private boolean turnoConfermato = false;

	/* Costruttore, chiama le funzioni di primo avvio */
	/**
	 * Costruttore per la classe Logica.
	 */
	public Logica () { // Testato
		try {
			caricaPartitaDaFile();
		}
		catch (FileNotFoundException e) {
			System.out.println("No save files found, creating a new map..");
			creaNuovaMappa();
			pMan = new PlayerManager();
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
		cMan = new ConnectionManager();
		rrsched = new RRScheduler(numero_MASSIMO_GIOCATORI_INGAME);
		logicaIsRunning = true;
	}
	/**
	 * Carica i file di salvataggio, se esistono.
	 * @throws IOException Se ci sono problemi nel leggere il file.
	 * @throws FileNotFoundException Se il file non viene trovato.
	 * @throws ClassNotFoundException Se la classe che deve essere usata per leggere i file non viene trovata. 
	 */
	private void caricaPartitaDaFile() throws IOException, ClassNotFoundException, FileNotFoundException {
		/**
		 * Carica file di mappa, se esiste deve esistere anche il file dei giocatori.
		 * In caso il primo o l'altro non esistano l'eccezione viene gestita e passata al chiamante, che quindi assume un primo avvio.
		 */
		caricaFileMappa(nome_FILE_MAPPA);
		caricaFileGiocatori(nome_FILE_GIOCATORI);
	}
	/**
	 * Implementa il caricamento del file di mappa, se esiste.
	 * @throws IOException Se ci sono problemi nel leggere il file.
	 * @throws FileNotFoundException Se il file non viene trovato.
	 * @throws ClassNotFoundException Se la classe che deve essere usata per leggere i file non viene trovata. 
	 */
	private void caricaFileMappa(String nomefile) throws IOException, ClassNotFoundException, FileNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(nomefile)));
		rifMappa = (Mappa) ois.readObject();
	}
	/**
	 * Implementa il caricamento del file giocatori, se esiste.
	 * @throws IOException Se ci sono problemi nel leggere il file.
	 * @throws FileNotFoundException Se il file non viene trovato.
	 * @throws ClassNotFoundException Se la classe che deve essere usata per leggere i file non viene trovata.
	 */
	private void caricaFileGiocatori(String nomefile) throws IOException, FileNotFoundException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(nomefile)));
		pMan = (PlayerManager) ois.readObject();
	}
	/**
	 * Crea una nuova mappa.
	 */
	private void creaNuovaMappa() { rifMappa = new Mappa(lato_MAPPA); }

	/* Tutti i getter */
	/**
	 * Restituisce l'oggetto Mappa.
	 * @return L'oggetto Mappa.
	 */
	protected Mappa getMappa() { return rifMappa; } // Testato
	/**
	 * Restituisce l'oggetto PlayerManager.
	 * @return L'oggetto PlayerManager.
	 */
	protected PlayerManager getPMan() { return pMan; } // Testato
	/**
	 * Restituisce l'oggetto ConnectionManager.
	 * @return L'oggetto ConnectionManager.
	 */
	protected ConnectionManager getCMan() { return cMan; } // Testato
	/**
	 * Restituisce l'oggetto RRScheduler.
	 * @return L'oggetto RRScheduler.
	 */
	protected RRScheduler getRRSched() { return rrsched; } // Testato
	/**
	 * Dice se la logica sta ancora lavorando.
	 * @return True se la logica sta ancora lavorando, false altrimenti.
	 */
	protected boolean isLogicaRunning() { return logicaIsRunning; } // Testato
	/**
	 * Ritorna il giocatore richiesto tramite il token.
	 * @param token Il token dell'utente richiesto.
	 * @return Il giocatore richiesto.
	 * @throws InvalidTokenException Se il token non e' valido.
	 */
	protected Giocatore getPlayerByToken(String token) throws InvalidTokenException { // Testato
		if (getCMan().existsToken(token) && getPMan().exists(getCMan().getName(token))) return getPMan().getPlayer(getCMan().getName(token));
		else throw new InvalidTokenException();
	}

	/**
	 * Verifica se e' il turno del giocatore che chiama la funzione. 
	 * @return True se e' il turno del giocatore, false altrimenti.
	 * @throws InvalidTokenException Se il token non e' valido.
	 * @throws NonInPartitaException Se il giocatore non e' in partita.
	 */
	protected boolean isMioTurno(String token) throws InvalidTokenException, NonInPartitaException {
		if (isPlayerInGame(token)) {
			if (tokenGiocatoreCorrente.equals(token)) return true;
			else return false;
		}
		else throw new NonInPartitaException();
	}
	/**
	 * Verifica se il giocatore è in partita.
	 * Se si' ritorna true, se no ritorna false. Se non e' loggato correttamente (il token non esiste) lancia eccezione.
	 * @throws InvalidTokenException Se il token non e' valido. 
	 * @throws NonInPartitaException Se il giocatore non e' in partita.
	 */
	protected boolean isPlayerInGame(String token) throws InvalidTokenException { // Testato
		if (isPlayerLogged(token)) {
			if (getRRSched().hasTask(token)) return true;
			else return false;
		}
		else throw new InvalidTokenException();
	}
	/**
	 * Verifica se l'utente è autenticato.
	 * @param token Il token del giocatore da verificare.
	 * @return True se il giocatore e' loggato, false altrimenti.
	 * @throws InvalidTokenException Se il token non e' valido.
	 */
	protected boolean isPlayerLogged(String token) throws InvalidTokenException { // Testato
		if (getCMan().existsToken(token)) return true;
		else return false;
	}
	/**
	 * Controlla se esiste un dinosauro tra tutti quelli dei giocatori che hanno una razza.
	 * @param idDinosauro L'id del dinosauro da cercare.
	 * @return True se il dinosauro esiste nella razza di un giocatore, false altrimenti.
	 */
	protected boolean existsDinosauroWithId(String idDinosauro) { // Testato
		Iterator<Giocatore> itGiocatori = getPMan().iterator();
		Giocatore tempGiocatore = null;
		while (itGiocatori.hasNext()) {
			tempGiocatore = itGiocatori.next();
			if (tempGiocatore.hasRazza())
				if (tempGiocatore.getRazza().existsDinosauroWithId(idDinosauro)) {
					return true;
				}
		}
		return false;
	}
	/**
	 * Restituisce il giocatore che possiede il dato dinosauro.
	 * @param idDinosauro L'id del dinosauro da cercare.
	 * @return Il giocatore a cui appartiene il dinosauro.
	 */
	protected Giocatore getPlayerByIdDinosauro(String idDinosauro) { // Testato
		Iterator<Giocatore> itSuiGiocatori = getPMan().iterator();
		Giocatore tempGiocatore = null;
		if (existsDinosauroWithId(idDinosauro)) {
			while (itSuiGiocatori.hasNext()) {
				tempGiocatore = itSuiGiocatori.next();
				if (tempGiocatore.hasRazza()) {
					if (tempGiocatore.getRazza().existsDinosauroWithId(idDinosauro))  {
						return tempGiocatore;
					}
				}
			}
		}
		return tempGiocatore;
	}

	private void broadcastCambioTurno() {

	}

	/**
	 * Aggiorna l'ambiente di gioco ogni volta che si passa da un giocatore all'altro.
	 */
	private void updatePartita() {
		getMappa().aggiorna();
		getPMan().aggiorna();
	}
	/*
	 * Algoritmo:
	 * fintanto che (la logica va)
	 * 		se (la pila dei giocatori in partita contiene giocatori)
	 * 			assegna al giocatore corrente la testa della pila
	 * 
	 */
	/**
	 * Questo metodo gestisce effettivamente la logica di turno.
	 * Creare un thread extra è più semplice che sincronizzare gli accessi da ogni client.
	 * In questo modo tutta la logica è effettivamente implementata in Logica e creare nuovi modi di accesso ai dati richiede solo l'implementazione di Adattatori.
	 */
	public void run () {
		/* Prova a ...*/
		try {
			/* Fintanto che logica è accesa...
			 * !!ATTENZIONE!!
			 * Impongo la condizione isLogicaRunning() in TUTTI i condizionali che controllano i cicli,
			 * di modo che non appena la logica viene spenta, l'uscita è immediata senza le attese di turno */
			while (isLogicaRunning()) {
				/* Se ci sono giocatori connessi */
				if (getRRSched().hasQueuedTasks() && isLogicaRunning()) {
					/* Prendi un nuovo giocatore dalla lista */
					tokenGiocatoreCorrente = getRRSched().getCurrentTask();
					/* Prendi l'istante da cui conto il tempo per confermare il turno */
					long conferma_start = System.currentTimeMillis();
					/* Fintanto che posso confermare il turno (30 secondi) */
					while (((System.currentTimeMillis()-conferma_start) < (sleep_CONFERMA_TURNO*1000)) && isLogicaRunning()) {
						/* Se il turno è stato confermato */
						if (turnoConfermato) {
							/* Prendi l'istante da cui conto il tempo per usare il turno */
							long turno_start = System.currentTimeMillis();
							/* Fintanto che posso usare il turno (muovere i dinosauri o usare le loro azioni) */
							while (((System.currentTimeMillis()-turno_start) < (sleep_TEMPO_TURNO*1000)) && isLogicaRunning()) {
								/* Se il turno è stato passato ferma il ciclo */
								if (!turnoConfermato) break;
								/* Altrimenti aspetta 1 secondo */
								else {
									Thread.sleep(1000);
								}
							}
							/* Ri-aggiungi il giocatore corrente in coda alla lista dei giocatori in gioco */
							getRRSched().newTask(tokenGiocatoreCorrente);
							/* Aggiorna il campo di gioco */
							updatePartita();
							/* Notifica che cambia il turno */
							broadcastCambioTurno();
						}
						/* Se il turno non è stato confermato aspetta 1 secondo,
						 * fino a quando il turno viene confermato OPPURE
						 * fino a quando scade il tempo per confermare il turno */
						else {
							Thread.sleep(1000);
						}
					}
				}
				/* Se non ci sono giocatori connessi aspetta 1 secondo */
				else {
					Thread.sleep(1000);
				}
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		notify();
	}
	/**
	 * Ferma la logica, imposta logicaIsRunning a false, così che il thread possa essere spento 
	 */
	public synchronized void stop() {
		logicaIsRunning = false;
		try {
			wait();
		}
		catch (InterruptedException e) {
			System.out.println("InterruptedException caught while stopping the thread. This is ok, so don't worry.");
		}
		try {
			salvaPartita();
		}
		catch (FileNotFoundException e) { System.out.println("FileNotFoundException thrown while trying to save the game. Unable to save the game to file, aborting."); }
		catch (IOException e) { System.out.println("IOException thrown while trying to save the game. Unable to save the game to file, aborting."); }
	}
	/**
	 * Salva la partita su file (la collezione di giocatori e il file di mappa).
	 * @throws FileNotFoundException Se il file risulta inaccessibile.
	 * @throws IOException Se si sono verificati problemi con la scrittura.
	 */
	private void salvaPartita() throws FileNotFoundException, IOException {
		salvaFileMappa(nome_FILE_MAPPA);
		salvaFileGiocatori(nome_FILE_GIOCATORI);
	}
	/**
	 * Salva la mappa su file.
	 * @param nomefile Il nome del file su quale salvare la mappa.
	 * @throws FileNotFoundException Se il file risulta inaccessibile.
	 * @throws IOException Se si sono verificati problemi con la scrittura.
	 */
	private void salvaFileMappa(String nomefile) throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(nomefile)));
		oos.writeObject(rifMappa);
	}
	/**
	 * Salva i giocatori su file.
	 * @param nomefile Il nome del file sul quale salvare i giocatori.
	 * @throws FileNotFoundException Se il file risulta inaccessibile.
	 * @throws IOException Se si sono verificati problemi con la scrittura.
	 */
	private void salvaFileGiocatori(String nomefile) throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(nomefile)));
		oos.writeObject(pMan);
	}
	
	/* Helper esterni */
	/**
	 * Conferma il turno  // TODO implementa il controllo di accesso, solo l'utente legittimo deve poterlo chiamare!
	 */
	void doConfermaTurno() {
		turnoConfermato = true;
	}
	/**
	 * Gestisce la rimozione di tutti i dinosauri dalla mappa per il dato giocatore.
	 * @param tempGiocatore Il giocatore i cui dinosauri devono essere rimossi dalla mappa.	
	 * @throws InvalidTokenException Se il token non e' valido.
	 */
	private void rimuoviDinosauriDallaMappa(Giocatore tempGiocatore) {
		if (tempGiocatore.hasRazza()) {
			Iterator<Dinosauro> itDinosauri = tempGiocatore.getRazza().iterator();
			Dinosauro tempDinosauro;
			while (itDinosauri.hasNext()) {
				tempDinosauro = itDinosauri.next();
				getMappa().rimuoviIlDinosauroDallaCella(tempDinosauro.getCoord());
			}
		}
		return;
	}
	/**
	 * Funzione ricorsiva che prova a piazzare un dinosauro sulla mappa.
	 * Teoricamente ritorna sempre true fino a che tutte le celle terra e vegetazione non sono piene, visto che e' ricorsiva e DIVERGENTE (parte dal punto ideale e gira attorno fino a quando non piazza).
	 * @param tempDinosauro Il dinosauro da piazzare
	 * @param maxDistance La distanza massima dal punto di origine dove piazzare il dinosauro.
	 * @return True se il dinosauro e' stato piazzato, false altrimenti.
	 */
	private boolean trySpawn(Dinosauro tempDinosauro, int maxDistance) {
		Coord tempCoord = null;
		if (maxDistance == 0) {
			if (!isCellaAcqua(tempDinosauro.getCoord()) &&
					!isCellaDinosauro(tempDinosauro.getCoord())) {
				getMappa().spawnDinosauro(tempDinosauro.getCoord(), tempDinosauro.getIdDinosauro());
				return true;
			}
			else return trySpawn(tempDinosauro, maxDistance+1);
		}
		else {
			int i = -maxDistance;
			int j = -maxDistance;
			while(i<(maxDistance+1)) {
				j = -maxDistance;
				while(j<(maxDistance+1)) {
					tempCoord = new Coord(tempDinosauro.getCoord().getX()+i, tempDinosauro.getCoord().getY()+j);
					if (isValidCoord(tempCoord)) {
						if (!isCellaAcqua(tempCoord) &&
								!isCellaDinosauro(tempCoord)) {
							tempDinosauro.setCoord(tempCoord);
							getMappa().spawnDinosauro(tempDinosauro.getCoord(), tempDinosauro.getIdDinosauro());
							return true;
						}
					}
					j++;
				}
				i++;
			}
			return trySpawn(tempDinosauro, maxDistance+1);
		}
	}
	/**
	 * Quando l'utente esegue il login aggiunge i dinosauri alla mappa.
	 * @throws InvalidTokenException Se il token non e' valido.
	 */
	private void inserisciDinosauriNellaMappa(String token) throws InvalidTokenException {
		if (getPlayerByToken(token).hasRazza()) {
			Iterator<Dinosauro> itDinosauri = getPlayerByToken(token).getRazza().iterator();
			while (itDinosauri.hasNext()) {
				if (trySpawn(itDinosauri.next(), 0)) {
					return;
				}
			}
		}
	}
	/**
	 * Verifica se la coordinata passata e' valida (e' dentro i confini della mappa).
	 * @param coordTest La coordinata da verificare.
	 * @return True se la coordinata e' valida, false altrimenti.
	 */
	private boolean isValidCoord(Coord coordTest) {
		if ( (0 <= coordTest.getX()) &&
				(coordTest.getX() < getMappa().getLatoDellaMappa()) &&
				(0 <= coordTest.getY()) &&
				(coordTest.getY() < getMappa().getLatoDellaMappa())) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * Passa il turno.
	 */
	void doPassaTurno() { // TODO implementa controllo degli accessi.
		turnoConfermato = false;
	}
	/**
	 * Fa deporre l'uovo al dinosauro.
	 * È un helper comune, gli adattatori non devono vedere niente della logica interna.
	 * @param token Il token del giocatore che vuole far deporre l'uovo al dinosauro.
	 * @param idDinosauro L'id del dinosauro a cui si vuole far deporre l'uovo.
	 * @return L'id del dinosauro se la deposizione dell'uovo ha successo, null altrimenti.
	 * @throws GenericDinosauroException Se viene riscontrata qualche eccezione nella gestione dei dinosauri.
	 * @throws InvalidTokenException Se il token non e' valido.
	 */
	protected String doDeponiUovo(String token, String idDinosauro) throws GenericDinosauroException, InvalidTokenException { // Testato
		Coord coordToRemove = null;
		try {
			if (getPlayerByToken(token).hasRazza()) {
				Dinosauro tempDinosauro = getPlayerByToken(token).getRazza().getDinosauroById(idDinosauro);
				coordToRemove = tempDinosauro.getCoord();
				if (!tempDinosauro.hasAzioneStatica()) throw new GenericDinosauroException("raggiuntoLimiteMosseDinosauro");
				getPlayerByToken(token).getRazza().deponiUovo(idDinosauro);
				if (getPlayerByToken(token).getRazza().getTipo().equals('c')) {
					Dinosauro newDinosauro = new Carnivoro(tempDinosauro.getCoord());
					newDinosauro.nonUsabile();
					if (getPlayerByToken(token).getRazza().add(newDinosauro)) {
						if(trySpawn(newDinosauro, 0)) {
							return newDinosauro.getIdDinosauro();
						}
					}
				}
				else if (getPlayerByToken(token).getRazza().getTipo().equals('e')) {
					Dinosauro newDinosauro = new Erbivoro(tempDinosauro.getCoord());
					newDinosauro.nonUsabile();
					if (getPlayerByToken(token).getRazza().add(newDinosauro)) {
						if(trySpawn(newDinosauro, 0)) {
							return newDinosauro.getIdDinosauro();
						}
					}
				}
			}
			return null;
		}
		catch (InvalidTokenException e) {
			throw new InvalidTokenException();
		}
		catch (GenericDinosauroException e) {
			if (e.getMessage().equals("mortePerInedia")) {
				getMappa().rimuoviIlDinosauroDallaCella(coordToRemove);
			}
			throw new GenericDinosauroException(e.getMessage());
		}
	}
	/**
	 * Fa crescere il dinosauro.
	 * È un helper comune, gli adattatori non devono vedere niente della logica interna.
	 * @param token Il token del giocatore che vuole far crescere il dinosauro.
	 * @param idDinosauro L'id del dinosauro che si vuole far crescere.
	 * @return True se l'operazione ha successo, false altrimenti.
	 * @throws GenericDinosauroException Se viene riscontrata qualche eccezione nella gestione dei dinosauri.
	 * @throws InvalidTokenException Se il token non e' valido.
	 */
	protected boolean doCresciDinosauro(String token, String idDinosauro) throws InvalidTokenException, GenericDinosauroException { // Testato
		Coord coordToRemove = null;
		try {
			if (getPlayerByToken(token).getRazza().existsDinosauroWithId(idDinosauro)) {
				coordToRemove = getPlayerByToken(token).getRazza().getDinosauroById(idDinosauro).getCoord();
				getPlayerByToken(token).getRazza().cresciDinosauro(idDinosauro);
				return true;
			}
			return false;
		} catch (InvalidTokenException e) {
			throw new InvalidTokenException();
		} catch (GenericDinosauroException e) {
			if (e.getMessage().equals("mortePerInedia")) {
				getMappa().rimuoviIlDinosauroDallaCella(coordToRemove);
			}
			throw new GenericDinosauroException(e.getMessage());
		}
	}
	/**
	 * Implementa la creazione dell'utente, se esiste già lancia eccezione.
	 * Viene chiamato dagli adattatori.
	 * @param user Il nome del nuovo utente.
	 * @param pwd La password del nuovo utente.
	 * @return True se l'operazione ha successo, false altrimenti.
	 * @throws UserExistsException Se l'utente esiste gia'.
	 */
	protected boolean doCreaUtente(String nomeGiocatore, String pwd) throws UserExistsException { // Testato
		if (!getPMan().exists(nomeGiocatore)) {
			return getPMan().add(new Giocatore(nomeGiocatore, pwd));
		}
		else throw new UserExistsException();
	}
	/**
	 * Implementa il login dell'utente.
	 * Se esiste permette il login.
	 * Se la password non è valida lancia un'eccezione.
	 * @param nomeGiocatore Il nome del giocatore che vuole effettuare il login.
	 * @param suppliedPassword La password del giocatore che vuole effettuare il login.
	 * @return True se l'operazione ha successo, false altrimenti.
	 * @throws UserAuthenticationFailedException Se l'autenticazione fallisce (la password non e' valida).
	 */
	protected boolean doLogin(String nomeGiocatore, String suppliedPassword) throws UserAuthenticationFailedException { // Testato
		if (getPMan().exists(nomeGiocatore)) {
			Giocatore tempGiocatore = getPMan().getPlayer(nomeGiocatore);
			if (tempGiocatore.passwordIsValid(suppliedPassword)) {
				getCMan().collega(nomeGiocatore, CommonUtils.getNewToken());
				return true;
			}
			else throw new UserAuthenticationFailedException();
		}
		else return false;
	}
	/**
	 * Implementa la creazione della razza per l'utente.
	 * @param token Il token dell'utente che vuole creare la razza
	 * @param nomeRazza Il nome della nuova razza.
	 * @param tipoRazza Il tipo della nuova razza.
	 * @return True se la razza e' stata creata, false altrimenti.
	 * @throws NomeRazzaOccupatoException Se il nome della razza e' occupato.
	 * @throws InvalidTokenException Se il token non e' valido.
	 */
	protected boolean doCreaRazza(String token, String nomeRazza, Character tipoRazza) throws NomeRazzaOccupatoException, InvalidTokenException { // Testato
		if (isPlayerLogged(token)) {
			Iterator<Giocatore> itGiocatori = getPMan().iterator();
			Giocatore tempGiocatore = null;
			while (itGiocatori.hasNext()) {
				tempGiocatore = itGiocatori.next();
				if (tempGiocatore.hasRazza()) {
					if (tempGiocatore.getRazza().getNome().equals(nomeRazza)) throw new NomeRazzaOccupatoException();
				}
			}
			if (!getPlayerByToken(token).hasRazza()) {
				getPlayerByToken(token).creaNuovaRazza(nomeRazza, tipoRazza);
				return true;
			}
			else return false;
		}
		else throw new InvalidTokenException();
	}
	/**
	 * Crea il primo dinosauro per l'utente.
	 * @param token Il token dell'utente per cui il primo dinosauro dev essere creato.
	 * @return True se il dinosauro e' stato creato, false altrimenti.
	 * @throws InvalidTokenException Se il token non e' valido.
	 */
	private boolean creaPrimoDinosauro(String token) throws InvalidTokenException {
		Giocatore tempGiocatore = getPlayerByToken(token);
		if (tempGiocatore.getRazza().getTipo().equals('c')) {
			if(tempGiocatore.getRazza().add(new Carnivoro(CommonUtils.getNewRandomCoord(getMappa().getLatoDellaMappa())))) {
				return true;
			}
		}
		else if (tempGiocatore.getRazza().getTipo().equals('e')) {
			if(tempGiocatore.getRazza().add(new Erbivoro(CommonUtils.getNewRandomCoord(getMappa().getLatoDellaMappa())))) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Permette l'accesso alla partita.
	 * @param token Il token dell'utente che vuole accedere alla partita.
	 * @return True se l'accesso alla partita ha avuto successo, false altrimenti.
	 * @throws InvalidTokenException Se il token non e' valido.
	 * @throws InterruptedException Se l'accesso alla collezione e' stato impedito per problemi di concorrenza.
	 * @throws TroppiGiocatoriException Se ci sono troppi giocatori in partita.
	 * @throws RazzaNonCreataException Se la razza per l'utente non e' stata creata.
	 */
	protected boolean doAccessoPartita(String token) throws InvalidTokenException, TroppiGiocatoriException, RazzaNonCreataException, InterruptedException { // Testato
		if (isPlayerLogged(token)) {
			if (getPlayerByToken(token).hasRazza()) {
				if (!getRRSched().maxPlayers()) {
					getRRSched().newTask(token);
					if (getPlayerByToken(token).getRazza().isEmpty()) {
						if (!creaPrimoDinosauro(token)) {
							return false;
						}
					}
					inserisciDinosauriNellaMappa(token);
					return true;
				}
				else throw new TroppiGiocatoriException();
			}
			else throw new RazzaNonCreataException();
		}
		else throw new InvalidTokenException();
	}
	/**
	 * Permette l'uscita dalla partita.
	 * @param token Il token dell'utente che vuole uscire dalla partita.
	 * @return True se l'uscita ha avuto successo, false altrimenti.
	 * @throws InvalidTokenException Se il token non e' valido.
	 */
	protected boolean doUscitaPartita(String token) throws InvalidTokenException { // Testato
		if (isPlayerInGame(token)) {
			getRRSched().killTask(token);
			rimuoviDinosauriDallaMappa(getPlayerByToken(token));
			return true;
		}
		return false;
	}
	/**
	 * Permette il logout dal server.
	 * @param token Il token dell'utente che vuole effettuare il logout.
	 * @return True se il logout ha avuto successo, false altrimenti.
	 * @throws InvalidTokenException Se il token non e' valido.
	 */
	protected boolean doLogout(String token) throws InvalidTokenException { // Testato
		if (isPlayerLogged(token)) {
			if (isPlayerInGame(token)) {
				if(!doUscitaPartita(token)) {
					return false;
				}
			}
			getCMan().scollega(token);
			return true;
		}
		else throw new InvalidTokenException();
	}
	/**
	 * Calcola e ritorna il costo dello spostamento tra due coordinate date.
	 * Il costo e' dato dalla somma della distanza assoluta tra X e Y delle due celle.
	 * abs(oldCoord(X)+newCoord(X)) + abs(oldCoord(Y)+newCoord(Y)) 
	 * @param oldCoord La coordinata di partenza.
	 * @param newCoord La coordinata d'arrivo.
	 * @return Un intero che rappresenta il costo di spostamento tra le due celle.
	 */
	private int getCostoSpostamento(Coord oldCoord, Coord newCoord) {
		if (oldCoord.equals(newCoord)) return 0;
		else {
			int costo = 0;
			costo += Math.abs(oldCoord.getX()-newCoord.getX());
			costo += Math.abs(oldCoord.getY()-newCoord.getY());
			return costo;
		}
	}
	/**
	 * Calcola e ritorna la coordinata con il costo minore dall'array passato.
	 * @param myArray L'array che contiene le coordinate delle celle da cui estrarre il minimo.
	 * @param endCoord La coordinata rispetto a cui calcolare il costo (la coordinata di arrivo).
	 * @return La coordinata con il costo minimo nell'array. Se ne esistono di piu' ritorna la prima che compare nell'array tra queste, senza ordine particolare.
	 */
	private Coord getMinimumFromCoordArrayList(ArrayList<Coord> myArray, Coord endCoord) {
		Iterator<Coord> itCoord = myArray.iterator();
		Coord curCoord = null;
		int curCosto = 0;
		int minCosto = 10000000;
		Coord coordToReturn = null;
		while (itCoord.hasNext()) {
			curCoord = itCoord.next();
			curCosto = getCostoSpostamento(curCoord, endCoord);
			if (curCosto < minCosto) {
				minCosto = curCosto;
				coordToReturn = curCoord;
			}
		}
		myArray.remove(coordToReturn);
		return coordToReturn;
	}
	/**
	 * Verifica se la coordinata di arrivo e' raggiungibile dalla coordinata di partenza nel numero massimo di passi permesso.
	 * @param oldCoord La coordinata di partenza.
	 * @param newCoord La coordinata di arrivo.
	 * @param maxHops Il numero massimo di passi permessi per raggiungere la cella di arrivo.
	 * @return True se la coordinata di arrivo e' raggiungibile dalla coordinata di partenza nel numero massimo di passi, false altrimenti.
	 */
	private boolean isCellaRaggiungibile(Coord oldCoord, Coord newCoord, int maxHops) { // TODO implementare la marcatura delle celle dove il dinosauro è passato
		// System.out.println("[isCellaRaggiungibile] start con maxHops " + maxHops);
		/* Se ho superato il numero massimo di passi */
		if (maxHops < 0) {
			// System.out.println("[isCellaRaggiungibile] ramo if con maxHops<0");
			return false;
		}
		/* Se sono arrivato alla cella desiderata nel numero massimo di passi */
		if ( (getCostoSpostamento(oldCoord, newCoord) == 0) &&
				maxHops >= 0) {
			// System.out.println("[isCellaRaggiungibile] ramo if con costo 0 e maxHops>=0");
			return true;
		}
		/* Se non ho ancora raggiunto il numero di passi o la cella desiderata */
		else {
			// System.out.println("[isCellaRaggiungibile] ramo else");
			/* Scansiona con due indici (righe, colonne) le celle adiacenti a quella di partenza, aggiungi le terre a un'ArrayList */
			int i = -1;
			int j = -1;
			ArrayList<Coord> myPaths = new ArrayList<Coord>();
			Coord tempCoord = null;
			while (i<2) {
				j = -1;
				while (j<2) {
					tempCoord = new Coord(oldCoord.getX()+i, oldCoord.getY()+j);
					if(!isCellaAcqua(tempCoord)) {
						myPaths.add(tempCoord);
						// System.out.println("[isCellaRaggiungibile] aggiunta cella a myPaths");
					}
					j++;
				}
				i++;
			}
			/* Se l'array non ha celle ritorno false.
			 * Attenzione! Dovrebbe essere una condizione NON verificabile, se qui l'array non ha celle o qualcosa è andato storto sopra,
			 * oppure sono su una singola isola di terra, che è comunque non verificabile come scenario. */
			if (myPaths.isEmpty()) {
				return false;
			}
			Coord curCoord = null;
			boolean hasPath = false;
			/* Fintanto che l'array non è vuoto */
			while (!myPaths.isEmpty()) {
				curCoord = getMinimumFromCoordArrayList(myPaths, newCoord);
				if (isCellaRaggiungibile(curCoord, newCoord, (maxHops-1))) {
					hasPath = true;
					// System.out.println("[isCellaRaggiungibile] hasPath = true");
				}
				if (hasPath) {
					return true;
				}
			}
			return hasPath;
		}
	}
	/**
	 * Permette ad un dinosauro di mangiare qualcosa su una cella.
	 * @param tempDinosauro Il dinosauro che deve mangiare qualcosa.
	 * @param newCoord La coordinata che il dinosauro deve mangiare.
	 */
	private void mangiaCella(Dinosauro tempDinosauro) {
		Cella tempCella = getMappa().getCella(tempDinosauro.getCoord()).getCellaSuCuiSiTrova();
		int valoreAttualeCella = tempCella.getValoreAttuale();
		int valoreAttualeDinosauro = tempDinosauro.getEnergiaAttuale();
		if ((valoreAttualeCella+valoreAttualeDinosauro) < tempDinosauro.getEnergiaMax()) {
			tempDinosauro.setEnergiaAttuale(valoreAttualeCella+valoreAttualeDinosauro);
			tempCella.mangia(valoreAttualeCella);
		}
		else {
			tempDinosauro.setEnergiaAttuale(tempDinosauro.getEnergiaMax());
			tempCella.mangia(tempDinosauro.getEnergiaMax()-valoreAttualeDinosauro);
		}
	}
	/**
	 * Verifica se entrambi i dinosauri (quello corrente e quello sulla cella scelta) sono entrambi erbivori.
	 * @param tmpDinosauro Il dinosauro che deve essere mosso.
	 * @param newCoord La coordinata da controllare.
	 * @return True se entrambi i dinosauri sono erbivori, altrimenti false.
	 */
	private boolean isEntrambiDinosauriErbivori(Dinosauro tmpDinosauro, Coord newCoord) {
		if (tmpDinosauro.getTipoRazza().toLowerCase().equals("erbivoro")) {
			if (isCellaDinosauro(newCoord)) {
				if (getPlayerByIdDinosauro(getMappa().getCella(newCoord).getIdDelDinosauro()).getRazza().getTipo().equals('e')) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Svolge il combattimento tra due dinosauri.
	 * Restituisce un booleano, true se vince l'attaccante, false se vince l'attaccato.
	 * @param dinosauroSfidante Il dinosauro che attacca.
	 * @param dinosauroAttaccato Il dinosauro che difende.
	 * @return True se vince l'attaccante, false se vince il difendente.
	 */
	private boolean combattimentoTraDinosauri(Dinosauro dinosauroSfidante, Dinosauro dinosauroAttaccato) {
		if (dinosauroSfidante.getForza() >= dinosauroAttaccato.getForza()) {
			dinosauroAttaccato.nonUsabile();
			return true;
		}
		else if (dinosauroSfidante.getForza() < dinosauroAttaccato.getForza()) {
			dinosauroSfidante.nonUsabile();
			return false;
		}
		return false;
	}
	/**
	 * Dice se la cella è una cella acqua.
	 * @param myCoord La coordinata da controllare.
	 * @return True se la cella è acqua, false altrimenti
	 */
	private boolean isCellaAcqua(Coord myCoord) {
		if (getMappa().getCella(myCoord).toString().toLowerCase().equals("acqua")) { return true; }
		else { return false; }
	}
	/**
	 * Dice se la cella è una cella con dinosauro.
	 * @param myCoord La coordinata da controllare.
	 * @return True se la cella è un dinosauro, false altrimenti.
	 */
	private boolean isCellaDinosauro(Coord myCoord) {
		if (getMappa().getCella(myCoord).toString().toLowerCase().equals("dinosauro")) { return true; }
		else { return false; }
	}
	/**
	 * Verifica se la cella e' contiene un dinosauro dell'utente che ha invocato il movimento.
	 * @param token Il token dell'utente da controllare.
	 * @param newCoord La coordinata da controllare.
	 * @return True se la cella contiene un dinosauro dell'utente, false altrimenti.
	 * @throws InvalidTokenException
	 */
	private boolean isCellaConMioDinosauro(String token, Coord newCoord) throws InvalidTokenException {
		if (isCellaDinosauro(newCoord)) {
			String tmpId = getMappa().getCella(newCoord).getIdDelDinosauro();
			if (getPlayerByToken(token).getRazza().existsDinosauroWithId(tmpId)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Permette il movimento del dinosauro da una cella ad un'altra.
	 * Gestisce le varie condizioni di errore e chiama le funzioni appropriate in caso di combattimento.
	 * @param token Il token dell'utente che invoca il movimento.
	 * @param idDinosauro L'id del dinosauro da muovere.
	 * @param newCoord La nuova coordinata in cui muovere il dinosauro.
	 * @return Una stringa che può essere: "destinazioneNonValida" se la destinazione non è valida, "p" se c'è stato un combattimento perso, "v" se il combattimento è stato vinto, "ok" se il movimento ha avuto successo.
	 * @throws InvalidTokenException Se il token non e' valido.
	 */
	protected String doMuoviDinosauro(String token, String idDinosauro, Coord newCoord) throws InvalidTokenException, GenericDinosauroException { // Testato // TODO fixare un apparente loop infinito.
		Dinosauro tempDinosauro = getPlayerByToken(token).getRazza().getDinosauroById(idDinosauro);
		Coord oldCoord = tempDinosauro.getCoord();
		if (!isValidCoord(newCoord) ||
				newCoord.equals(tempDinosauro.getCoord()) ||
				isCellaAcqua(newCoord) ||
				isEntrambiDinosauriErbivori(tempDinosauro, newCoord) ||
				isCellaConMioDinosauro(token, newCoord) ||
				!isCellaRaggiungibile(tempDinosauro.getCoord(), newCoord, tempDinosauro.getSpostamentoMax())) { return "destinazioneNonValida"; }
		Dinosauro oldDinosauro = null;
		String idOldDinosauro = null;
		System.runFinalization();
		System.gc();
		String tipoCella = getMappa().getCella(newCoord).toString().toLowerCase();
		getMappa().rimuoviIlDinosauroDallaCella(oldCoord);
		if (isCellaDinosauro(newCoord)) {
			idOldDinosauro = getMappa().getCella(newCoord).getIdDelDinosauro();
			oldDinosauro = getPlayerByIdDinosauro(idOldDinosauro).getRazza().getDinosauroById(idOldDinosauro);
			getMappa().rimuoviIlDinosauroDallaCella(newCoord);
		}
		try {
			getPlayerByToken(token).getRazza().muoviDinosauro(idDinosauro, newCoord);
		}
		catch (GenericDinosauroException e) {
			if (e.getMessage().equals("raggiuntoLimiteMosseDinosauro")) {
				getMappa().spawnDinosauro(oldCoord, tempDinosauro.getIdDinosauro());
			}
			if (oldDinosauro != null) {
				getMappa().spawnDinosauro(newCoord, oldDinosauro.getIdDinosauro());
			}
			throw new GenericDinosauroException(e.getMessage());
		}
		if (isCellaDinosauro(newCoord)) {
			if (combattimentoTraDinosauri(tempDinosauro, oldDinosauro)) {
				getMappa().spawnDinosauro(newCoord, tempDinosauro.getIdDinosauro());
				getPlayerByIdDinosauro(oldDinosauro.getIdDinosauro()).getRazza().removeById(oldDinosauro.getIdDinosauro());
				return "v";
			}
			else {
				getMappa().spawnDinosauro(newCoord, oldDinosauro.getIdDinosauro());
				getPlayerByIdDinosauro(tempDinosauro.getIdDinosauro()).getRazza().removeById(tempDinosauro.getIdDinosauro());
				return "p";
			}
		}
		getMappa().spawnDinosauro(newCoord, tempDinosauro.getIdDinosauro());
		if (tipoCella.equals("vegetazione")) {
			if (tempDinosauro.getTipoRazza().toLowerCase().equals("erbivoro")) {
				mangiaCella(tempDinosauro);
			}
		}
		else if (tipoCella.equals("carogna")) {
			if (tempDinosauro.getTipoRazza().toLowerCase().equals("carnivoro")) {
				mangiaCella(tempDinosauro);
			}
		}
		return "ok";
	}
}