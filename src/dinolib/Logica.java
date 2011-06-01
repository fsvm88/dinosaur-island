package dinolib;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Hashtable;
import java.util.Iterator;

import dinolib.UserExistsException;

public class Logica implements Runnable {
	/**
	 * Definisce staticamente e definitivamente il numero massimo di giocatori ammessi in partita.
	 * @uml.property name="NUMERO_MASSIMO_GIOCATORI_INGAME"
	 */
	private final int numero_MASSIMO_GIOCATORI_INGAME = 8;
	/**
	 * Definisce staticamente e definitivamente il lato della mappa.
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
	 * Definisce una variabile che assicura che qualcuno sta giocando.
	 * @uml.property name="qualcunoStaGiocando"
	 */
	private boolean qualcunoStaGiocando = false;
	
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
	
	private boolean logicaIsRunning = true;
	
	protected boolean isLogicaRunning() {
		return logicaIsRunning;
	}
	
	/**
	 * Questo metodo gestisce effettivamente la logica di turno.
	 * Creare un thread extra è più semplice che sincronizzare gli accessi da ogni client.
	 * In questo modo tutta la logica è effettivamente implementata in Logica e creare nuovi modi di accesso ai dati richiede solo l'implementazione di Adattatori.
	 */
	public void run () {
		while (isLogicaRunning()) {
			
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
		Iterator<String> itIdDinosauri = tempGiocatore.getItIdDinosauri();
		while (itIdDinosauri.hasNext()) {
			String idCorrente = itIdDinosauri.next();
			Dinosauro tempDinosauro = tempGiocatore.getDinosauro(idCorrente);
			rimuoviDinosauroDallaCella(tempDinosauro.getX(), tempDinosauro.getY());
		}
	}

	/**
	 * Quando l'utente esegue il login aggiunge i dinosauri alla mappa.
	 * @throws InvalidTokenException 
	 */
	protected void inserisciDinosauriNellaMappa(String token) throws InvalidTokenException {
		Iterator<String> itIdDinosauri = getPlayerByToken(token).getItIdDinosauri();
		while (itIdDinosauri.hasNext()) {
			String idCorrente = itIdDinosauri.next();
			Dinosauro tempDinosauro = getPlayerByToken(token).getDinosauro(idCorrente);
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
		int i = -maxDistance;
		int j = -maxDistance;
		do {
			i = -maxDistance;
			do {

				if (tryActualSpawn(i+x, j+y, idDinosauro)) {
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
			if (tempGiocatore.getNomeRazza().equals(nomeRazza)) throw new NomeRazzaOccupatoException();
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
		Iterator<Giocatore> itGiocatori = getIteratorOnPlayers();
		int i = 0;
		while (itGiocatori.hasNext()) {
			if (itGiocatori.next().isInGame()) i++;
		}
		if (i < numero_MASSIMO_GIOCATORI_INGAME) return false;
		else throw new TroppiGiocatoriException();
	}

	/**
	 * Helper per sapere se qualcuno sta giocando.
	 * @return
	 */
	public boolean isSomeonePlaying() {
		if (qualcunoStaGiocando) return true;
		else return false;
	}

	/**
	 * Helper per far sapere che qualcuno sta giocando.
	 */
	protected void someoneIsPlaying() {
		qualcunoStaGiocando = true;
	}

	/**
	 * Helper per far sapere che nessuno sta giocando.
	 */
	protected void nobodyIsPlaying() {
		qualcunoStaGiocando = false;
	}

	/**
	 * Controlla che qualcuno stia giocando e aggiorna lo stato di gioco. Basta un giocatore qualunque in gioco, ecco perchè il ciclo si spezza con un return immediato.
	 * @return
	 */
	private void updatePlayingStatus() {
		Iterator<Giocatore> itGiocatori = getIteratorOnPlayers();
		while (itGiocatori.hasNext()) {
			if (itGiocatori.next().isInGame()) {
				someoneIsPlaying();
				return;
			}
		}
	}

	/**
	 * Codice per l'uscita dalla partita. Viene chiamato direttamente o tramite l'adattatore.
	 * @throws InvalidTokenException 
	 */
	protected void doEsciDallaPartita(String token) throws InvalidTokenException {
		Giocatore tempGiocatore = getPlayerByToken(token);
		rimuoviDinosauriDallaMappa(tempGiocatore);
		tempGiocatore.notInGame();
		updatePlayingStatus();
		if (isSomeonePlaying()) return;
		else nomeGiocatoreCorrente = null;
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
		// TODO Auto-generated method stub

	}
	/**
	 * Prova a inserire un dinosauro appena nato. Questo metodo viene usato da deponiUovo.
	 * @return
	 * @throws InvalidTokenException 
	 */
	protected boolean trySpawnOfAnEgg(String token, int x, int y, Dinosauro newDinosauro, String newIdDinosauro) throws InvalidTokenException {
		int i = 1;
		do {
			if (tryNearestSpawn(x, y, i, newIdDinosauro, newDinosauro)) {
				getPlayerByToken(token).aggiungiDinosauro(newDinosauro, newIdDinosauro);
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
		if (getPlayerByToken(token).existsDinosauro(idDinosauro)) return true;
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
		if (getPlayerByToken(token).specieHaNumeroMassimo()) return true;
		else throw new GenericDinosauroException("raggiuntoNumeroMaxDinosauri");
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
	protected boolean puoCrescere(Dinosauro dinosauro) throws GenericDinosauroException {
		if (haAbbastanzaEnergiaPerCrescere(dinosauro) &&
				!haDimensioneMassima(dinosauro)) return true;
		else return false;
	}
}