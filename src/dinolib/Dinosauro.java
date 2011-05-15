package dinolib;

import java.util.Random;
/**
 * @author fabio
 * Questa classe implementa l'oggetto Dinosauro
 * con le sue proprietà. È fornita di metodi setter e getter per comodità.
 * Implementa l'istanza di un solo dinosauro, il
 * compito del tracking di più dinosauri è lasciato alla logica giocatore.
 * La logica gestisce anche le azioni.
 */
public class Dinosauro {
	/** Dichiarazione di alcune variabili di classe */
	private int turnoDiVita = 0;
	private int vitaMax = 0;
	private int energiaAttuale = 0;
	private int energiaMax = 0;
	private int dimensione = 0;
	private int x = 0;
	private int y = 0;
	private int forza = 0;
	private int spostamentoMaxPerTurno = 0;
	/** Dichiarazione di una costante di gioco*/
	private final int DIMENSIONE_MASSIMA = 5;
	
	Dinosauro (int x, int y) /**
	 * @param turnoDiVita vita attuale del dinosauro
	 * @param vitaMax massima eta' raggiungibile [data da 30+-20%)
	 * @param energiaAttuale energia attuale del dinosauro
	 * @param energiaMax energia massima del dinosauro
	 * @param dimensione dimensione attuale del dinosauro
	 */
	{
		this.x = x;
		this.y = y;
		Random generator = new Random();
		turnoDiVita = 1;
		vitaMax = 24 + (generator.nextInt(8)+1);
		energiaAttuale = 750;
		dimensione = 1;
		energiaMax = 1000*dimensione;
	}


	/**
	 * @return la x
	 */
	public synchronized int getX() {
		return x;
	}
	
	/**
	 * @return la y
	 */
	public synchronized int getY() {
		return y;
	}

	/**
	 * @param x,y imposta x e y
	 */
	public synchronized void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return il turnoDiVita
	 */
	public int getTurnoDiVita() {
		return turnoDiVita;
	}

	/**
	 * @param turnoDiVita il turnoDiVita da impostare
	 */
	public void setTurnoDiVita(int turnoDiVita) {
		this.turnoDiVita = turnoDiVita;
	}

	/**
	 * @return l'energiaAttuale
	 */
	public int getEnergiaAttuale() {
		return energiaAttuale;
	}

	/**
	 * @param energiaAttuale l'energiaAttuale da impostare
	 */
	public void setEnergiaAttuale(int energiaAttuale) {
		this.energiaAttuale = energiaAttuale;
	}

	/**
	 * @return la dimensione
	 */
	public int getDimensione() {
		return dimensione;
	}

	/**
	 * @param dimensione la dimensione da impostare
	 */
	private void setDimensione(int dimensione) {
		this.dimensione = dimensione;
	}

	/**
	 * @return la vitaMax
	 */
	public int getVitaMax() {
		return vitaMax;
	}

	/**
	 * @return l'energiaMax
	 */
	public int getEnergiaMax() {
		return energiaMax;
	}

	/**
	 * @param energiaMax imposta l'energia massima
	 */
	private void setEnergiaMax(int energiaMax) {
		this.energiaMax = energiaMax;
	}
	
	/**
	 * @return la Forza
	 */
	protected int getForzaComune(int moltiplicatore) {
		setForza(moltiplicatore);
		return forza;
	}
	/**
	 * @param moltiplicatore imposta la forza
	 * @return 
	 */
	protected void setForza (int moltiplicatore) {
		forza = (moltiplicatore*this.getDimensione()*this.getEnergiaAttuale());
	}
	
	/**
	 * @return the spostamentoMaxPerTurno
	 */
	public int getSpostamentoMaxPerTurno() {
		return spostamentoMaxPerTurno;
	}

	/**
	 * @param spostamentoMaxPerTurno lo spostamentoMaxPerTurno da impostare
	 */
	protected void setSpostamentoMaxPerTurno(int spostamentoMaxPerTurno) {
		this.spostamentoMaxPerTurno = spostamentoMaxPerTurno;
	}

	/**
	 * Fa crescere il dinosauro, parte comune
	 */
	protected void ComuneCrescita (int moltiplicatore_forza){
		if (this.getDimensione() < DIMENSIONE_MASSIMA) {
			this.setDimensione((this.getDimensione()+1));
			this.setEnergiaMax((1000*this.getDimensione()));
			this.setForza(moltiplicatore_forza);
		}
	}
	
	public String toString () {
		return this.getClass().getName();
	}
}
