package dinolib;

import dinolib.Razza;

/**
 * @author  fabio
 */
public class Giocatore {
	/* Tutte le variabili statiche/definitive e non modificabili */
	
	/* Tutte le variabili istanziabili */
	/**
	 * Contiene il nome del giocatore corrente.
	 * @uml.property  name="nome"
	 */
	private String nome = null;
	/**
	 * Contiene la password dell'utente.
	 * @uml.property  name="password"
	 */
	private String password = null;
	/**
	 * Fa riferimento a un oggetto Specie, viene inizializzato a zero fino a che non viene costruita la specie tramite l'implementazione in server.
	 * @uml.property  name="specieDiDinosauri"
	 * @uml.associationEnd  
	 */
	private Razza razzaDelGiocatore;
	/**
	 * Variabile per gestire il login degli utenti.
	 * @uml.property  name="logged"
	 */
	private boolean iAmLogged = false;
	/**
	 * Variabile per gestire il fatto che l'utente è in partita.
	 * @uml.property  name="inGame"
	 */
	private boolean iAmInGame = false;

	/* Costruttore */
	/**
	 * Costruttore pubblico per la classe giocatore. Richiede solo nome utente e password.
	 */
	protected Giocatore(String nome, String password) {
		this.nome = nome;
		this.password = password;
	}
	
	/* Tutti i getter */
	public boolean isInGame() { return iAmInGame; }
	public boolean isLogged() { return iAmLogged; }
	public int getPunteggio() { return razzaDelGiocatore.getPunteggio(); }
	public Razza getRazza() { return razzaDelGiocatore; }
	/**
	 * @return
	 * @uml.property  name="nome"
	 */
	public String getNome() { return nome; }
	
	/* Tutti i setter */
	protected void logged() { iAmLogged = true; }
	protected void notLogged() { iAmLogged = false; }
	public void inGame() { iAmInGame = true; }
	protected void notInGame() { iAmInGame = false; }
	public void aggiornaGiocatoreSuTurno() { razzaDelGiocatore.aggiornaRazza(); }
	
	/* Funzioni miscellanee */
	/**
	 * Validate the password and return a boolean value.
	 * @param passwordToMatch
	 */
	protected boolean passwordIsValid(String suppliedPassword) {
		if (password.equals(suppliedPassword)) return true;
		else return false;
	}
	
	/**
	 * Crea la razza di dinosauri e assegna un tipo specie.
	 * Richiede il nuovo nome della razza come parametro.
	 * Modifica il campo privato specieDiDinosauri.
	 * @param nuovoNomeRazza
	 * @param nuovoDinosauro
	 */
	protected void creaNuovaRazza(String nuovoNomeRazza, Dinosauro nuovoDinosauro) {
		razzaDelGiocatore = new Razza(nuovoNomeRazza, nuovoDinosauro);
	}
	/**
	 * Helper per verificare che l'utente abbia una razza di dinosauri.
	 * @return
	 */
	protected boolean hasRazza() {
		if (getRazza() != null) return true;
		else return false;
	}
}