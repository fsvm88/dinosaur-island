package dinolib;


public class Mappa {
	/**
	 * La mappa a celle, contiene tutta la mappa in celle. Attenzione! Il sistema di conteggio è un sistema di coordinate cartesiane! Non una matrice! Verrà usata come piano cartesiano anche se l'accesso verrà effettuato come una matrice. Le colonne tengono la stessa numerazione (l'ascissa non cambia). Le righe invece hanno la seguente numerazione: 0 indica la riga più in basso (il bordo in basso della mappa), 39 indica la riga più in alto (il bordo in alto della mappa).
	 * @uml.property  name="MappaACelle" multiplicity="(0 -1)" dimension="2"
	 */
	private Cella[][] MappaACelle;

	/**
	 * Getter of the property <tt>MappaACelle</tt>
	 * @return  Returns the MappaACelle.
	 * @uml.property  name="MappaACelle"
	 */
	public Cella[][] getMappaACelle() {
		return MappaACelle;
	}
	
	/**
	 * Costruttore protetto per soddisfare la sottoclasse Cella
	 */
	protected Mappa() { }

	/**
	 * Setter of the property <tt>MappaACelle</tt>
	 * @param MappaACelle  The MappaACelle to set.
	 * @uml.property  name="MappaACelle"
	 */
	public void setMappaACelle(Cella[][] mappaACelle) {
		MappaACelle = mappaACelle;
	}

	/**
	 * Percentuale della mappa che deve essere composta di acqua.
	 * @uml.property  name="FIXED_WATER_PERCENT" readOnly="true"
	 */
	private final int fixed_WATER_PERCENT = 20;

	/**
	 * Percentuale della mappa che deve essere composta di terra.
	 * Viene ricavata dalla percentuale d'acqua.
	 * @uml.property name="FIXED_EARTH_PERCENT" readOnly="true"
	 */
	private final int fixed_EARTH_PERCENT = (100 - this.fixed_WATER_PERCENT);

	/**
	 * Dichiara la variabile che verrà usata per gestire il lato della mappa in tutta la classe.
	 * @uml.property name="latoDellaMappa"
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
	 * @uml.property name="conteggioAcquaStatico"
	 */
	private int conteggioAcquaStatico = 0;

	/**
	 * Contatore per le celle di terra, indica quante celle di terra ci sono sulla mappa.
	 * @uml.property name="conteggioTerraStatico"
	 */
	private int conteggioTerraStatico = 0;


	/**
	 * Restituisce il lato della mappa.
	 */
	public int getLatoDellaMappa() {
		return latoDellaMappa;
	}

	/**
	 * Calcola il numero di Celle con il lato fornito.
	 */
	private int calcolaCelle () {
		return (latoDellaMappa*latoDellaMappa);
	}

	/**
	 * Calcola il numero di Celle per di Acqua e Terra sulla Mappa.
	 */
	private void calcolaNumeroCelleDaPercentuali () {
		conteggioAcquaStatico = ( ( calcolaCelle() / 100 ) * fixed_WATER_PERCENT );
		conteggioTerraStatico = calcolaCelle() - conteggioAcquaStatico;
	}

	private void popolaMappa() {
		// TODO : scrivere popolaMappa.
	}
}