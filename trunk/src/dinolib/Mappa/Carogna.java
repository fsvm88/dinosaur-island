package dinolib.Mappa;


class Carogna extends Cella {
	/**
	 * Il costruttore si occupa di inizializzare il valore di tettoMassimo.
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
	 * Override del metodo della classe cella.
	 */
	@Override
	public int getValoreAttuale() { return valoreAttuale; }
}
