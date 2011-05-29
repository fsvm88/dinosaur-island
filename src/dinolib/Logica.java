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
	private void inserisciDinosauriNellaMappa(String token) throws InvalidTokenException {
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
	private Iterator<Giocatore> returnIteratoreSuiGiocatori() {
		return Giocatori.values().iterator();
	}

	/**
	 * Verifica se esiste l'utente, nel caso fa il throw di UserExistsException.
	 */
	private boolean existsUser(String user) throws UserExistsException {
		if (Giocatori.containsKey(user)) throw new UserExistsException();
		else return false;
	}

	/**
	 * Crea un utente a partire da username e password.
	 * @param user
	 * @param pwd
	 */
	private void doCreaUtente(String user, String pwd) {
		Giocatori.put(user, new Giocatore(user, pwd));
	}

	/**
	 * Adattatore per creare l'utente
	 * @param user
	 * @param pwd
	 * @throws UserExistsException
	 */
	public void aCreaUtente(String user, String pwd) throws UserExistsException {
		if (!existsUser(user)) {
			doCreaUtente(user, pwd);
			return;
		}
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
	 * Adattatore per creare l'utente
	 * @param user
	 * @param pwd
	 * @throws UserExistsException
	 */
	public void aLoginUtente(String user, String pwd) throws AuthenticationFailedException {
		if (Giocatori.containsKey(user)) {
			Giocatore tempGiocatore = ritornaGiocatoreRichiestoPerNome(user);
			if (tempGiocatore.passwordIsValid(pwd)) {
				TokenENome.put(CommonUtils.getNewToken(), user);
				tempGiocatore.iAmLogged();
				return;
			}
		}
		throw new AuthenticationFailedException();
	}

	/**
	 * Helper per restituire il token dell'utente richiesto.
	 * @param user
	 * @return
	 * @throws InvalidTokenException 
	 */
	public String aGetTokenUtente(String user) throws InvalidTokenException {
		if (TokenENome.containsValue(user)) {
			Iterator<String> itToken = TokenENome.keySet().iterator();
			String tempToken = null;
			while (itToken.hasNext()) {
				tempToken = itToken.next();
				if (TokenENome.get(tempToken).equals(user)) {
					return tempToken;
				}
			}
		}
		throw new InvalidTokenException();
	}

	/**
	 * Ritorna il giocatore richiesto tramite il token.
	 * @param token
	 * @return
	 */
	private Giocatore ritornaGiocatoreRichiestoPerToken(String token) throws InvalidTokenException {
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
	private boolean existsUserWithToken (String token) throws InvalidTokenException {
		if (TokenENome.containsKey(token)) return true;
		else return false;
	}

	private boolean existsRaceWithSameName (String nomeRazza) {
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
	private boolean existsRaceForPlayer (String token) throws InvalidTokenException {
		return ritornaGiocatoreRichiestoPerToken(token).hasRazza();
	}

	/**
	 * Crea una nuova razza di dinosauri per l'utente.
	 */
	public void aCreaRazzaETipo(String token, String nomeRazza, String tipoRazza) throws RaceAlreadyCreatedException, RaceNameExistsException, InvalidTokenException {
		if (existsUserWithToken(token) && !existsRaceWithSameName(nomeRazza)) {
			if (!existsRaceForPlayer(token)) {
				Dinosauro tempDinosauro;
				if (tipoRazza.equals("c")) {
					tempDinosauro = new Carnivoro(CommonUtils.getNewRandomIntValueOnMyMap(rifMappa.getLatoDellaMappa()), CommonUtils.getNewRandomIntValueOnMyMap(rifMappa.getLatoDellaMappa()));
					ritornaGiocatoreRichiestoPerToken(token).creaNuovaRazzaDiDinosauri(nomeRazza, tempDinosauro);
				}
				else if (tipoRazza.equals("e")) {
					tempDinosauro = new Erbivoro(CommonUtils.getNewRandomIntValueOnMyMap(rifMappa.getLatoDellaMappa()), CommonUtils.getNewRandomIntValueOnMyMap(rifMappa.getLatoDellaMappa()));
					ritornaGiocatoreRichiestoPerToken(token).creaNuovaRazzaDiDinosauri(nomeRazza, tempDinosauro);
				}
			}
		}
	}

	/**
	 * Helper per verificare che l'utente sia effettivamente loggato.
	 * @return 
	 * @throws InvalidTokenException 
	 */
	public boolean userIsLogged(String token) throws InvalidTokenException {
		if (existsUserWithToken(token)) {
			if (ritornaGiocatoreRichiestoPerToken(token).isLogged()) return true;
			else return false;
		}
		else throw new InvalidTokenException();
	}

	/**
	 * Verifica se il numero massimo di utenti è connesso. Se sì lancia una eccezione, altrimenti ritorna false.
	 * ATTENZIONE! Il valore di ritorno di di default è FALSE! (Contrariamente a tutti gli altri helper).
	 * @return
	 * @throws TroppiGiocatoriException
	 */
	private boolean massimoNumeroUtentiConnesso() throws TroppiGiocatoriException {
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
	private boolean isSomeonePlaying() {
		if (qualcunoStaGiocando) return true;
		else return false;
	}

	/**
	 * Helper per far sapere che qualcuno sta giocando.
	 * @return
	 */
	private boolean someoneIsPlaying() {
		if (qualcunoStaGiocando) return true;
		else return false;
	}

	/**
	 * Helper per far sapere che nessuno sta giocando.
	 * @return
	 */
	private boolean nobodyIsPlaying() {
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
	 * Implementa l'accesso alla partita.
	 * @throws NonInPartitaException 
	 */
	public void aAccediAPartita(String token) throws TroppiGiocatoriException, RazzaNonCreataException, InvalidTokenException, NonInPartitaException {
		Giocatore tempGiocatore = ritornaGiocatoreRichiestoPerToken(token);
		if (playerIsInGame(token) &&
				!massimoNumeroUtentiConnesso()) {
			if (tempGiocatore.hasRazza()) {
				if (!isSomeonePlaying()) someoneIsPlaying();
				inserisciDinosauriNellaMappa(token);
			}
			else throw new RazzaNonCreataException();
		}
		else return;
	}

	/**
	 * Codice per l'uscita dalla partita. Viene chiamato direttamente o tramite l'adattatore.
	 * @throws InvalidTokenException 
	 */
	private void esciDallaPartita(Giocatore tempGiocatore) throws InvalidTokenException {
		rimuoviDinosauriDallaMappa(tempGiocatore);
		tempGiocatore.iAmNotInGame();
		verifySomeoneIsPlaying();
		if (isSomeonePlaying()) return;
		else nomeGiocatoreCorrente = null;
	}

	/**
	 * Adattatore per l'uscita dalla partita.
	 * @param token
	 * @throws InvalidTokenException
	 */
	public void aEsciDallaPartita(String token) throws InvalidTokenException {
		if (existsUserWithToken(token)) esciDallaPartita(ritornaGiocatoreRichiestoPerToken(token));
	}

	/**
	 * Helper per le funzioni che restituiscono liste
	 * @param buffer
	 * @param toAppend
	 */
	private void assemblaBuffer(String buffer, String toAppend) {
		if (buffer != null) 
			buffer = buffer + "," + toAppend;
		else 
			buffer = toAppend;
	}
	/**
	 * Adattatore del comando per restituire la lista dei giocatori al client.
	 * @throws IOException 
	 */
	public String aListaDeiGiocatori(String token) throws InvalidTokenException {
		if (existsUserWithToken(token)) {
			Iterator<String> itNomiGiocatori = returnIteratoreSulleChiaviGiocatori();
			String buffer = null;
			while (itNomiGiocatori.hasNext()) {
				String curNomeGiocatore = itNomiGiocatori.next();
				if (Giocatori.get(curNomeGiocatore).isLogged() &&
						Giocatori.get(curNomeGiocatore).isInGame()) {
					assemblaBuffer(buffer, curNomeGiocatore);
				}
			}
			return buffer;
		}
		return null;
	}

	/**
	 * Metodo per gestire il logout dell'utente
	 * @throws NonInPartitaException 
	 */
	public void aLogout(String token) throws InvalidTokenException, NonInPartitaException {
		if (existsUserWithToken(token)) {
			Giocatore tempGiocatore = ritornaGiocatoreRichiestoPerToken(token);
			if (tempGiocatore.isLogged()) {
				if (playerIsInGame(token)) {
					esciDallaPartita(tempGiocatore);
				}
				tempGiocatore.iAmNotLogged();
			}
		}
	}

	/**
	 * Spedisce la lista dei dinosauri all'utente.
	 * @throws IOException
	 */
	public String aSendListaDinosauri(String token) throws InvalidTokenException, NonInPartitaException {
		if (existsUserWithToken(token) && playerIsInGame(token)) {
			String buffer = null;
			if (ritornaGiocatoreRichiestoPerToken(token).hasRazza()) {
				Iterator<String> itIdDinosauri = ritornaGiocatoreRichiestoPerToken(token).getItIdDinosauri();
				while (itIdDinosauri.hasNext()) {
					assemblaBuffer(buffer, itIdDinosauri.next());
				}
				return buffer;
			}
			return buffer;
		}
		return null;
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
	 * Verifica se il dinosauro ha abbastanza energia per deporre un uovo, altrimenti lancia eccezione con causa.
	 * @throws GenericDinosauroException 
	 */
	private boolean haAbbastanzaEnergiaPerDeporreUnUovo(Dinosauro dinosauro) throws GenericDinosauroException {
		if (dinosauro.haAbbastanzaEnergiaPerDeporreUnUovo()) return true;
		else throw new GenericDinosauroException("mortePerInedia");
	}

	/**
	 * Verifica se la specie ha già 5 dinosauri. Se la specie ha dimensione massima lancia eccezione con causa.
	 * @throws GenericDinosauroException 
	 * @throws InvalidTokenException 
	 */
	private boolean haNumeroMassimoPerSpecie(String token) throws GenericDinosauroException, InvalidTokenException {
		if (ritornaGiocatoreRichiestoPerToken(token).specieHaNumeroMassimo()) return true;
		else throw new GenericDinosauroException("raggiuntoNumeroMaxDinosauri");
	}

	/**
	 * Verifica se il dinosauro può deporre un uovo.
	 * @throws GenericDinosauroException 
	 * @throws InvalidTokenException 
	 */
	private boolean puoDeporreUnUovo(String token, Dinosauro dinosauro) throws GenericDinosauroException, InvalidTokenException {
		if (haAbbastanzaEnergiaPerDeporreUnUovo(dinosauro) &&
				!haNumeroMassimoPerSpecie(token)) return true;
		else return false;
	}

	/**
	 * Verifica se il dinosauro ha abbastanza energia per crescere, altrimenti lancia eccezione con causa.
	 * @throws GenericDinosauroException 
	 */
	private boolean haAbbastanzaEnergiaPerCrescere(Dinosauro dinosauro) throws GenericDinosauroException {
		if (dinosauro.haAbbastanzaEnergiaPerCrescere()) return true;
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
	private boolean puoCrescere(Dinosauro dinosauro) throws GenericDinosauroException {
		if (haAbbastanzaEnergiaPerCrescere(dinosauro) &&
				!haDimensioneMassima(dinosauro)) return true;
		else return false;
	}

	/**
	 * Prova a inserire un dinosauro appena nato. Questo metodo viene usato da deponiUovo.
	 * @return
	 * @throws InvalidTokenException 
	 */
	private boolean trySpawnOfAnEgg(String token, int x, int y, Dinosauro newDinosauro, String newIdDinosauro) throws InvalidTokenException {
		int i = 1;
		do {
			if (tryNearestSpawn(x, y, i, newIdDinosauro, newDinosauro)) {
				ritornaGiocatoreRichiestoPerToken(token).aggiungiDinosauroAllaRazza(newDinosauro, newIdDinosauro);
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
	 * @throws GenericDinosauroException 
	 * @throws NonInPartitaException 
	 * @throws InvalidIDException 
	 * @throws InvalidTokenException 
	 */
	public String deponiUovo(String token, String idDinosauro) throws IOException, InvalidTokenException, InvalidIDException, NonInPartitaException, GenericDinosauroException {
		if (existsUserWithToken(token) &&
				playerHasDinosauro(token, idDinosauro) &&
				playerIsInGame(token) &&
				isMioTurno(token)) { // TODO aggiungere limite mosse
			Dinosauro curDinosauro = ritornaGiocatoreRichiestoPerToken(token).getDinosauro(idDinosauro);
			int x = curDinosauro.getX();
			int y = curDinosauro.getY();
			if (puoDeporreUnUovo(token, curDinosauro)) {
				String newIdDinosauro = CommonUtils.getNewToken();
				if (curDinosauro.getTipoRazza().equals("Carnivoro")) {
					Dinosauro newDinosauro = new Carnivoro(x, y);
					newDinosauro.nonSonoUsabile();
					trySpawnOfAnEgg(token, x, y, newDinosauro, newIdDinosauro);
					return newIdDinosauro;
				}
				else if (curDinosauro.getTipoRazza().equals("Erbivoro")) {
					Dinosauro newDinosauro = new Erbivoro(x, y);
					newDinosauro.nonSonoUsabile();
					trySpawnOfAnEgg(token, x, y, newDinosauro, newIdDinosauro);
					return newIdDinosauro;
				}
			}
		}
		return null;
	}

	/**
	 * Verifica se l'utente possiede il dinosauro con l'id cercato, altrimenti lancia eccezione.
	 * @return 
	 */
	private boolean playerHasDinosauro(String token, String idDinosauro) throws InvalidIDException, InvalidTokenException {
		if (ritornaGiocatoreRichiestoPerToken(token).existsDinosauro(idDinosauro)) return true;
		else throw new InvalidIDException();
	}

	/**
	 * Chiama la funzione di crescita del dinosauro.
	 */
	public void aCresciDinosauro(String token, String idDinosauro) throws InvalidTokenException, InvalidIDException, GenericDinosauroException, NonInPartitaException, NonIlTuoTurnoException {
		if (existsUserWithToken(token) &&
				playerHasDinosauro(token, idDinosauro) &&
				playerIsInGame(token) &&
				isMioTurno(token)) {
			if (puoCrescere(ritornaGiocatoreRichiestoPerToken(token).getDinosauro(idDinosauro))) {
				ritornaGiocatoreRichiestoPerToken(token).getDinosauro(idDinosauro).cresci();
			}
		}
	}

	/**
	 * Verifica se l'utente è in partita, altrimenti lancia eccezione.
	 * @throws InvalidTokenException 
	 * @throws NonInPartitaException 
	 */
	private boolean playerIsInGame(String token) throws InvalidTokenException, NonInPartitaException {
		if (ritornaGiocatoreRichiestoPerToken(token).isInGame()) return true;
		else throw new NonInPartitaException();
	}

	/**
	 * Assembla lo stato comune del dinosauro
	 */
	private void assemblaStatoComuneDinosauro(String buffer, Giocatore tempGiocatore, Dinosauro tempDinosauro) {
		buffer = tempGiocatore.getNome() + "," +
		tempGiocatore.getNomeRazzaDinosauri() + "," +
		tempGiocatore.getTipoRazza().toLowerCase().charAt(0) + "," +
		"{" + "," + tempDinosauro.getX() + "," + CommonUtils.translateYforClient(tempDinosauro.getY(), rifMappa.getLatoDellaMappa()) + "," + "}" + "," +
		tempDinosauro.getDimensione();
	}

	/**
	 * Invia lo stato del dinosauro richiesto all'utente.
	 * @param scanner
	 * @throws IOException
	 */
	public String aStatoDinosauro(String token, String idDinosauro) throws InvalidTokenException, InvalidIDException, NonInPartitaException {
		if (existsUserWithToken(token) && playerHasDinosauro(token, idDinosauro)) {
			Giocatore tempGiocatore = ritornaGiocatoreRichiestoPerToken(token);
			Dinosauro tempDinosauro = tempGiocatore.getDinosauro(idDinosauro);
			String buffer = null;
			assemblaStatoComuneDinosauro(buffer, tempGiocatore, tempDinosauro);
			buffer = buffer + "," +
			tempDinosauro.getEnergiaAttuale() + "," +
			tempDinosauro.getTurnoDiVita();
			return buffer;
		}
		else if (existsUserWithToken(token) && !playerHasDinosauro(token, idDinosauro)) {
			Iterator<Giocatore> itSuiGiocatori = returnIteratoreSuiGiocatori();
			while (itSuiGiocatori.hasNext()) {
				Giocatore tempGiocatore = itSuiGiocatori.next();
				Iterator<String> itSuIdDinosauri = tempGiocatore.getItIdDinosauri();
				while (itSuIdDinosauri.hasNext()) {
					if (itSuIdDinosauri.next() == idDinosauro) {
						Dinosauro tempDinosauro = tempGiocatore.getDinosauro(idDinosauro);
						String buffer = null;
						assemblaStatoComuneDinosauro(buffer, tempGiocatore, tempDinosauro);
						buffer = buffer + "," +
						tempDinosauro.getEnergiaAttuale() + "," +
						tempDinosauro.getTurnoDiVita();
						return buffer;
					}

				}
			}
		}
		else throw new InvalidIDException();
		return null;
	}

	/**
	 * Ritorna la x più a sinistra possibile rispetto a quella specificata.
	 * @param x
	 * @param rangeVista
	 * @return
	 */
	private int doSubtraction(int coord, int rangeVista) {
		int subtraction = (coord - rangeVista);
		if (subtraction<0) return 0;
		else if (subtraction>=rifMappa.getLatoDellaMappa()) return (rifMappa.getLatoDellaMappa()-1);
		else return subtraction;
	}

	private int doAddition(int coord, int rangeVista) {
		int addition = (coord + rangeVista);
		if (addition<0) return 0;
		else if (addition>=rifMappa.getLatoDellaMappa()) return (rifMappa.getLatoDellaMappa()-1);
		else return addition;
	}

	private String getCellaDellaMappaPerBuffer(int x, int y) {
		Character tipoCella = rifMappa.getTipoCella(x, y).toLowerCase().charAt(0);
		if ((tipoCella.equals("t")) ||
				(tipoCella.equals("a"))) {
			return "[" + tipoCella.charValue() + "]";
		}
		else if ((tipoCella.equals("v")) ||
				tipoCella.equals("c")) {
			return "[" + tipoCella.charValue() + "," + rifMappa.getCella(x, y).getValoreAttuale() + "]";
		}
		else if ((tipoCella.equals("d"))) {
			return "[" + tipoCella.charValue() + "," + rifMappa.getCella(x, y).getIdDelDinosauro() + "]";
		}
		return null;
	}

	private String getRigaDellaMappa(int yRiga, int fromX, int toX) {
		String tmpBuf = null;
		int i = fromX;
		if (fromX == toX) {
			return getCellaDellaMappaPerBuffer(fromX, yRiga);
		}
		else {
			while ((i<toX)) {
				tmpBuf = getCellaDellaMappaPerBuffer(i, yRiga) + " ";
				i++;
			}
			tmpBuf = getCellaDellaMappaPerBuffer(toX, yRiga);
			return tmpBuf;
		}
	}

	/**
	 * Invia la vista locale del dinosauro richiesto all'utente.
	 * @param token
	 * @param idDinosauro
	 * @return
	 * @throws InvalidTokenException
	 * @throws NonInPartitaException
	 * @throws InvalidIDException
	 */
	public String aVistaLocale(String token, String idDinosauro) throws InvalidTokenException, NonInPartitaException, InvalidIDException {
		if (existsUserWithToken(token) &&
				playerIsInGame(token) &&
				playerHasDinosauro(token, idDinosauro)) {
			String buffer = null;
			Dinosauro tempDinosauro = ritornaGiocatoreRichiestoPerToken(token).getDinosauro(idDinosauro);
			int rangeVista = tempDinosauro.getRangeVista();
			int leftCornerX = doSubtraction(tempDinosauro.getX(), rangeVista);
			int bottomCornerY = doSubtraction(tempDinosauro.getY(), rangeVista);
			int rightCornerX = doAddition(tempDinosauro.getX(), rangeVista);
			int topCornerY = doAddition(tempDinosauro.getY(), rangeVista);
			buffer = "{" + leftCornerX + "," + CommonUtils.translateYforClient(bottomCornerY, rifMappa.getLatoDellaMappa()) + "}";
			buffer = buffer + "," + "{" + (topCornerY-bottomCornerY) + "," + (rightCornerX-leftCornerX) + "}" + ",";
			int j = bottomCornerY;
			do {
				buffer = getRigaDellaMappa(j, leftCornerX, rightCornerX) + ";";
				j++;
			} while (j<topCornerY);
			return buffer;
		}
		return null;
	}

	private void sendMappaGenerale() {
		// TODO Auto-generated method stub

	}

	/**
	 * Helper per assemblare il punteggio per un singolo giocatore.
	 * @param buffer
	 * @param giocatore
	 * @return
	 */
	private void assemblaPunteggio(String buffer, Giocatore giocatore) {
		assemblaBuffer(buffer, giocatore.getNome());
		assemblaBuffer(buffer, giocatore.getNomeRazzaDinosauri());
		buffer = buffer + "," + giocatore.getPunteggio();
		if (giocatore.isSpecieEstinta()) assemblaBuffer(buffer, "s");
		else assemblaBuffer(buffer, "n");
	}

	/**
	 * Ritorna la classifica di gioco.
	 * @return
	 * @throws InvalidTokenException 
	 * @throws NonInPartitaException 
	 */
	public String aClassifica(String token) throws InvalidTokenException {
		if (existsUserWithToken(token) &&
				userIsLogged(token)) {
			String buffer = null;
			Iterator<Giocatore> itGiocatori = returnIteratoreSuiGiocatori();
			while (itGiocatori.hasNext()) {
				assemblaPunteggio(buffer, itGiocatori.next());
			}
			return buffer;
		}
		return null;
	}
	/**
	 * Helper per verificare che sia il turno del giocatore che chiama la funzione. 
	 * @return
	 * @throws InvalidTokenException 
	 */
	private boolean isMioTurno (String token) throws InvalidTokenException {
		if (nomeGiocatoreCorrente.equals(ritornaGiocatoreRichiestoPerToken(token).getNome())) return true;
		else return false;
	}
}