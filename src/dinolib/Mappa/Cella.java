package dinolib.Mappa;

import java.util.HashSet;
/**
 * @author fabio
 */
/**
 * Classe che gestisce una singola cella.
 * E' astratta perche' non e' istanziabile come classe a se' stante ma solo tramite le sottoclassi.
 * E' l'oggetto che compone tutta la mappa.
 */
public abstract class Cella {
	/**
	 * Istanzia una cella comune (non cella con dinosauro).
	 */
	protected Cella() { }
	/**
	 * Istanzia una cella con dinosauro, che ha bisogno due parametri extra.
	 * @param idDelDinosauro L'id del dinosauro che occupa la cella.
	 * @param cellaSuCuiSiTrova La cella su cui il dinosauro si trova.
	 */
	protected Cella(String idDelDinosauro, Cella cellaSuCuiSiTrova) {
		this.idDelDinosauro = idDelDinosauro;
		this.cellaSuCuiSiTrova = cellaSuCuiSiTrova;
		visitedFrom = new HashSet<String>();
	}
	
	/**
	 * Override del metodo di default toString.
	 * Dato che viene ereditato dalle sottoclassi lo definisco solo qui.
	 */
	public String toString() { return this.getClass().getSimpleName(); }
	/**
	 * Contiene l'id del dinosauro che occupa la cella.
	 * @uml.property  name="idDelDinosauro"
	 */
	protected String idDelDinosauro = null;

	/**
	 * Restituisce l'id del dinosauro occupante.
	 * Ne viene fatto l'override nella sottoclasse.
	 * Se l'istanza dell'oggetto non e' una cella con dinosauro di default ritorna null.
	 * @return L'id del dinosauro occupante.
	 * @uml.property  name="idDelDinosauro"
	 */
	public String getIdDelDinosauro() { return null; }

	/**
	 * Contiene un riferimento al valore della cella sottostante il dinosauro (velocizza operazioni di lookup e movimento).
	 * @uml.property  name="cellaSuCuiSiTrova"
	 */
	protected Cella cellaSuCuiSiTrova;

	/**
	 * Restituisce la cella occupata dal dinosauro.
	 * Ne viene fatto l'override nella sottoclasse.
	 * Se l'istanza dell'oggetto non e' una cella con dinosauro di default ritorna null.
	 * @uml.property  name="cellaSuCuiSiTrova"
	 */
	public Cella getCellaSuCuiSiTrova() { return null; }
	/**
	 * Contiene il valore iniziale dell'energia massima ricavabile dalla cella.
	 * @uml.property  name="valoreIniziale"
	 */
	protected int valoreIniziale = 0;
	/**
	 * Contiene il valore attuale della massima energia ricavabile dalla cella.
	 * @uml.property  name="valoreAttuale"
	 */
	protected int valoreAttuale = 0;
	/**
	 * Restituisce il valore attuale della cella.
	 * Ne viene fatto l'override nella sottoclasse.
	 * Se l'istanza dell'oggetto non e' una cella carogna o vegetazione, di default ritorna 0.
	 */
	public int getValoreAttuale() { return 0; }
	/**
	 * Aggiorna il valore della cella quando viene mangiata energia da questa.
	 * @param valoreMangiato Il valore in energia che viene mangiato dalla cella.
	 */
	public void mangia(int valoreMangiato) {
		int diffTemp = valoreAttuale - valoreMangiato;
		if (diffTemp <= 0) {
			valoreAttuale = 0;
		}
		else if (diffTemp > 0) {
			valoreAttuale = diffTemp;
		}
	}
	/**
	 * Aggiorna il valore della cella sul cambio di turno.
	 * Ne viene fatto l'override nella sottoclasse.
	 * Di default non fa nulla, ritorna il controllo al chiamante senza eseguire operazioni.
	 */
	public void aggiorna() { return; }
	/**
	 * Contiene la lista dei nomi dei giocatori che hanno visitato la cella.
	 */
	private HashSet<String> visitedFrom;
	/**
	 * Controlla se la cella e' stata visitata dall'utente.
	 * @param userName Il nome del giocatore che non so se ha visitato la cella.
	 * @return True se il nome del giocatore passato come argomento e' gia' passato sulla cella, false se il giocatore e' passato.
	 */
	public boolean isUserPassed(String nomeGiocatore) {
		if (visitedFrom == null) { return false; }
		if (visitedFrom.contains(nomeGiocatore)) { return true; } else { return false; }
	}
	/**
	 * Aggiunge l'utente alla lista che dice se e' passato per questa cella.
	 * @param userName Il nome del giocatore che ha visitato la cella.
	 */
	public void userIsPassed(String userName) {
		if (visitedFrom == null) { visitedFrom = new HashSet<String>(); }
		visitedFrom.add(userName);
	}
}
