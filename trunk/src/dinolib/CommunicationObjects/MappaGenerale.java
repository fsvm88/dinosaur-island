package dinolib.CommunicationObjects;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import dinolib.Logica;
import dinolib.Exceptions.InvalidTokenException;
import dinolib.GameObjects.Cella;

public class MappaGenerale implements Serializable {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = 6166979583603547189L;
	/**
	 * Contiene le celle nella vista locale, ordinate da sinistra a destra, dal basso in alto.
	 * @uml.property name="celleNellaVista"
	 */
	private List<Character> celleMappa = null;
	/**
	 * Contiene il lato della mappa, da esportare quando l'oggetto viene letto.
	 * @uml.property name="latoDellaMappa"
	 */
	private Integer latoDellaMappa = null;
	public MappaGenerale(Logica myLogica, String token) throws InvalidTokenException {
		latoDellaMappa = myLogica.getMappa().getLatoDellaMappa();
		Iterator<Cella> itCelle = myLogica.getMappa().iterator();
		int i = 0;
		Cella tempCella = null;
		Character tipoCella = null;
		while (itCelle.hasNext()) {
			do {
				tempCella = itCelle.next();
				tipoCella = null;
				if (tempCella.isUserPassed(myLogica.getCMan().getName(token))) {
					tipoCella = tempCella.toString().toLowerCase().charAt(0);
					if (tipoCella.equals('d')) {
						tipoCella = tempCella.getCellaSuCuiSiTrova().toString().toLowerCase().charAt(0);
					}
					if ((tipoCella.equals('t')) ||
							(tipoCella.equals('a')) ||
							(tipoCella.equals('v'))) {
						celleMappa.add(tempCella.toString().toLowerCase().charAt(0));
					}
					else if (tipoCella.equals('c')) {
						celleMappa.add('t');
					}
				}
				else {
					celleMappa.add('b');
				}
				i++;
			} while (i < myLogica.getMappa().getLatoDellaMappa());
		}
	}
	/**
	 * Restituisce la lista delle celle della mappa generale.
	 * @return La lista delle celle.
	 */
	List<Character> getCelleMappa() { return celleMappa; }
	/**
	 * Restituisce il lato della mappa della mappa generale.
	 * @return Il lato della mappa.
	 */
	Integer getLatoDellaMappa() { return latoDellaMappa; }
	/**
	 * Aggiunge al buffer una singola cella della mappa.
	 * Non include informazioni addizionali se questa è vegetazione o carogna.
	 * @param tmpBuf Il buffer corrente a cui aggiungere la stringa.
	 * @param myChar Il tipo della cella corrente.
	 * @return Una stringa contenente il buffer in ingresso con in coda la cella richiesta.
	 */
	private String aggiungiCellaSingolaSenzaInfoAddizionali(Character myChar) { return "[" + myChar.charValue() + "]"; }
	
	/**
	 * Restituisce un iteratore sulle celle contenute nella mappa generale.
	 * @return 
	 * @return L'iteratore sulle celle.
	 */
	Iterator<Character> iterator() { return celleMappa.iterator(); }
	
	@Override
	public String toString() {
		String headBuffer = "{" + getLatoDellaMappa() + "," + getLatoDellaMappa() + "}";
		String tailBuffer = null;
		Iterator<Character> itCelle = iterator();
		int i = 0;
		while (itCelle.hasNext()) {
			do {
				tailBuffer += aggiungiCellaSingolaSenzaInfoAddizionali(itCelle.next());
				i++;
			} while (i < getLatoDellaMappa());
			tailBuffer += ";";
			i = 0;
		}
		return headBuffer + "," + tailBuffer;
	}
}
