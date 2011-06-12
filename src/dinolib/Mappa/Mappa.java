package dinolib.Mappa;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

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
	 * Percentuale della mappa che deve essere composta di vegetazione
	 * @uml.property name="FIXED_FLORA_PERCENT" readOnly="true"
	 */
	private final int fixed_FLORA_PERCENT = 40;
	/**
	 * Numero di carogne sempre presenti sulla mappa.
	 * ATTENZIONE! Questo è un numero! NON una percentuale!
	 * @uml.property name="FIXED_SOD_COUNT" readOnly="true"
	 */
	private final int fixed_SOD_COUNT = 20;
	/**
	 * Contiene il numero minimo di celle acqua presenti in un gruppo.
	 * @uml.property name="MIN_GRUPPO_ACQUA" readOnly="true"
	 */
	private final int min_GRUPPO_ACQUA = 5;
	/**
	 * Contiene il numero massimo di celle acqua presenti in un gruppo.
	 * @uml.property name="MAX_GRUPPO_ACQUA" readOnly="true"
	 */
	private final int max_GRUPPO_ACQUA = 15;
	

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
	 * Contatore per le celle vegetazione, indica quante celle vegetazione ci devono essere sulla mappa.
	 * @uml.property name="conteggioVegetazioneStatico" 
	 */
	private int conteggioVegetazioneStatico = 0;
	
	/**
	 * Contatore per le celle di acqua, indica quante celle di acqua ci devono essere sulla mappa.
	 * @uml.property  name="conteggioAcquaStatico"
	 */
	private int conteggioAcquaStatico = 0;

	/**
	 * Contatore per le celle di terra, indica quante celle di terra ci devono essere sulla mappa.
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
		conteggioVegetazioneStatico = ((calcolaTotaleCelle() / 100) * fixed_FLORA_PERCENT );
	}
	
	/**
	 * Controlla se la cella richiesta è una terra. Utile per semplificare popolaMappa().
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isCellaTerra(int x, int y) {
		return this.getCella(x, y).toString().toLowerCase().equals("terra");
	}

	private ArrayList<Coord> getListOfNearbyEarthCells(int x, int y, int countCells) {
		ArrayList
	}
	
	private void allocaAcqua(int numeroCelleNelGruppo) {
		int x = (1+CommonUtils.getNewRandomIntValueOnMyMap(getLatoDellaMappa()-2));
		int y = (1+CommonUtils.getNewRandomIntValueOnMyMap(getLatoDellaMappa()-2));
		int curCount = 0;
		if (isCellaTerra(x, y)) {
			while (curCount<numeroCelleNelGruppo) {
				if (isCellaTerra(x+1, y)) {
					MappaACelle[x+1][y] = new Acqua();
					curCount++;
				}
				else if (isCellaTerra(x-1, y)) {
					MappaACelle[x-1][y] = new Acqua();
					curCount++;
				}
				else if (isCellaTerra(x, y+1))
			}
		}
	}
	
	/**
	 * Implementa la creazione di una nuova mappa.
	 */
	private void popolaMappa() {
		// TODO : scrivere popolaMappa.
		/* Conteggio celle acqua attualmente piazzate */
		int curAcqua = 0;
		/* righe */
		int i = 0;
		/* colonne */
		int j = 0;
		/* Bordo sinistro e destro (colonne 0 e latoDellaMappa-1) */
		while (i<getLatoDellaMappa()) {
			MappaACelle[i][0] = new Acqua();
			MappaACelle[i][(getLatoDellaMappa()-1)] = new Acqua();
			curAcqua+=2;
			i++;
		}
		/* Bordo in alto e in basso (righe 0 e latoDellaMappa-1) */
		j = 0;
		while (j<getLatoDellaMappa()) {
			MappaACelle[0][j] = new Acqua();
			MappaACelle[(getLatoDellaMappa()-1)][j] = new Acqua();
			curAcqua+=2;
			j++;
		}
		/* Conteggio cella terra attualmente piazzate */
		int curTerre = 0;
		/* Riempio di terre il resto della mappa: righe 0 < i < latoDellaMappa-2, colonne 0 < j < latoDellaMappa-2 */
		i = 1;
		while (i<(getLatoDellaMappa()-2)) {
			j = 1;
			while (j<(getLatoDellaMappa()-2)) {
				MappaACelle[i][j] = new Terra();
				curTerre++;
			}
		}
		/* Alloco le acque rimanenti.
		 * Evito un underflow di acque disponibili allocandone fino a raggiungere il massimo per la mappa meno il massimo di un gruppo. */
		while (curAcqua<(conteggioAcquaStatico-max_GRUPPO_ACQUA)) {
			allocaAcqua(min_GRUPPO_ACQUA+CommonUtils.getNewRandomIntValueOnMyMap(max_GRUPPO_ACQUA-min_GRUPPO_ACQUA+1));
		}
		/* Alloco le acque rimanenti dal passo precedente. È per evitare un underflow di acque disponibili. */
		allocaAcqua(conteggioAcquaStatico-curAcqua);
		/* Devo controllare che tutte le celle di terra siano semplicemente connesse, nel caso riporto il primo errore di connessione e lo correggo */
		while (!isSemplicementeConnesso()) {
			sistemaErroreDiConnessione(trovaPrimoErroreDiConnessione());
		}
		/* Ho sistemato tutti gli errori di connessione, ora le terre sono semplicemente connesse.
		* Ora controllo che il conteggio di acque e terre sia giusto */
		while (!conteggioTerraEAcquaGiusto()) {
			sistemaErroreDiConteggio();
		}
		/* Ho sistemato gli errori di conteggio, ora la mappa ha terre e acque nel numero giusto.
		 * In più le terre sono semplicemente connesse e le acque sono in gruppi in numero giusto.
		 * Ora penso alla vegetazione. */
		int curVeg = 0;
		int x = 0;
		int y = 0;
		while (curVeg<conteggioVegetazioneStatico) {
			x = CommonUtils.getNewRandomIntValueOnMyMap(getLatoDellaMappa());
			y = CommonUtils.getNewRandomIntValueOnMyMap(getLatoDellaMappa());
			if (isCellaTerra(x, y)) {
				MappaACelle[x][y] = new Vegetazione((150+CommonUtils.getNewRandomIntValueOnMyMap(201)));
				curVeg++;
			}
			else continue;
		}
		/* Ho completato l'inserimento della vegetazione, ora penso alle carogne. */
		int curCarogne = 0;
		while (curCarogne<fixed_SOD_COUNT) {
			x = CommonUtils.getNewRandomIntValueOnMyMap(getLatoDellaMappa());
			y = CommonUtils.getNewRandomIntValueOnMyMap(getLatoDellaMappa());
			if (isCellaTerra(x, y)) {
				MappaACelle[x][y] = new Carogna((350+CommonUtils.getNewRandomIntValueOnMyMap(301)));
				curCarogne++;
			}
			else continue;
		}
		/* Ho completato l'inserimento delle carogne.
		 * La mappa è completa.
		 * Attenzione! Devo aggiornare il numero di carogne ogni volta!!!
		 */
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

	/**
	 * Restituisce un iteratore sulle celle della mappa.
	 * Lo fa usando l'ultima riga della matrice come riga zero della mappa.
	 * Quindi scorre dal basso verso l'alto, restituendo le celle in ordine come le vuole la logica client (non avrebbe avuto senso esporle in modo diverso).
	 */
	@Override
	public Iterator<Cella> iterator() {
		return new MapIterator();
	}

	private class MapIterator implements Iterator<Cella> {
		private int rowCount;
		private int columnCount;
		private int latoDellaMappaIterator;

		MapIterator() {
			this.latoDellaMappaIterator = getLatoDellaMappa();
			this.rowCount = (this.latoDellaMappaIterator-1);
			this.columnCount = 0;
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
	
	/**
	 * Restituisce un iteratore sulle celle per una porzione richiesta di Mappa.
	 * È uguale a MapIterator nella logica di funzionamento.
	 * @param fromX
	 * @param fromY
	 * @param rangeX
	 * @param rangeY
	 * @return
	 */
	public Iterator<Cella> subIterator(int fromX, int fromY, int rangeX, int rangeY) {
		return new subMapIterator(fromX, CommonUtils.translateYforClient(fromY, getLatoDellaMappa()), rangeX, rangeY);
	}
	
	private class subMapIterator implements Iterator<Cella> {
		private int startRow;
		private int startColumn;
		private int rowRange;
		private int columnRange;
		private int rowCount;
		private int columnCount;
		private int latoDellaMappaIterator;
		
		subMapIterator(int startX, int startY, int rangeX, int rangeY) {
			this.latoDellaMappaIterator = getLatoDellaMappa();
			if (!(0 <= startX) ||
					!(0 <= startY) ||
					!(startX < latoDellaMappaIterator) ||
					!(startY < latoDellaMappaIterator) ||
					!(0 <= rangeX) ||
					!(0 <= rangeY)) throw new IndexOutOfBoundsException();
			this.startRow = startY;
			this.startColumn = startX;
			this.rowRange = rangeY;
			this.columnRange = rangeX;
			this.latoDellaMappaIterator = getLatoDellaMappa();
			rowCount = startRow;
			columnCount = 0;
			if (0 <= startX) throw new IndexOutOfBoundsException();
		}

		private boolean rowIsInRange() {
			return (((startRow-rowRange) <= rowCount) &&
					(0 <= rowCount) &&
					(rowCount < latoDellaMappaIterator));
		}
		
		private boolean columnIsInRange() {
			return ((columnCount <= (startColumn+columnRange)) &&
					(0 <= columnCount) &&
					(columnCount < latoDellaMappaIterator));
		}
		
		@Override
		public boolean hasNext() {
			return rowIsInRange();
		}

		@Override
		public Cella next() {
			Cella tempCella = null;
			if (rowIsInRange()) {
				if (columnIsInRange()) {
					tempCella = getCella(columnCount, rowCount);
				}
				else {
					columnCount = startColumn;
					rowCount--;
					tempCella = getCella(columnCount, rowCount);
				}
				columnCount++;
				return tempCella;
			}
			else throw new NoSuchElementException();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}