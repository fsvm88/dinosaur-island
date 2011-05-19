package dinolib;


public class Cella extends Mappa {
	protected Cella() { }

	/**
	 * Override del metodo di default toString. Dato che viene ereditato dalle sottoclassi lo definisco solo qui.
	 */
	public String toString() {
		return this.getClass().getSimpleName().toLowerCase();
	}
}
