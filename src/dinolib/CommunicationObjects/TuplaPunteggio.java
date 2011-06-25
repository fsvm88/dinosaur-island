package dinolib.CommunicationObjects;

import java.io.Serializable;

class TuplaPunteggio implements Serializable {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = 1115057801316261171L;
	/**
	 * Contiene il nome del giocatore della tupla.
	 * @uml.property name="nomeGiocatore"
	 */
	private String nomeGiocatore = null;
	/**
	 * Contiene il nome della razza della tupla.
	 * @uml.property name="nomeRazza" 
	 */
	private String nomeRazza = null;
	/**
	 * Contiene il punteggio della razza della tupla.
	 * @uml.property name="punteggioRazza"
	 */
	private Integer punteggioRazza = null;
	/**
	 * Contiene lo stato della razza (se è estinta o meno).
	 * @uml.property name="statoRazza"
	 */
	private Character statoRazza = null;
	
	/**
	 * Crea una tupla con i parametri specificati.
	 * @param newNomeGiocatore Il nome del giocatore.
	 * @param newNomeRazza Il nome della razza del giocatore.
	 * @param newPunteggioRazza Il punteggio della razza del giocatore.
	 * @param newStatoRazza Lo stato della razza del giocatore.
	 */
	TuplaPunteggio(String newNomeGiocatore, String newNomeRazza, Integer newPunteggioRazza, Character newStatoRazza) {
		this.nomeGiocatore = newNomeGiocatore;
		this.nomeRazza = newNomeRazza;
		this.punteggioRazza = newPunteggioRazza;
		this.statoRazza = newStatoRazza;
	}
	/**
	 * Override del metodo toString, ritorna la tupla gia' formattata per la versione Socket.
	 */
	public String toString() { return this.nomeGiocatore + "," + this.nomeRazza + "," + this.punteggioRazza.intValue() + "," + this.statoRazza.charValue(); }
	/**
	 * Ritorna il nome del giocatore della tupla.
	 * @return Il nome del giocatore della tupla.
	 */
	public String getNome() { return this.nomeGiocatore; }
	/**
	 * Ritorna il nome della razza della tupla.
	 * @return Il nome della razza della tupla.
	 */
	public String getNomeRazza() { return this.nomeRazza; }
	/**
	 * Ritorna il punteggio della razza della tupla.
	 * @return Il punteggio della razza della tupla.
	 */
	public Integer getPunteggioRazza() { return this.punteggioRazza; }
	/**
	 * Ritorna lo stato corrente della razza della tupla.
	 * @return Lo stato corrente della razza della tupla.
	 */
	public Character getStatoRazza() { return this.statoRazza; }
}
