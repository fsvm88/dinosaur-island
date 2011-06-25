package dinolib.GameObjects;

import java.io.Serializable;

/**
 * Classe che contiene una coppia di coordinate, serve per semplificare la logica di alto livello.
 * Dichiara il metodo equals, di modo da avere un confronto diretto corretto tra due elementi.
 */
/**
 * @author fabio
 */
public class Coord implements Serializable {
	/**
	 * Generated version ID for Serializable.
	 */
	private static final long serialVersionUID = -4481817555753451477L;
	/**
	 * La x delle coordinate.
	 */
	private int x = 0;
	/**
	 * La y delle coordinate.
	 */
	private int y = 0;

	/**
	 * Richiede le x e y desiderate.
	 * @param x La x desiderata.
	 * @param y La y desiderata.
	 */
	public Coord(int x, int y) { // Testato
		this.x = x;
		this.y = y;
	}

	/**
	 * Restituisce la x delle coordinate.
	 * @return La x delle coordinate.
	 */
	public int getX() { return x; } // Testato
	/**
	 * Restituisce la y delle coordinate.
	 * @return La y delle coordinate.
	 */
	public int getY() { return y; } // Testato

	/**
	 * Confronta l'oggetto attuale con l'argomento.
	 * @param coordToConf La coordinata con cui confrontare l'oggetto corrente.
	 * @return True se l'argomento e' uguale all'oggetto corrente, false altrimenti.
	 */
	@Override
	public boolean equals(Object coordToConf) { // Testato
		if (coordToConf == null) { return false; }
		if ((this.x == ((Coord) coordToConf).getX()) &&
				(this.y == ((Coord) coordToConf).getY())) { return true; }
		else { return false; }
	}
	
	// /**
	// * Override del metodo hashCode (teoricamente va ridefinito assieme ad equals).
	// * Dico semplicemente che non e' implementato correttamente e ritorno un valore qualunque.
	// */
	/*@Override
	public int hashCode() {
		assert false : "hashCode not designed";
		return 42; // any arbitrary constant will do
	}*/
	
	//	/**
	//	 * Ritorna un hashCode univoco per l'oggetto corrente.
	//	 * @return L'hashCode dell'oggetto.
	//	 */
	/*@Override
	public int hashCode() { return ((1000*this.getX()) + this.getY()); }*/
}