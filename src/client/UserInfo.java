package client;

public class UserInfo {
	/**
	 * Contiene il nome dell'utente.
	 * @uml.property name="nomeUtente"
	 */
	private String nomeUtente = null;
	/**
	 * Contiene la password dell'utente
	 * @uml.property name="pwdUtente"
	 */
	private String pwdUtente = null;
	/**
	 * Contiene il token corrente dell'utente.
	 * @uml.property name="curToken"
	 */
	private String curToken = null;
	/**
	 * Variabile che dice se l'utente e' loggato.
	 * @uml.property name="isLogged"
	 */
	private boolean isLogged = false;
	/**
	 * Variabile che dice se l'utente e' in partita.
	 * @uml.property name="isInGame"
	 */
	private boolean isInGame = false;
	
	/**
	 * Imposta il nome dell'utente.
	 * @param newNomeUtente Il nome dell'utente
	 */
	void setNome(String newNomeUtente) { this.nomeUtente = newNomeUtente; }
	/**
	 * Imposta la password dell'utente.
	 * @param newNomeUtente La password dell'utente
	 */
	void setPwd(String newPwdUtente) { this.pwdUtente = newPwdUtente; }
	/**
	 * Imposta il token dell'utente.
	 * @param newNomeUtente Il token dell'utente
	 */
	void setToken(String newToken) { this.curToken = newToken; }
	/**
	 * Restituisce il nome dell'utente.
	 * @return Il nome dell'utente.
	 */
	String getNome() { return this.nomeUtente; }
	/**
	 * Restituisce la password dell'utente.
	 * @return La password dell'utente.
	 */
	String getPwd() {return this.pwdUtente; }
	/**
	 * Restituisce il token dell'utente.
	 * @return Il token dell'utente.
	 */
	String getToken() { return this.curToken; }
	/**
	 * Dice se l'utente e' loggato.
	 * @return True se e' loggato, false altrimenti.
	 */
	boolean isLogged() { return isLogged; }
	/**
	 * Dice se l'utente e' in partita.
	 * @return True se l'utente e' in partita, false altrimenti.
	 */
	boolean isInGame() { return isInGame; }
	/**
	 * Cambia lo stato della variabile che dice se l'utente e' loggato. 
	 */
	void toggleLogged() { this.isLogged = !isLogged; }
	/**
	 * Cambia lo stato della variabile che dice se l'utente e' in partita. 
	 */
	void toggleInGame() { this.isInGame = !isInGame; }
}