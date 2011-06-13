package dinolib.Mappa;

import java.util.ArrayList;
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
		conteggioVegetazioneStatico = ((calcolaTotaleCelle() / 100) * fixed_FLORA_PERCENT );
	}

	/**
	 * Controlla se la cella richiesta è una terra. Utile per semplificare popolaMappa().
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isCellaTerra(Coord nCoord) {
		return this.getCella(nCoord).toString().toLowerCase().equals("terra");
	}
	
	/**
	 * Controlla se la cella di coordinate richieste ha celle Acqua adiacenti.
	 * @param nCoord
	 * @return
	 */
	private boolean haCelleAcquaVicine(Coord nCoord) {
		int i = -1;
		int j = -1;
		while (i<2) {
			while (j<2) {
				if (MappaACelle[i+nCoord.getX()][j+nCoord.getY()].toString().toLowerCase().equals("acqua")) return true;
				j++;
			}
			i++;
		}
		return false;
	}

	private void addNearbyEarthCells(Coord startCoord, ArrayList<Coord> myArray, int countCells, int maxDistance) {
		Coord tempCoord = null;
		int rndX = 0;
		int rndY = 0;
		while (myArray.size()<countCells) {
			rndX = (-maxDistance+CommonUtils.getNewRandomIntValueOnMyMap(1+(2*maxDistance)));
			rndY = (-maxDistance+CommonUtils.getNewRandomIntValueOnMyMap(1+(2*maxDistance)));
			tempCoord = new Coord(startCoord.getX()+rndX, startCoord.getY()+rndY);
			if (!myArray.contains(tempCoord) &&
					isCellaTerra(tempCoord) &&
					!haCelleAcquaVicine(tempCoord)) {
				myArray.add(tempCoord);
			}
		}
	}

	/**
	 * Aggiunge alla lista passata in ingresso un numero richiesto di celle di terra da convertire poi in celle di acqua.
	 * @param myArray
	 * @param nCoord
	 * @param countCells
	 */
	private void getListOfNearbyEarthCells(ArrayList<Coord> myArray, Coord nCoord, int countCells) {
		if (countCells<8) {
			addNearbyEarthCells(nCoord, myArray, countCells, 1);
		}
		else {
			int j = 1;
			while (myArray.size()<countCells) {
				addNearbyEarthCells(nCoord, myArray, countCells, j);
				j++;
			}
		}
	}

	/**
	 * Alloca le celle di acqua sulla mappa.
	 * @param numeroCelleNelGruppo
	 */
	private void allocaAcqua(int numeroCelleNelGruppo) {
		Coord newCoord = new Coord((1+CommonUtils.getNewRandomIntValueOnMyMap(getLatoDellaMappa()-2)),
				(1+CommonUtils.getNewRandomIntValueOnMyMap(getLatoDellaMappa()-2)));
		int curCount = 0;
		ArrayList<Coord> myCells = new ArrayList<Coord>();
		if (isCellaTerra(newCoord)) {
			myCells.add(newCoord);
			getListOfNearbyEarthCells(myCells, newCoord, numeroCelleNelGruppo);
			Iterator<Coord> itOnCoords = myCells.iterator();
			while (itOnCoords.hasNext()) {
				MappaACelle[newCoord.getX()][newCoord.getY()] = new Acqua();
				newCoord = itOnCoords.next();
				curCount++;
			}
		}
	}
	
	/**
	 * Implementa la creazione di una nuova mappa.
	 */
	private void popolaMappa() {
		MappaACelle = new Cella[getLatoDellaMappa()][getLatoDellaMappa()];
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
		/* A questo punto, grazie all'algoritmo con cui vengono piazzate le celle d'acqua,
		 * la mappa è semplicemente connessa per quanto riguarda le terre e rispetta
		 * il conteggio di acque e terre prefissato. */
		/* Ora penso alla vegetazione. */
		int curVeg = 0;
		while (curVeg<conteggioVegetazioneStatico) {
			Coord vegCoord = new Coord((CommonUtils.getNewRandomIntValueOnMyMap(getLatoDellaMappa())),
					CommonUtils.getNewRandomIntValueOnMyMap(getLatoDellaMappa()));
			if (isCellaTerra(vegCoord)) {
				MappaACelle[vegCoord.getX()][vegCoord.getY()] = new Vegetazione((150+CommonUtils.getNewRandomIntValueOnMyMap(201)));
				curVeg++;
			}
			else continue;
		}
		/* Ho completato l'inserimento della vegetazione, ora penso alle carogne. */
		int curCarogne = 0;
		while (curCarogne<fixed_SOD_COUNT) {
			Coord carCoord = new Coord((CommonUtils.getNewRandomIntValueOnMyMap(getLatoDellaMappa())),
					CommonUtils.getNewRandomIntValueOnMyMap(getLatoDellaMappa()));
			if (isCellaTerra(carCoord)) {
				MappaACelle[carCoord.getX()][carCoord.getY()] = new Carogna((350+CommonUtils.getNewRandomIntValueOnMyMap(301)));
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
	public void spawnDinosauro(Coord myCoord, String idDinosauroOccupante, Cella cellaSuCuiSiTrova) {
		MappaACelle[myCoord.getX()][CommonUtils.translateYforServer(myCoord.getY(), getLatoDellaMappa())] = new CellaConDinosauro(idDinosauroOccupante, cellaSuCuiSiTrova);
	}

	/**
	 * Restituisce la cella richiesta.
	 */
	public Cella getCella(Coord myCoord) {
		return MappaACelle[myCoord.getX()][CommonUtils.translateYforServer(myCoord.getY(), getLatoDellaMappa())];
	}

	/**
	 * Rimuove il dinosauro dalla cella corrente e reimposta la cella al suo vecchio valore.
	 */
	public void rimuoviIlDinosauroDallaCella(Coord coordToRemove) {
		int tempY = CommonUtils.translateYforServer(coordToRemove.getY(), getLatoDellaMappa());
		if ( (MappaACelle[coordToRemove.getX()][tempY].getIdDelDinosauro() != null) &&
				(MappaACelle[coordToRemove.getX()][tempY].getCellaSuCuiSiTrova() != null) ) {
			Cella vecchiaCella = MappaACelle[coordToRemove.getX()][tempY].getCellaSuCuiSiTrova();
			MappaACelle[coordToRemove.getX()][tempY] = vecchiaCella;
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
	
	private Cella getCellaForIterator(Coord myCoords) {
		return MappaACelle[myCoords.getX()][myCoords.getY()];
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
		private Coord curCoord = null;
		private int latoDellaMappaIterator;

		MapIterator() {
			this.latoDellaMappaIterator = getLatoDellaMappa();
			curCoord = new Coord(0, (this.latoDellaMappaIterator-1));
		}

		@Override
		public boolean hasNext() {
			return (0 <= curCoord.getY());
		}

		@Override
		public Cella next() {
			Cella tempCella = null;
			if (hasNext()) {
				if (curCoord.getX() < latoDellaMappaIterator) {
					tempCella = getCellaForIterator(curCoord);
				}
				else {
					curCoord = new Coord(0, ((curCoord.getY())-1));
					tempCella = getCellaForIterator(curCoord);
				}
				curCoord = new Coord((curCoord.getX()+1), curCoord.getY());
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
	public Iterator<Cella> subIterator(Coord myCoords, int rangeX, int rangeY) {
		return new subMapIterator(new Coord(myCoords.getX(), CommonUtils.translateYforClient(myCoords.getY(), getLatoDellaMappa())), rangeX, rangeY);
	}

	private class subMapIterator implements Iterator<Cella> {
		private int startRow;
		private int startColumn;
		private int rowRange;
		private int columnRange;
		private Coord curCoord = null;
		private int latoDellaMappaIterator;

		subMapIterator(Coord startCoord, int rangeX, int rangeY) {
			this.latoDellaMappaIterator = getLatoDellaMappa();
			if (!(0 <= startCoord.getX()) ||
					!(0 <= startCoord.getY()) ||
					!(startCoord.getX() < latoDellaMappaIterator) ||
					!(startCoord.getY() < latoDellaMappaIterator) ||
					!(0 <= rangeX) ||
					!(0 <= rangeY)) throw new IndexOutOfBoundsException();
			this.startRow = startCoord.getY();
			this.startColumn = startCoord.getX();
			this.rowRange = rangeY;
			this.columnRange = rangeX;
			this.latoDellaMappaIterator = getLatoDellaMappa();
			curCoord = new Coord(startColumn, startRow);
		}

		private boolean rowIsInRange() {
			return (((startRow-rowRange) <= curCoord.getY()) &&
					(0 <= curCoord.getY()) &&
					(curCoord.getY() < latoDellaMappaIterator));
		}

		private boolean columnIsInRange() {
			return (((curCoord.getX()) <= (startColumn+columnRange)) &&
					(0 <= curCoord.getX()) &&
					(curCoord.getX() < latoDellaMappaIterator));
		}

		@Override
		public boolean hasNext() {
			return rowIsInRange();
		}

		@Override
		public Cella next() {
			Cella tempCella = null;
			if (hasNext()) {
				if (columnIsInRange()) {
					tempCella = getCellaForIterator(curCoord);
				}
				else {
					curCoord = new Coord(startColumn, (curCoord.getY()-1));
					tempCella = getCellaForIterator(curCoord);
				}
				curCoord = new Coord((curCoord.getX()+1), curCoord.getY());
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