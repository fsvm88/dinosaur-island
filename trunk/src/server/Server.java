package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import old_Server.ClientListener;

import dinolib.Mappa;
import dinolib2.Giocatore;
import server.Server;

public class Server {
	/**
	 * Definisce staticamente il lato della mappa.
	 * @uml.property name="LATO_MAPPA"
	 */
	private final int lato_MAPPA = 40;
	/**
	 * Definisce staticamente la porta di gioco.
	 * @uml.property name="PORTA_DI_GIOCO"
	 */
	private final int PORTA_DI_GIOCO = 32845;
	/**
	 * Definisce il riferimento alla mappa.
	 * @uml.property name="rifMappa"
	 */
	protected Mappa rifMappa;
	/**
	 * Definisce la lista dei giocatori.
	 * @uml.property name="Giocatori"
	 */
	protected ConcurrentHashMap<String, Giocatore> Giocatori;
	/**
	 * Definisce la stringa che contiene il nome del giocatore che in questo momento ha il turno.
	 * @uml.property name="Giocatori"
	 */
	protected String nomeGiocatoreCorrente;
	/**
	 * Definisce il riferimento al socket del server.
	 * @uml.property name="serverInstSocket"
	 */
	private ServerSocket serverInstSocket;
	/**
	 * Definisce una variabile che assicura che qualcuno sta giocando.
	 * @uml.property name="qualcunoStaGiocando"
	 */
	protected boolean qualcunoStaGiocando = false;

	/**
	 * Definisce il generatore di numeri casuali.
	 * @uml.property name="rnd"
	 */
	Random rnd = new Random();

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main (String[] args) {
		Server server = new Server();
	}

	/**
	 * Dichiara il costruttore dell'oggetto Server che poi gestisce tutto.
	 */
	public Server(){
		try {
			caricaPartitaDaFile();
		}
		catch (FileNotFoundException e) {
			creaNuovaMappa();
		}
		listenSocket();
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
		rifMappa = new Mappa(lato_MAPPA);
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
			ClientWorker clientWorker;
			try {
				clientWorker = new ClientWorker(serverInstSocket.accept());
				Thread threadedClientWorker = new Thread(clientWorker);
				threadedClientWorker.start();
				System.out.println("Server started successfully, creating threads on need..");
			}
			catch (IOException e) {
				System.out.println("Accept failed on " + PORTA_DI_GIOCO);
				System.exit(-1);
			}
		}
	}

	/**
	 * Helper per la generazione di un nuovo token alfanumerico.
	 * @return
	 */
	protected static String getNewToken() {
		return Long.toString(Double.doubleToLongBits(Math.random()));
	}
}