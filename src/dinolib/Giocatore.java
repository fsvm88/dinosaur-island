package dinolib;

import java.util.ArrayList;
/**
 * @author fabio
 *
 */
public class Giocatore {
	private String nome;
	private String password;
	private String token;
	
	private ArrayList<Dinosauro> Dinosauri = new ArrayList<Dinosauro>();
	
	public Giocatore (String nome, String password, String newtoken) {
		this.nome = nome;
		this.password = password;
		this.token = newtoken;
	}
	
	/**
	 * @return il nome
	 */
	public String getNome() {
		return nome;
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
	public void setPassword(String newPassword) {
		this.password = newPassword;
	}
	/**
	 * @return the dinosauri
	 */
	public ArrayList<Dinosauro> getDinosauri() {
		return Dinosauri;
	}
	/**
	 * @param Dinosauri funzioni per le manipolazioni sulla lista dei dinosauri
	 */
	public void aggiungiDinosauro(Dinosauro nuovoDinosauro) {
		Dinosauri.add(nuovoDinosauro);
	}
	public void rimuoviDinosauro(Dinosauro vecchioDinosauro) {
		Dinosauri.remove(vecchioDinosauro);
	}
	public int numeroDinosauro() {
		return Dinosauri.size();
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getToken() {
		return token;
	}
}