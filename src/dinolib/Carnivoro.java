package dinolib;


public class Carnivoro extends Dinosauro {
	/**
	 * @uml.property  name="SPOSTAMENTO_MAX" readOnly="true"
	 */
	private static final int spostamento_MAX = 3;
	/**
	 * @uml.property  name="MOLTIPLICATORE_FORZA" readOnly="true"
	 */
	private static final int moltiplicatore_FORZA = 2;
		
	/**
	 * Implementa il costruttore pubblico per il tipo di dinosauro Carnivoro.
	 */
	public Carnivoro(int x, int y){
		super(x, y);
		super.setSpostamentoMaxPerTurno(spostamento_MAX);
		super.setForza(moltiplicatore_FORZA);
	}

	/**
	 * Implementa la crescita del dinosauro, modificando tutte le variabili necessarie.
	 */
	public void Crescita(){
		super.comuneCrescita(moltiplicatore_FORZA);
	}

	/**
	 * Restituisce la forza del dinosauro, parte specializzata per le sottoclassi.
	 */
	public int getForza() {
		return super.getForzaComune(moltiplicatore_FORZA);
	}
}
