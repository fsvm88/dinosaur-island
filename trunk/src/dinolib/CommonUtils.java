package dinolib;

import java.util.Random;

import dinolib.Mappa.Coord;

public abstract class CommonUtils {
	/**
	 * Helper per la generazione di un nuovo token alfanumerico.
	 * @return
	 */
	public static String getNewToken() {
		return Long.toString(Double.doubleToLongBits(Math.random()));
	}
	
	/**
	 * Ritorna un valore casuale 0<rnd.nextInt()<LATO_DELLA_MAPPA
	 * @return
	 */
	public static int getNewRandomIntValueOnMyMap(int latoDellaMappa) {
		Random rnd = new Random();
		return rnd.nextInt(latoDellaMappa);
	}
	
	/**
	 * Trasla la Y di modo da far corrispondere una matrice a un piano cartesiano.
	 * La x rimane uguale quindi non ha bisogno di ulteriori rifiniture.
	 * La traslazione viene fatta a bassissimo livello, di modo da limitare la complessità per la logica di alto livello.
	 * Viene chiamata da oggetti come Mappa. (serve il server)
	 * @param yToTranslate
	 * @param modulus
	 * @return
	 */
	public static int translateYforServer (int yToTranslate, int modulus) {
		return (modulus-1) - yToTranslate;
	}
	
	/**
	 * Trasla la Y di modo da far corrispondere una matrice a un piano cartesiano.
	 * La x rimane uguale quindi non ha bisogno di ulteriori rifiniture.
	 * La traslazione viene fatta a bassissimo livello, di modo da limitare la complessità per la logica di alto livello.
	 * Viene chiamata da logica di alto livello come Logica. (serve il client)
	 * @param yToTranslate
	 * @param modulus
	 * @return
	 */
	public static int translateYforClient(int yToTranslate, int modulus) {
		return modulus + yToTranslate;
	}
	
	/**
	 * Fa la sottrazione per le coordinate, che rimangano nel range specificato.
	 * @param coord
	 * @param rangeVista
	 * @return
	 */
	public static int doSubtraction(int coord, int rangeVista, int latoDellaMappa) {
		int subtraction = (coord - rangeVista);
		if (subtraction<0) return 0;
		else if (subtraction>=latoDellaMappa) return (latoDellaMappa-1);
		else return subtraction;
	}
	/**
	 * Fa l'addizione per le coordinate, che rimangano in range.
	 * @param coord
	 * @param rangeVista
	 * @return
	 */
	public static int doAddition(int coord, int rangeVista, int latoDellaMappa) {
		int addition = (coord + rangeVista);
		if (addition<0) return 0;
		else if (addition>=latoDellaMappa) return (latoDellaMappa-1);
		else return addition;
	}
	
	/**
	 * Restituisce una coordinata a caso sulla mappa.
	 * @param latoDellaMappa
	 * @return
	 */
	public static Coord getNewRandomCoord(int latoDellaMappa) { return new Coord(
			(CommonUtils.getNewRandomIntValueOnMyMap(latoDellaMappa)),
			(CommonUtils.getNewRandomIntValueOnMyMap(latoDellaMappa)));
	}
}