package dinolib;

import java.util.Hashtable;
import java.util.Iterator;

public class Punteggio {
	/**
	 * Contiene la lista di punteggio del giocatore corrente.
	 * @uml.property name="listaPunteggio"
	 */
	private Hashtable<String, Integer> listaPunteggio = null;
	
	/**
	 * Aggiorna un punteggio, sia che sia già esistente sia che sia nuovo.
	 * In questo  modo la classifica è sempre consistente anche con la razza attualmente in gioco.
	 * @param nomeRazza
	 * @param punteggioToUpdate
	 * @return
	 */
	public boolean updatePunteggio(String nomeRazza, int punteggioToUpdate) { 
		if ((nomeRazza != null) && (punteggioToUpdate>0)) {
			listaPunteggio.put(nomeRazza, new Integer(punteggioToUpdate));
			return true;
		}
		else return false;
	}
	/**
	 * Ritorna un iteratore sui nomi delle razze che sono in classifica.
	 * @return
	 */
	public Iterator<String> getIteratorOnRaces() {
		return listaPunteggio.keySet().iterator();
	}
	/**
	 * Ritorna un valore di un punteggio a partire dal nome della razza.
	 * @param nomeRazza
	 * @return
	 */
	public int getPunteggioDaNome(String nomeRazza) {
		return listaPunteggio.get(nomeRazza).intValue();
	}
}