package dinolib;

import java.util.ArrayList;
import dinolib.*;

public class Giocatore {
	/**
	 * Contiene il nome del giocatore corrente.
	 * @uml.property  name="nome"
	 */
	private String nome = null;

	/**
	 * Getter of the property <tt>nome</tt>
	 * @return  Returns the nome.
	 * @uml.property  name="nome"
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Contiene la password dell'utente.
	 * @uml.property  name="password"
	 */
	private String password = null;

	/**
	 * Getter of the property <tt>password</tt>
	 * @return  Returns the password.
	 * @uml.property  name="password"
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @uml.property  name="Dinosauri"
	 */
	private ArrayList dinosauri = null;

	/**
	 * Getter of the property <tt>Dinosauri</tt>
	 * @return  Returns the dinosauri.
	 * @uml.property  name="Dinosauri"
	 */
	public ArrayList getDinosauri() {
		return dinosauri;
	}

	/**
	 * Setter of the property <tt>Dinosauri</tt>
	 * @param Dinosauri  The dinosauri to set.
	 * @uml.property  name="Dinosauri"
	 */
	public void setDinosauri(ArrayList dinosauri) {
		this.dinosauri = dinosauri;
	}

	/**
	 * Fa riferimento a un oggetto Specie, viene inizializzato a zero fino a che non viene costruita la specie tramite l'implementazione in server.
	 * @uml.property  name="specieDiDinosauri"
	 */
	private Specie specieDiDinosauri;

	/**
	 * Getter of the property <tt>specieDiDinosauri</tt>
	 * @return  Returns the specieDiDinosauri.
	 * @uml.property  name="specieDiDinosauri"
	 */
	public Specie getSpecieDiDinosauri() {
		return specieDiDinosauri;
	}

	/**
	 * Setter of the property <tt>specieDiDinosauri</tt>
	 * @param specieDiDinosauri  The specieDiDinosauri to set.
	 * @uml.property  name="specieDiDinosauri"
	 */
	public void setSpecieDiDinosauri(Specie specieDiDinosauri) {
		this.specieDiDinosauri = specieDiDinosauri;
	}
	
	public void getNumeroDinosauri() {
		return specieDiDinosauri.getNumeroDinosauri();
	}
}
