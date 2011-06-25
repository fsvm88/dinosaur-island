package dinolib.CommunicationObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import dinolib.Logica;
import dinolib.Exceptions.InvalidTokenException;

public class ListaGiocatori implements Serializable {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -1278722880250044464L;
	/**
	 * Contiene l'ArrayList con i nomi dei giocatori.
	 * @uml.property name="nameList"
	 */
	private ArrayList<String> nameList = null;
	/**
	 * Crea un'istanza tramite il Player Manager.
	 * @param pMan Il Player Manager corrente.
	 * @throws InvalidTokenException 
	 */
	public ListaGiocatori(Logica newLogica) {
		nameList = new ArrayList<String>();
		if (newLogica.getRRSched().hasQueuedTasks()) {
			Iterator<String> itInGameTokens = newLogica.getRRSched().iterator();
			while (itInGameTokens.hasNext()) {
				try { nameList.add(newLogica.getCMan().getName(itInGameTokens.next())); }
				catch (InvalidTokenException e) { System.out.println("[ListaGiocatori] Incontrata InvalidTokenException dove non dovrei avere eccezioni!!!"); }
			}
		}
	}
	/**
	 * Ritorna un iteratore sui nomi dei giocatori.
	 * @return Iteratore sui nomi dei giocatori.
	 */
	public Iterator<String> iterator() { return nameList.iterator(); }
}
