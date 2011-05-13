package dinolib;
/**
 * @author fabio
 *
 */
public class Mappa {
	private final int FIXED_WATER_PERCENT = 20;
	private final int FIXED_EARTH_PERCENT = (100 - this.FIXED_WATER_PERCENT);
	
	public final int singleTon = 0;
	
	private int latoDellaMappa = 0;
	private int conteggioAcquaStatico = 0;
	private int conteggioTerraStatico = 0;
	private Cella[][] MappaACelle;
	
	Mappa (int lato) {
		latoDellaMappa = lato;
		calcolaNumeroCelleDaPercentuali();
		popolaMappa();
	}

	/**
	 * @return il latoDellaMappa
	 */
	public int getLatoDellaMappa() {
		return latoDellaMappa;
	}
	
	/**
	 * calcola il numero di Celle con il lato fornito
	 */
	private int calcolaCelle () {
		return (latoDellaMappa*latoDellaMappa);
	}
	
	private void calcolaNumeroCelleDaPercentuali () {
		conteggioAcquaStatico = ( ( calcolaCelle() / 100 ) * FIXED_WATER_PERCENT );
		conteggioTerraStatico = calcolaCelle() - conteggioAcqua;
	}
		
	/**
	 * popola la mappa seguendo l'algoritmo descritto sotto
	 */
	private void popolaMappa () {
		/*
		 * passo 1: riempio tutte le Celle di acqua
		 */
		for (int i = 0; i < latoDellaMappa; i++) {
			for (int j = 0; j < latoDellaMappa; j++) {
				MappaACelle[i][j] = new Acqua;
			}
		}
		/*
		 * passo 2: seleziono il punto centrale della mappa e pianto un sito terra, poi la faccio crescere intorno
		 */
		int puntoCentrale = latoDellaMappa/2;
		
		for (int i = 0; i < latoDellaMappa; i++) {
			for (int j = 0; j < latoDellaMappa; j++) {
				MappaACelle[i][j] = new Acqua;
			}
		}
	}
	
	/*
	 * Nuovo algoritmo.
	 * 1. tutti i lati sono acqua
	 * 2. il resto (interno) diventa terra
	 * 3. dell'interno riciclo alcuni elementi e li faccio diventare acqua (faccio crescere pozze all'interno a random)
	 * 4. 
	 */
	
	/*
	 * algoritmo per la creazione della mappa:
	 * -> tengo il conto di tutte le Celle dopo averlo calcolato all'inizio
	 * 1. riempio tutte le Celle di acqua
	 * 2. seleziono il punto centrale della mappa e pianto un sito "terra" scalandolo dal conto
	 * 3. attorno a questo sito faccio crescere il resto della terra con l'unica condizione che deve essere contigua
	 * 4. finisco le terre, dovrei aver finito un abbozzo della mappa
	 * 5. dato che è troppo uniforme, aggiungo celle terra extra (fino a MAX_GRUPPO_ACQUA per volta) e vado in negativo
	 * 6. prendo un punto a caso di terra e comincio a riempire di acqua fino a MAX_GRUPPO_ACQUA con la condizione che siano
	 *		tutte contigue
	 * 7. faccio un controllo per evitare "isole di terra", se ce ne sono le rimuovo e riequilibro il conto rimpiazzando
	 * 		l'isola di terra con l'acqua e allargando una pozza o un punto a caso partendo dal bordo
	 */
}