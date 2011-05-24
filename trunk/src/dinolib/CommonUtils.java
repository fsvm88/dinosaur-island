package dinolib;

import java.util.Random;

public class CommonUtils {
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
}