package dinolib;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

import dinolib.Exceptions.*;
import dinolib.Razza.Carnivoro;
import dinolib.Razza.Dinosauro;
import dinolib.Razza.Erbivoro;
import dinolib.Mappa.Cella;
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

	/* Tutte le variabili istanziabili */
	/**
	 * Definisce il riferimento alla mappa.
	 * @uml.property  name="rifMappa"
	 * @uml.associationEnd  
	 */
	private Mappa rifMappa;
	/**
	 * Istanzia il riferimento alla lista dei giocatori.
	 * @uml.property name="playerManager"
	 */
	private PlayerManager pMan = null;
	/**
	 * Istanzia il riferimento alla lista dei giocatori connessi e dei loro token.
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
	private String giocatoreCorrente = null;
	/**
	 * Contiene una variabile che dice se la logica sta funzionando.
	 * @uml.property  name="logicaIsRunning"
	 */
	private boolean logicaIsRunning = true;
	/**
	 * Variabile che dice se il turno del giocatore è stato confermato.
	 * @uml.property  name="turnoConfermato"
	 */
	private boolean turnoConfermato = false;


	/* Costruttore e funzioni di primo avvio */
	/**
	 * Costruttore di default per la classe Logica.
	 */
	public Logica () {
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
	}
	/**
	 * Se i file di salvataggio esistono li carica, altrimenti assume primo avvio
	 * @throws ClassNotFoundException 
	 * @throws IOException
	 * @throws FileNotFoundException 
	 */
	private void caricaPartitaDaFile() throws IOException, ClassNotFoundException, FileNotFoundException {
		/**
		 * Carica file di mappa, se esiste deve esistere anche il file dei giocatori.
		 * In caso il primo o l'altro non esistano l'eccezione viene gestita e passata al chiamante, che quindi assume un primo avvios
		 */
		caricaFileMappa("mappa.dat");
		caricaFileGiocatori("giocatori.dat");
	}
	/**
	 * Implementa il caricamento del file di mappa, se esiste
	 * Se non esiste lancia FileNotFoundException
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	private void caricaFileMappa(String nomefile) throws IOException, ClassNotFoundException, FileNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(nomefile)));
		rifMappa = (Mappa) ois.readObject();
	}
	/**
	 * Implementa il caricamento del file giocatori se esiste
	 * Se non esiste lancia FileNotFoundException
	 * @throws IOException 
	 * @throws ClassNotFoundException
	 * @throws FileNotFoundException  
	 */
	private void caricaFileGiocatori(String nomefile) throws IOException, ClassNotFoundException, FileNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(nomefile)));
		pMan = (PlayerManager) ois.readObject();
	}
	/**
	 * Helper per la creazione di una mappa nuova
	 */
	private void creaNuovaMappa() {
		rifMappa = new Mappa(lato_MAPPA);
	}

	/* Tutti i getter */
	protected Mappa getMappa() { return rifMappa; }
	protected PlayerManager getPMan() { return pMan; }
	protected ConnectionManager getCMan() { return cMan; }
	protected RRScheduler getRRSched() { return rrsched; }
	protected boolean isLogicaRunning() { return logicaIsRunning; }
	protected Giocatore getPlayerByToken(String token) throws InvalidTokenException {
		if (getCMan().existsToken(token) && getPMan().exists(getCMan().getName(token))) return getPMan().getPlayer(getCMan().getName(token));
		else return null;
	}

	/**
	 * Helper per verificare che sia il turno del giocatore che chiama la funzione. 
	 * @return
	 * @throws InvalidTokenException 
	 * @throws NonInPartitaException 
	 * @throws NonAutenticatoException 
	 */
	protected boolean isMioTurno(String token) throws InvalidTokenException, NonInPartitaException, NonAutenticatoException {
		if (isPlayerInGame(token)) {
			if (giocatoreCorrente.equals(token)) return true;
			else return false;
		}
		else throw new NonInPartitaException();
	}
	/**
	 * Verifica se l'utente è in partita, altrimenti lancia eccezione.
	 * @throws InvalidTokenException 
	 * @throws NonInPartitaException 
	 * @throws NonAutenticatoException 
	 */
	protected boolean isPlayerInGame(String token) throws InvalidTokenException, NonAutenticatoException {
		if (getRRSched().hasTask(token)) return true;
		else return false;
	}
	/**
	 * Verifica se l'utente è autenticato, altrimenti lancia eccezione.
	 * @param token
	 * @return
	 * @throws NonAutenticatoException
	 * @throws InvalidTokenException
	 */
	protected boolean isPlayerLogged(String token) throws NonAutenticatoException, InvalidTokenException {
		if (getCMan().existsToken(token)) return true;
		else throw new NonAutenticatoException();
	}

	/**
	 * Helper per verificare che esista una razza con il nome specificato.
	 * Serve per controllare che il nome richiesto non sia già in uso.
	 * @param nomeRazza
	 * @return
	 * @throws NomeRazzaOccupatoException 
	 */
	protected boolean existsRaceWithName (String nomeRazza) throws NomeRazzaOccupatoException {
		Iterator<Giocatore> itGiocatori = getPMan().getIteratorOnPlayers();
		while (itGiocatori.hasNext()) {
			Giocatore tempGiocatore = itGiocatori.next();
			if (tempGiocatore.getRazza().getNome().equals(nomeRazza)) throw new NomeRazzaOccupatoException();
		}
		return false;
	}

	protected void broadcastCambioTurno() {

	}

	/**
	 * Aggiorna la mappa ogni volta che si passa da un giocatore all'altro.
	 */
	private void updateMappa() {
		getMappa().aggiornaSuTurno();
	}
	/**
	 * Aggiorna l'ambiente di gioco ogni volta che si passa da un giocatore all'altro.
	 */
	private void updatePartita() {
		updateMappa();
		getPMan().updateGiocatori();
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
		try {
			while (isLogicaRunning()) {
				if (getRRSched().hasQueuedTasks()) {
					giocatoreCorrente = getRRSched().getCurrentTask();
					long conferma_start = System.currentTimeMillis();
					while ((System.currentTimeMillis()-conferma_start) < (sleep_CONFERMA_TURNO*1000)) {
						if (turnoConfermato) {
							long turno_start = System.currentTimeMillis();
							while ((System.currentTimeMillis()-turno_start) < (sleep_TEMPO_TURNO*1000)) {
								if (!turnoConfermato) break;
								else {
									Thread.sleep(1000);
								}
							}
							getRRSched().newTask(giocatoreCorrente);
							updatePartita();
							broadcastCambioTurno();
						}
						else {
							Thread.sleep(1000);
						}
					}
				}
				else {
					Thread.sleep(1000);
				}
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/* Helper esterni */
	void confermaTurno() {
		turnoConfermato = true;
	}
	/**
	 * Gestisce la rimozione di tutti i dinosauri dalla mappa.	
	 * @throws InvalidTokenException 
	 */
	private void rimuoviDinosauriDallaMappa(Giocatore tempGiocatore) {
		Iterator<Dinosauro> itDinosauri = tempGiocatore.getRazza().iterator();
		Dinosauro tempDinosauro;
		while (itDinosauri.hasNext()) {
			tempDinosauro = itDinosauri.next();
			getMappa().rimuoviIlDinosauroDallaCella(tempDinosauro.getX(), tempDinosauro.getY());
		}
	}
	/**
	 * Quando l'utente esegue il login aggiunge i dinosauri alla mappa.
	 * @throws InvalidTokenException 
	 */
	protected void inserisciDinosauriNellaMappa(String token) throws InvalidTokenException {
		Iterator<Dinosauro> itDinosauri = getPlayerByToken(token).getRazza().iterator();
		Dinosauro tempDinosauro;
		while (itDinosauri.hasNext()) {
			tempDinosauro = itDinosauri.next();
			int x = tempDinosauro.getX(), y = tempDinosauro.getY();
			if (tryActualSpawn(x, y, tempDinosauro.getIdDinosauro())) return;
			else {
				int i = 1;
				do {
					if (tryNearestSpawn(x, y, i, tempDinosauro)) {
						return;
					}
					else i++;
				} while (i < getMappa().getLatoDellaMappa());
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
		if (getMappa().isLibera(x, y)) {
			getMappa().spawnDinosauro(x, y, idDinosauro, getMappa().getCella(x, y));
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
	private boolean tryNearestSpawn(int x, int y, int maxDistance, Dinosauro tempDinosauro) {
		int i = -maxDistance;
		int j = -maxDistance;
		do {
			i = -maxDistance;
			do {

				if (tryActualSpawn(i+x, j+y, tempDinosauro.getIdDinosauro())) {
					tempDinosauro.setXY(i+x, j+CommonUtils.translateYforServer(y, getMappa().getLatoDellaMappa()));
					return true;
				}
				i++;
			} while ((i<(maxDistance+1)) &&
					(0<=(i+x)) &&
					((i+x)<getMappa().getLatoDellaMappa()));
			j++;
		} while ((j<(maxDistance+1)) &&
				(0<=(j+y)) &&
				((j+y)<getMappa().getLatoDellaMappa()));
		return false;
	}
	/**
	 * Codice per l'uscita dalla partita. Viene chiamato direttamente o tramite l'adattatore.
	 * @throws InvalidTokenException 
	 */
	protected void doEsciDallaPartita(String token) throws InvalidTokenException {
		Giocatore tempGiocatore = getPlayerByToken(token);
		rimuoviDinosauriDallaMappa(tempGiocatore);
		tempGiocatore.notInGame();
		if (playersQueue.contains(getCMan().getPlayerName(token))) {
			playersQueue.remove(getPlayerName(token));
			return;
		}
		else return;
	}
	/**
	 * Fa il logout dell'utente col token richiesto.
	 * @param token
	 * @throws InvalidTokenException
	 * @throws NonInPartitaException
	 * @throws NonAutenticatoException 
	 */
	protected void doLogout(String token) throws InvalidTokenException, NonInPartitaException, NonAutenticatoException {
		Giocatore tempGiocatore = getPlayerByToken(token);
		if (tempGiocatore.isLogged()) {
			if (isPlayerInGame(token)) {
				doEsciDallaPartita(token);
			}
			tempGiocatore.notLogged();
		}
	}
	private void passaTurno() {
		// TODO Auto-generated method stub
	}
	/**
	 * Prova a inserire un dinosauro appena nato. Questo metodo viene usato da deponiUovo.
	 * @return
	 * @throws InvalidTokenException 
	 */
	protected boolean trySpawnOfAnEgg(String token, int x, int y, Dinosauro newDinosauro) throws InvalidTokenException {
		int i = 1;
		do {
			if (tryNearestSpawn(x, y, i, newDinosauro)) {
				getPlayerByToken(token).getRazza().add(newDinosauro);
				return true;
			}
			else i++;
		} while (i<getMappa().getLatoDellaMappa());
		return false;
	}
	/**
	 * Ritorna la x più a sinistra possibile rispetto a quella specificata.
	 * @param x
	 * @param rangeVista
	 * @return
	 */
	protected int doSubtraction(int coord, int rangeVista) {
		int subtraction = (coord - rangeVista);
		if (subtraction<0) return 0;
		else if (subtraction>=getMappa().getLatoDellaMappa()) return (getMappa().getLatoDellaMappa()-1);
		else return subtraction;
	}
	/**
	 * Fa l'addizione per le coordinate, che rimangano in range.
	 * @param coord
	 * @param rangeVista
	 * @return
	 */
	protected int doAddition(int coord, int rangeVista) {
		int addition = (coord + rangeVista);
		if (addition<0) return 0;
		else if (addition>=getMappa().getLatoDellaMappa()) return (getMappa().getLatoDellaMappa()-1);
		else return addition;
	}
	/**
	 * Implementa l'accesso alla partita, la parte non adattabile e condivisa da ogni modo di accesso.
	 * @throws InvalidTokenException 
	 * @throws RazzaNonCreataException 
	 * @throws TroppiGiocatoriException 
	 * @throws NonInPartitaException 
	 * @throws InterruptedException 
	 * @throws NonAutenticatoException 
	 */
	protected void accediAPartita(String token) throws InvalidTokenException, NonInPartitaException, TroppiGiocatoriException, RazzaNonCreataException, InterruptedException, NonAutenticatoException {
		Giocatore tempGiocatore = getPlayerByToken(token);
		if (!isPlayerInGame(token) &&
				!isMaxPlayersInGame()) {
			if (tempGiocatore.hasRazza()) {
				inserisciDinosauriNellaMappa(token);
				playersQueue.put(getPlayerName(token));
			}
			else throw new RazzaNonCreataException();
		}
		else return;
	}

	/**
	 * Fa deporre l'uovo al dinosauro.
	 * È un helper comune, socketAdaptor non deve vedere niente della logica interna.
	 * @param token
	 * @param idDinosauro
	 * @return
	 * @throws GenericDinosauroException
	 * @throws InvalidTokenException
	 */
	protected String deponiUovo(String token, String idDinosauro) throws GenericDinosauroException, InvalidTokenException {
		getPlayerByToken(token).getRazza().deponiUovo(idDinosauro);
		Dinosauro tempDinosauro = getPlayerByToken(token).getRazza().getDinosauroById(idDinosauro);
		int x = tempDinosauro.getX();
		int y = tempDinosauro.getY();
		if (tempDinosauro.getTipoRazza().equals("Carnivoro")) {
			Dinosauro newDinosauro = new Carnivoro(x, y);
			newDinosauro.nonUsabile();
			trySpawnOfAnEgg(token, x, y, newDinosauro);
			return newDinosauro.getIdDinosauro();
		}
		else if (tempDinosauro.getTipoRazza().equals("Erbivoro")) {
			Dinosauro newDinosauro = new Erbivoro(x, y);
			newDinosauro.nonUsabile();
			trySpawnOfAnEgg(token, x, y, newDinosauro);
			return newDinosauro.getIdDinosauro();
		}
		return null;
	}
	/**
	 * Helper per fare il login dell'utente, di modo da separare la logica di programma da socketAdaptor.
	 * @param tempGiocatore
	 * @return
	 */
	protected String doLoginUtente(Giocatore tempGiocatore) {
		getListaGiocatori().remove(tempGiocatore);
		tempGiocatore.logged();
		getListaGiocatori().put(CommonUtils.getNewToken(), tempGiocatore);
		return getPlayerToken(tempGiocatore.getNome());
	}

	/**
	 * Implementa la creazione dell'utente, se esiste già lancia eccezione.
	 * Viene chiamato dagli adattatori.
	 * @param user
	 * @param pwd
	 * @return
	 * @throws UserExistsException 
	 */
	protected boolean doCreaUtente(String nomeGiocatore, String pwd) throws UserExistsException {
		if (!getPMan().exists(nomeGiocatore)) {
			return getPMan().aggiungi(new Giocatore(nomeGiocatore, pwd));
		}
		else throw new UserExistsException();
	}

	/**
	 * Implementa il login dell'utente, se esiste permette il login.
	 * Se la password non è valida lancia u'eccezione.
	 * @param nomeGiocatore
	 * @param suppliedPassword
	 * @return
	 * @throws UserAuthenticationFailedException
	 */
	protected boolean doLogin(String nomeGiocatore, String suppliedPassword) throws UserAuthenticationFailedException {
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
	 * Se esiste già ritorna false, altrimenti la crea e ritorna true.
	 * @param token
	 * @param nomeRazza
	 * @param tipoRazza
	 * @return
	 * @throws NomeRazzaOccupatoException
	 * @throws InvalidTokenException
	 */
	protected boolean doCreaRazza(String token, String nomeRazza, String tipoRazza) throws NomeRazzaOccupatoException, InvalidTokenException {
		Iterator<Giocatore> itGiocatori = getPMan().getIteratorOnPlayers();
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

	/**
	 * Permette l'accesso alla partita.
	 * Ritorna true se ha successo, false altrimenti.
	 * @param token
	 * @return
	 * @throws NonAutenticatoException
	 * @throws InvalidTokenException
	 * @throws InterruptedException
	 * @throws TroppiGiocatoriException
	 * @throws RazzaNonCreataException
	 */
	protected boolean doAccessoPartita(String token) throws NonAutenticatoException, InvalidTokenException, InterruptedException, TroppiGiocatoriException, RazzaNonCreataException {
		if (isPlayerLogged(token)) {
			if (getPlayerByToken(token).hasRazza()) {
				if (!getRRSched().maxPlayers()) {
					getRRSched().newTask(token);
					return true;
				}
				else throw new TroppiGiocatoriException();
			}
			else throw new RazzaNonCreataException();
		}
		return false;
	}
	/**
	 * Permette l'uscita dalla partita.
	 * Ritorna true se l'utente può uscire, altrimenti false.
	 * @param token
	 * @return
	 * @throws InvalidTokenException
	 * @throws NonAutenticatoException
	 */
	protected boolean doUscitaPartita(String token) throws InvalidTokenException, NonAutenticatoException {
		if (isPlayerInGame(token)) {
			getRRSched().killTask(token);
			return true;
		}
		else return false;
	}
}