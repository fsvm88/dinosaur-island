package dinolib;

import java.util.Iterator;

import dinolib.Specie;

class Giocatore {
	/**
	 * Contiene il nome del giocatore corrente.
	 * @uml.property  name="nome"
	 */
	private String nome = null;

	/**
	 * Contiene il numero massimo di dinosauri per specie. È una costante di gioco.
	 * @uml.property name="numero_MAX_DINOSAURI"
	 */
	private final int numero_MAX_DINOSAURI = 5;
	
	/**
	 * Getter of the property <tt>nome</tt>
	 * @return  Returns the nome.
	 * @uml.property  name="nome"
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Contiene la password dell'utente.
	 * @uml.property  name="password"
	 */
	private String password = null;

	/**
	 * Validate the password and return a boolean value.
	 * @param passwordToMatch
	 */
	protected boolean passwordIsValid(String suppliedPassword) {
		if (password.equals(suppliedPassword)) return true;
		else return false;
	}
	
	/**
	 * Fa riferimento a un oggetto Specie, viene inizializzato a zero fino a che non viene costruita la specie tramite l'implementazione in server.
	 * @uml.property  name="specieDiDinosauri"
	 */
	private Specie specie;

	/**
	 * Restituisce il numero di dinosauri presenti nella specie.
	 * @return
	 */
	public int getNumeroDinosauri() {
		return specie.getNumeroDinosauri();
	}

	/**
	 * Restituisce un dinosauro presente nella specie.
	 * @param idDinosauroCercato
	 * @return
	 */
	protected Dinosauro getDinosauro(String idDinosauroCercato) {
		return specie.getDinosauro(idDinosauroCercato);
	}

	/**
	 * Restituisce il nome della razza dei dinosauri.
	 * @return
	 */
	public String getNomeRazza() {
		return specie.getNomeRazza();
	}

	/**
	 * Restituisce un iteratore sui dinosauri nella specie, è un wrapper per la classe Specie.
	 */
	public Iterator<Dinosauro> getItDinosauri() {
		return specie.getItDinosauri();
	}


	/**
	 * Costruttore pubblico per la classe giocatore. Richiede solo nome utente e password.
	 */
	protected Giocatore(String nome, String password) {
		this.nome = nome;
		this.password = password;
	}
	
	/**
	 * Aggiunge un nuovo dinosauro alla razza.
	 * Richiede come parametro un nuovo oggetto di tipo di dinosauro e l'ID del dinosauro.
	 */
	protected void aggiungiDinosauro(Dinosauro nuovoDinosauro, String nuovoIdDinosauro) {
		specie.aggiungiDinosauro(nuovoDinosauro, nuovoIdDinosauro);
	}
	
	/**
	 * Crea la razza di dinosauri e assegna un tipo specie.
	 * Richiede il nuovo nome della razza come parametro.
	 * Modifica il campo privato specieDiDinosauri.
	 * @param nuovoNomeRazza
	 * @param nuovoDinosauro
	 */
	protected void creaNuovaRazza(String nuovoNomeRazza, Dinosauro nuovoDinosauro) {
		specie = new Specie(nuovoNomeRazza, nuovoDinosauro);
	}
	
	/**
	 * Restituisce una enumerazione sugli id dei dinosauri.
	 */
	public Iterator<String> getItIdDinosauri() {
		return specie.getItIdDinosauri();
	}

	/**
	 * Helper booleano per verificare l'esistenza di un dinosauro con un dato ID.
	 * Ritorna true se esiste, false altrimenti.
	 * @param idDinosauro
	 * @return
	 */
	protected boolean existsDinosauro(String idDinosauro) {
		if (specie.existsDinosauroWithId(idDinosauro)) return true;
		else return false;
	}
	
	public String getTipoRazza () {
		return specie.getTipoRazza();
	}
	
	/**
	 * Variabile per gestire il login degli utenti.
	 * @uml.property name="logged"
	 */
	private boolean iAmLogged = false;
	/**
	 * Variabile per gestire il fatto che l'utente è in partita.
	 * @uml.property name="inGame"
	 */
	private boolean iAmInGame = false;
	
	/* Due helper per impostare lo stato del login dell'utente. */
	/**
	 * Imposta logged a true, l'utente è loggato.
	 */
	protected void logged() {
		iAmLogged = true;
	}
	
	/**
	 * Imposta logged a false, l'utente non è loggato.
	 */
	protected void notLogged() {
		iAmLogged = false;
	}
	
	/**
	 * Imposta inGame a true, l'utente è in partita.
	 */
	public void inGame() {
		iAmInGame = true;
	}
	
	/**
	 * Imposta inGame a false, l'utente non è in partita.
	 */
	protected void notInGame() {
		iAmInGame = false;
	}
	
	/**
	 * Helper per verificare se l'utente sta giocando.
	 */
	public boolean isInGame() {
		return iAmInGame;
	}
	
	/**
	 * Helper per verificare se l'utente è loggato.
	 * @return
	 */
	public boolean isLogged() {
		return iAmLogged;
	}
	
	/**
	 * Helper per verificare che l'utente abbia una razza di dinosauri.
	 * @return
	 */
	protected boolean hasRazza() {
		if ((getNomeRazza() != null) &&
				(getNumeroDinosauri() > 0)) return true;
		else return false;
	}
	
	/**
	 * Helper per verificare se la specie ha il numero massimo di dinosauri.
	 * @return
	 */
	protected boolean specieHaNumeroMassimo() {
		if (getNumeroDinosauri() >= numero_MAX_DINOSAURI) return true;
		else return false;
	}
	
	/**
	 * Getter per il punteggio della specie.
	 */
	public int getPunteggio() {
		return specie.getPunteggio();
	}
	
	/**
	 * Helper per sapere se la specie è estinta.
	 */
	public boolean isSpecieEstinta() {
		if (specie.isEstinta()) return true;
		else return false;
	}
}