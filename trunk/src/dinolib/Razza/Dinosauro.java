package dinolib.Razza;

import java.util.Random;

import dinolib.CommonUtils;


/**
 * @author  fabio
 */
public abstract class Dinosauro {
	/* Tutte le variabili statiche/definitive e non modificabili */
	/**
	 * Indica l'energia necessaria per deporre un uovo. È dichiarata final static perchè è fissa per ogni dinosauro, indipendente da altri parametri.
	 * @uml.property  name="energiaDeposizioneUovo"
	 */
	private static final int energia_DEPOSIZIONE_UOVO = 1500;
	/**
	 * @uml.property  name="DIMENSIONE_MASSIMA" readOnly="true"
	 */
	private static final int dimensione_MASSIMA = 5;
	/**
	 * Contiene lo spostamento massimo per turno sdel dinosauro impostato dalle sottoclassi. Serve a fattorizzare ancora più codice. Viene impostato in modo final dal costruttore.
	 * @uml.property  name="spostamento_MAX"
	 */
	private int spostamento_MAX = 0;
	/**
	 * Contiene il moltiplicatore della forza del dinosauro impostato dalle sottoclassi. Serve a fattorizzare ancora più codice. Viene impostato in modo final dal costr.
	 * @uml.property  name="moltiplicatore_FORZA"
	 */
	private int moltiplicatore_FORZA = 0;
	
	/* Tutte le variabili istanziabili */
	/**
	 * Indica l'energia spesa per crescere.
	 * @uml.property  name="energiaCrescita"
	 */
	private int energiaCrescita = 0;
	/**
	 * Contiene un valore che dice se il dinosauro è utilizzabile direttamente o no. Serve per deponi uovo (posticipare l'utilizzo di un dinosauro) o per verificare che il dinosauro abbia ancora mosse a disposizione.
	 * @uml.property  name="usabile"
	 */
	private boolean isUsabile = false;
	/**
	 * Indica l'energia corrente del dinosauro.
	 * @uml.property  name="energiaAttuale"
	 */
	private int energiaAttuale = 0;
	/**
	 * Indica l'energia massima per il dinosauro. Visibile pubblicamente tramite il getter, Modificabile privatamente solo tramite il setter.
	 * @uml.property  name="energiaMax"
	 */
	private int energiaMax = 0;
	/**
	 * Indica la dimensione del dinosauro. Visibile pubblicamente tramite il getter, modificabile privatamente solo tramite il setter.
	 * @uml.property  name="dimensione"
	 */
	private int dimensione = 0;
	/**
	 * Indica la durata massima della vita del dinosauro. Viene impostata nel costruttore alla creazione del dinosauro e può solo essere letta.
	 * @uml.property  name="durataVitaMax"
	 */
	private int durataVitaMax = 0;
	/**
	 * Indica la posizione del dinosauro sull'ascissa.
	 * @uml.property  name="x"
	 */
	private int x = 0;
	/**
	 * Indica la posizione del dinosauro sull'ordinata.
	 * @uml.property  name="y"
	 */
	private int y = 0;
	/**
	 * Indica la forza del dinosauro.
	 * @uml.property  name="forza"
	 */
	private int forza = 0;
	/**
	 * Indica il turno di vita del dinosauro.
	 * @uml.property  name="turnoDiVita"
	 */
	private int turnoDiVita = 0;
	/**
	 * Indica l'id del dinosauro.
	 * @uml.property  name="idDinosauro"
	 */
	private String idDinosauro = null;
	
	/* Costruttore */
	/**
	 * Costruttore visibile solo alle sottoclassi da usare come costruttore comune per Carnivoro e Erbivoro.
	 */
	protected Dinosauro(int x, int y, int nuovo_spostamento_MAX, int nuovo_moltiplicatore_FORZA) {
		this.x = x;
		this.y = y;
		Random generator = new Random();
		this.turnoDiVita = 1;
		this.durataVitaMax = 24 + (generator.nextInt(13));
		this.energiaAttuale = 750;
		this.dimensione = 1;
		this.energiaMax = 1000*dimensione;
		this.spostamento_MAX = nuovo_spostamento_MAX;
		this.moltiplicatore_FORZA = nuovo_moltiplicatore_FORZA;
		this.updateEnergiaCrescita();
		this.usabile();
		this.idDinosauro = CommonUtils.getNewToken();
	}
	
	/* Tutti i getter */
	/**
	 * @return
	 * @uml.property  name="energiaAttuale"
	 */
	public int getEnergiaAttuale() { return energiaAttuale; }
	/**
	 * @return
	 * @uml.property  name="energiaMax"
	 */
	public int getEnergiaMax() { return energiaMax; }
	/**
	 * @return
	 * @uml.property  name="dimensione"
	 */
	public int getDimensione() { return dimensione; }
	/**
	 * @return
	 * @uml.property  name="durataVitaMax"
	 */
	public int getDurataVitaMax() {	return durataVitaMax; }
	/**
	 * @return
	 * @uml.property  name="forza"
	 */
	public int getForza() { updateForza(); return forza; }
	/**
	 * @return
	 * @uml.property  name="x"
	 */
	public int getX() { return x; }
	/**
	 * @return
	 * @uml.property  name="y"
	 */
	public int getY() { return y; }
	/**
	 * @return
	 * @uml.property  name="turnoDiVita"
	 */
	public int getTurnoDiVita() { return turnoDiVita; }
	public int getSpostamentoMax() { return spostamento_MAX; }
	/**
	 * @return
	 * @uml.property  name="usabile"
	 */
	public boolean isUsabile() { return isUsabile; }
	/**
	 * @return
	 * @uml.property  name="idDinosauro"
	 */
	public String getIdDinosauro() { return idDinosauro; }
	/**
	 * Restituisce come String il nome della classe per avere il tipo della razza di dinosauri.
	 * @return
	 */
	public String getTipoRazza() {
		return toString();
	}
	/**
	 * Restituisce un valore che indica quante caselle il dinosauro vede attorno a sè (range di visuale).
	 * Dipende dalla dimensione del dinosauro.
	 * I valori sono hard-coded.
	 * @return
	 */
	public int getRangeVista() {
		switch (getDimensione()) {
		case 1:
			return 2;
		case 2:
		case 3:
			return 3;
		case 4:
		case 5:
		default:
			return 4;
		}
	}
	
	/* Tutti i setter */
	protected void nonUsabile() { isUsabile = false; }
	public void usabile() { isUsabile = true; }
	/**
	 * @param energiaAttuale
	 * @uml.property  name="energiaAttuale"
	 */
	public void setEnergiaAttuale(int energiaAttuale) { this.energiaAttuale = energiaAttuale; }
	/**
	 * @param dimensione
	 * @uml.property  name="dimensione"
	 */
	private void setDimensione(int dimensione) { this.dimensione = dimensione; }
	/**
	 * @param x
	 * @uml.property  name="x"
	 */
	public void setX(int x) { this.x = x; }
	/**
	 * @param y
	 * @uml.property  name="y"
	 */
	public void setY(int y) { this.y = y; }
	protected void setXY(int x, int y) { this.x = x; this.y = y; }
	private void updateEnergiaCrescita() { energiaCrescita = energiaMax/2; }
	private void updateForza() { this.forza = (moltiplicatore_FORZA*this.getDimensione()*this.getEnergiaAttuale()); }
	private void updateEnergiaMax() { this.energiaMax = 1000*this.dimensione; }
	/**
	 * Aumenta di uno il turno di vita del dinosauro. (Invecchia il dinosauro).
	 */
	public void invecchia() { this.turnoDiVita += 1; }
	
	/* Funzioni miscellanee */
	/**
	 * Helper, verifica se il dinosauro è già alla dimensione massima.
	 * Risponde true se dimensione = dimensione_MAX, false altrimenti.
	 * @return
	 */
	public boolean isAtDimensioneMax() {
		if (this.getDimensione() >= 5) return true;
		else return false;
	}
	/**
	 * Controlla che il dinosauro abbia abbastanza energia per crescere.
	 * È booleano e ritorna true se energiaAttuale > energiaCrescita; false altrimenti.
	 * @return
	 */
	protected boolean hasEnergyToGrow() {
		if (energiaAttuale > energiaCrescita) return true;
		else return false;
	}
	/**
	 * Controlla che il dinosauro abbia abbastanza energia per deporre un uomo.
	 * È booleano e ritorna true se energiaAttuale > energiaCrescita; false altrimenti.
	 * @return
	 */
	protected boolean hasEnergyToRepl() {
		if (energiaAttuale > energia_DEPOSIZIONE_UOVO) return true;
		else return false;
	}
	/**
	 * Fa crescere il dinosauro.
	 */
	protected void cresci() {
		if (this.getDimensione() < dimensione_MASSIMA) {
			this.setDimensione((this.getDimensione()+1));
			this.updateEnergiaMax();
			this.updateForza();
			this.updateEnergiaCrescita();
		}
	}
	/**
	 * Fa deporre un uovo al dinosauro (serve solo per la parte di aggiornamento dell'energia del dinosauro, il resto è fatto più in alto nella logica).
	 */
	protected void deponiUovo() {
		this.setEnergiaAttuale(this.getEnergiaAttuale()-energia_DEPOSIZIONE_UOVO);
	}
	
	/**
	 * Override del metodo toString per questa classe.
	 */
	public String toString () {
		return this.getClass().getSimpleName();
	}
}