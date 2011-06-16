package dinolib;

import java.util.Hashtable;
import java.util.Iterator;

class Punteggio {
	/**
	 * Contiene la lista di punteggio del giocatore corrente.
	 * @uml.property name="listaPunteggio"
	 */
	private Hashtable<String, Integer> listaPunteggio = null;

	public Punteggio() { // Testato
		this.listaPunteggio = new Hashtable<String, Integer>();
	}
	/**
	 * Aggiorna un punteggio, sia che sia già esistente sia che sia nuovo.
	 * In questo  modo la classifica è sempre consistente anche con la razza attualmente in gioco.
	 * @param nomeRazza
	 * @param punteggioToUpdate
	 * @return
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
	 * Ritorna un iteratore sui nomi delle razze che sono in classifica.
	 * @return
	 */
	protected Iterator<String> iterator() { // Testato
		return listaPunteggio.keySet().iterator();
	}
	/**
	 * Ritorna un valore di un punteggio a partire dal nome della razza.
	 * @param nomeRazza
	 * @return
	 */
	protected Integer getPunteggioDaNome(String nomeRazza) { // Testato
		if (listaPunteggio.containsKey(nomeRazza)) {
			return listaPunteggio.get(nomeRazza).intValue();
		}
		else return 0;
	}
}