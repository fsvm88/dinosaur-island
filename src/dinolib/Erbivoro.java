package dinolib;


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
	public Erbivoro(int x, int y){
		super(x, y, spostamento_MAX, moltiplicatore_FORZA);
	}
}