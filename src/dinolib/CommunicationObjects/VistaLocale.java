package dinolib.CommunicationObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import dinolib.CommonUtils;
import dinolib.Logica;
import dinolib.GameObjects.Cella;
import dinolib.GameObjects.Coord;
import dinolib.GameObjects.Dinosauro;

public class VistaLocale implements Serializable {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -8763927193500330660L;
	/**
	 * Contiene la coordinata del punto in basso a sinistra da cui restituisco la vista locale.
	 * @uml.property name="vistaLocale"
	 */
	private Coord startCoord = null;

	/**
	 * Contiene il numero di righe nella vista locale dal punto in basso a sinistra.
	 * @uml.property name="numeroRighe"
	 */
	private Integer numeroRighe = null;
	/**
	 * Contiene il numero di colonne nella vista locale dal punto in basso a sinistra.
	 * @uml.property name="numeroColonne"
	 */
	private Integer numeroColonne = null;
	/**
	 * Contiene le celle nella vista locale, ordinate da sinistra a destra, dal basso in alto.
	 * @uml.property name="celleNellaVista"
	 */
	private List<Cella> celleNellaVista = null;
	
	public VistaLocale(Logica myLogica, Dinosauro tempDinosauro) {
		celleNellaVista = Collections.synchronizedList(new ArrayList<Cella>());
		int rangeVista = tempDinosauro.getRangeVista();
		int leftCornerX = CommonUtils.doSubtraction(tempDinosauro.getCoord().getX(), rangeVista, myLogica.getMappa().getLatoDellaMappa());
		int bottomCornerY = CommonUtils.doSubtraction(tempDinosauro.getCoord().getY(), rangeVista, myLogica.getMappa().getLatoDellaMappa());
		int rightCornerX = CommonUtils.doAddition(tempDinosauro.getCoord().getX(), rangeVista, myLogica.getMappa().getLatoDellaMappa());
		int topCornerY = CommonUtils.doAddition(tempDinosauro.getCoord().getY(), rangeVista, myLogica.getMappa().getLatoDellaMappa());
		startCoord = new Coord(leftCornerX, bottomCornerY);
		numeroRighe = (topCornerY-bottomCornerY);
		numeroColonne = (rightCornerX-leftCornerX);
		Iterator<Cella> subItCelle = myLogica.getMappa().subIterator(startCoord, numeroRighe.intValue(), numeroColonne.intValue());
		int row = bottomCornerY;
		int col = leftCornerX;
		do {
			do {
				if (subItCelle.hasNext()) {
					celleNellaVista.add(subItCelle.next());
					col++;
				}
				else throw new NoSuchElementException();
			} while (col <= rightCornerX);
			row++;
			col = leftCornerX;
		} while (row <= topCornerY);
	}
	/**
	 * Restituisce la coordinata iniziale (il punto in basso a sinistra da cui cominciano le celle).
	 * @return La coordinata iniziale.
	 */
	Coord getStartCoord() { return startCoord; }
	/**
	 * Restituisce il numero di righe della vista locale.
	 * @return Il numero di righe della vista.
	 */
	Integer getNumeroRighe() { return numeroRighe; }
	/**
	 * Restituisce il numero di colonne della vista locale.
	 * @return Il numero di colonne della vista.
	 */
	Integer getNumeroColonne() { return numeroColonne; }
	/**
	 * Restituisce la lista di celle nella vista locale.
	 * @return La lista di celle nella vista.
	 */
	List<Cella> getCelleNellaVista() { return celleNellaVista; }
	/**
	 * Restituisce un iteratore sulle celle contenute nella vista locale.
	 * @return L'iteratore sulle celle nella vista locale.
	 */
	Iterator<Cella> iterator() { return celleNellaVista.iterator(); }
	
	/**
	 * Aggiunge al buffer una singola cella della mappa.
	 * Aggiunge anche informazioni addizionali se si tratta di carogna, vegetazione o dinosauro.
	 * @param tmpBuf Il buffer in ingresso a cui voglio aggiungere la cella.
	 * @param myChar Il tipo della cella che voglio aggiungere.
	 * @param myInfo L'informazione addizionale sulla cella che voglio aggiungere.
	 * @return Una stringa contenente il buffer in input con in coda la cella richiesta. 
	 */
	private String aggiungiCellaSingolaConInfoAddizionali(String tmpBuf, Character myChar, Object myInfo) { return tmpBuf + "[" + myChar.charValue() + "," + myInfo.toString() + "]"; }
	
	/**
	 * Aggiunge al buffer una singola cella della mappa.
	 * Non include informazioni addizionali se questa è vegetazione o carogna.
	 * @param tmpBuf Il buffer corrente a cui aggiungere la stringa.
	 * @param myChar Il tipo della cella corrente.
	 * @return Una stringa contenente il buffer in ingresso con in coda la cella richiesta.
	 */
	private String aggiungiCellaSingolaSenzaInfoAddizionali(String tmpBuf, Character myChar) { return tmpBuf + "[" + myChar.charValue() + "]"; }
	
	/**
	 * Aggiunge al buffer una cella.
	 * @param buffer Il buffer a cui voglio aggiungere la cella.
	 * @param miaCella La cella che voglio venga aggiunta al buffer.
	 * @return Una stringa con il buffer in input e la cella desiderata in coda.
	 */
	private String assemblaBufferCellaSingolaPerVistaLocale(String buffer, Cella miaCella) {
		Character tipoCella = miaCella.toString().toLowerCase().charAt(0);
		if (tipoCella.equals('t') ||
				tipoCella.equals('a')) return aggiungiCellaSingolaSenzaInfoAddizionali(buffer, tipoCella);
		else if (tipoCella.equals('v') ||
				tipoCella.equals('c')) return aggiungiCellaSingolaConInfoAddizionali(buffer, tipoCella, miaCella.getValoreAttuale());
		else if (tipoCella.equals('d')) return aggiungiCellaSingolaConInfoAddizionali(buffer, tipoCella, miaCella.getIdDelDinosauro());
		return null;
	}
	
	@Override
	public String toString() {
		String headBuffer = "{" + getStartCoord().getX() + "," + getStartCoord().getY() + "},{" + getNumeroRighe() + "," + getNumeroColonne() + "}";
		Iterator<Cella> itCelleVista = this.iterator();
		String tailBuffer = null;
		int row = startCoord.getY();
		int col = startCoord.getX();
		do {
			do {
				if (itCelleVista.hasNext()) {
					tailBuffer = assemblaBufferCellaSingolaPerVistaLocale(tailBuffer, itCelleVista.next());
					col++;
				}
				else throw new NoSuchElementException();
			} while (col <= (startCoord.getX()+getNumeroColonne().intValue()));
			tailBuffer = tailBuffer + ";";
			row++;
			col = startCoord.getX();
		} while (row <= (startCoord.getY()+getNumeroRighe().intValue()));
		return headBuffer + "," + tailBuffer;
	}
}
