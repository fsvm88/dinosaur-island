package dinolib;

import java.util.Hashtable;
import java.util.Iterator;
/**
 * @author fabio
 */
/**
 * Classe che gestisce il punteggio delle razze di un giocatore.
 */
class Punteggio {
	/**
	 * Contiene la lista di punteggio del giocatore corrente.
	 * @uml.property name="listaPunteggio"
	 */
	private Hashtable<String, Integer> listaPunteggio = null;
	/**
	 * Inizializza la struttura che contiene i punteggi.
	 */
	public Punteggio() { this.listaPunteggio = new Hashtable<String, Integer>(); } // Testato
	/**
	 * Aggiorna un punteggio, sia che sia già esistente sia che sia nuovo.
	 * In questo  modo la classifica è sempre consistente anche con la razza attualmente in gioco.
	 * @param nomeRazza Il nome della razza di cui aggiornare il punteggio.
	 * @param punteggioToUpdate Il punteggio della razza di cui aggiornare il punteggio.
	 * @return True se il punteggio è stato aggiornato (la collezione è stata modificata), false altrimenti
	 */
	protected boolean updatePunteggio(String nomeRazza, Integer punteggioToUpdate) {  // Testato
		if ((nomeRazza != null) &&
				(punteggioToUpdate != null) &&
				(punteggioToUpdate.intValue()>0)) {
			listaPunteggio.put(nomeRazza, punteggioToUpdate);
			return true;
		}
		else return false;
	}
	/**
	 * Restituisce un iteratore sui nomi delle razze che sono in classifica.
	 * @return Un iteratore sui nomi delle razze che sono in classifica.
	 */
	protected Iterator<String> iterator() { return listaPunteggio.keySet().iterator(); } // Testato
	/**
	 * Ritorna un valore di un punteggio a partire dal nome della razza.
	 * @param nomeRazza Il nome della razza di cui e' richiesto il punteggio.
	 * @return Il punteggio della razza richiesto.
	 */
	protected Integer getPunteggioDaNome(String nomeRazza) { // Testato
		if (listaPunteggio.containsKey(nomeRazza)) {
			return listaPunteggio.get(nomeRazza).intValue();
		}
		else return 0;
	}
}