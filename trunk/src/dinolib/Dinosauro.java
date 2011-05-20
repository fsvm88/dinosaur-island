package dinolib;

import java.util.Random;


public class Dinosauro extends Specie {
	/**
	 * Indica l'energia corrente del dinosauro.
	 * @uml.property  name="energiaAttuale"
	 */
	private int energiaAttuale = 0;

	/**
	 * Getter of the property <tt>energiaAttuale</tt>
	 * @return  Returns the energiaAttuale.
	 * @uml.property  name="energiaAttuale"
	 */
	public int getEnergiaAttuale() {
		return energiaAttuale;
	}

	/**
	 * Setter of the property <tt>energiaAttuale</tt>
	 * @param energiaAttuale  The energiaAttuale to set.
	 * @uml.property  name="energiaAttuale"
	 */
	public void setEnergiaAttuale(int energiaAttuale) {
		this.energiaAttuale = energiaAttuale;
	}

	/**
	 * Indica l'energia massima per il dinosauro. Visibile pubblicamente tramite il getter, Modificabile privatamente solo tramite il setter.
	 * @uml.property  name="energiaMax"
	 */
	private int energiaMax = 0;

	/**
	 * Getter of the property <tt>energiaMax</tt>
	 * @return  Returns the energiaMax.
	 * @uml.property  name="energiaMax"
	 */
	public int getEnergiaMax() {
		return energiaMax;
	}

	/**
	 * Setter of the property <tt>energiaMax</tt>
	 * @param energiaMax  The energiaMax to set.
	 * @uml.property  name="energiaMax"
	 */
	private void setEnergiaMax(int energiaMax) {
		this.energiaMax = energiaMax;
	}

	/**
	 * Indica la dimensione del dinosauro. Visibile pubblicamente tramite il getter, modificabile privatamente solo tramite il setter.
	 * @uml.property  name="dimensione"
	 */
	private int dimensione = 0;

	/**
	 * Getter of the property <tt>dimensione</tt>
	 * @return  Returns the dimensione.
	 * @uml.property  name="dimensione"
	 */
	public int getDimensione() {
		return dimensione;
	}

	/**
	 * Setter of the property <tt>dimensione</tt>
	 * @param dimensione  The dimensione to set.
	 * @uml.property  name="dimensione"
	 */
	private void setDimensione(int dimensione) {
		this.dimensione = dimensione;
	}

	/**
	 * Indica la durata massima della vita del dinosauro. Viene impostata nel costruttore alla creazione del dinosauro e può solo essere letta.
	 * @uml.property  name="durataVitaMax"
	 */
	private int durataVitaMax = 0;

	/**
	 * Getter of the property <tt>durataVitaMax</tt>
	 * @return  Returns the durataVitaMax.
	 * @uml.property  name="durataVitaMax"
	 */
	public int getDurataVitaMax() {
		return durataVitaMax;
	}

	/**
	 * Indica la posizione del dinosauro sull'ascissa.
	 * @uml.property  name="x"
	 */
	private int x = 0;

	/**
	 * Getter of the property <tt>x</tt>
	 * @return  Returns the x.
	 * @uml.property  name="x"
	 */
	public int getX() {
		return x;
	}

	/**
	 * Setter of the property <tt>x</tt>
	 * @param x  The x to set.
	 * @uml.property  name="x"
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Indica la posizione del dinosauro sull'ordinata.
	 * @uml.property  name="y"
	 */
	private int y = 0;

	/**
	 * Getter of the property <tt>y</tt>
	 * @return  Returns the y.
	 * @uml.property  name="y"
	 */
	public int getY() {
		return y;
	}

	/**
	 * Setter of the property <tt>y</tt>
	 * @param y  The y to set.
	 * @uml.property  name="y"
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Setter of the properties <tt>x</tt> and <tt>y</tt>.
	 * @param x  The x to set.
	 * @param y  The y to set.
	 * @uml.property  name="x"
	 * @uml.property  name="y"
	 */
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @uml.property  name="forza"
	 */
	private int forza = 0;

	/**
	 * Getter of the property <tt>forza</tt>
	 * @return  Returns the forza.
	 * @uml.property  name="forza"
	 */
	public int getForza() {
		setForza();
		return forza;
	}

	/**
	 * Setter of the property <tt>forza</tt>
	 * @param forza  The forza to set.
	 * @uml.property  name="forza"
	 */
	public void setForza() {
		this.forza = (moltiplicatore_FORZA*this.getDimensione()*this.getEnergiaAttuale());
	}

	/**
	 * @uml.property  name="DIMENSIONE_MASSIMA" readOnly="true"
	 */
	private static final int dimensione_MASSIMA = 5;

	/**
	 * Costruttore visibile solo alle sottoclassi da usare come costruttore comune per Carnivoro e Erbivoro.
	 */
	protected Dinosauro(int x, int y, int nuovo_spostamento_MAX, int nuovo_moltiplicatore_FORZA) {
		super();
		this.x = x;
		this.y = y;
		Random generator = new Random();
		turnoDiVita = 1;
		durataVitaMax = 24 + (generator.nextInt(13));
		energiaAttuale = 750;
		dimensione = 1;
		energiaMax = 1000*dimensione;
		spostamento_MAX = nuovo_spostamento_MAX;
		moltiplicatore_FORZA = nuovo_moltiplicatore_FORZA;
	}

	/**
	 * Indica il turno di vita del dinosauro.
	 * @uml.property  name="turnoDiVita"
	 */
	private int turnoDiVita = 0;

	/**
	 * Getter of the property <tt>turnoDiVita</tt>
	 * @return  Returns the turnoDiVita.
	 * @uml.property  name="turnoDiVita"
	 */
	public int getTurnoDiVita() {
		return turnoDiVita;
	}
	
	/**
	 * Aumenta di uno il turno di vita del dinosauro. (Invecchia il dinosauro).
	 */
	public void invecchia() {
		this.turnoDiVita += 1;
	}

	/**
	 * Fa crescere il dinosauro, parte comune.
	 */
	public void cresci(int moltiplicatore_forza){
		if (this.getDimensione() < dimensione_MASSIMA) {
			this.setDimensione((this.getDimensione()+1));
			this.setEnergiaMax((1000*this.getDimensione()));
			this.setForza();
		}
	}

	/**
	 * Override del metodo toString per questa classe in particolare.
	 */
	public String toString () {
		return this.getClass().getName();
	}

	/**
	 * Il getter per la proprietà <tt>Spostamento_Max</tt>
	 * @return
	 */
	public int getSpostamentoMax() {
		return spostamento_MAX;
	}

	/**
	 * Contiene lo spostamento massimo per turno sdel dinosauro impostato dalle sottoclassi.
	 * Serve a fattorizzare ancora più codice.
	 * Viene impostato in modo final dal costruttore.
	 * @uml.property name="spostamento_MAX"
	 */
	private int spostamento_MAX = 0;
	
	/**
	 * Contiene il moltiplicatore della forza del dinosauro impostato dalle sottoclassi.
	 * Serve a fattorizzare ancora più codice.
	 * Viene impostato in modo final dal costruttore.
	 * @uml.property name="moltiplicatore_FORZA"
	 */
	private int moltiplicatore_FORZA = 0;
}
