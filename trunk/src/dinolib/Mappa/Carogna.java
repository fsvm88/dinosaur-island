package dinolib.Mappa;
/**
 * @author fabio
 */
/**
 * Classe per la cella Carogna.
 * Eredita parte dei metodi dalla superclasse. Fa l'override di altri implementandoli.
 */
class Carogna extends Cella {
	/**
	 * Il costruttore si occupa di prendere in input il valore massimo per la cella e impostarlo nell'oggetto.
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
