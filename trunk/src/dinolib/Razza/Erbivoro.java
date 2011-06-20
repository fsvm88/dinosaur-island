package dinolib.Razza;

import dinolib.Mappa.Coord;


public class Erbivoro extends Dinosauro {
	/**
	 * Definisce in modo statico e definitivo lo spostamento massimo per un dinosauro di questo tipo.
	 * Viene passato al costruttore e poi non serve piu'.
	 * @uml.property  name="SPOSTAMENTO_MAX" readOnly="true"
	 */
	private static final int spostamento_MAX = 2;
	/**
	 * Definisce in modo statico e definitivo il moltiplicatore della forza per un dinosauro di questo tipo.
	 * Viene passato al costruttore e poi non serve piu'.
	 * @uml.property  name="MOLTIPLICATORE_FORZA" readOnly="true"
	 */
	private static final int moltiplicatore_FORZA = 1;
		
	/**
	 * Costruttore pubblico per un dinosauro di tipo Carnivoro.
	 * @param myCoords Le coordinate del nuovo dinosauro.
	 */
	public Erbivoro(Coord myCoords){
		super(myCoords, spostamento_MAX, moltiplicatore_FORZA);
	}
}