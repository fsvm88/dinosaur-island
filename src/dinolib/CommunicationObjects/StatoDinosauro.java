package dinolib.CommunicationObjects;

import java.io.Serializable;

import dinolib.GameObjects.Coord;

public class StatoDinosauro implements Serializable {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -6419981221479339962L;
	/**
	 * Contiene il nome dell'utente a cui appartiene il dinosauro.
	 * @uml.property name="nomeUtente"
	 */
	private String nomeUtente = null;
	/**
	 * Contiene il nome della razza dell'utente a cui appartiene il dinosauro.
	 * @uml.property name="nomeRazza"
	 */
	private String nomeRazza = null;
	/**
	 * Contiene il tipo della razza di dinosauri.
	 * @uml.property name="tipoRazza"
	 */
	private Character tipoRazza = null;
	/**
	 * Contiene le coordinate del dinosauro.
	 * @uml.property name="coordinateDinosauro"
	 */
	private Coord coordinateDinosauro = null;
	/**
	 * Contiene la dimensione del dinosauro.
	 * @uml.property name="dimensioneDinosauro"
	 */
	private Integer dimensioneDinosauro = null;
	/**
	 * Contiene l'energia del dinosauro, se questo e' dell'utente che richiede lo stato.
	 * @uml.property name="energiaDinosauro"
	 */
	private Integer energiaDinosauro = null;
	/**
	 * Contiene i turni vissuti dal dinosauro, se questo e' dell'utente che richiede lo stato.
	 * @uml.property name="turniVissuti"
	 */
	private Integer turniVissuti = null;

	/**
	 * Costruttore per il dinosauro di un utente diverso dal proprietario.
	 * @param newNomeUtente Il nome del proprietario.
	 * @param newNomeRazza Il nome della razza a cui appartiene il dinosauro.
	 * @param newTipoRazza Il tipo della razza a cui appartiene il dinosauro.
	 * @param newCoordinate Le coordinate del dinosauro.
	 * @param newDimensione La dimensione del dinosauro.
	 */
	public StatoDinosauro (String newNomeUtente, String newNomeRazza, Character newTipoRazza, Coord newCoordinate, Integer newDimensione) {
		this.nomeUtente = newNomeUtente;
		this.nomeRazza = newNomeRazza;
		this.tipoRazza = newTipoRazza;
		this.coordinateDinosauro = newCoordinate;
		this.dimensioneDinosauro = newDimensione;
	}
	/**
	 * Costruttore per il dinosauro dell'utente proprietario.
	 * @param newNomeUtente Il nome del proprietario.
	 * @param newNomeRazza Il nome della razza a cui appartiene il dinosauro.
	 * @param newTipoRazza Il tipo della razza a cui appartiene il dinosauro.
	 * @param newCoordinate Le coordinate del dinosauro.
	 * @param newDimensione La dimensione del dinosauro.
	 * @param newEnergia L'energia del dinosauro.
	 * @param newTurniVissuti I turni vissuti dal dinosauro.
	 */
	public StatoDinosauro (String newNomeUtente, String newNomeRazza, Character newTipoRazza, Coord newCoordinate, Integer newDimensione, Integer newEnergia, Integer newTurniVissuti) {
		this.nomeUtente = newNomeUtente;
		this.nomeRazza = newNomeRazza;
		this.tipoRazza = newTipoRazza;
		this.coordinateDinosauro = newCoordinate;
		this.dimensioneDinosauro = newDimensione;
		this.energiaDinosauro = newEnergia;
		this.turniVissuti = newTurniVissuti;
	}

	/**
	 * Restituisce il nome dell'utente proprietario del dinosauro.
	 * @return Il nome dell'utente proprietario.
	 */
	String getNomeUtente() { return nomeUtente; }
	/**
	 * Restituisce il nome della razza del dinosauro.
	 * @return Il nome della razza.
	 */
	String getNomeRazza() { return nomeRazza; }
	/**
	 * Restituisce il tipo della razza di dinosauri.
	 * @return Il tipo della razza.
	 */
	Character getTipoRazza() { return tipoRazza.charValue(); }
	/**
	 * Restituisce le coordinate del dinosauro.
	 * @return Le coordinate del dinosauro.
	 */
	Coord getCoordinateDinosauro() { return coordinateDinosauro; }
	/**
	 * Restituisce la dimensione del dinosauro.
	 * @return La dimensione del dinosauro.
	 */
	Integer getDimensioneDinosauro() { return dimensioneDinosauro.intValue(); }
	/**
	 * Restituisce l'energia del dinosauro.
	 * @return L'energia del dinosauro.
	 */
	Integer getEnergiaDinosauro() { return energiaDinosauro.intValue(); }
	/**
	 * Restituisce i turni vissuti dal dinosauro
	 * @return I turni vissuti dal dinosauro.
	 */
	Integer getTurniVissuti() { return turniVissuti.intValue(); }

	@Override
	public String toString() {
		String tmpBuf = this.nomeUtente + "," + this.nomeRazza + "," + this.tipoRazza.charValue() + ",{" + this.coordinateDinosauro.getX() + "," +
		this.coordinateDinosauro.getY() + "}," + this.dimensioneDinosauro;
		if ((getEnergiaDinosauro() != null) &&
				(getTurniVissuti() != null)) { return tmpBuf + "," + this.energiaDinosauro.intValue() + "," + this.turniVissuti.intValue(); }
		else return tmpBuf;
	}
}