package dinolib;


public class Vegetazione extends Cella {
	/**
	 * Il costruttore si occupa di inizializzare il valore di tettoMassimo.
	 */
	public Vegetazione(int tettoMassimo) {
		this.tettoMassimo = tettoMassimo;
		this.valoreAttuale = tettoMassimo;
	}


	/**
	 * Contiene il valore massimo dell'energia ricavabile dalla vegetazione.
	 * @uml.property  name="tettoMassimo"
	 */
	private int tettoMassimo;

	/**
	 * Contiene il valore corrente dell'energia massima ottenibile dalla vegetazione.
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
	 * Aggiorna il valore attuale della cella al passare dei turni di gioco.
	 */
	public void aggiornaCellaSulTurno() {
		if (valoreAttuale < tettoMassimo) {
			int sommaTemp = valoreAttuale + (tettoMassimo/20);
			if (sommaTemp < tettoMassimo) {
				valoreAttuale = sommaTemp;
			}
			else valoreAttuale = tettoMassimo;
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
