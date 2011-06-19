package dinolib.Mappa;
/**
 * @author fabio
 */
/**
 * Classe per la cella Vegetazione.
 * Eredita parte dei metodi dalla superclasse. Fa l'override di altri implementandoli.
 */
class Vegetazione extends Cella {
	/**
	 * Il costruttore si occupa di prendere in input il valore massimo per la cella e impostarlo nell'oggetto.
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
