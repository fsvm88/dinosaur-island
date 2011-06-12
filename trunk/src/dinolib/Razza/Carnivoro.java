package dinolib.Razza;

import dinolib.Mappa.Coord;

public class Carnivoro extends Dinosauro {
	/**
	 * @uml.property  name="SPOSTAMENTO_MAX" readOnly="true"
	 */
	private static final int spostamento_MAX = 3;
	/**
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
