package dinolib;


public class Carogna extends Cella {
	/**
	 * Contiene il valore iniziale dell'energia massima ricavabile dalla carogna.
	 * @uml.property  name="valoreIniziale"
	 */
	private int valoreIniziale;

	/**
	 * Contiene il valore attuale della massima energia ricavabile dalla carogna.
	 * @uml.property  name="valoreAttuale"
	 */
	private int valoreAttuale;

	/**
	 * Getter of the property <tt>valoreAttuale</tt>
	 * @return  Returns the valoreAttuale.
	 * @uml.property  name="valoreAttuale"
	 */
	public int getValoreAttuale() {
		return valoreAttuale;
	}

	/**
	 * Il costruttore si occupa di inizializzare il valore di tettoMassimo.
	 */
	public Carogna(int valoreIniziale){
		this.valoreIniziale = valoreIniziale;
		this.valoreAttuale = valoreIniziale;
	}
	
	/**
	 * Aggiorna il valore attuale della cella al passare dei turni di gioco
	 */
	public void aggiornaCellaSulTurno() {
		int diffTemp = valoreAttuale - (valoreIniziale/20);
		if (diffTemp <= 0) {
			valoreAttuale = 0;
		}
		else if (diffTemp > 0) {
			valoreAttuale = diffTemp;
		}
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
}
