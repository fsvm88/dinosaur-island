package dinolib;


public class CellaConDinosauro extends Cella {

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
		return idDelDinosauro;
	}

	/**
	 * Setter of the property <tt>idDelDinosauro</tt>
	 * @param idDelDinosauro  The idDelDinosauro to set.
	 * @uml.property  name="idDelDinosauro"
	 */
	public void setIdDelDinosauro(String idDelDinosauro) {
		this.idDelDinosauro = idDelDinosauro;
	}

	/**
	 * Contiene un riferimento al valore della cella sottostante il dinosauro (velocizza operazioni di lookup e movimento).
	 * @uml.property  name="cellaSenzaDinosauro"
	 */
	private Cella cellaSenzaDinosauro;

	/**
	 * Getter of the property <tt>cellaSenzaDinosauro</tt>
	 * @return  Returns the cellaSenzaDinosauro.
	 * @uml.property  name="cellaSenzaDinosauro"
	 */
	public Cella getCellaSenzaDinosauro() {
		return cellaSenzaDinosauro;
	}

	/**
	 * Setter of the property <tt>cellaSenzaDinosauro</tt>
	 * @param cellaSenzaDinosauro  The cellaSenzaDinosauro to set.
	 * @uml.property  name="cellaSenzaDinosauro"
	 */
	public void setCellaSenzaDinosauro(Cella cellaSenzaDinosauro) {
		this.cellaSenzaDinosauro = cellaSenzaDinosauro;
	}

}
