package dinolib.Mappa;


public class Vegetazione extends Cella {
	/**
	 * Il costruttore si occupa di inizializzare il valore di tettoMassimo.
	 */
	public Vegetazione(int tettoMassimo) {
		super.valoreIniziale = tettoMassimo;
		super.valoreAttuale = tettoMassimo;
	}
	
	/**
	 * Aggiorna il valore attuale della cella al passare dei turni di gioco.
	 */
	public void aggiornaCellaSulTurno() {
		if (super.valoreAttuale < super.valoreIniziale) {
			int sommaTemp = super.valoreAttuale + (super.valoreIniziale/20);
			if (sommaTemp < super.valoreIniziale) {
				super.valoreAttuale = sommaTemp;
			}
			else super.valoreAttuale = super.valoreIniziale;
		}
	}
}
