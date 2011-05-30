package dinolib;

import java.util.Iterator;

import dinolib.Specie;

public class Giocatore {
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
	public boolean passwordIsValid(String suppliedPassword) {
		if (password.equals(suppliedPassword)) return true;
		else return false;
	}
	
	/**
	 * Fa riferimento a un oggetto Specie, viene inizializzato a zero fino a che non viene costruita la specie tramite l'implementazione in server.
	 * @uml.property  name="specieDiDinosauri"
	 */
	private Specie specieDiDinosauri;

	/**
	 * Restituisce il numero di dinosauri presenti nella specie.
	 * @return
	 */
	public int getNumeroDinosauri() {
		return specieDiDinosauri.getNumeroDinosauri();
	}

	/**
	 * Restituisce un dinosauro presente nella specie.
	 * @param idDinosauroCercato
	 * @return
	 */
	public Dinosauro getDinosauro(String idDinosauroCercato) {
		return specieDiDinosauri.getDinosauro(idDinosauroCercato);
	}

	/**
	 * Restituisce il nome della razza dei dinosauri.
	 * @return
	 */
	public String getNomeRazzaDinosauri() {
		return specieDiDinosauri.getNomeRazza();
	}

	/**
	 * Restituisce un iteratore sui dinosauri nella specie, è un wrapper per la classe Specie.
	 */
	public Iterator<Dinosauro> getIteratoreSuiDinosauriNellaSpecie() {
		return specieDiDinosauri.getIteratoreSuiDinosauri();
	}


	/**
	 * Costruttore pubblico per la classe giocatore. Richiede solo nome utente e password.
	 */
	public Giocatore(String nome, String password) {
		this.nome = nome;
		this.password = password;
	}
	
	/**
	 * Aggiunge un nuovo dinosauro alla razza.
	 * Richiede come parametro un nuovo oggetto di tipo di dinosauro e l'ID del dinosauro.
	 */
	public void aggiungiDinosauroAllaRazza(Dinosauro nuovoDinosauro, String nuovoIdDinosauro) {
		specieDiDinosauri.aggiungiDinosauroAllaSpecie(nuovoDinosauro, nuovoIdDinosauro);
	}
	
	/**
	 * Crea la razza di dinosauri e assegna un tipo specie.
	 * Richiede il nuovo nome della razza come parametro.
	 * Modifica il campo privato specieDiDinosauri.
	 * @param nuovoNomeRazza
	 * @param nuovoDinosauro
	 */
	public void creaNuovaRazzaDiDinosauri(String nuovoNomeRazza, Dinosauro nuovoDinosauro) {
		specieDiDinosauri = new Specie(nuovoNomeRazza, nuovoDinosauro);
	}
	
	/**
	 * Restituisce una enumerazione sugli id dei dinosauri.
	 */
	public Iterator<String> getItIdDinosauri() {
		return specieDiDinosauri.getItIdDinosauri();
	}

	/**
	 * Helper booleano per verificare l'esistenza di un dinosauro con un dato ID.
	 * Ritorna true se esiste, false altrimenti.
	 * @param idDinosauro
	 * @return
	 */
	public boolean existsDinosauro(String idDinosauro) {
		if (specieDiDinosauri.validateIdExistance(idDinosauro)) return true;
		else return false;
	}
	
	public String getTipoRazza () {
		return specieDiDinosauri.getTipoRazza();
	}
	
	/**
	 * Variabile per gestire il login degli utenti.
	 * @uml.property name="logged"
	 */
	private boolean logged = false;
	/**
	 * Variabile per gestire il fatto che l'utente è in partita.
	 * @uml.property name="inGame"
	 */
	private boolean inGame = false;
	
	/* Due helper per impostare lo stato del login dell'utente. */
	/**
	 * Imposta logged a true, l'utente è loggato.
	 */
	public void iAmLogged() {
		logged = true;
	}
	
	/**
	 * Imposta logged a false, l'utente non è loggato.
	 */
	public void iAmNotLogged() {
		logged = false;
	}
	
	/**
	 * Imposta inGame a true, l'utente è in partita.
	 */
	public void iAmInGame() {
		inGame = true;
	}
	
	/**
	 * Imposta inGame a false, l'utente non è in partita.
	 */
	public void iAmNotInGame() {
		inGame = false;
	}
	
	/**
	 * Helper per verificare se l'utente sta giocando.
	 */
	public boolean isInGame() {
		return inGame;
	}
	
	/**
	 * Helper per verificare se l'utente è loggato.
	 * @return
	 */
	public boolean isLogged() {
		return logged;
	}
	
	/**
	 * Helper per verificare che l'utente abbia una razza di dinosauri.
	 * @return
	 */
	public boolean hasRazza() {
		if ((getNomeRazzaDinosauri() != null) &&
				(getNumeroDinosauri() > 0)) return true;
		else return false;
	}
	
	/**
	 * Helper per verificare se la specie ha il numero massimo di dinosauri.
	 * @return
	 */
	public boolean specieHaNumeroMassimo() {
		if (getNumeroDinosauri() >= numero_MAX_DINOSAURI) return true;
		else return false;
	}
	
	/**
	 * Getter per il punteggio della specie.
	 */
	public int getPunteggio() {
		return specieDiDinosauri.getPunteggio();
	}
	
	/**
	 * Helper per sapere se la specie è estinta.
	 */
	public boolean isSpecieEstinta() {
		if (specieDiDinosauri.isEstinta()) return true;
		else return false;
	}
}