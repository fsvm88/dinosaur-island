package dinolib.Razza;

import dinolib.Mappa.Coord;


public class Erbivoro extends Dinosauro {
	/**
	 * @uml.property  name="SPOSTAMENTO_MAX" readOnly="true"
	 */
	private static final int spostamento_MAX = 2;
	/**
	 * @uml.property  name="MOLTIPLICATORE_FORZA" readOnly="true"
	 */
	private static final int moltiplicatore_FORZA = 1;
		
	/**
	 * Implementa il costruttore pubblico per la sottoclasse Erbivoro.
	 */
	public Erbivoro(Coord myCoords){
		super(myCoords, spostamento_MAX, moltiplicatore_FORZA);
	}
}