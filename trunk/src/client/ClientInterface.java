package client;

public class ClientInterface implements Communication, Runnable {
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

	@Override
	public boolean doLogin() {
		// TODO Auto-generated method stub
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
}