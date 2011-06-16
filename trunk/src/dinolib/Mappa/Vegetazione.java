package dinolib.Mappa;


class Vegetazione extends Cella {
	/**
	 * Il costruttore si occupa di inizializzare il valore di tettoMassimo.
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
	 * Override del metodo della classe cella.
	 */
	@Override
	public int getValoreAttuale() { return valoreAttuale; }
}
