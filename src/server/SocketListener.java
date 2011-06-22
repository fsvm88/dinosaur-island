package server;

import java.io.IOException;
import java.net.ServerSocket;

import dinolib.Logica;
import dinolib.SocketAdapter;

/**
 * @author fabio
 */
/**
 * Classe che gestisce le connessioni via socket.
 */
public class SocketListener implements Runnable {
	/**
	 * Definisce staticamente la porta di gioco.
	 * @uml.property  name="PORTA_DI_GIOCO"
	 */
	private final int PORTA_DI_GIOCO = 32845;
	/**
	 * Definisce il riferimento al socket del server.
	 * @uml.property  name="serverInstSocket"
	 */
	private ServerSocket serverSocket = null;
	/**
	 * Definisce il riferimento all'istanza di Logica.
	 * @uml.property name="servLogica"
	 */
	private Logica servLogica = null;
	/**
	 * Definisce il riferimento al SocketAdapter.
	 * @uml.property name="sockAdap"
	 */
	protected SocketAdapter socketAdapter = null;

	/**
	 * Variabile che dice se il SocketListener sta ancora funzionando.
	 * @uml.property name="socketListenerRunning"
	 */
	private boolean socketListenerRunning = false;

	/**
	 * Inizializza il socket.
	 */
	public SocketListener(Logica inLogica) {
		servLogica = inLogica;
		try {
			System.out.println("[SocketListener] Trying to instantiate a new ServerSocket...");
			serverSocket = new ServerSocket(PORTA_DI_GIOCO);
			System.out.println("[SocketListener] ServerSocket instantiated.");
		}
		catch (IOException e) {
			System.out.println("[SocketListener] Could not listen on port " + PORTA_DI_GIOCO);
			System.exit(-1);
		}
		System.out.println("[SocketListener] Instantiating the Socket Adapter for ClientWorkers...");
		socketAdapter = new SocketAdapter(servLogica);
		System.out.println("[SocketListener] Socket Adapter instantiated.");
		socketListenerRunning = true;
	}

	@Override
	public void run() {
		System.out.println("[SocketListener] Now listening for incoming connections...");
		while (isSocketListenerRunning()) {
			try {
				ClientWorker clientWorker = new ClientWorker(serverSocket.accept(), socketAdapter);
				System.out.println("[SocketListener] Received new connection, creating thread for it...");
				Thread threadedClientWorker = new Thread(clientWorker);
				System.out.println("[SocketListener] Thread created, starting it...");
				threadedClientWorker.start();
				System.out.println("[SocketListener] Thread started.");
			}
			catch (IOException e) { System.out.println("[SocketListener] Accept failed on " + PORTA_DI_GIOCO); }
		}
		System.out.println("[SocketListener] Shutting down...");
	}

	public boolean isSocketListenerRunning() { return socketListenerRunning; }
	public void shutdownSocketListener() { socketListenerRunning = false; }
	@Override
	public void finalize() {
		System.out.println("[SocketListener] Closing socket...");
		try { serverSocket.close(); System.out.println("[SocketListener] Socket closed, thread shutdown complete."); }
		catch (IOException e) { System.out.println("[SocketListener] IOException while closing the socket"); }
	}
}
