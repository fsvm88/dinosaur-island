package dinolib.CommunicationObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import dinolib.GameObjects.Dinosauro;

public class ListaDinosauri implements Serializable {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -1397985031791748635L;
	/**
	 * Contiene l'ArrayList<String> degli id dei dinosauri.
	 * @uml.property name="idDinosauri"
	 */
	private ArrayList<String> idDinosauri = null;
	/**
	 * Crea un'istanza e riempie la lista dei dinosauri con gli id dei dinosauri.
	 * @param itDinosauri L'iteratore sugli id dei dinosauri.
	 */
	public ListaDinosauri(Iterator<Dinosauro> itDinosauri) {
		idDinosauri = new ArrayList<String>();
		while (itDinosauri.hasNext()) {
			idDinosauri.add(itDinosauri.next().getIdDinosauro());
		}
	}
}
