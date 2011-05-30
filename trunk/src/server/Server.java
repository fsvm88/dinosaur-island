package server;

import java.io.IOException;
import java.net.ServerSocket;

import dinolib.Logica;
import dinolib.SocketAdaptor;

public class Server {
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
	protected Logica logica = new Logica();
	/**
	 * Dichiara l'oggetto attraverso cui passano le comunicazioni su socket.
	 */
	protected SocketAdaptor socketAdaptor = new SocketAdaptor(logica);
	
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
			try {
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
}