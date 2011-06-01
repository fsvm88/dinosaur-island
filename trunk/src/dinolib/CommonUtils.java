package dinolib;

import java.util.Random;

class CommonUtils {
	/**
	 * Helper per la generazione di un nuovo token alfanumerico.
	 * @return
	 */
	protected static String getNewToken() {
		return Long.toString(Double.doubleToLongBits(Math.random()));
	}
	
	/**
	 * Ritorna un valore casuale 0<rnd.nextInt()<LATO_DELLA_MAPPA
	 * @return
	 */
	protected static int getNewRandomIntValueOnMyMap(int latoDellaMappa) {
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
	protected static int translateYforServer (int yToTranslate, int modulus) {
		return modulus - yToTranslate;
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
	protected static int translateYforClient(int yToTranslate, int modulus) {
		return modulus + yToTranslate;
	}
}