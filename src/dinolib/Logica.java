package dinolib;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import dinolib.UserExistsException;

public class Logica {
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
	private Hashtable<String, Giocatore> Giocatori;
	/**
	 * Accoppia il token al giocatore.
	 * Chiavi: token
	 * Value: nomi giocatori.
	 * @uml.property name="NomeEToken"
	 */
	private Hashtable<String, String> TokenENome;
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
	private void rimuoviDinosauriDallaMappa(Giocatore tempGiocatore) throws InvalidTokenException {
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
	public void inserisciDinosauriNellaMappa(String token) throws InvalidTokenException {
		Iterator<String> itIdDinosauri = ritornaGiocatoreRichiestoPerToken(token).getItIdDinosauri();
		while (itIdDinosauri.hasNext()) {
			String idCorrente = itIdDinosauri.next();
			Dinosauro tempDinosauro = ritornaGiocatoreRichiestoPerToken(token).getDinosauro(idCorrente);
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
	 * Ritorna un iteratore sulle chiavi dei giocatori.
	 * @return
	 */
	private Iterator<String> returnIteratoreSulleChiaviGiocatori() {
		return Giocatori.keySet().iterator();
	}

	/**
	 * Ritorna un iteratore sui giocatori.
	 * @return
	 */
	public Iterator<Giocatore> returnIteratoreSuiGiocatori() {
		return Giocatori.values().iterator();
	}

	/**
	 * Verifica se esiste l'utente, nel caso fa il throw di UserExistsException.
	 */
	boolean existsUser(String user) throws UserExistsException {
		if (Giocatori.containsKey(user)) throw new UserExistsException();
		else return false;
	}

	/**
	 * Verifica se l'utente con il nome richiesto è connesso.
	 */
	public boolean playerIsConnected(String nome) {
		if (TokenENome.containsValue(nome)) return true;
		else return false;
	}

	/**
	 * Crea un utente a partire da username e password.
	 * @param user
	 * @param pwd
	 */
	public void doCreaUtente(String user, String pwd) {
		Giocatori.put(user, new Giocatore(user, pwd));
	}

	/**
	 * Ritorna il Giocatore richiesto tramite il suo nome.
	 * @param nomeGiocatoreRichiesto
	 * @return
	 */
	public Giocatore ritornaGiocatoreRichiestoPerNome(String nomeGiocatoreRichiesto) {
		return Giocatori.get(nomeGiocatoreRichiesto);
	}





	/**
	 * Ritorna il giocatore richiesto tramite il token.
	 * @param token
	 * @return
	 */
	Giocatore ritornaGiocatoreRichiestoPerToken(String token) throws InvalidTokenException {
		if (existsUserWithToken(token)) {
			return Giocatori.get(TokenENome.get(token));
		}
		else return null;
	}

	/**
	 * Helper per verificare che effettivamente il token sia registrato.
	 * @param token
	 * @return
	 * @throws InvalidTokenException
	 */
	boolean existsUserWithToken (String token) throws InvalidTokenException {
		if (TokenENome.containsKey(token)) return true;
		else return false;
	}

	boolean existsRaceWithSameName (String nomeRazza) {
		Iterator<Giocatore> itGiocatori = returnIteratoreSuiGiocatori();
		while (itGiocatori.hasNext()) {
			Giocatore tempGiocatore = itGiocatori.next();
			if (tempGiocatore.getNomeRazzaDinosauri().equals(nomeRazza)) return true;
		}
		return false;
	}

	/**
	 * Helper per verificare l'esistenza della razza per il giocatore con il dato token.
	 * @param token
	 * @return
	 * @throws InvalidTokenException
	 */
	boolean existsRaceForPlayer (String token) throws InvalidTokenException {
		return ritornaGiocatoreRichiestoPerToken(token).hasRazza();
	}
	/**
	 * Verifica se il numero massimo di utenti è connesso. Se sì lancia una eccezione, altrimenti ritorna false.
	 * ATTENZIONE! Il valore di ritorno di di default è FALSE! (Contrariamente a tutti gli altri helper).
	 * @return
	 * @throws TroppiGiocatoriException
	 */
	public boolean massimoNumeroUtentiConnesso() throws TroppiGiocatoriException {
		Iterator<Giocatore> itGiocatori = returnIteratoreSuiGiocatori();
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
	 * @return
	 */
	public boolean someoneIsPlaying() {
		if (qualcunoStaGiocando) return true;
		else return false;
	}

	/**
	 * Helper per far sapere che nessuno sta giocando.
	 * @return
	 */
	public boolean nobodyIsPlaying() {
		if (qualcunoStaGiocando) return true;
		else return false;
	}

	/**
	 * Verifica (fa il controllo) che qualcuno stia giocando. Basta un giocatore qualunque, ecco perchè il ciclo si spezza con un return immediato.
	 * @return
	 */
	private void verifySomeoneIsPlaying() {
		Iterator<Giocatore> itGiocatori = returnIteratoreSuiGiocatori();
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
	public void esciDallaPartita(String token) throws InvalidTokenException {
		Giocatore tempGiocatore = ritornaGiocatoreRichiestoPerToken(token);
		rimuoviDinosauriDallaMappa(tempGiocatore);
		tempGiocatore.iAmNotInGame();
		verifySomeoneIsPlaying();
		if (isSomeonePlaying()) return;
		else nomeGiocatoreCorrente = null;
	}

	/**
	 * Fa il logout dell'utente col token richiesto.
	 * @param token
	 * @throws InvalidTokenException
	 * @throws NonInPartitaException
	 */
	public void doLogout(String token) throws InvalidTokenException, NonInPartitaException {
		Giocatore tempGiocatore = ritornaGiocatoreRichiestoPerToken(token);
		if (tempGiocatore.isLogged()) {
			if (playerIsInGame(token)) {
				esciDallaPartita(token);
			}
			tempGiocatore.iAmNotLogged();
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
	public boolean trySpawnOfAnEgg(String token, int x, int y, Dinosauro newDinosauro, String newIdDinosauro) throws InvalidTokenException {
		int i = 1;
		do {
			if (tryNearestSpawn(x, y, i, newIdDinosauro, newDinosauro)) {
				ritornaGiocatoreRichiestoPerToken(token).aggiungiDinosauroAllaRazza(newDinosauro, newIdDinosauro);
				return true;
			}
			else i++;
		} while (i<rifMappa.getLatoDellaMappa());
		return false;
	}/**
	 * Verifica se l'utente possiede il dinosauro con l'id cercato, altrimenti lancia eccezione.
	 * @return 
	 */
	public boolean playerHasDinosauro(String token, String idDinosauro) throws InvalidIDException, InvalidTokenException {
		if (ritornaGiocatoreRichiestoPerToken(token).existsDinosauro(idDinosauro)) return true;
		else throw new InvalidIDException();
	}



	/**
	 * Verifica se l'utente è in partita, altrimenti lancia eccezione.
	 * @throws InvalidTokenException 
	 * @throws NonInPartitaException 
	 */
	public boolean playerIsInGame(String token) throws InvalidTokenException, NonInPartitaException {
		if (ritornaGiocatoreRichiestoPerToken(token).isInGame()) return true;
		else throw new NonInPartitaException();
	}
	/**
	 * Ritorna la x più a sinistra possibile rispetto a quella specificata.
	 * @param x
	 * @param rangeVista
	 * @return
	 */
	public int doSubtraction(int coord, int rangeVista) {
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
	public int doAddition(int coord, int rangeVista) {
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
	public boolean isMioTurno (String token) throws InvalidTokenException {
		if (nomeGiocatoreCorrente.equals(ritornaGiocatoreRichiestoPerToken(token).getNome())) return true;
		else return false;
	}

	/**
	 * Aggiunge alla lista degli utenti connessi un giocatore che si è appena connesso.
	 */
	public void aggiungiUtenteConnesso(String newId, String nomeUser) {
		TokenENome.put(newId, nomeUser);
	}

	/**
	 * Restituisce un iteratore sugli ID dei giocatori.
	 * @return
	 */
	public Iterator<String> getIteratorOnPIds () {
		return TokenENome.keySet().iterator();
	}

	/**
	 * Ritorna il token dell'utente richiesto.
	 * @param user
	 * @return
	 */
	public String getPlayerToken(String user) {
		Iterator<String> itToken = getIteratorOnPIds();
		String tempToken = null;
		while (itToken.hasNext()) {
			tempToken = itToken.next();
			if (TokenENome.get(tempToken).equals(user)) {
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
	public void createNewRaceForPlayer(String token, String raceName, Dinosauro dinosauro) throws InvalidTokenException {
		ritornaGiocatoreRichiestoPerToken(token).creaNuovaRazzaDiDinosauri(raceName, dinosauro);
	}
	
	/**
	 * Ritorna un iteratore sui nomi dei giocatori.
	 * @return
	 */
	public Iterator<String> getIteratorOnPNames() {
		return TokenENome.values().iterator();
	}
	
	/**
	 * Ritorna la Cella della Mappa richiesta.
	 * @param x
	 * @param y
	 * @return
	 */
	public Cella getCella(int x, int y) {
		return rifMappa.getCella(x, y);
	}
}