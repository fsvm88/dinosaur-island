package client.FrontendCommunication;

import client.Exceptions.GenericConnectionException;

public class ClientInterface implements ClientFrontendCommunication, Runnable {
	/**
	 * Variabile che aiuta gli utenti di ClientInterface a selezionare il metodo di connessione senza conoscere i dettagli implementativi.
	 * E' per la connessione via RMI.
	 * @uml.property name="CONN_RMI"
	 */
	public static final String CONN_RMI = "conn_rmi";
	/**
	 * Variabile che aiuta gli utenti di ClientInterface a selezionare il metodo di connessione senza conoscere i dettagli implementativi.
	 * E' per la connessione via Socket.
	 * @uml.property name="CONN_SOCKET"
	 */
	public static final String CONN_SOCKET = "conn_socket";
	/**
	 * Variabile che aiuta gli utenti di ClientInterface a selezionare il metodo di connessione senza conoscere i dettagli implementativi.
	 * E' per la connessione in locale.
	 * @uml.property name="CONN_LOCAL"
	 */
	public static final String CONN_LOCAL = "conn_local";
	/**
	 * Contiene il valore predefinito per l'hostname.
	 * @uml.property name="DEFAULT_HOSTNAME"
	 */
	private final String default_HOSTNAME = "localhost";
	/**
	 * Contiene il valore predefinito per la porta di gioco.
	 * @uml.property name="DEFAULT_PORT"
	 */
	private final int default_PORT = 32845;
	
	/**
	 * Variabile che contiene il riferimento a ciUserInfo.
	 * @uml.property name="ciUserInfo"
	 */
	private UserInfo ciUserInfo = null;
	/**
	 * Variabile che dice se ClientInterface sta funzionando.
	 * @uml.property name="isCIRunning"
	 */
	private boolean isCIRunning = false;
	/**
	 * Variabile che dice se ClientInterface si sta spegnendo.
	 * @uml.property name="isCIShuttingDown"
	 */
	private boolean isCIShuttingDown = true;
	/**
	 * Contiene l'hostname a cui connettersi.
	 * @uml.property name="hostname"
	 */
	private String hostname = null;
	/**
	 * Contiene la porta di gioco a cui collegarsi.
	 * @uml.property name="porta"
	 */
	private Integer porta = 0;
	/**
	 * Variabile che contiene il tip di connessione desiderato.
	 */
	private String selectedConnType = null;
	
	/**
	 * Crea le strutture e rende usabile ClientInterface.
	 * @param newUserInfo Le informazioni sull'utente richieste per far funzionare ClientInterface.
	 */
	public ClientInterface(UserInfo newUserInfo) {
		this.isCIShuttingDown = false;
		this.ciUserInfo = newUserInfo;
		this.isCIRunning = true;
		this.hostname = default_HOSTNAME;
		this.porta = default_PORT;
	}
	
	/**
	 * Dice se ClientInterface sta ancora funzionando.
	 * @return True se ClientInterface sta funzionando, false altrimenti.
	 */
	boolean isCIRunning() { return isCIRunning; }
	/**
	 * Dice se ClientInterface si sta spegnendo.
	 * @return True se ClientInterface si sta spegnendo, false altrimenti.
	 */
	boolean isCIShuttingDown() { return isCIShuttingDown; }
	/**
	 * Riceve il segnale di chiusura e imposta a true la variabile di spegnimento.
	 */
	void shutdownCI() { this.isCIShuttingDown = true; }
	
	@Override
	public void run() {
		while(!isCIShuttingDown()) {
			
		}
	}
	
	/**
	 * Restituisce il valore corrente dell'hostname a cui collegarsi.
	 * @return Una stringa contenente l'hostname a cui collegarsi.
	 */
	public String getHost() { return this.hostname; }
	/**
	 * Restituisce il valore corrente della porta di gioco a cui collegarsi.
	 * @return Un intero che indica la porta di gioco a cui collegarsi.
	 */
	public Integer getPort() { return this.porta; }
	/**
	 * Imposta l'hostname a cui collegarsi.
	 * @param newHost L'hostname.
	 */
	public void setHost(String newHost) { this.hostname = newHost; }
	/**
	 * Imposta la porta dell'hostname a cui collegarsi.
	 * @param newPort La porta.
	 */
	public void setPort(Integer newPort) { this.porta = newPort; }
	/**
	 * Imposta il tipo di connessione da usare.
	 * @param newConnType Il nuovo tipo di connessione desiderato.
	 */
	public void setConnType(String newConnType) { this.selectedConnType = newConnType; }

	private void checkCredentialsValidity() throws GenericConnectionException {
		if (ciUserInfo.getNome() == null) { throw new GenericConnectionException("Nessun nome utente specificato!"); }
		if (ciUserInfo.getPwd() == null) { throw new GenericConnectionException("Nessuna password specificata!"); }
	}
	
	private void checkConnTypeValidity() throws GenericConnectionException {
		if ((selectedConnType != null)) {
			if (selectedConnType.equals(CONN_LOCAL) ||
					selectedConnType.equals(CONN_RMI) ||
					selectedConnType.equals(CONN_SOCKET)) { return; }
		}
		throw new GenericConnectionException("Nessun tipo di connessione selezionato!");
	}
	
	private void connectionSetup() throws GenericConnectionException {
		checkConnTypeValidity();
		checkCredentialsValidity();
		if (selectedConnType.equals(CONN_RMI)) {
			
		}
		else if (selectedConnType.equals(CONN_RMI)) {
			
		}
		else if (selectedConnType.equals(CONN_LOCAL)) {
			
		}
	}
	
	@Override
	public boolean doLogin() throws GenericConnectionException {
		connectionSetup();
		return false;
	}

	@Override
	public boolean doLogout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object doClassifica() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object doListaGiocatori() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean doAccediPartita() {
		// TODO Auto-generated method stub
		return false;
	}
}