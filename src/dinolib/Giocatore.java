package dinolib;

import java.util.ArrayList;
import java.util.Iterator;
/**
 * @author fabio
 *
 */
public class Giocatore {
	private String nome;
	private String password;
	private String token;
	private String nomeRazzaDinosauro;
	
	private ArrayList<Dinosauro> Dinosauri = new ArrayList<Dinosauro>();
	
	public Giocatore (String nome, String password, String newtoken) {
		this.nome = nome;
		this.password = password;
		this.token = newtoken;
	}
	
	public Iterator<Dinosauro> dammiIteratoreSuiDinosauri() {
		return Dinosauri.iterator();
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
	 * @param Dinosauri funzioni per le manipolazioni sulla lista dei dinosauri
	 */
	public void aggiungiDinosauro(Dinosauro nuovoDinosauro) {
		Dinosauri.add(nuovoDinosauro);
	}
	public void rimuoviDinosauro(Dinosauro vecchioDinosauro) {
		Dinosauri.remove(vecchioDinosauro);
	}
	public int numeroDinosauri() {
		return Dinosauri.size();
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getToken() {
		return token;
	}

	public void setNomeRazzaDinosauro(String nomeRazzaDinosauro) {
		this.nomeRazzaDinosauro = nomeRazzaDinosauro;
	}

	public String getNomeRazzaDinosauro() {
		return nomeRazzaDinosauro;
	}
}