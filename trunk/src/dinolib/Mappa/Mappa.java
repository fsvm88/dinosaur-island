package dinolib.Mappa;

import java.util.Iterator;
import java.util.NoSuchElementException;

import dinolib.CommonUtils;


/**
 * @author  fabio
 */
public class Mappa implements Iterable<Cella> {
	/**
	 * La mappa a celle, contiene tutta la mappa in celle. Attenzione! Il sistema di conteggio è un sistema di coordinate cartesiane! Non una matrice! Verrà usata come piano cartesiano anche se l'accesso verrà effettuato come una matrice. Le colonne tengono la stessa numerazione (l'ascissa non cambia). Le righe invece hanno la seguente numerazione: 0 indica la riga più in basso (il bordo in basso della mappa), 39 indica la riga più in alto (il bordo in alto della mappa).
	 * @uml.property  name="MappaACelle"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Cella[][] MappaACelle;

	/**
	 * Costruttore protetto per soddisfare la sottoclasse Cella
	 */
	protected Mappa() { }

	/**
	 * Percentuale della mappa che deve essere composta di acqua.
	 * @uml.property  name="FIXED_WATER_PERCENT" readOnly="true"
	 */
	private final int fixed_WATER_PERCENT = 20;

	/**
	 * Percentuale della mappa che deve essere composta di terra. Viene ricavata dalla percentuale d'acqua.
	 * @uml.property  name="FIXED_EARTH_PERCENT" readOnly="true"
	 */
	private final int fixed_EARTH_PERCENT = (100 - this.fixed_WATER_PERCENT);

	/**
	 * Dichiara la variabile che verrà usata per gestire il lato della mappa in tutta la classe.
	 * @uml.property  name="latoDellaMappa"
	 */
	private int latoDellaMappa = 0;


	/**
	 * Costruttore per la mappa. Richiede il lato della nuova mappa come argomento.
	 */
	public Mappa(int lato) {
		latoDellaMappa = lato;
		calcolaNumeroCelleDaPercentuali();
		popolaMappa();
	}

	/**
	 * Contatore per le celle di acqua, indica quante celle di acqua ci sono sulla mappa.
	 * @uml.property  name="conteggioAcquaStatico"
	 */
	private int conteggioAcquaStatico = 0;

	/**
	 * Contatore per le celle di terra, indica quante celle di terra ci sono sulla mappa.
	 * @uml.property  name="conteggioTerraStatico"
	 */
	private int conteggioTerraStatico = 0;

	/**
	 * Restituisce il lato della mappa.
	 * @uml.property  name="latoDellaMappa"
	 */
	public int getLatoDellaMappa() {
		return latoDellaMappa;
	}

	/**
	 * Calcola il numero di Celle con il lato fornito.
	 */
	private int calcolaTotaleCelle () {
		return (latoDellaMappa*latoDellaMappa);
	}

	/**
	 * Calcola il numero di Celle per di Acqua e Terra sulla Mappa.
	 */
	private void calcolaNumeroCelleDaPercentuali () {
		conteggioAcquaStatico = ( ( calcolaTotaleCelle() / 100 ) * fixed_WATER_PERCENT );
		conteggioTerraStatico = calcolaTotaleCelle() - conteggioAcquaStatico;
	}

	/**
	 * Implementa la creazione di una nuova mappa.
	 */
	private void popolaMappa() {
		// TODO : scrivere popolaMappa.
	}

	/**
	 * Interroga la cella per vedere se è occupata da dinosauro
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isOccupataDaDinosauro(int x, int y) {
		if (MappaACelle[x][CommonUtils.translateYforServer(y, getLatoDellaMappa())].toString().equals("CellaConDinosauro")) return true;
		else return false;
	}

	/**
	 * Interroga la cella per vedere se è libera (aka terra semplice).
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isLibera(int x, int y) {
		if (MappaACelle[x][CommonUtils.translateYforServer(y, getLatoDellaMappa())].toString().equals("Terra")) return true;
		else return false;
	}

	/**
	 * Interroga la cella per sapere di che tipo è. 
	 * Helper molto generico per qualunque tipo di cella.
	 * @param x
	 * @param y
	 * @return
	 */
	public String getTipoCella(int x, int y) {
		return MappaACelle[x][CommonUtils.translateYforServer(y, getLatoDellaMappa())].toString();
	}

	/**
	 * Interroga la cella per sapere se è di tipo acqua.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isAcqua(int x, int y) {
		if (MappaACelle[x][CommonUtils.translateYforServer(y, getLatoDellaMappa())].toString().equals("Acqua")) return true;
		else return false;
	}

	/**
	 * Inserisce un dinosauro sulla mappa.
	 * Passa il tipo corrente della cella così che venga tenuta valida.
	 */
	public void spawnDinosauro(int x, int y, String idDinosauroOccupante, Cella cellaSuCuiSiTrova) {
		MappaACelle[x][CommonUtils.translateYforServer(y, getLatoDellaMappa())] = new CellaConDinosauro(idDinosauroOccupante, cellaSuCuiSiTrova);
	}

	/**
	 * Restituisce la cella richiesta.
	 */
	public Cella getCella(int x, int y) {
		return MappaACelle[x][CommonUtils.translateYforServer(y, getLatoDellaMappa())];
	}

	/**
	 * Rimuove il dinosauro dalla cella corrente e reimposta la cella al suo vecchio valore.
	 */
	public void rimuoviIlDinosauroDallaCella(int x, int y) {
		int tempY = CommonUtils.translateYforServer(y, getLatoDellaMappa());
		if ( (MappaACelle[x][tempY].getIdDelDinosauro() != null) &&
				(MappaACelle[x][tempY].getCellaSuCuiSiTrova() != null) ) {
			Cella vecchiaCella = MappaACelle[x][tempY].getCellaSuCuiSiTrova();
			MappaACelle[x][tempY] = vecchiaCella;
		}
	}

	/**
	 * Aggiorna le celle su cambio del turno.
	 */
	private void aggiornaCelle() {

	}


	/**
	 * Aggiorna la mappa su cambio del turno.
	 */
	public void aggiornaSuTurno() {
		aggiornaCelle();
	}

	@Override
	public Iterator<Cella> iterator() {
		return new MapIterator();
	}

	private class MapIterator implements Iterator<Cella> {
		private int rowCount;
		private int columnCount;
		private int latoDellaMappaIterator;

		MapIterator() {
			latoDellaMappaIterator = getLatoDellaMappa();
			rowCount = (latoDellaMappaIterator-1);
			columnCount = 0;
		}

		@Override
		public boolean hasNext() {
			return (0 <= rowCount);
		}

		@Override
		public Cella next() {
			Cella tempCella = null;
			if (0 <= rowCount) {
				if (columnCount < latoDellaMappaIterator) {
					tempCella = getCella(columnCount, rowCount);
				}
				else {
					columnCount = 0;
					rowCount--;
					tempCella = getCella(columnCount, rowCount);
				}
				columnCount++;
				return tempCella;
			}
			else throw new NoSuchElementException();
		}

		@Override
		public void remove() { throw new UnsupportedOperationException(); }
	}
}