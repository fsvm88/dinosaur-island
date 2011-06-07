package dinolib;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

public class Logica implements Runnable {
	/**
	 * Definisce definitivamente il numero massimo di giocatori ammessi in partita.
	 * @uml.property name="NUMERO_MASSIMO_GIOCATORI_INGAME"
	 */
	private final int numero_MASSIMO_GIOCATORI_INGAME = 8;
	/**
	 * Definisce definitivamente il lato della mappa.
	 * @uml.property name="LATO_MAPPA"
	 */
	private final int lato_MAPPA = 40;
	/**
	 * Definisce definitivamente il tempo da attendere senza la conferma di turno.
	 * @uml.property name="SLEEP_CONFERMA_TURNO"
	 */
	private final int sleep_CONFERMA_TURNO = 30;
	/**
	 * Definisce definitivamente il tempo da dedicare ad ogni giocatore quando questo conferma il turno.
	 * @uml.property name="SLEEP_TEMPO_TURNO"
	 */
	private final int sleep_TEMPO_TURNO = 120;
	/**
	 * Definisce il riferimento alla mappa.
	 * @uml.property name="rifMappa"
	 */
	private Mappa rifMappa;
	/**
	 * Definisce la lista dei giocatori.
	 * @uml.property name="Giocatori"
	 */
	private Hashtable<String, Giocatore> listaGiocatori;
	/**
	 * Accoppia il token al giocatore.
	 * Chiavi: token
	 * Value: nomi giocatori.
	 * @uml.property name="NomeEToken"
	 */
	private Hashtable<String, String> connectionTable;
	/**
	 * Definisce la stringa che contiene il nome del giocatore che in questo momento ha il turno.
	 * @uml.property name="Giocatori"
	 */
	private String nomeGiocatoreCorrente = null;

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
			listaGiocatori = new Hashtable<String, Giocatore>();
			connectionTable = new Hashtable<String, String>();
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
	 * Contiene una variabile che dice se la logica sta funzionando.
	 * @uml.property name="logicaIsRunning"
	 */
	private boolean logicaIsRunning = true;

	/**
	 * Helper per sapere se la logica continua a funzionare.
	 * @return
	 */
	protected boolean isLogicaRunning() {
		return logicaIsRunning;
	}

	/**
	 * Variabile che dice se il turno del giocatore è stato confermato.
	 * @uml.property name="turnoConfermato"
	 */
	private boolean turnoConfermato = false;

	/**
	 * Contiene tutti i giocatori correntemente connessi alla partita.
	 * @uml.property name="playersQueue"
	 */
	private ArrayBlockingQueue<String> playersQueue = new ArrayBlockingQueue<String>(numero_MASSIMO_GIOCATORI_INGAME);

	protected void broadcastCambioTurno() {

	}

	/**
	 * Aggiorna la mappa ogni volta che si passa da un giocatore all'altro.
	 */
	private void updateMappa() {
		rifMappa.aggiornaSuTurno();
	}

	private void updateGiocatori() {
		Iterator<Giocatore> itGioc = getIteratorOnPlayers();
		while (itGioc.hasNext()) {
			itGioc.next().aggiornaGiocatoreSuTurno();
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
		caricaFileMappa("mappa.dat");
		caricaFileGiocatori("giocatori.dat");
	}
	/**
	 * Implementa il caricamento del file di mappa, se esiste
	 * Se non esiste lancia FileNotFoundException
	 * @throws IOException 
	 * @throws ClassNotFoundException 
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
	 */
	@SuppressWarnings("unchecked")
	private void caricaFileGiocatori(String nomefile) throws IOException, ClassNotFoundException, FileNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(nomefile)));
		listaGiocatori = (Hashtable<String, Giocatore>) ois.readObject();
	}

	/**
	 * Helper per la creazione di una mappa nuova
	 */
	private void creaNuovaMappa() {
		rifMappa = new Mappa(lato_MAPPA);
	}

	/**
	 * Crea un utente a partire da username e password.
	 * @param user
	 * @param pwd
	 * @throws UserExistsException 
	 */
	protected void doCreaUtente(String user, String pwd) throws UserExistsException {
		if (!existsUserWithName(user)) {
			listaGiocatori.put(user, new Giocatore(user, pwd));
		}
		else throw new UserExistsException();
	}

	/**
	 * Rimuove un dinosauro dalla cella.
	 */
	private void rimuoviDinosauroDallaCella(int x, int y) {
		rifMappa.rimuoviIlDinosauroDallaCella(x, y);
	}

	/**
	 * Gestisce la rimozione di tutti i dinosauri dalla mappa.	
	 * @throws InvalidTokenException 
	 */
	private void rimuoviDinosauriDallaMappa(Giocatore tempGiocatore) {
		Iterator<Dinosauro> itDinosauri = getItDinosauri(tempGiocatore);
		Dinosauro tempDinosauro;
		while (itDinosauri.hasNext()) {
			tempDinosauro = itDinosauri.next();
			rimuoviDinosauroDallaCella(tempDinosauro.getX(), tempDinosauro.getY());
		}
	}

	/**
	 * Quando l'utente esegue il login aggiunge i dinosauri alla mappa.
	 * @throws InvalidTokenException 
	 */
	protected void inserisciDinosauriNellaMappa(String token) throws InvalidTokenException {
		Iterator<Dinosauro> itDinosauri = getItDinosauri(getPlayerByToken(token));
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
	 * Ritorna un iteratore sui giocatori.
	 * @return
	 */
	public Iterator<Giocatore> getIteratorOnPlayers() {
		return listaGiocatori.values().iterator();
	}

	/**
	 * Verifica se esiste l'utente, helper per SocketAdaptor.
	 */
	boolean existsUserWithName(String user) {
		if (listaGiocatori.containsKey(user)) return true;
		else return false;
	}
	/**
	 * Verifica se l'utente con il nome richiesto è connesso.
	 * @throws UserExistsException 
	 */
	protected boolean isPlayerConnected(String nome) {
		if (existsUserWithName(nome) &&
				connectionTable.containsValue(nome)) return true;
		else return false;
	}
	/**
	 * Ritorna il Giocatore richiesto tramite il suo nome.
	 * @param nomeGiocatoreRichiesto
	 * @return
	 */
	protected Giocatore getPlayerByName(String nomeGiocatoreRichiesto) {
		return listaGiocatori.get(nomeGiocatoreRichiesto);
	}
	/**
	 * Ritorna il giocatore richiesto tramite il token.
	 * @param token
	 * @return
	 */
	protected Giocatore getPlayerByToken(String token) throws InvalidTokenException {
		if (existsPlayerWithToken(token)) return listaGiocatori.get(connectionTable.get(token));
		else return null;
	}

	/**
	 * Helper per verificare che effettivamente il token sia registrato.
	 * @param token
	 * @return
	 * @throws InvalidTokenException
	 */
	protected boolean existsPlayerWithToken (String token) throws InvalidTokenException {
		if (connectionTable.containsKey(token)) return true;
		else throw new InvalidTokenException();
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
	 * Helper per sapere se qualcuno sta giocando.
	 * @return
	 */
	public boolean isSomeonePlaying() {
		if (playersQueue.size() > 0) return true;
		else return false;
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
	 */
	protected void doLogout(String token) throws InvalidTokenException, NonInPartitaException {
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

	private void confermaTurno() {
		turnoConfermato = true;
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
	}/**
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
	 */
	protected boolean isPlayerInGame(String token) throws InvalidTokenException, NonInPartitaException {
		if (getPlayerByToken(token).isInGame()) return true;
		else throw new NonInPartitaException();
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
	 * Helper per verificare che sia il turno del giocatore che chiama la funzione. 
	 * @return
	 * @throws InvalidTokenException 
	 */
	protected boolean isMioTurno(String token) throws InvalidTokenException {
		if (nomeGiocatoreCorrente.equals(getPlayerByToken(token).getNome())) return true;
		else return false;
	}

	/**
	 * Aggiunge alla lista degli utenti connessi un giocatore che si è appena connesso.
	 */
	protected void addPlayerToConnTable(String newId, String nomeUser) {
		connectionTable.put(newId, nomeUser);
	}

	/**
	 * Restituisce un iteratore sugli ID dei giocatori.
	 * @return
	 */
	public Iterator<String> getIteratorOnPIds() {
		return connectionTable.keySet().iterator();
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
			if (connectionTable.get(tempToken).equals(user)) {
				return tempToken;
			}
		}
		return null;
	}

	/**
	 * Ritorna il nome dell'utente associato al token richiesto.
	 * @param user
	 * @return
	 */
	protected String getPlayerName(String token) {
		return connectionTable.get(token);
	}

	/**
	 * Ritorna il lato della mappa. Helper per gli adattatori.
	 * @return
	 */
	public int getLatoDellaMappa() {
		return rifMappa.getLatoDellaMappa();
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
	 * Ritorna un iteratore sui nomi dei giocatori.
	 * @return
	 */
	public Iterator<String> getIteratorOnPNames() {
		return connectionTable.values().iterator();
	}

	/**
	 * Ritorna la Cella della Mappa richiesta.
	 * @param x
	 * @param y
	 * @return
	 */
	protected Cella getCella(int x, int y) {
		return rifMappa.getCella(x, y);
	}
	/**
	 * Verifica se il dinosauro ha abbastanza energia per deporre un uovo, altrimenti lancia eccezione con causa.
	 * @throws GenericDinosauroException 
	 */
	private boolean haAbbastanzaEnergiaPerDeporreUnUovo(Dinosauro dinosauro) throws GenericDinosauroException {
		if (dinosauro.hasEnergyToRepl()) return true;
		else throw new GenericDinosauroException("mortePerInedia");
	}
	/**
	 * Verifica se la specie ha già 5 dinosauri. Se la specie ha dimensione massima lancia eccezione con causa.
	 * @throws GenericDinosauroException 
	 * @throws InvalidTokenException 
	 */
	private boolean haNumeroMassimoPerSpecie(String token) throws InvalidTokenException, GenericDinosauroException {
		if (getPlayerByToken(token).getRazza().hasNumeroMassimo()) throw new GenericDinosauroException("raggiuntoNumeroMaxDinosauri");
		else return false;
	}

	/**
	 * Verifica se il dinosauro può deporre un uovo.
	 * @throws GenericDinosauroException 
	 * @throws InvalidTokenException 
	 */
	protected boolean puoDeporreUnUovo(String token, Dinosauro dinosauro) throws GenericDinosauroException, InvalidTokenException {
		if (haAbbastanzaEnergiaPerDeporreUnUovo(dinosauro) &&
				!haNumeroMassimoPerSpecie(token)) return true;
		else return false;
	}
	/**
	 * Verifica se il dinosauro ha abbastanza energia per crescere, altrimenti lancia eccezione con causa.
	 * @throws GenericDinosauroException 
	 */
	private boolean haAbbastanzaEnergiaPerCrescere(Dinosauro dinosauro) throws GenericDinosauroException {
		if (dinosauro.hasEnergyToGrow()) return true;
		else throw new GenericDinosauroException("mortePerInedia");
	}

	/**
	 * Verifica se la specie ha già 5 dinosauri. Se la specie ha dimensione massima lancia eccezione con causa.
	 * @throws GenericDinosauroException 
	 * @throws InvalidTokenException 
	 */
	private boolean haDimensioneMassima(Dinosauro dinosauro) throws GenericDinosauroException {
		if (dinosauro.isAtDimensioneMax()) return true;
		else throw new GenericDinosauroException("raggiuntoNumeroMaxDinosauri");
	}

	/**
	 * Verifica se il dinosauro può crescere.
	 * @throws GenericDinosauroException 
	 */
	protected boolean puoCrescere(Dinosauro dinosauro) throws GenericDinosauroException { // TODO reimplementare, devo uccidere il dinosauro dentro razza, non da socketAdaptor.
		if (haAbbastanzaEnergiaPerCrescere(dinosauro) &&
				!haDimensioneMassima(dinosauro)) return true;
		else return false;
	}

	/**
	 * Implementa l'accesso alla partita, la parte non adattabile e condivisa da ogni modo di accesso.
	 * @throws InvalidTokenException 
	 * @throws RazzaNonCreataException 
	 * @throws TroppiGiocatoriException 
	 * @throws NonInPartitaException 
	 * @throws InterruptedException 
	 */
	protected void accediAPartita(String token) throws InvalidTokenException, NonInPartitaException, TroppiGiocatoriException, RazzaNonCreataException, InterruptedException {
		Giocatore tempGiocatore = getPlayerByToken(token);
		if (isPlayerInGame(token) &&
				isMaxPlayersInGame()) {
			if (tempGiocatore.hasRazza()) {
				inserisciDinosauriNellaMappa(token);
				playersQueue.put(getPlayerName(token));
			}
			else throw new RazzaNonCreataException();
		}
		else return;
	}
	
	Iterator<Dinosauro> getItDinosauri(Giocatore tempGiocatore) {
		return tempGiocatore.getRazza().iterator();
	}
}