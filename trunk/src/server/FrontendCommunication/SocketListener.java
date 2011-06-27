package server.FrontendCommunication;

import java.io.IOException;
import java.net.ServerSocket;

import server.BackendCommunication.SocketAdapter;

import dinolib.Logica;
import dinolib.ConfigurationOpts;

/**
 * @author fabio
 */
/**
 * Classe che gestisce le connessioni via socket.
 */
public class SocketListener implements Runnable {
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
	 * @uml.property name="socketAdapter"
	 */
	private SocketAdapter socketAdapter = null;

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
			serverSocket = new ServerSocket(ConfigurationOpts.PORTA_DI_GIOCO);
			System.out.println("[SocketListener] ServerSocket instantiated.");
		}
		catch (IOException e) {
			System.out.println("[SocketListener] Could not listen on port " + ConfigurationOpts.PORTA_DI_GIOCO);
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
			catch (IOException e) { System.out.println("[SocketListener] Accept failed on " + ConfigurationOpts.PORTA_DI_GIOCO); }
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
