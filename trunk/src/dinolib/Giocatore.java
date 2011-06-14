package dinolib;

import dinolib.Razza.*;
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
	private Razza razzaDelGiocatore = null;
	/**
	 * Istanzia il riferimento al punteggio del giocatore.
	 */
	private Punteggio punteggio = null;

	/* Costruttore */
	/**
	 * Costruttore pubblico per la classe giocatore. Richiede solo nome utente e password.
	 */
	protected Giocatore(String nome, String password) { // Testato
		this.nome = nome;
		this.password = password;
		this.punteggio = new Punteggio();
	}

	/* Tutti i getter */
	public Punteggio getPunteggio() { return punteggio; } // Testato
	public Razza getRazza() { return razzaDelGiocatore; } // Testato
	/**
	 * @return
	 * @uml.property  name="nome"
	 */
	public String getNome() { return nome; } // Testato

	/* Tutti i setter */
	/**
	 * Invoca l'aggiornamento su tutte le variabili aggiornabili del giocatore.
	 */
	protected void aggiorna() { // Testato - fallisce
		if (hasRazza()) {
			getRazza().aggiornaRazza();
		}
		getPunteggio().updatePunteggio(getRazza().getNome(), getRazza().getPunteggio());
	}

	/* Funzioni miscellanee */
	/**
	 * Validate the password and return a boolean value.
	 * @param passwordToMatch
	 */
	protected boolean passwordIsValid(String suppliedPassword) { // Testato
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
	protected void creaNuovaRazza(String nuovoNomeRazza, Character nuovoTipoRazza) { // Testato
		razzaDelGiocatore = new Razza(nuovoNomeRazza, nuovoTipoRazza);
	}
	/**
	 * Helper per verificare che l'utente abbia una razza di dinosauri.
	 * @return
	 */
	protected boolean hasRazza() { // Testato
		if ((getRazza() != null) &&
				!getRazza().isEmpty()) return true;
		else return false;
	}
}