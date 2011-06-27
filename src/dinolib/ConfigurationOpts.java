package dinolib;

public abstract class ConfigurationOpts {
	/* Costanti del server. */
	/**
	 * Definisce la porta di gioco per le connessioni via socket.
	 * @uml.property  name="PORTA_DI_GIOCO"
	 */
	public static final int PORTA_DI_GIOCO = 32845;
	/**
	 * Definisce il numero massimo di giocatori ammessi in partita.
	 * @uml.property  name="NUMERO_MASSIMO_GIOCATORI_INGAME"
	 */
	public static final int NUMERO_MASSIMO_GIOCATORI_INGAME = 8;
	/**
	 * Definisce il tempo da attendere per la conferma del turno.
	 * @uml.property  name="SCHEDULER_WAIT_CONFERMA_TURNO"
	 */
	public static final int SCHEDULER_WAIT_CONFERMA_TURNO = 30;
	/**
	 * Definisce il tempo da dedicare ad ogni giocatore quando questo conferma il turno.
	 * @uml.property  name="SCHEDULER_TEMPO_TURNO"
	 */
	public static final int SCHEDULER_TEMPO_TURNO = 120;
	/**
	 * Definisce il nome del file della mappa.
	 * @uml.property  name="NOME_FILE_MAPPA"
	 */
	public static final String NOME_FILE_MAPPA = "mappa.dat";
	/**
	 * Definisce il nome del file dei giocatori.
	 * @uml.property  name="NOME_FILE_GIOCATORI"
	 */
	public static final String NOME_FILE_GIOCATORI = "giocatori.dat";
	
	/* Costanti di gioco. */
	/* --> Costanti della mappa. */
	/**
	 * Definisce il lato della mappa.
	 * @uml.property  name="LATO_MAPPA"
	 */
	public static final int LATO_MAPPA = 40;
	/**
	 * Percentuale della mappa che deve essere composta di acqua.
	 * @uml.property  name="FIXED_WATER_PERCENT" readOnly="true"
	 */
	public static final int FIXED_WATER_PERCENT = 20;
	/**
	 * Percentuale della mappa che deve essere composta di vegetazione.
	 * @uml.property name="FIXED_FLORA_PERCENT" readOnly="true"
	 */
	public static final int FIXED_FLORA_PERCENT = 40;
	/**
	 * Numero di carogne sempre presenti sulla mappa.
	 * ATTENZIONE! Questo e' un numero! NON una percentuale!
	 * @uml.property name="FIXED_SOD_COUNT" readOnly="true"
	 */
	public static final int FIXED_SOD_COUNT = 20;
	/**
	 * Definisce il numero minimo di celle acqua presenti in un gruppo.
	 * @uml.property name="MIN_GRUPPO_ACQUA" readOnly="true"
	 */
	public static final int MIN_GRUPPO_ACQUA = 5;
	/**
	 * Definisce il numero massimo di celle acqua presenti in un gruppo.
	 * @uml.property name="MAX_GRUPPO_ACQUA" readOnly="true"
	 */
	public static final int MAX_GRUPPO_ACQUA = 15;
	/* --> Costanti della razza. */
	/**
	 * Definisce il numero massimo di dinosauri per razza.
	 * @uml.property  name="NUMERO_MAX_DINOSAURI"
	 */
	public static final int NUMERO_MAX_DINOSAURI = 5;
	/**
	 * Definisce i turni di vita massimi per una specie.
	 * @uml.property name="TURNI_DI_VITA_MAX"
	 */
	public static final int TURNI_DI_VITA_MAX = 120;
	/* --> Costanti del dinosauro. */
	/**
	 * Indica l'energia necessaria per deporre un uovo.
	 * E' dichiarata costante perche' e' fissa per ogni dinosauro.
	 * @uml.property  name="ENERGIA_DEPOSIZIONE_UOVO"
	 */
	public static final int ENERGIA_DEPOSIZIONE_UOVO = 1500;
	/**
	 * Indica la dimensione massima per un dinosauro.
	 * E' dichiarata costante perche' e' fissa per ogni dinosauro.
	 * @uml.property  name="DIMENSIONE_MASSIMA_DINOSAURO" readOnly="true"
	 */
	public static final int DIMENSIONE_MASSIMA_DINOSAURO = 5;
	/* --> Costanti del dinosauro erbivoro. */
	/**
	 * Definisce lo spostamento massimo per un dinosauro erbivoro.
	 * @uml.property  name="SPOSTAMENTO_MAX_ERBIVORO" readOnly="true"
	 */
	public static final int SPOSTAMENTO_MAX_ERBIVORO = 2;
	/**
	 * Definisce il moltiplicatore della forza per un dinosauro erbivoro.
	 * @uml.property  name="MOLTIPLICATORE_FORZA_ERBIVORO" readOnly="true"
	 */
	public static final int MOLTIPLICATORE_FORZA_ERBIVORO = 1;
	/* --> Costanti del dinosauro carnivoro. */
	/**
	 * Definisce lo spostamento massimo per un dinosauro carnivoro.
	 * @uml.property  name="SPOSTAMENTO_MAX_CARNIVORO" readOnly="true"
	 */
	public static final int SPOSTAMENTO_MAX_CARNIVORO = 3;
	/**
	 * Definisce il moltiplicatore della forza per un dinosauro carnivoro.
	 * @uml.property  name="MOLTIPLICATORE_FORZA_CARNIVORO" readOnly="true"
	 */
	public static final int MOLTIPLICATORE_FORZA_CARNIVORO = 2;
}