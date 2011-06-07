package dinolib;

import java.util.HashSet;

abstract class Cella extends Mappa {
	protected Cella() { }

	protected Cella(String idDelDinosauro, Cella cellaSuCuiSiTrova) {
		this.idDelDinosauro = idDelDinosauro;
		this.cellaSuCuiSiTrova = cellaSuCuiSiTrova;
		visitedFrom = new HashSet<String>();
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
		if (this.toString().equals("dinosauro")) {
			return idDelDinosauro;
		}
		else return null;
	}

	/**
	 * Contiene un riferimento al valore della cella sottostante il dinosauro (velocizza operazioni di lookup e movimento).
	 * @uml.property  name="cellaSuCuiSiTrova"
	 * @uml.associationEnd  
	 */
	private Cella cellaSuCuiSiTrova;

	/**
	 * Getter of the property <tt>cellaSuCuiSiTrova</tt>
	 * @return  Returns the cellaSuCuiSiTrova.
	 * @uml.property  name="cellaSuCuiSiTrova"
	 */
	public Cella getCellaSuCuiSiTrova() {
		if (this.toString().equals("dinosauro")) {
			return cellaSuCuiSiTrova;
		}
		else return null;
	}
	/**
	 * Contiene il valore iniziale dell'energia massima ricavabile dalla carogna.
	 * @uml.property  name="valoreIniziale"
	 */
	protected int valoreIniziale = 0;
	/**
	 * Contiene il valore attuale della massima energia ricavabile dalla carogna.
	 * @uml.property  name="valoreAttuale"
	 */
	protected int valoreAttuale = 0;
	/**
	 * Getter of the property <tt>valoreAttuale</tt>
	 * @return  Returns the valoreAttuale.
	 * @uml.property  name="valoreAttuale"
	 */
	public int getValoreAttuale() {
		return valoreAttuale;
	}
	
	/**
	 * Aggiorna il valore della cella quando questa viene mangiata.
	 */
	public void mangia(int valoreMangiato) {
		int diffTemp = valoreAttuale - valoreMangiato;
		if (diffTemp <= 0) {
			valoreAttuale = 0;
		}
		else if (diffTemp > 0) {
			valoreAttuale = diffTemp;
		}
	}
	
	/**
	 * Stub per esporre il metodo comune.
	 * Nel caso sia una cella senza vegetazione o carogna non fa nulla.
	 * Nel caso la cella invece sia vegetazione o carogna viene chiamato il metodo delle sottoclassi.
	 */
	public void aggiornaCellaSulTurno() {}
	
	/**
	 * Contiene la lista dei nomi dei giocatori che hanno visitato la cella.
	 */
	private HashSet<String> visitedFrom;
	
	/**
	 * Controlla se la cella è stata visitata dall'utente.
	 * @param userName
	 * @return
	 */
	public boolean isUserPassed(String userName) {
		if (visitedFrom.contains(userName)) return true;
		else return false;
	}
	
	/**
	 * Aggiunge l'utente alla lista che dice se è passato per questa cella.
	 * @param userName
	 */
	public void userIsPassed(String userName) {
		visitedFrom.add(userName);
	}
}
