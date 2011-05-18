package dinolib;


public class Erbivoro extends Dinosauro {
	/**
	 * @uml.property  name="SPOSTAMENTO_MAX" readOnly="true"
	 */
	private static final int spostamento_MAX = 2;
	/**
	 * @uml.property  name="MOLTIPLICATORE_FORZA" readOnly="true"
	 */
	private static final int moltiplicatore_FORZA = 1;
		
	/**
	 * Implementa il costruttore pubblico per la sottoclasse Erbivoro.
	 */
	public Erbivoro(int x, int y){
		super(x, y);
		super.setSpostamentoMaxPerTurno(spostamento_MAX);
		super.setForza(moltiplicatore_FORZA);
	}
	
	/**
	 * Implementa la crescita del dinosauro, modificando tutte le variabili necessarie.
	 */
	public void cresciDinosauro(){
		super.comuneCrescita(moltiplicatore_FORZA);
	}

	/**
	 * Restituisce la forza del dinosauro, parte specializzata per le sottoclassi.
	 */
	public int getForza() {
		return super.getForzaComune(moltiplicatore_FORZA);
	}
}