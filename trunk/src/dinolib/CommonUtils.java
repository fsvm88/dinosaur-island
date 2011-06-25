package dinolib;

import java.util.Random;

import dinolib.GameObjects.Coord;
/**
 * @author fabio
 */
/**
 * Classe che contiene un po' di funzioni di utilita'.
 */
public abstract class CommonUtils {
	/* Funzioni per la generazione di valori casuali. */
	/**
	 * Genera di un nuovo token alfanumerico.
	 * @return Un token alfanumerico casuale.
	 */
	public static String getNewToken() { return Long.toString(Double.doubleToLongBits(Math.random())); }
	/**
	 * Ritorna un valore casuale compreso tra 0 e il massimo specificato
	 * @return
	 */
	public static int getNewRandomIntValue(int maxValue) { Random rnd = new Random(); return rnd.nextInt(maxValue); }
	/**
	 * Restituisce una coordinata a caso sulla mappa.
	 * @param latoDellaMappa
	 * @return
	 */
	public static Coord getNewRandomCoord(int latoDellaMappa) { return new Coord(
			(CommonUtils.getNewRandomIntValue(latoDellaMappa)),
			(CommonUtils.getNewRandomIntValue(latoDellaMappa)));
	}
	
	/* Funzioni per la traslazione di coordinate.
	 * La traslazione delle coordinate viene fatta a bassissimo livello, di modo da limitare la complessita' per la logica di alto livello. */
	/**
	 * Trasla la Y di modo da far corrispondere una matrice a un piano cartesiano.
	 * Converte da piano cartesiano a matrice.
	 * Viene chiamata da oggetti come Mappa.
	 * @param yToTranslate La y da traslare.
	 * @param modulus Il valore secondo cui traslare la y.
	 * @return La y traslata rispetto al valore richiesto.
	 */
	public static int translateYforServer (int yToTranslate, int modulus) { return (modulus-1) - yToTranslate; }
	/**
	 * Trasla la Y di modo da far corrispondere una matrice a un piano cartesiano.
	 * Converte da matrice a piano cartesiano.
	 * Viene chiamata da logica di alto livello come Logica.
	 * @param yToTranslate La y da traslare.
	 * @param modulus Il valore secondo cui traslare la y.
	 * @return La y traslata rispetto al valore richiesto.
	 */
	public static int translateYforClient(int yToTranslate, int modulus) { return modulus + yToTranslate; }
	
	/* Funzioni per la somma e differenza di coordinate, le mantiene in range sulla matrice. */
	/**
	 * Fa la sottrazione per le coordinate, che rimangano nel range specificato.
	 * @param coord La coordinata di partenza.
	 * @param rangeVista Il valore da sottrarre alla coordinata.
	 * @return La coordinata traslata, mantenuta entro i confini della mappa.
	 */
	protected static int doSubtraction(int coord, int rangeVista, int latoDellaMappa) {
		int subtraction = (coord - rangeVista);
		if (subtraction<0) return 0;
		else if (subtraction>=latoDellaMappa) return (latoDellaMappa-1);
		else return subtraction;
	}
	/**
	 * Fa l'addizione per le coordinate, che rimangano nel range specificato.
	 * @param coord La coordinata di partenza.
	 * @param rangeVista Il valore da aggiungere alla coordinata.
	 * @return La coordinata traslata, mantenuta entro i confini della mappa.
	 */
	protected static int doAddition(int coord, int rangeVista, int latoDellaMappa) {
		int addition = (coord + rangeVista);
		if (addition<0) return 0;
		else if (addition>=latoDellaMappa) return (latoDellaMappa-1);
		else return addition;
	}
}