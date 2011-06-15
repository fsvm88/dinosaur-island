package dinolib;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
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
	private boolean logicaIsRunning = false;
	/**
	 * Variabile che dice se il turno del giocatore è stato confermato.
	 * @uml.property  name="turnoConfermato"
	 */
	private boolean turnoConfermato = false;


	/* Costruttore e funzioni di primo avvio */
	/**
	 * Costruttore di default per la classe Logica.
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
	protected Mappa getMappa() { return rifMappa; } // Testato
	protected PlayerManager getPMan() { return pMan; } // Testato
	protected ConnectionManager getCMan() { return cMan; } // Testato
	protected RRScheduler getRRSched() { return rrsched; } // Testato
	protected boolean isLogicaRunning() { return logicaIsRunning; } // Testato
	protected Giocatore getPlayerByToken(String token) throws InvalidTokenException { // Testato
		if (getCMan().existsToken(token) && getPMan().exists(getCMan().getName(token))) return getPMan().getPlayer(getCMan().getName(token));
		else throw new InvalidTokenException();
	}

	/**
	 * Helper per verificare che sia il turno del giocatore che chiama la funzione. 
	 * @return
	 * @throws InvalidTokenException 
	 * @throws NonInPartitaException 
	 * @throws NonAutenticatoException 
	 */
	protected boolean isMioTurno(String token) throws InvalidTokenException, NonInPartitaException {
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
	protected boolean isPlayerInGame(String token) throws InvalidTokenException {
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
	protected boolean isPlayerLogged(String token) throws InvalidTokenException { // Testato
		if (getCMan().existsToken(token)) return true;
		else throw new InvalidTokenException();
	}

	/**
	 * Controlla se esiste un dinosauro tra tutti quelli dei giocatori che hanno una razza.
	 * @param idDinosauro
	 * @return
	 */
	protected boolean existsDinosauroWithId(String idDinosauro) {
		Iterator<Giocatore> itGiocatori = getPMan().iterator();
		Giocatore tempGiocatore = null;
		while (itGiocatori.hasNext()) {
			tempGiocatore = itGiocatori.next();
			if (tempGiocatore.hasRazza())
				if (itGiocatori.next().getRazza().existsDinosauroWithId(idDinosauro)) return true;
		}
		return false;
	}

	/**
	 * Restituisce il giocatore che possiede il dato dinosauro.
	 * @param idDinosauro
	 * @return
	 */
	protected Giocatore getPlayerByIdDinosauro(String idDinosauro) {
		Iterator<Giocatore> itSuiGiocatori = getPMan().iterator();
		Giocatore tempGiocatore = null;
		while (itSuiGiocatori.hasNext()) {
			tempGiocatore = itSuiGiocatori.next();
			if (tempGiocatore.getRazza().existsDinosauroWithId(idDinosauro)) break;
		}
		return tempGiocatore;
	}

	protected void broadcastCambioTurno() {

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
	void doConfermaTurno() {
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
			getMappa().rimuoviIlDinosauroDallaCella(tempDinosauro.getCoord());
		}
	}
	/**
	 * Quando l'utente esegue il login aggiunge i dinosauri alla mappa.
	 * @throws InvalidTokenException 
	 */
	private void inserisciDinosauriNellaMappa(String token) throws InvalidTokenException {
		Iterator<Dinosauro> itDinosauri = getPlayerByToken(token).getRazza().iterator();
		Dinosauro tempDinosauro;
		while (itDinosauri.hasNext()) {
			tempDinosauro = itDinosauri.next();
			if (tryActualSpawn(tempDinosauro.getCoord(), tempDinosauro.getIdDinosauro())) return;
			else {
				int i = 1;
				do {
					if (tryNearestSpawn(i, tempDinosauro)) {
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
	private boolean tryActualSpawn(Coord newCoord, String idDinosauro) {
		if ( !isCellaAcqua(newCoord) &&
				!isCellaDinosauro(newCoord)) {
			getMappa().spawnDinosauro(newCoord, idDinosauro);
			return true;
		}
		return false;
	}

	private boolean isValidCoord(Coord coordTest) {
		if ((0 <= coordTest.getX()) &&
				(0 <= coordTest.getY()) &&
				(coordTest.getX() < getMappa().getLatoDellaMappa()) &&
				(coordTest.getY() < getMappa().getLatoDellaMappa())) return true;
		else return false;
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
	private boolean tryNearestSpawn(int maxDistance, Dinosauro tempDinosauro) {
		int i = -maxDistance;
		int j = -maxDistance;
		Coord curCoord = null;
		do {
			i = -maxDistance;
			do {
				curCoord = new Coord(i+tempDinosauro.getCoord().getX(), j+tempDinosauro.getCoord().getY());
				if (tryActualSpawn(curCoord, tempDinosauro.getIdDinosauro())) {
					tempDinosauro.setCoord(curCoord);
					return true;
				}
				i++;
			} while ((i<(maxDistance+1)) &&
					isValidCoord(curCoord));
			j++;
		} while ((j<(maxDistance+1)) &&
				isValidCoord(curCoord));
		return false;
	}
	void doPassaTurno() {
		// TODO Auto-generated method stub
	}
	/**
	 * Prova a inserire un dinosauro appena nato. Questo metodo viene usato da deponiUovo.
	 * @return
	 * @throws InvalidTokenException 
	 */
	private boolean trySpawnOfAnEgg(String token, Coord newCoords, Dinosauro newDinosauro) throws InvalidTokenException {
		int i = 1;
		do {
			newDinosauro.setCoord(newCoords);
			if (tryNearestSpawn(i, newDinosauro)) {
				getPlayerByToken(token).getRazza().add(newDinosauro);
				return true;
			}
			else i++;
		} while (i<getMappa().getLatoDellaMappa());
		return false;
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
	protected String doDeponiUovo(String token, String idDinosauro) throws GenericDinosauroException, InvalidTokenException {
		Dinosauro tempDinosauro = getPlayerByToken(token).getRazza().getDinosauroById(idDinosauro);
		if (!tempDinosauro.hasAzioneStatica()) throw new GenericDinosauroException("raggiuntoLimiteMosseDinosauro");
		getPlayerByToken(token).getRazza().deponiUovo(idDinosauro);
		Coord newCoords = tempDinosauro.getCoord();
		if (getPlayerByToken(token).getRazza().getTipo().equals("Carnivoro")) {
			Dinosauro newDinosauro = new Carnivoro(newCoords);
			newDinosauro.nonUsabile();
			trySpawnOfAnEgg(token, newCoords, newDinosauro);
			return newDinosauro.getIdDinosauro();
		}
		else if (tempDinosauro.getTipoRazza().equals("Erbivoro")) {
			Dinosauro newDinosauro = new Erbivoro(newCoords);
			newDinosauro.nonUsabile();
			trySpawnOfAnEgg(token, newCoords, newDinosauro);
			return newDinosauro.getIdDinosauro();
		}
		return null;
	}

	/**
	 * Implementa la creazione dell'utente, se esiste già lancia eccezione.
	 * Viene chiamato dagli adattatori.
	 * @param user
	 * @param pwd
	 * @return
	 * @throws UserExistsException 
	 */
	protected boolean doCreaUtente(String nomeGiocatore, String pwd) throws UserExistsException { // Testato
		if (!getPMan().exists(nomeGiocatore)) {
			return getPMan().add(new Giocatore(nomeGiocatore, pwd));
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
	 * Se esiste già ritorna false, altrimenti la crea e ritorna true.
	 * @param token
	 * @param nomeRazza
	 * @param tipoRazza
	 * @return
	 * @throws NomeRazzaOccupatoException
	 * @throws InvalidTokenException
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
		else return false;
	}

	private boolean creaPrimoDinosauro(String token) throws InvalidTokenException {
		Giocatore tempGiocatore = getPlayerByToken(token);
		if (tempGiocatore.getRazza().getTipo().equals('c')) {
			if(tempGiocatore.getRazza().add(new Carnivoro(CommonUtils.getNewRandomCoord(getMappa().getLatoDellaMappa())))) return true;
		}
		else if (tempGiocatore.getRazza().getTipo().equals('e')) {
			if(tempGiocatore.getRazza().add(new Erbivoro(CommonUtils.getNewRandomCoord(getMappa().getLatoDellaMappa())))) return true;
		}
		return false;
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
	protected boolean doAccessoPartita(String token) throws InvalidTokenException, InterruptedException, TroppiGiocatoriException, RazzaNonCreataException {
		if (isPlayerLogged(token)) {
			if (getPlayerByToken(token).hasRazza()) {
				if (!getRRSched().maxPlayers()) {
					getRRSched().newTask(token);
					if (getPlayerByToken(token).getRazza().isEmpty()) {
						if (!creaPrimoDinosauro(token)) return false;
					}
					inserisciDinosauriNellaMappa(token);
					return true;
				}
				else throw new TroppiGiocatoriException();
			}
			else throw new RazzaNonCreataException();
		}
		else return false;
	}
	/**
	 * Permette l'uscita dalla partita.
	 * Ritorna true se l'utente può uscire, altrimenti false.
	 * @param token
	 * @return
	 * @throws InvalidTokenException
	 * @throws NonAutenticatoException
	 */
	protected boolean doUscitaPartita(String token) throws InvalidTokenException {
		if (isPlayerInGame(token)) {
			getRRSched().killTask(token);
			rimuoviDinosauriDallaMappa(getPlayerByToken(token));
			return true;
		}
		else return false;
	}
	/**
	 * Permette il logout dal server.
	 * Ritorna true se l'utente può uscire, altrimenti false.
	 * @param token
	 * @return
	 * @throws InvalidTokenException
	 * @throws NonAutenticatoException
	 */
	protected boolean doLogout(String token) throws InvalidTokenException {
		if (isPlayerLogged(token)) {
			if (isPlayerInGame(token)) {
				getRRSched().killTask(token);
			}
			getCMan().scollega(token);
			return true;
		}
		else return false;
	}

	private int getCostoSpostamento(Coord oldCoord, Coord newCoord) {
		if (oldCoord.equals(newCoord)) return 0;
		else {
			int costo = 0;
			costo += Math.abs(oldCoord.getX()-newCoord.getX());
			costo += Math.abs(oldCoord.getY()-newCoord.getY());
			return costo;
		}
	}

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

	private boolean isCellaRaggiungibile(Coord oldCoord, Coord newCoord, int maxHops) {
		/* Se ho superato il numero massimo di passi */
		if (maxHops < 0) {
			return false;
		}
		/* Se ho raggiunto il numero massimo di passi */
		else if (maxHops == 0) {
			/* e sono arrivato alla cella ritorna true */
			if (getCostoSpostamento(oldCoord, newCoord) == 0) return true;
			/* e non sono arrivato alla cella ritorna false */
			else return false;
		}
		/* Se non ho ancora raggiunto il numero di passi */
		else {
			/* Scansiona con due indici (righe, colonne) le celle adiacenti a quella di partenza, aggiungi le terre a un'ArrayList */
			int i = -1;
			int j = -1;
			ArrayList<Coord> myPaths = new ArrayList<Coord>();
			Coord tempCoord = null;
			while (i<2) {
				while (j<2) {
					tempCoord = new Coord(oldCoord.getX()+i, oldCoord.getY()+j);
					if(!isCellaAcqua(tempCoord)) myPaths.add(tempCoord);
				}
			}
			/* Se l'array non ha celle ritorno false.
			 * Attenzione! Dovrebbe essere una condizione NON verificabile, se qui l'array non ha celle o qualcosa è andato storto sopra,
			 * oppure sono su una singola isola di terra, che è comunque non verificabile come scenario. */
			if (myPaths.isEmpty()) return false;
			Coord curCoord = null;
			boolean hasPath = false;
			/* Fintanto che l'array non è vuoto */
			while (!myPaths.isEmpty()) {
				curCoord = getMinimumFromCoordArrayList(myPaths, newCoord);
				if (isCellaRaggiungibile(curCoord, newCoord, (maxHops-1))) hasPath = true;
				else continue;
				if (hasPath) return hasPath;
			}
			return hasPath;
		}
	}

	/**
	 * Permette ad un dinosauro di mangiare qualcosa su una cella.
	 * @param tempDinosauro
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
	 * @param tmpDinosauro
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isEntrambiDinosauriErbivori(Dinosauro tmpDinosauro, Coord newCoord) {
		if (tmpDinosauro.getTipoRazza().toLowerCase().equals("erbivoro") &&
				getPlayerByIdDinosauro(getMappa().getCella(newCoord).getIdDelDinosauro()).getRazza().getDinosauroById(getMappa().getCella(newCoord).getIdDelDinosauro()).getTipoRazza().toLowerCase().equals("erbivoro"))
			return true;
		else return false;
	}

	/**
	 * Svolge il combattimento tra due dinosauri.
	 * Restituisce un booleano, true se vince l'attaccante, false se vince l'attaccato.
	 * @param dinosauroSfidante
	 * @return
	 */
	private boolean combattimentoTraDinosauri(Dinosauro dinosauroSfidante) {
		Coord coordSfidante = dinosauroSfidante.getCoord();
		String idDinosauroSfidante = getMappa().getCella(coordSfidante).getIdDelDinosauro();
		String idDinosauroAttaccato = getMappa().getCella(coordSfidante).getIdDelDinosauro();
		Dinosauro dinosauroAttaccato = getPlayerByIdDinosauro(idDinosauroAttaccato).getRazza().getDinosauroById(idDinosauroAttaccato);
		int forzaSfidante = dinosauroSfidante.getForza();
		int forzaAttaccato = dinosauroAttaccato.getForza();
		if (forzaSfidante >= forzaAttaccato) {
			dinosauroAttaccato.nonUsabile();
			getPlayerByIdDinosauro(idDinosauroAttaccato).getRazza().removeById(idDinosauroAttaccato);
			return true;
		}
		else if (forzaSfidante < forzaAttaccato) {
			dinosauroSfidante.nonUsabile();
			getPlayerByIdDinosauro(idDinosauroSfidante).getRazza().removeById(idDinosauroSfidante);
			return false;
		}
		return false;
	}

	private boolean isCellaAcqua(Coord myCoord) { if (getMappa().getCella(myCoord).toString().toLowerCase().equals("acqua")) return true; else return false; }
	private boolean isCellaDinosauro(Coord myCoord) { if (getMappa().getCella(myCoord).toString().toLowerCase().equals("dinosauro")) return true; else return false; }

	/**
	 * Permette il movimento del dinosauro da una cella ad un'altra.
	 * Gestisce le varie condizioni di errore e chiama le funzioni appropriate in caso di combattimento.
	 * @param token
	 * @param idDinosauro
	 * @param newX
	 * @param newY
	 * @return
	 * @throws InvalidTokenException 
	 */
	protected String doMuoviDinosauro(String token, String idDinosauro, Coord newCoord) throws InvalidTokenException, GenericDinosauroException {
		Dinosauro tempDinosauro = getPlayerByToken(token).getRazza().getDinosauroById(idDinosauro);
		if (!tempDinosauro.hasMovimento()) throw new GenericDinosauroException("raggiuntoLimiteMosseDinosauro");
		if (isCellaRaggiungibile(tempDinosauro.getCoord(), newCoord, tempDinosauro.getSpostamentoMax()) && // TODO invoca garbage collector dopo questi check!
				!isCellaAcqua(newCoord) &&
				!isEntrambiDinosauriErbivori(tempDinosauro, newCoord)) {
			Character tipoCella = getMappa().getCella(newCoord).toString().toLowerCase().charAt(0);
			getPlayerByToken(token).getRazza().muoviDinosauro(idDinosauro, newCoord);
			if (tipoCella.equals('v') &&
					tempDinosauro.getTipoRazza().toLowerCase().equals("erbivoro")) {
				mangiaCella(tempDinosauro);
			}
			if (tipoCella.equals('c') &&
					tempDinosauro.getTipoRazza().toLowerCase().equals("carnivoro")) {
				mangiaCella(tempDinosauro);
			}
			if (tipoCella.equals('d')) {
				if (combattimentoTraDinosauri(tempDinosauro)) {
					return "v";
				}
				else return "p";
			}
			return "@ok";

		}
		else return "destinazioneNonValida";
	}
}