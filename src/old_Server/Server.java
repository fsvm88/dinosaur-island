package old_Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import dinolib2.Dinosauro;
import dinolib2.Giocatore;
import dinolib2.Mappa;

public class Server {
	/*
	 * 1. crea i riferimenti agli oggetti: giocatori, mappa NON istanziarli
	 * 2. se i file di salvataggio esistono li carica, altrimenti assume primo avvio
	 * 3. se ha caricato i file, inizializza il threading per la ricezione dei comandi
	 * 4. se non ha caricato i file, primo avvio: crea una nuova mappa
	 * 5. Inizializza gestione per thread e apri socket per ricezione
	 * 		a. per la sincronizzazione utilizza runqueue singola con locking
	 * 		b. da quale giocatore parte? dal primo che si collega?
	 * 6. resta attivo finchè la mappa è in gioco
	 * 		a. continua a ricevere eventuali connessioni
	 * 		b. quando un giocatore si scollega salva stato e dinosauri e rimuovili dall'istanza di mappa
	 * 7. se il server viene chiuso salva lo stato di tutti i giocatori e di tutta la mappa per poterlo ricaricare 
	 */
	
	/**
	 * Definisce staticamente il lato della mappa,
	 * la porta di gioco,
	 * il socketServer
	 */
	private final int LATO_MAPPA = 40;
	private final int PORTA_DI_GIOCO = 32845;
	/**
	 * Passo 1
	 * Crea rif. agli oggetti (mappa, giocatori) ma NON istanziarli
	 * Implementare Giocatori come hashmap con nome come chiave
	 */
	protected Mappa rifMappa;
	protected ConcurrentHashMap<String, Giocatore> Giocatori;
	protected String nomeGiocatoreCorrente;
	private ServerSocket serverInstSocket;
	protected int qualcunoStaGiocando = 0;
	
	Random rnd = new Random();
	
 	public Server(){
		/**
		 * Il passo 1 è implementato come semplice definizione di attributi di classe dei riferimenti
		 * Il passo 2 è implementato con caricaPartitaDaFile
		 * Nel catch, visto che non posso caricare nè mappa nè giocatori, implemento la creazione della mappa (passo 4)
		 */
		
		try {
			caricaPartitaDaFile();
		}
		catch (FileNotFoundException e) {
			creaNuovaMappa();
		}
		
		listenSocket();
	}
	
	@SuppressWarnings("unused")
	public static void main (String[] args) {
		Server server = new Server();
	}
	
	/**
	 * Implementa Passo 2
	 * Se i file di salvataggio esistono li carica, altrimenti assume primo avvio
	 */
	private void caricaPartitaDaFile() throws FileNotFoundException {
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
	 */
	private void caricaFileDiMappa(String nomefile) throws FileNotFoundException {

	}
	/**
	 * Implementa il caricamento del file giocatori se esiste
	 * Se non esiste lancia FileNotFoundException
	 */
	private void caricaFileGiocatori(String nomefile) throws FileNotFoundException {
		
	}
	
	/**
	 * Helper per la creazione di una mappa nuova
	 */
	private void creaNuovaMappa() {
		rifMappa = new Mappa(LATO_MAPPA);
	}
	
	/**
	 * Helper per l'inizializzazione e la gestione dei socket per i client tramite threading
	 */
	public void listenSocket () {
		try {
			serverInstSocket = new ServerSocket(PORTA_DI_GIOCO);
		}
		catch (IOException e) {
			System.out.println("Could not listen on port " + PORTA_DI_GIOCO);
			System.exit(-1);
		}
		boolean running = true;
		while (running) {
			ClientListener clientListener;
			try {
				clientListener = new ClientListener(serverInstSocket.accept());
				Thread threadedClientListener = new Thread(clientListener);
				threadedClientListener.start();
				System.out.println("Server started successfully, creating threads on need..");
			}
			catch (IOException e) {
				System.out.println("Accept failed on " + PORTA_DI_GIOCO);
				System.exit(-1);
			}
		}
	}
	
	protected void broadcastCambioTurno() {
		// TODO implementa il broadcast per cambiare turno
	}
	
	/* Seguono un mare di helper comuni a tutti i thread e spostabili nella main class.*/
	
	/**
	 * Helper per rimuovere il dinosauro dalla mappa.
	 * @param x
	 * @param y
	 */
	protected void rimuoviDinosauroDallaCella(int x, int y) {
		rifMappa.rimuoviDinosauroDallaCella(x, y);
	}
	
	/**
	 * Helper per la generazione di un nuovo token alfanumerico.
	 * @return
	 */
	protected static String getNewToken() {
		return Long.toString(Double.doubleToLongBits(Math.random()));
	}
	
	/**
	 * Helper per verificare la correttezza della password.
	 * @param curGamer
	 * @param suppliedPassword
	 * @return
	 */
	protected boolean passwordIsValid(Giocatore curGamer, String suppliedPassword) {
		if ( curGamer.getPassword().equals(suppliedPassword) ) return true;
		else return false;
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
	 * Gestisce la rimozione di tutti i dinosauri dalla mappa.	
	 */
	protected void rimuoviDinosauriDallaMappa(Giocatore curPlayer) {
		/* Usa iteratore per iterare tutti i dinosauri dell'utente e impostarli sulla mappa */
		Iterator<Dinosauro> IteratorePerDinosauriDelGiocatore = curPlayer.dammiIteratoreSuiDinosauri();
		while (IteratorePerDinosauriDelGiocatore.hasNext()) {
			Dinosauro tempDinosauro = IteratorePerDinosauriDelGiocatore.next();
			int x = tempDinosauro.getX(), y = tempDinosauro.getY();
			rimuoviDinosauroDallaCella(x, y);
		}
	}
	
	/**
	 * Quando l'utente esegue il login aggiunge i dinosauri alla mappa.
	 */
	protected void inserisciDinosauriNellaMappa(Giocatore curPlayer) {
		/* Usa iteratore per iterare tutti i dinosauri dell'utente e impostarli sulla mappa */
		Iterator<Dinosauro> IteratorePerDinosauriDelGiocatore = curPlayer.dammiIteratoreSuiDinosauri();
		while (IteratorePerDinosauriDelGiocatore.hasNext()) {
			Dinosauro tempDinosauro = IteratorePerDinosauriDelGiocatore.next();
			int x = tempDinosauro.getX(), y = tempDinosauro.getY();
			if (tryActualSpawn(x, y)) return;
			else {
				int i = 1;
				do {
					if (tryNearestSpawn(x, y, i, tempDinosauro)) {
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
	private boolean tryActualSpawn(int x, int y) {
		if (rifMappa.isLibera(x, y)) {
			rifMappa.spawnDinosauro(x, y);
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
		int i = -1;
		int j = -1;
		do {
			for (i = -maxDistance; i < (maxDistance+1); i++) {
				if (tryActualSpawn(i+x, j+y)) {
					tempDinosauro.setXY(i+x, j+y);
					return true;
				}
			}
			j++;
		} while (j<maxDistance && (0<(i+x)) &&
				((i+x)<rifMappa.getLatoDellaMappa()) &&
				(0<(j+y)) &&
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
	protected boolean existsRazza(Giocatore curPlayer) {
		if (curPlayer.numeroDinosauri() > 0) return true;
		else return false;
	}
	
	/**
	 * Helper che verifica l'esistenza del nome della razza del dinosauro
	 * @return
	 */
	protected boolean existsNomeRazza(Giocatore curPlayer) {
		if (curPlayer.getNomeRazzaDinosauro() != null) return true;
		return false;
	}
}