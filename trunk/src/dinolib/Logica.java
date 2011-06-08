package dinolib;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

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
	 * Contiene la lista dei giocatori.
	 * Uso l'hashmap perchè posso avere chiavi nulle, in questo modo posso rimuovere i token e tenere la lista dei giocatori, ma tutto in un'unica struttura.
	 * @uml.property name="listaGiocatori"
	 */
	private HashMap<String, Giocatore> listaGiocatori = null;
	/**
	 * Definisce la stringa che contiene il nome del giocatore che in questo momento ha il turno.
	 * @uml.property  name="Giocatori"
	 */
	private String nomeGiocatoreCorrente = null;
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
	/**
	 * Contiene tutti i giocatori correntemente connessi alla partita.
	 * @uml.property  name="playersQueue"
	 */
	private ArrayBlockingQueue<String> playersQueue = new ArrayBlockingQueue<String>(numero_MASSIMO_GIOCATORI_INGAME);

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
			listaGiocatori = new HashMap<String, Giocatore>();
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
		listaGiocatori = (HashMap<String, Giocatore>) ois.readObject();
	}
	/**
	 * Helper per la creazione di una mappa nuova
	 */
	private void creaNuovaMappa() {
		rifMappa = new Mappa(lato_MAPPA);
	}

	/* Tutti i getter */
	private HashMap<String, Giocatore> getListaGiocatori() { return listaGiocatori; }
	protected String getPlayerName(String token) { return getListaGiocatori().get(token).getNome(); }
	public int getLatoDellaMappa() { return rifMappa.getLatoDellaMappa(); }
	protected Cella getCella(int x, int y) { return rifMappa.getCella(x, y); }
	public boolean isSomeonePlaying() { if (playersQueue.size() > 0) return true; else return false; }
	protected boolean isLogicaRunning() { return logicaIsRunning; }
	public Iterator<String> getIteratorOnPIds() { return getListaGiocatori().keySet().iterator(); }
	Iterator<Giocatore> getIteratorOnPlayers() { return getListaGiocatori().values().iterator(); }
	protected Giocatore getPlayerByToken(String token) throws InvalidTokenException {
		if (existsPlayerWithToken(token)) return getListaGiocatori().get(token);
		else return null;
	}
	protected boolean existsPlayerWithName(String user) {
		Iterator<Giocatore> itGiocatori = getIteratorOnPlayers();
		while (itGiocatori.hasNext()) {
			if (itGiocatori.next().getNome().equals(user)) return true;
		}
		return false;
	}
	protected Giocatore getPlayerByName(String user) {
		Iterator<Giocatore> itGiocatori = getIteratorOnPlayers();
		Giocatore tempGiocatore = null;
		while (itGiocatori.hasNext()) {
			tempGiocatore = itGiocatori.next();
			if (tempGiocatore.getNome().equals(user)) break;
		}
		return tempGiocatore;
	}
	/**
	 * Helper per verificare che effettivamente il token sia registrato.
	 * @param token
	 * @return
	 * @throws InvalidTokenException
	 */
	protected boolean existsPlayerWithToken (String token) throws InvalidTokenException {
		if (getListaGiocatori().containsKey(token)) return true;
		else throw new InvalidTokenException();
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
			if (nomeGiocatoreCorrente.equals(getPlayerByToken(token).getNome())) return true;
			else return false;
		}
		else throw new NonInPartitaException();
	}
	/**
	 * Crea una nuova razza per l'utente specificato tramite token
	 * @param token
	 * @param raceName
	 * @param dinosauro
	 * @throws InvalidTokenException
	 */
	protected void createNewRaceForPlayer(String token, String raceName, Dinosauro dinosauro) throws InvalidTokenException {
		getPlayerByToken(token).creaNuovaRazza(raceName, dinosauro);
	}
	/**
	 * Verifica se il numero massimo di utenti è connesso. Se sì lancia una eccezione, altrimenti ritorna false.
	 * ATTENZIONE! Il valore di ritorno di di default è FALSE! (Contrariamente a tutti gli altri helper).
	 * @return
	 * @throws TroppiGiocatoriException
	 */
	public boolean isMaxPlayersInGame() throws TroppiGiocatoriException {
		if (playersQueue.size() < numero_MASSIMO_GIOCATORI_INGAME) return false;
		else throw new TroppiGiocatoriException();
	}
	/**
	 * Helper per verificare l'esistenza della razza per il giocatore con il dato token.
	 * @param token
	 * @return
	 * @throws InvalidTokenException
	 * @throws RazzaGiaCreataException 
	 */
	boolean existsRaceForPlayer (String token) throws InvalidTokenException, RazzaGiaCreataException {
		if (getPlayerByToken(token).hasRazza()) throw new RazzaGiaCreataException();
		else return false;
	}
	/**
	 * Verifica se l'utente possiede il dinosauro con l'id cercato, altrimenti lancia eccezione.
	 * @return 
	 * @throws InvalidIDException 
	 * @throws InvalidTokenException 
	 */
	protected boolean playerHasDinosauro(String token, String idDinosauro) throws InvalidTokenException, InvalidIDException {
		if (getPlayerByToken(token).getRazza().existsDinosauroWithId(idDinosauro)) return true;
		else throw new InvalidIDException();
	}
	/**
	 * Verifica se l'utente è in partita, altrimenti lancia eccezione.
	 * @throws InvalidTokenException 
	 * @throws NonInPartitaException 
	 * @throws NonAutenticatoException 
	 */
	protected boolean isPlayerInGame(String token) throws InvalidTokenException, NonAutenticatoException {
		if (isPlayerLogged(token) && 
				getPlayerByToken(token).isInGame()) return true;
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
		if (getPlayerByToken(token).isLogged()) return true;
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
		Iterator<Giocatore> itGiocatori = getIteratorOnPlayers();
		while (itGiocatori.hasNext()) {
			Giocatore tempGiocatore = itGiocatori.next();
			if (tempGiocatore.getRazza().getNome().equals(nomeRazza)) throw new NomeRazzaOccupatoException();
		}
		return false;
	}
	
	/**
	 * Ritorna il token dell'utente richiesto.
	 * @param user
	 * @return
	 */
	protected String getPlayerToken(String user) {
		Iterator<String> itToken = getIteratorOnPIds();
		String tempToken = null;
		while (itToken.hasNext()) {
			tempToken = itToken.next();
			if (getListaGiocatori().get(tempToken).getNome().equals(user)) {
				return tempToken;
			}
		}
		return null;
	}

	protected void broadcastCambioTurno() {

	}

	/**
	 * Aggiorna la mappa ogni volta che si passa da un giocatore all'altro.
	 */
	private void updateMappa() {
		rifMappa.aggiornaSuTurno();
	}
	private void updateGiocatori() {
		Iterator<Giocatore> itGiocatori = getIteratorOnPlayers();
		while (itGiocatori.hasNext()) {
			itGiocatori.next().aggiornaGiocatoreSuTurno();
		}
	}
	/**
	 * Aggiorna l'ambiente di gioco ogni volta che si passa da un giocatore all'altro.
	 */
	private void updatePartita() {
		updateMappa();
		updateGiocatori();
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
				if (isSomeonePlaying()) {
					nomeGiocatoreCorrente = playersQueue.poll();
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
							playersQueue.put(nomeGiocatoreCorrente);
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
	 * Crea un utente a partire da username e password.
	 * @param user
	 * @param pwd
	 * @throws UserExistsException 
	 */
	protected void doCreaUtente(String user, String pwd) throws UserExistsException {
		Iterator<Giocatore> itGiocatori = getIteratorOnPlayers();
		while (itGiocatori.hasNext()) {
			if (itGiocatori.next().getNome().equals(user)) throw new UserExistsException();
		}
		getListaGiocatori().put(CommonUtils.getNewToken(), new Giocatore(user, pwd));
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
			rifMappa.rimuoviIlDinosauroDallaCella(tempDinosauro.getX(), tempDinosauro.getY());
		}
	}
	/**
	 * Quando l'utente esegue il login aggiunge i dinosauri alla mappa.
	 * @throws InvalidTokenException 
	 */
	protected void inserisciDinosauriNellaMappa(String token) throws InvalidTokenException {
		Iterator<Dinosauro> itDinosauri = getListaGiocatori().get(token).getRazza().iterator();
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
	private boolean tryNearestSpawn(int x, int y, int maxDistance, Dinosauro tempDinosauro) {
		int i = -maxDistance;
		int j = -maxDistance;
		do {
			i = -maxDistance;
			do {

				if (tryActualSpawn(i+x, j+y, tempDinosauro.getIdDinosauro())) {
					tempDinosauro.setXY(i+x, j+CommonUtils.translateYforServer(y, rifMappa.getLatoDellaMappa()));
					return true;
				}
				i++;
			} while ((i<(maxDistance+1)) &&
					(0<=(i+x)) &&
					((i+x)<rifMappa.getLatoDellaMappa()));
			j++;
		} while ((j<(maxDistance+1)) &&
				(0<=(j+y)) &&
				((j+y)<rifMappa.getLatoDellaMappa()));
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
		if (playersQueue.contains(getPlayerName(token))) {
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
		} while (i<rifMappa.getLatoDellaMappa());
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
		else if (subtraction>=rifMappa.getLatoDellaMappa()) return (rifMappa.getLatoDellaMappa()-1);
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
		else if (addition>=rifMappa.getLatoDellaMappa()) return (rifMappa.getLatoDellaMappa()-1);
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
}