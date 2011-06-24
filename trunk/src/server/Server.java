package server;

import dinolib.Logica;

/**
 * @author  fabio
 */
class Server {
	/**
	 * Dichiara la classe logica che gestisce tutta la logica di gioco indipendentemente dall'interfaccia di comunicazione.
	 * @uml.property  name="servLogica"
	 * @uml.associationEnd  
	 */
	protected Logica servLogica = null;
	/**
	 * Variabile che dice se il server sta funzionando.
	 * @uml.property name="serverIsRunning"
	 */
	private boolean serverIsRunning = false;
	/**
	 * Contiene il socketListener.
	 * @uml.property name="socketListener"
	 */
	SocketListener socketListener = null;
	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main (String[] args) { Server server = new Server(); }

	/**
	 * Dichiara il costruttore dell'oggetto Server che poi gestisce tutto.
	 */
	public Server () {
		System.out.println("[Server] Trying to add Shutdown Hook...");
		Runtime.getRuntime().addShutdownHook(new
				Thread() {
			public void run() {
				System.out.println("[Server] Shutting down the server with client threads...");
				serverIsRunning = false;
				System.out.println("[Server] Sending stop signal to Logica and SocketListener...");
				servLogica.shutdownLogica();
				if (socketListener != null) {
					if (socketListener.isSocketListenerRunning()) { socketListener.shutdownSocketListener(); }
				}
				try {
					while (servLogica.isLogicaRunning()) { sleep(500); }
				}
				catch (InterruptedException e) { System.out.println("[Server] InterruptedException caught while stopping Logica or SocketListener."); }
				System.out.println("[Server] Shutdown complete.");
			}});
		System.out.println("[Server] Shutdown Hook added.");
		System.out.println("[Server] Trying to instantiate Logica...");
		servLogica = new Logica();
		System.out.println("[Server] Logica istantiated, pushing Logica activity to thread...");
		Thread threadedLogica = new Thread(servLogica);
		System.out.println("[Server] Logica threaded, starting it...");
		threadedLogica.start();
		System.out.println("[Server] Logica started.");
		serverIsRunning = true;
		System.out.println("[Server] Trying to instantiate the socket listener...");
		socketListener = new SocketListener(servLogica);
		System.out.println("[Server] Socket listener instantiated, pushing socket listener activity to thread...");
		Thread threadedSocketListener = new Thread(socketListener);
		System.out.println("[Server] Socket Listener threaded, starting it...");
		threadedSocketListener.start();
		System.out.println("[Server] Socket Listener started.");
	}

	protected boolean isServerRunning() { return serverIsRunning; }
}