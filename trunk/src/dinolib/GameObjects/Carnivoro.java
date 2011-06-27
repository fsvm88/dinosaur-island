package dinolib.GameObjects;

import java.io.Serializable;

import dinolib.ConfigurationOpts;

public class Carnivoro extends Dinosauro implements Serializable {
	/**
	 * Generated version ID for Serializable.
	 */
	private static final long serialVersionUID = -5143991022903996497L;
	/**
	 * Costruttore pubblico per un dinosauro di tipo Carnivoro.
	 * @param myCoords Le coordinate del nuovo dinosauro.
	 */
	public Carnivoro(Coord myCoord){
		super(myCoord, ConfigurationOpts.SPOSTAMENTO_MAX_CARNIVORO, ConfigurationOpts.MOLTIPLICATORE_FORZA_CARNIVORO);
	}
}
