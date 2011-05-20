package dinolib;

import java.util.Enumeration;
import java.util.Iterator;

import dinolib.Specie;

public class Giocatore {
	/**
	 * Contiene il nome del giocatore corrente.
	 * @uml.property  name="nome"
	 */
	private String nome = null;

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
	 * Contiene il token univoco del giocatore per poter comunicare tra server e client.
	 * @uml.property name="tokenUnivoco"
	 */
	private String tokenUnivoco = null;

	/**
	 * Restituisce il valore corrente del token.
	 * @return
	 */
	public String getTokenUnivoco() {
		return tokenUnivoco;
	}

	/**
	 * Imposta il nuovo valore del token.
	 * @param tokenUnivoco
	 */
	public void setTokenUnivoco(String tokenUnivoco) {
		this.tokenUnivoco = tokenUnivoco;
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
	 * Genera un token univoco e lo assegna al dinosauro in questione.
	 * Richiede come parametro un nuovo oggetto di tipo di dinosauro.
	 */
	public void aggiungiDinosauroAllaRazza(Dinosauro nuovoDinosauro) {
		specieDiDinosauri.aggiungiDinosauroAllaSpecie(nuovoDinosauro);
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
	public Enumeration<String> getEnumerazioneDegliIdDeiDinosauri() {
		return specieDiDinosauri.getEnumerazioneDegliIdDeiDinosauri();
	}
}