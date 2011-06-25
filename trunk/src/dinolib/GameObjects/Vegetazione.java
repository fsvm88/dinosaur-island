package dinolib.GameObjects;

import java.io.Serializable;

/**
 * @author fabio
 */
/**
 * Classe per la cella Vegetazione.
 * Eredita parte dei metodi dalla superclasse. Fa l'override di altri implementandoli.
 */
class Vegetazione extends Cella implements Serializable {
	/**
	 * Generated version ID for Serializable.
	 */
	private static final long serialVersionUID = -5913453482942723982L;

	/**
	 * Prende in input il valore massimo per la cella e lo imposta nell'oggetto.
	 * @param nuovoValore Il valore iniziale della cella.
	 */
	protected Vegetazione(int nuovoValore) {
		super.valoreIniziale = nuovoValore;
		super.valoreAttuale = nuovoValore;
	}
	
	/**
	 * Aggiorna il valore attuale della cella al passare dei turni di gioco.
	 */
	@Override
	public void aggiorna() {
		if (valoreAttuale < valoreIniziale) {
			int sommaTemp = valoreAttuale + (valoreIniziale/20);
			if (sommaTemp < valoreIniziale) {
				valoreAttuale = sommaTemp;
			}
			else valoreAttuale = valoreIniziale;
		}
	}
	
	/**
	 * Override del metodo della classe Cella.
	 * Restituisce il valore attuale della cella.
	 */
	@Override
	public int getValoreAttuale() { return valoreAttuale; }
}
