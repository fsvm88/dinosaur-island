package dinolib.Mappa;
/**
 * Classe che contiene una coppia di coordinate, serve per semplificare la logica di alto livello.
 * Dichiara il metodo equals, di modo da avere un confronto diretto corretto tra due elementi.
 * @author fabio
 */
public class Coord {
	/**
	 * La x delle coordinate.
	 */
	private int x = 0;
	/**
	 * La y delle coordinate.
	 */
	private int y = 0;
	
	/**
	 * Costruttorem richiede le x e y desiderata.
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
	public boolean equals(Coord coordToConf) { // Testato
		if ((this.x == coordToConf.getX()) &&
				(this.y == coordToConf.getY())) return true;
		else return false;
	}
}
