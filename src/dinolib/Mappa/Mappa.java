package dinolib.Mappa;

import java.util.HashSet;
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
	public Mappa(int lato) { // Testato
		latoDellaMappa = lato;
		calcolaNumeroCelleDaPercentuali();
		System.runFinalization();
		System.gc();
		popolaMappa();
		System.runFinalization();
		System.gc();
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
	private int calcolaTotaleCelle () { return (latoDellaMappa*latoDellaMappa); } // Testato - chiamato da un chiamato dal costruttore

	/**
	 * Calcola il numero di Celle per di Acqua e Terra sulla Mappa.
	 */
	private void calcolaNumeroCelleDaPercentuali () { // Testato - col costruttore
		conteggioAcquaStatico = (int) ( ( calcolaTotaleCelle() / 100 ) * fixed_WATER_PERCENT );
		conteggioVegetazioneStatico = (int) ((calcolaTotaleCelle() / 100) * fixed_FLORA_PERCENT );
	}

	/**
	 * Controlla se la cella richiesta è una terra. Utile per semplificare popolaMappa().
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isCellaTerra(Coord nCoord) { // Testato da popolaMappa
		return MappaACelle[nCoord.getX()][nCoord.getY()].toString().toLowerCase().equals("terra");
	}

	private boolean isCellaAcqua(Coord nCoord) { // Testato da popolaMappa
		return MappaACelle[nCoord.getX()][nCoord.getY()].toString().toLowerCase().equals("acqua");
	}

	/**
	 * Controlla se la cella di coordinate richieste ha celle Acqua adiacenti.
	 * @param nCoord
	 * @return
	 */
	private boolean haCelleAcquaVicine(Coord nCoord) { // Testato da popolaMappa
		int i = -1;
		int j = -1;
		while (i<2) {
			j = -1;
			while (j<2) {
				if (isCellaAcqua(new Coord(nCoord.getX()+i, nCoord.getY()+j))) return true;
				j++;
			}
			i++;
		}
		return false;
	}

	private void getNearbyEarthCells(Coord myCoord, HashSet<Coord> tempSet) { // Testato da popolaMappa
		int i = -1;
		int j = -1;
		Coord tempCoord = null;
		while(i<2) {
			j = -1;
			while (j<2) {
				tempCoord = new Coord(myCoord.getX()+i, myCoord.getY()+j);
				if (isCellaTerra(tempCoord) &&
						!haCelleAcquaVicine(tempCoord)) tempSet.add(tempCoord);
				j++;
			}
			i++;
		}
	}

	private void allocaAcqua(int numeroCelle) { // Testato da popolaMappa
		Coord tempCoord = null;
		HashSet<Coord> mySet = new HashSet<Coord>();
		Iterator<Coord> itCoord = null;
		do {
			tempCoord = getNewRandomCoord();
		} while (!isCellaTerra(tempCoord));
		mySet.add(tempCoord);
		HashSet<Coord> tempSet = new HashSet<Coord>();
		//		System.out.println("[allocaAcqua] sto per entrare nel ciclo per allocare " + numeroCelle + " acque");
		while (mySet.size()<numeroCelle) {
			//				System.out.println("[allocaAcqua] cerco " + (numeroCelle-mySet.size()) + " celle");
			tempSet.clear();
			itCoord = mySet.iterator();
			while (itCoord.hasNext()) {
				getNearbyEarthCells(itCoord.next(), tempSet);
				if (tempSet.size() != 0) break;
			}
			//				System.out.println("[allocaAcqua] trovate " + tempSet.size() + " celle");
			if (tempSet.size() != 0) {
				itCoord = tempSet.iterator();
				while (itCoord.hasNext()) {
					if (mySet.size()>=numeroCelle) break;
					mySet.add(itCoord.next());
				}
				//					System.out.println("[allocaAcqua] ho aggiunto le celle nuove al set.");
			}
			else {
				if (mySet.size()<numeroCelle) return;
			}
		}
		itCoord = mySet.iterator();
		while(itCoord.hasNext()) {
			tempCoord = itCoord.next();
			MappaACelle[tempCoord.getX()][tempCoord.getY()] = new Acqua();
		}
		//		System.out.println("[allocaAcqua] allocate " + mySet.size() + " celle");
	}

	private int contaAcque() { // Testato da popolaMappa
		int i = 0;
		int j = 0;
		int counter = 0;
		while (i<(getLatoDellaMappa()-1)) {
			j = 0;
			while (j<(getLatoDellaMappa()-1)) {
				if (getCellaForIterator(new Coord(i, j)).toString().toLowerCase().equals("acqua")) counter++;
				j++;
			}
			i++;
		}
		return counter;
	}

	private Coord getNewRandomCoord() {
		return new Coord((CommonUtils.getNewRandomIntValueOnMyMap(getLatoDellaMappa())),
				(CommonUtils.getNewRandomIntValueOnMyMap(getLatoDellaMappa())));
	}
	
	/**
	 * Implementa la creazione di una nuova mappa.
	 */
	private void popolaMappa() { // Testato - col costruttore
		//		System.out.println("[popolaMappa] start");
		MappaACelle = new Cella[getLatoDellaMappa()][getLatoDellaMappa()];
		/* Conteggio celle acqua attualmente piazzate */
		/* righe */
		int i = 0;
		/* colonne */
		int j = 0;
		/* Bordo sinistro e destro (colonne 0 e latoDellaMappa-1) */
		while (i<getLatoDellaMappa()) {
			MappaACelle[i][0] = new Acqua();
			MappaACelle[i][(getLatoDellaMappa()-1)] = new Acqua();
			i++;
		}
		/* Bordo in alto e in basso (righe 0 e latoDellaMappa-1) */
		j = 0;
		while (j<getLatoDellaMappa()) {
			MappaACelle[0][j] = new Acqua();
			MappaACelle[(getLatoDellaMappa()-1)][j] = new Acqua();
			j++;
		}
		//		System.out.println("[popolaMappa] allocate le acque sui bordi");
		/* Riempio di terre il resto della mappa: righe 0 < i < latoDellaMappa-2, colonne 0 < j < latoDellaMappa-2 */
		i = 1;
		while (i<=(getLatoDellaMappa()-2)) {
			j = 1;
			while (j<=(getLatoDellaMappa()-2)) {
				MappaACelle[i][j] = new Terra();
				j++;
			}
			i++;
		}
		//		System.out.println("[popolaMappa] allocate tutte le terre all'interno della mappa");
		System.runFinalization();
		System.gc();
		/* Alloco le acque rimanenti.
		 * Evito un underflow di acque disponibili allocandone fino a raggiungere il massimo per la mappa meno il massimo di un gruppo. */
		//		System.out.println("[popolaMappa] sto per allocare le acque");
		int curAcqua = contaAcque();
		while (curAcqua<(conteggioAcquaStatico-max_GRUPPO_ACQUA-min_GRUPPO_ACQUA+9)) {
			//			System.out.println("[popolaMappa] chiamo allocaAcqua");
			allocaAcqua(min_GRUPPO_ACQUA+CommonUtils.getNewRandomIntValueOnMyMap(max_GRUPPO_ACQUA-min_GRUPPO_ACQUA+1));
			curAcqua = contaAcque();
		}
		/* Alloco le acque rimanenti dal passo precedente. È per evitare un underflow di acque disponibili. */
		allocaAcqua(conteggioAcquaStatico-curAcqua);
		//		System.out.println("[popolaMappa] allocate le acque rimanenti");
		/* A questo punto, grazie all'algoritmo con cui vengono piazzate le celle d'acqua,
		 * la mappa è semplicemente connessa per quanto riguarda le terre e rispetta
		 * il conteggio di acque e terre prefissato. */
		/* Ora penso alla vegetazione. */
		//		System.out.println("[popolaMappa] sto per allocare la vegetazione");
		int curVeg = 0;
		while (curVeg<conteggioVegetazioneStatico) {
			Coord vegCoord = getNewRandomCoord();
			if (isCellaTerra(vegCoord)) {
				MappaACelle[vegCoord.getX()][vegCoord.getY()] = new Vegetazione((150+CommonUtils.getNewRandomIntValueOnMyMap(201)));
				curVeg++;
			}
			else continue;
		}
		//		System.out.println("[popolaMappa] allocata la vegetazione");
		/* Ho completato l'inserimento della vegetazione, ora penso alle carogne. */
		//		System.out.println("[popolaMappa] sto per allocare le carogne");
		int curCarogne = 0;
		while (curCarogne<fixed_SOD_COUNT) {
			Coord carCoord = getNewRandomCoord();
			if (isCellaTerra(carCoord)) {
				MappaACelle[carCoord.getX()][carCoord.getY()] = new Carogna((350+CommonUtils.getNewRandomIntValueOnMyMap(301)));
				curCarogne++;
			}
			else continue;
		}
		//		System.out.println("[popolaMappa] allocate le carogne");
		/* Ho completato l'inserimento delle carogne.
		 * La mappa è completa.
		 * Attenzione! Devo aggiornare il numero di carogne ogni volta!!!
		 */
		//		System.out.println("[popolaMappa] termino.");
	}

	/**
	 * Inserisce un dinosauro sulla mappa.
	 * Passa il tipo corrente della cella così che venga tenuta valida.
	 */
	public void spawnDinosauro(Coord myCoord, String idDinosauroOccupante) { // Testato
		Cella tempCella = getCella(myCoord);
		MappaACelle[myCoord.getX()][CommonUtils.translateYforServer(myCoord.getY(), getLatoDellaMappa())] = new CellaConDinosauro(idDinosauroOccupante, tempCella);
	}

	/**
	 * Restituisce la cella richiesta.
	 */
	public Cella getCella(Coord myCoord) { // Testato
		if ((0 <= myCoord.getX()) &&
				(myCoord.getX() < getLatoDellaMappa()) &&
				(0 <= myCoord.getY()) &&
				(myCoord.getY() < getLatoDellaMappa())) 
			return MappaACelle[myCoord.getX()][CommonUtils.translateYforServer(myCoord.getY(), getLatoDellaMappa())];
		else return null;
	}

	/**
	 * Rimuove il dinosauro dalla cella corrente e reimposta la cella al suo vecchio valore.
	 */
	public void rimuoviIlDinosauroDallaCella(Coord coordToRemove) { // Testato
		int tempY = CommonUtils.translateYforServer(coordToRemove.getY(), getLatoDellaMappa());
		if ( (MappaACelle[coordToRemove.getX()][tempY].getIdDelDinosauro() != null) &&
				(MappaACelle[coordToRemove.getX()][tempY].getCellaSuCuiSiTrova() != null) ) {
			Cella vecchiaCella = MappaACelle[coordToRemove.getX()][tempY].getCellaSuCuiSiTrova();
			MappaACelle[coordToRemove.getX()][tempY] = vecchiaCella;
		}
	}
	/**
	 * Aggiorna la mappa su cambio del turno.
	 */
	public void aggiorna() { // Testato
		Iterator<Cella> itCelle = this.iterator();
		while (itCelle.hasNext()) {
			itCelle.next().aggiorna();
		}
	}

	private Cella getCellaForIterator(Coord myCoords) { // Testato
		return MappaACelle[myCoords.getX()][myCoords.getY()];
	}

	/**
	 * Restituisce un iteratore sulle celle della mappa.
	 * Lo fa usando l'ultima riga della matrice come riga zero della mappa.
	 * Quindi scorre dal basso verso l'alto, restituendo le celle in ordine come le vuole la logica client (non avrebbe avuto senso esporle in modo diverso).
	 */
	@Override
	public Iterator<Cella> iterator() { // Testato
		return new MapIterator();
	}

	private class MapIterator implements Iterator<Cella> { // Testato
		private Coord curCoord = null;
		private int latoDellaMappaIterator;

		MapIterator() {
			this.latoDellaMappaIterator = getLatoDellaMappa();
			curCoord = new Coord(0, (this.latoDellaMappaIterator-1));
		}

		@Override
		public boolean hasNext() {
			return !((curCoord.getY() == 0) && (curCoord.getX() == getLatoDellaMappa()-1));
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
	public Iterator<Cella> subIterator(Coord myCoords, int rangeX, int rangeY) { // Testato
		return new subMapIterator(new Coord(myCoords.getX(), CommonUtils.translateYforServer(myCoords.getY(), getLatoDellaMappa())), rangeX, rangeY);
	}

	private class subMapIterator implements Iterator<Cella> { // Testato
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

		private boolean columnIsInRange() {
			return (((curCoord.getX()) <= (startColumn+columnRange)) &&
					(0 <= curCoord.getX()) &&
					(curCoord.getX() < latoDellaMappaIterator));
		}

		@Override
		public boolean hasNext() {
			if ( ((startColumn+columnRange) == curCoord.getX()) &&
					((startRow-rowRange) == curCoord.getY()) ) return false;
			else return true;
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