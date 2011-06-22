package dinolib.Razza;

import java.io.Serializable;

import dinolib.CommonUtils;
import dinolib.Mappa.Coord;
/**
 * @author  fabio
 */
/**
 * Rappresenta un dinosauro singolo.
 * Il tipo e i parametri vengono impostati dal costruttore della sottoclasse.
 */
public abstract class Dinosauro implements Serializable {
	/**
	 * Generated version ID for Serializable.
	 */
	private static final long serialVersionUID = 7956386749602820936L;
	/* Tutte le variabili statiche/definitive e non modificabili */
	/**
	 * Indica l'energia necessaria per deporre un uovo.
	 * E' dichiarata final static perche' e' fissa per ogni dinosauro, indipendente da altri parametri.
	 * @uml.property  name="energiaDeposizioneUovo"
	 */
	private static final int energia_DEPOSIZIONE_UOVO = 1500;
	/**
	 * Indica la dimensione massima per un dinosauro.
	 * E' dichiarata final static perche' e' fissa per ogni dinosauro, indipendente da altri parametri.
	 * @uml.property  name="DIMENSIONE_MASSIMA" readOnly="true"
	 */
	private static final int dimensione_MASSIMA = 5;
	/**
	 * Contiene lo spostamento massimo per turno del dinosauro impostato dalle sottoclassi tramite il costruttore.
	 * @uml.property  name="spostamento_MAX"
	 */
	private int spostamento_MAX = 0;
	/**
	 * Contiene lo spostamento massimo per turno del dinosauro impostato dalle sottoclassi tramite il costruttore.
	 * @uml.property  name="moltiplicatore_FORZA"
	 */
	private int moltiplicatore_FORZA = 0;
	
	/* Tutte le variabili istanziabili */
	/**
	 * Indica l'energia spesa per crescere.
	 * @uml.property  name="energiaCrescita"
	 */
	private int energiaCrescita = 0;
	/**
	 * Contiene un valore che dice se il dinosauro e' utilizzabile o no.
	 * Serve per deponi uovo (posticipare l'utilizzo di un dinosauro) o per verificare che il dinosauro abbia ancora mosse a disposizione.
	 * Serve anche per evitare che un dinosauro che sta per essere rimosso possa venire utilizzato.
	 * @uml.property  name="usabile"
	 */
	private boolean isUsabile = false;
	/**
	 * Indica l'energia corrente del dinosauro.
	 * @uml.property  name="energiaAttuale"
	 */
	private int energiaAttuale = 0;
	/**
	 * Indica l'energia massima per il dinosauro.
	 * Visibile pubblicamente tramite il getter, modificabile privatamente solo tramite il setter.
	 * @uml.property  name="energiaMax"
	 */
	private int energiaMax = 0;
	/**
	 * Indica la dimensione del dinosauro.
	 * Visibile pubblicamente tramite il getter, modificabile privatamente solo tramite il setter.
	 * @uml.property  name="dimensione"
	 */
	private int dimensione = 0;
	/**
	 * Indica la durata massima della vita del dinosauro.
	 * Viene impostata nel costruttore alla creazione del dinosauro e puo' solo essere letta.
	 * @uml.property  name="durataVitaMax"
	 */
	private int durataVitaMax = 0;
	/**
	 * Istanzia riferimento alle coordinate correnti del dinosauro.
	 * @uml.property name="coord"
	 */
	private Coord coord = null;
	/**
	 * Indica la forza del dinosauro.
	 * @uml.property  name="forza"
	 */
	private int forza = 0;
	/**
	 * Indica il turno di vita del dinosauro.
	 * @uml.property  name="turnoDiVita"
	 */
	private int turnoDiVita = 0;
	/**
	 * Indica l'id del dinosauro.
	 * @uml.property  name="idDinosauro"
	 */
	private String idDinosauro = null;
	/**
	 * Indica se il dinosauro ha gia' eseguito un'azione statica o meno (crescita o deposizione uovo).
	 */
	private boolean azioneStaticaCompiuta = false;
	/**
	 * Indica se il dinosauro si e' gia' mosso.
	 */
	private boolean movimentoCompiuto = false;
	
	/* Costruttore */
	/**
	 * Visibile solo alle sottoclassi da usare come costruttore comune per Carnivoro e Erbivoro.
	 * @param nCoord Le nuove coordinate del dinosauro.
	 * @param nuovo_spostamento_MAX Impostato dalle sottoclassi, lo spostamento massimo del dinosauro quando esegue il movimento.
	 * @param nuovo_moltiplicatore_FORZA Impostato dalle sottoclassi, il moltiplicatore della forza del dinosauro.
	 */
	protected Dinosauro(Coord nCoord, int nuovo_spostamento_MAX, int nuovo_moltiplicatore_FORZA) { // Testato
		this.setCoord(nCoord);
		this.turnoDiVita = 1;
		this.durataVitaMax = 24 + (CommonUtils.getNewRandomIntValue(13));
		this.setEnergiaAttuale(750);
		this.dimensione = 1;
		this.updateEnergiaMax();
		this.spostamento_MAX = nuovo_spostamento_MAX;
		this.moltiplicatore_FORZA = nuovo_moltiplicatore_FORZA;
		this.updateEnergiaCrescita();
		this.usabile();
		this.idDinosauro = CommonUtils.getNewToken();
	}
	
	/* Tutti i getter */
	/**
	 * Dice se il dinosauro ha ancora a disposizione un'azione statica (deposizione uovo o crescita).
	 * @return True se ha ancora l'azione a disposizione, false se non ha l'azione a disposizione.
	 */
	public boolean hasAzioneStatica() { return !azioneStaticaCompiuta; }
	/**
	 * Dice se il dinosauro ha ancora a disposizione l'azione di movimento.
	 * @return True se ha ancora l'azione a disposizione, false se non ha l'azione a disposizione.
	 */
	protected boolean hasMovimento() { return !movimentoCompiuto; }
	/**
	 * Restituisce l'energia attuale del dinosauro.
	 * @return L'energia attuale del dinosauro.
	 */
	public int getEnergiaAttuale() { return energiaAttuale; } // Testato
	/**
	 * Restituisce l'energia massima del dinosauro.
	 * @return L'energia massima del dinosauro.
	 */
	public int getEnergiaMax() { return energiaMax; }
	/**
	 * Restituisce la dimensione corrente del dinosauro.
	 * @return La dimensione corrente del dinosauro.
	 */
	public int getDimensione() { return dimensione; } // Testato
	/**
	 * Restituisce la durata massima della vita del dinosauro.
	 * @return La durata massima della vita del dinosauro.
	 */
	public int getDurataVitaMax() {	return durataVitaMax; }
	/**
	 * Restituisce la forza del dinosauro.
	 * La aggiorna prima di restituirla (e' una proprieta' che cambia in base all'energia attuale, al moltiplicatore e alla dimensione).
	 * @return La forza del dinosauro.
	 */
	public int getForza() { updateForza(); return forza; }
	/**
	 * Restituisce le coordinate correnti del dinosauro.
	 * @return Le coordinate correnti del dinosauro.
	 */
	public Coord getCoord() { return coord; } // Testato
	/**
	 * Restituisce il turno di vita del dinosauro.
	 * @return Il turno di vita del dinosauro.
	 * @uml.property  name="turnoDiVita"
	 */
	public int getTurnoDiVita() { return turnoDiVita; } // Testato
	/**
	 * Restituisce lo spostamento massimo del dinosauro per l'azione di movimento.
	 * @return Lo spostamento massimo del dinosauro per l'azione di movimento.
	 */
	public int getSpostamentoMax() { return spostamento_MAX; }
	/**
	 * Dice se il dinosauro puo' essere usato.
	 * @return True se il dinosauro puo' essere usato, false altrimenti.
	 */
	public boolean isUsabile() { return isUsabile; } // Testato
	/**
	 * Restituisce l'Id del dinosauro.
	 * @return L'Id del dinosauro.
	 */
	public String getIdDinosauro() { return idDinosauro; }
	/**
	 * Restituisce come String il nome della classe per avere il tipo della razza del dinosauro.
	 * @return Il tipo della razza del dinosauro.
	 */
	public String getTipoRazza() { return toString(); } // Testato
	/**
	 * Restituisce un valore che indica quante caselle il dinosauro vede attorno a se' (range di visuale).
	 * Dipende dalla dimensione del dinosauro.
	 * I valori sono hard-coded.
	 * @return Il range di vista del dinosauro in caselle di distanza.
	 */
	public int getRangeVista() { // Testato
		switch (getDimensione()) {
		case 1:
			return 2;
		case 2:
		case 3:
			return 3;
		case 4:
		case 5:
		default:
			return 4;
		}
	}
	/**
	 * Restituisce l'energia necessaria per il movimento del dinosauro.
	 * @return L'energia necessaria per il movimento.
	 */
	protected int getEnergiaMovimento() {
		return (int) (10*Math.pow(2, this.getDimensione()));
	}
	
	/* Tutti i setter */
	/**
	 * Permette di modificare lo stato del dinosauro, cosi' che si sappia che ha gia' compiuto il movimento.
	 */
	protected void haMosso() { movimentoCompiuto = true; }
	/**
	 * Permette di modificare lo stato del dinosauro, così che si sappia che non e' usabile.
	 */
	public void nonUsabile() { isUsabile = false; } // Testato
	/**
	 * Permette di modificare lo stato del dinosauro, così che si sappia che e' usabile.
	 */
	protected void usabile() { isUsabile = true; } // Testato
	/**
	 * Permette di impostare le coordinate correnti del dinosauro.
	 */
	public void setCoord(Coord myCoords) { this.coord = myCoords; } // Testato
	/**
	 * Permette di impostare l'energia attuale del dinosauro.
	 * @param energiaAttuale La nuova energia attuale del dinosauro.
	 */
	public void setEnergiaAttuale(int energiaAttuale) { this.energiaAttuale = energiaAttuale; } // Testato
	/**
	 * Permette di impostare la dimensione del dinosauro.
	 * @param dimensione La nuova dimensione del dinosauro.
	 */
	private void setDimensione(int dimensione) { this.dimensione = dimensione; }
	/**
	 * Permette di aggiornare l'energia necessaria per la crescita del dinosauro.
	 */
	private void updateEnergiaCrescita() { energiaCrescita = energiaMax/2; }
	/**
	 * Permette di aggiornare la forza corrente del dinosauro.
	 */
	private void updateForza() { this.forza = (moltiplicatore_FORZA*this.getDimensione()*this.getEnergiaAttuale()); }
	/**
	 * Permette di aggiornare l'energia massima del dinosauro.
	 */
	private void updateEnergiaMax() { this.energiaMax = 1000*this.dimensione; }
	/**
	 * Invecchia il dinosauro.
	 * Aumenta di uno il turno di vita e rimuove i marker per movimento e azione statica compiuti.
	 */
	protected void invecchia() { // Testato
		this.turnoDiVita += 1;
		this.azioneStaticaCompiuta = false;
		this.movimentoCompiuto = false;
	}
	
	/* Funzioni miscellanee */
	/**
	 * Verifica se il dinosauro e' gia' alla dimensione massima.
	 * @return True se la dimensione del dinosauro e' la dimensione massima, false altrimenti.
	 */
	public boolean isAtDimensioneMax() { // Testato
		if (this.getDimensione() >= 5) return true;
		else return false;
	}
	/**
	 * Controlla che il dinosauro abbia abbastanza energia per crescere.
	 * @return True se ha abbastanza energia per crescere, false altrimenti.
	 */
	protected boolean hasEnergyToGrow() { // Testato
		if (energiaAttuale > energiaCrescita) return true;
		else return false;
	}
	/**
	 * Controlla che il dinosauro abbia abbastanza energia per deporre un uomo.
	 * @return True se ha abbastanza energia per deporre un uovo, false altrimenti.
	 */
	protected boolean hasEnergyToRepl() { // Testato
		if (energiaAttuale > energia_DEPOSIZIONE_UOVO) return true;
		else return false;
	}
	/**
	 * Fa crescere il dinosauro.
	 * Aumenta la dimensione di 1.
	 * Aggiorna l'energia massima.
	 * Aggiorna la forza.
	 * Aggiorna l'energia necessaria per la crescita.
	 * Assegna il marker che dice che ha gia' compiuto un'azione statica.
	 * Aggiorna l'energia attuale del dinosauro.
	 */
	protected void cresci() { // Testato
		if (this.getDimensione() < dimensione_MASSIMA) {
			this.setDimensione((this.getDimensione()+1));
			this.updateEnergiaMax();
			this.updateForza();
			this.updateEnergiaCrescita();
			this.azioneStaticaCompiuta = true;
			this.setEnergiaAttuale(this.getEnergiaAttuale()-energiaCrescita);
		}
	}
	/**
	 * Fa deporre un uovo al dinosauro.
	 * Serve solo per la parte di aggiornamento dell'energia del dinosauro, il resto e' fatto piu' in alto nella logica).
	 * Assegna il marker che dice che ha gia' compiuto un'azione statica.
	 */
	protected void deponiUovo() {
		this.setEnergiaAttuale(this.getEnergiaAttuale()-energia_DEPOSIZIONE_UOVO);
		this.azioneStaticaCompiuta = true;
	}
	
	/**
	 * Override del metodo toString per questa classe.
	 * Ritorna il nome semplice della classe.
	 * Dato che dinosauro non e' istanziabile (abstract) questo serve solo per farlo ereditare in comune alle sottoclassi.
	 */
	public String toString () { return this.getClass().getSimpleName(); } // Testato
}