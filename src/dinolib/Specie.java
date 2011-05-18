package dinolib;


public class Specie {

	/**
	 * Variabile che definisce il nome della specie di Dinosauri.
	 * @uml.property  name="nomeRazza"
	 */
	private String nomeRazza = new java.lang.String();

	/** 
	 * Getter of the property <tt>nomeSpecie</tt>
	 * @return  Returns the nomeSpecie.
	 * @uml.property  name="nomeRazza"
	 */
	public String getNomeRazza() {
		return nomeRazza;
	}

	/** 
	 * Setter of the property <tt>nomeSpecie</tt>
	 * @param nomeSpecie  The nomeSpecie to set.
	 * @uml.property  name="nomeRazza"
	 */
	public void setNomeRazza(String nomeRazza) {
		this.nomeRazza = nomeRazza;
	}

}
