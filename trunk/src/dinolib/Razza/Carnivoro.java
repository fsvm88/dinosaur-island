package dinolib.Razza;

import dinolib.Mappa.Coord;

public class Carnivoro extends Dinosauro {
	/**
	 * Definisce in modo statico e definitivo lo spostamento massimo per un dinosauro di questo tipo.
	 * Viene passato al costruttore e poi non serve più.
	 * @uml.property  name="SPOSTAMENTO_MAX" readOnly="true"
	 */
	private static final int spostamento_MAX = 3;
	/**
	 * Definisce in modo statico e definitivo il moltiplicatore della forza per un dinosauro di questo tipo.
	 * Viene passato al costruttore e poi non serve più.
	 * @uml.property  name="MOLTIPLICATORE_FORZA" readOnly="true"
	 */
	private static final int moltiplicatore_FORZA = 2;
		
	/**
	 * Implementa il costruttore pubblico per il tipo di dinosauro Carnivoro.
	 */
	public Carnivoro(Coord myCoord){
		super(myCoord, spostamento_MAX, moltiplicatore_FORZA);
	}
}
