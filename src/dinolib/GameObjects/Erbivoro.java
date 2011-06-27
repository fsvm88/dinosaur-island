package dinolib.GameObjects;

import java.io.Serializable;

import dinolib.ConfigurationOpts;

public class Erbivoro extends Dinosauro implements Serializable {
	/**
	 * Generated version ID for Serializable.
	 */
	private static final long serialVersionUID = -3344882289341289703L;
	/**
	 * Costruttore pubblico per un dinosauro di tipo Carnivoro.
	 * @param myCoords Le coordinate del nuovo dinosauro.
	 */
	public Erbivoro(Coord myCoords){
		super(myCoords, ConfigurationOpts.SPOSTAMENTO_MAX_ERBIVORO, ConfigurationOpts.MOLTIPLICATORE_FORZA_ERBIVORO);
	}
}