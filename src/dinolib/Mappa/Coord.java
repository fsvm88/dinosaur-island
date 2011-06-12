package dinolib.Mappa;
/**
 * Classe che contiene una coppia di coordinate, serve per semplificare la logica di alto livello.
 * Dichiara il metodo equals, di modo da avere un confronto diretto corretto tra due elementi.
 * @author fabio
 */
public class Coord {
	private int x = 0;
	private int y = 0;
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	
	public boolean equals(Coord coordToConf) {
		return ((this.x == coordToConf.getX()) &&
				(this.y == coordToConf.getY())); 
	}
}
