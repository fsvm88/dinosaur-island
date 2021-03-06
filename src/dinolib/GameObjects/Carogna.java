package dinolib.GameObjects;

import java.io.Serializable;

/**
 * @author fabio
 */
/**
 * Classe per la cella Carogna.
 * Eredita parte dei metodi dalla superclasse. Fa l'override di altri implementandoli.
 */
class Carogna extends Cella implements Serializable {
	/**
	 * Generated version ID for Serializable.
	 */
	private static final long serialVersionUID = 4095487320686924269L;
	/**
	 * Prende in input il valore massimo per la cella e lo imposta nell'oggetto.
	 * @param nuovoValore Il valore iniziale della cella.
	 */
	protected Carogna(int nuovoValore){
		super.valoreIniziale = nuovoValore;
		super.valoreAttuale = nuovoValore;
	}
	
	/**
	 * Aggiorna il valore attuale della cella al passare dei turni di gioco
	 */
	@Override
	public void aggiorna() {
		int diffTemp = valoreAttuale - (valoreIniziale/20);
		if (diffTemp <= 0) {
			valoreAttuale = 0;
		}
		else if (diffTemp > 0) {
			valoreAttuale = diffTemp;
		}
	}
	/**
	 * Override del metodo della classe Cella.
	 * Restituisce il valore attuale della cella.
	 */
	@Override
	public int getValoreAttuale() { return valoreAttuale; }
}
