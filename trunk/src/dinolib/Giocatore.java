package dinolib;

import java.util.ArrayList;

/**
 * @author fabio
 *
 */
public class Giocatore {
	private String nome;
	private String password;
	private int ID = 0;
	private int numeroDinosauri = 0;
	
	private ArrayList<Dinosauro> Dinosauri = new ArrayList<Dinosauro>();
		
	/**
	 * @return il nome
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome il nome da impostare
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * @return l'iD
	 */
	public int getID() {
		return ID;
	}
	/**
	 * @param iD l'iD da impostare
	 */
	public void setID(int iD) {
		ID = iD;
	}
	/**
	 * @return il numeroDinosauri
	 */
	public int getNumeroDinosauri() {
		return numeroDinosauri;
	}
	/**
	 * @param numeroDinosauri il numeroDinosauri da impostare
	 */
	public void setNumeroDinosauri(int num_dinosauri) {
		this.numeroDinosauri = num_dinosauri;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the dinosauri
	 */
	public ArrayList<Dinosauro> getDinosauri() {
		return Dinosauri;
	}
	/**
	 * @param dinosauri the dinosauri to set
	 */
	public void setDinosauri(ArrayList<Dinosauro> dinosauri) {
		Dinosauri = dinosauri;
	}
	
	
}