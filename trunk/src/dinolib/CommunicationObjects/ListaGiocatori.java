package dinolib.CommunicationObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import dinolib.Giocatore;
import dinolib.PlayerManager;

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
	 */
	public ListaGiocatori(PlayerManager pMan) {
		if (pMan != null) {
			nameList = new ArrayList<String>();
			Iterator<Giocatore> itPMan = pMan.iterator();
			while (itPMan.hasNext()) {
				nameList.add(itPMan.next().getNome());
			}
		}
		else throw new NullPointerException();
	}
	/**
	 * Ritorna un iteratore sui nomi dei giocatori.
	 * @return Iteratore sui nomi dei giocatori.
	 */
	public Iterator<String> iterator() { return nameList.iterator(); }
}
