package dinolib;


public class Cella extends Mappa {
	protected Cella() { }

	protected Cella(String idDelDinosauro, Cella cellaSuCuiSiTrova) {
		this.idDelDinosauro = idDelDinosauro;
		this.cellaSuCuiSiTrova = cellaSuCuiSiTrova;
	}
	/**
	 * Override del metodo di default toString. Dato che viene ereditato dalle sottoclassi lo definisco solo qui.
	 */
	public String toString() {
		return this.getClass().getSimpleName().toLowerCase();
	}
	
	/**
	 * Contiene l'id del dinosauro che occupa la cella.
	 * @uml.property  name="idDelDinosauro"
	 */
	private String idDelDinosauro;

	/**
	 * Getter of the property <tt>idDelDinosauro</tt>
	 * @return  Returns the idDelDinosauro.
	 * @uml.property  name="idDelDinosauro"
	 */
	public String getIdDelDinosauro() {
		if (this.toString().equals("CellaConDinosauro")) {
			return idDelDinosauro;
		}
		else return null;
	}

	/**
	 * Contiene un riferimento al valore della cella sottostante il dinosauro (velocizza operazioni di lookup e movimento).
	 * @uml.property  name="cellaSuCuiSiTrova"
	 */
	private Cella cellaSuCuiSiTrova;

	/**
	 * Getter of the property <tt>cellaSuCuiSiTrova</tt>
	 * @return  Returns the cellaSuCuiSiTrova.
	 * @uml.property  name="cellaSuCuiSiTrova"
	 */
	public Cella getCellaSuCuiSiTrova() {
		if (this.toString().equals("CellaConDinosauro")) {
			return cellaSuCuiSiTrova;
		}
		else return null;
	}
}
