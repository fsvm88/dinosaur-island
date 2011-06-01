package server;

import java.io.IOException;
import java.net.ServerSocket;

import dinolib.Logica;
import dinolib.SocketAdaptor;

class Server {
	/**
	 * Definisce staticamente la porta di gioco.
	 * @uml.property name="PORTA_DI_GIOCO"
	 */
	private final int PORTA_DI_GIOCO = 32845;
	/**
	 * Definisce il riferimento al socket del server.
	 * @uml.property name="serverInstSocket"
	 */
	private ServerSocket serverInstSocket;
	/**
	 * Dichiara la classe logica che gestisce tutta la logica di gioco indipendentemente dall'interfaccia di comunicazione.
	 */
	private Logica servLogica = null;
	/**
	 * Dichiara l'oggetto attraverso cui passano le comunicazioni su socket.
	 */
	protected SocketAdaptor socketAdaptor = null;
	
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
	public Server () {
		listenSocket();
	}
	
	private boolean serverIsRunning = true; 

	/**
	 * Helper per l'inizializzazione e la gestione dei socket per i client tramite threading
	 */
	private void listenSocket () {
		try {
			serverInstSocket = new ServerSocket(PORTA_DI_GIOCO);
		}
		catch (IOException e) {
			System.out.println("Could not listen on port " + PORTA_DI_GIOCO);
			System.exit(-1);
		}
		while (isServerRunning()) {
			try {
				servLogica = new Logica();
				Thread threadedLogica = new Thread(servLogica);
				threadedLogica.start();
				socketAdaptor = new SocketAdaptor(servLogica);
				System.out.println("Server started successfully, creating threads on need..");
				ClientWorker clientWorker = new ClientWorker(serverInstSocket.accept());
				Thread threadedClientWorker = new Thread(clientWorker);
				threadedClientWorker.start();
			}
			catch (IOException e) {
				System.out.println("Accept failed on " + PORTA_DI_GIOCO);
				System.exit(-1);
			}
		}
	}
	
	protected boolean isServerRunning() {
		return serverIsRunning;
	}
}