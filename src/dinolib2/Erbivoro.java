package dinolib2;
/**
 * @author fabio
 *
 */
public class Erbivoro extends Dinosauro {
	private final int SPOSTAMENTO_MAX_PER_TURNO=2;
	private final int MOLTIPLICATORE_FORZA=1;
	
	/**
	 * @param x
	 * @param y
	 */
	public Erbivoro (int x, int y) {
		super(x, y);
		super.setSpostamentoMaxPerTurno(SPOSTAMENTO_MAX_PER_TURNO);
		super.setForza(MOLTIPLICATORE_FORZA);
	}
		
	/**
	 * fa crescere il dinosauro
	 */
	public void Crescita () {
		super.ComuneCrescita(MOLTIPLICATORE_FORZA);
	}
	
	public int getForza () {
		return super.getForzaComune(MOLTIPLICATORE_FORZA);
	}
}