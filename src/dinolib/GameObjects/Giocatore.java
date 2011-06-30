package dinolib.GameObjects;

import java.io.Serializable;

/**
 * @author  fabio
 */
/**
 * Classe che gestisce il singolo giocatore.
 */
public class Giocatore implements Serializable {
	/**
	 * Generated version ID for Serializable.
	 */
	private static final long serialVersionUID = 5141915650424446123L;
	/* Tutte le variabili istanziabili */
	/**
	 * Contiene il nome del giocatore.
	 * @uml.property  name="nome"
	 */
	private String nome = null;
	/**
	 * Contiene la password dell'utente.
	 * @uml.property  name="password"
	 */
	private String password = null;
	/**
	 * Fa riferimento a un oggetto Razza.
	 * @uml.property  name="razzaDelGiocatore"
	 */
	private Razza razzaDelGiocatore = null;
	/**
	 * Istanzia il riferimento al punteggio del giocatore.
	 */
	private Punteggio punteggio = null;

	/* Costruttore */
	/**
	 * Assegna il nome e la password passate al nuovo utente, istanzia l'oggetto punteggio.
	 * @param nome Il nome del nuovo giocatore.
	 * @param password La password del nuovo giocatore.
	 */
	public Giocatore(String nome, String password) { // Testato
		this.nome = nome;
		this.password = password;
		this.punteggio = new Punteggio();
	}

	/* Tutti i getter */
	/**
	 * Restituisce l'oggetto Punteggio.
	 * @return L'oggetto Punteggio.
	 */
	public synchronized Punteggio getPunteggio() { return punteggio; } // Testato
	/**
	 * Restituisce l'oggetto Razza.
	 * @return L'oggetto Razza.
	 */
	public synchronized Razza getRazza() { return razzaDelGiocatore; } // Testato
	/**
	 * Restituisce il nome del giocatore.
	 * @return Il nome del giocatore.
	 * @uml.property  name="nome"
	 */
	public String getNome() { return nome; } // Testato

	/* Tutti i setter */
	/**
	 * Invoca l'aggiornamento su tutte le variabili aggiornabili del giocatore.
	 * Punteggio e/o razza.
	 */
	public synchronized void aggiorna() { // Testato
		if (hasRazza() && getRazza().isEmpty()) {
			getPunteggio().updatePunteggio(getRazza().getNome(), getRazza().getPunteggio());
			razzaDelGiocatore = null;
		}
		if (hasRazza() && !getRazza().isEmpty()) {
			getRazza().aggiornaRazza();
			getPunteggio().updatePunteggio(getRazza().getNome(), getRazza().getPunteggio());
		}
	}

	/* Funzioni miscellanee */
	/**
	 * Valida la password passata.
	 * @param passwordToMatch La password da controllare.
	 * @return True se la password e' valida, false se non e' valida.
	 */
	public boolean passwordIsValid(String suppliedPassword) { // Testato
		if (password.equals(suppliedPassword)) { return true; }
		else { return false; }
	}
	/**
	 * Crea la razza di dinosauri e assegna un tipo alla razza.
	 * Richiede il nuovo nome della razza e il tipo come parametri.
	 * @param nuovoNomeRazza Il nome della nuova razza.
	 * @param nuovoTipoRazza Il tipo della nuova razza.
	 */
	public synchronized void creaNuovaRazza(String nuovoNomeRazza, Character nuovoTipoRazza) { // Testato
		razzaDelGiocatore = new Razza(nuovoNomeRazza, nuovoTipoRazza.charValue());
	}
	/**
	 * Verifica che l'utente abbia una razza di dinosauri.
	 * @return True se il giocatore ha gia' una razza, false se non ce l'ha gia'.
	 */
	public boolean hasRazza() { // Testato
		if ((getRazza() == null)) { return false; }
		else { return true; }
	}
}