package dinolib.Mappa;


public class Carogna extends Cella {
	/**
	 * Il costruttore si occupa di inizializzare il valore di tettoMassimo.
	 */
	public Carogna(int valoreIniziale){
		super.valoreIniziale = valoreIniziale;
		super.valoreAttuale = valoreIniziale;
	}
	
	/**
	 * Aggiorna il valore attuale della cella al passare dei turni di gioco
	 */
	public void aggiornaCellaSulTurno() {
		int diffTemp = super.valoreAttuale - (super.valoreIniziale/20);
		if (diffTemp <= 0) {
			super.valoreAttuale = 0;
		}
		else if (diffTemp > 0) {
			super.valoreAttuale = diffTemp;
		}
	}
}
