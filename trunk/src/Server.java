import dinolib;

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
	private final int PORTA_DI_GIOCO = 54532;
	private ServerSocket serverISocket;
	/**
	 * Passo 1
	 * Crea rif. agli oggetti (mappa, giocatori) ma NON istanziarli
	 * Implementare Giocatori come hashmap con nome come chiave
	 */
	public Mappa rifMappa;
	public ConcurrentHashMap<String, Giocatore> Giocatori;
	private ServerSocket serverISocket;
	
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
	
	public static void main (String[] args) {
		Server server = new Server();
		
	}
	
	/**
	 * Implementa Passo 2
	 * Se i file di salvataggio esistono li carica, altrimenti assume primo avvio
	 */
	private void caricaPartitaDaFile throws FileNotFoundException {
		/**
		 * Carica file di mappa, se esiste deve esistere anche il file dei giocatori.
		 * In caso il primo o l'altro non esistano l'eccezione viene gestita e passata al chiamante, che quindi assume un primo avvios
		 */
		try {
			caricaFileDiMappa("mappa.dat");
		}
		catch (FileNotFoundException e){
			throw e;
		}
		try {
			caricaFileGiocatori();
		}
		catch (FileNotFoundException e){
			throw e;
		}
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
	
	private void creaNuovaMappa() {
		rifMappa = new Mappa(LATO_MAPPA);
	}
	
	public void listenSocket () {
		try {
			serverISocket = new ServerSocket(PORTA_DI_GIOCO);
		}
		catch (IOException e) {
			System.out.println("Could not listen on port" + PORTA_DI_GIOCO);
			System.exit(-1);
		}
		while (running) {
			ClientListener clientListener;
			String buffer;
			try {
				clientListener = new ClientListener(server.accept(), buffer);
				Thread threadedClientListener = new Thread(clientListener);
				threadedClientListener.start();
			}
			catch (IOException e) {
				System.out.println("Accept failed on" + PORTA_DI_GIOCO);
				System.exit(-1);
			}
		}
	}
	
}