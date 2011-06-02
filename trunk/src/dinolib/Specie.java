package dinolib;

import java.util.Iterator;
import java.util.Hashtable;

import dinolib.CommonUtils;

/** 
 * Astrae la collezione dei dinosauri rispetto al giocatore.
 */
class Specie {
	/**
	 * Variabile che definisce il nome della specie di Dinosauri.
	 * @uml.property  name="nomeRazza"
	 */
	private String nomeRazza;

	/** 
	 * Getter of the property <tt>nomeSpecie</tt>
	 * @return  Returns the nomeSpecie.
	 * @uml.property  name="nomeRazza"
	 */
	public String getNomeRazza() {
		return nomeRazza;
	}

	/** 
	 * ConcurrentHashMap con tutti i dinosauri presenti nella specie.
	 * @uml.property name="Dinosauri"
	 */
	private Hashtable<String, Dinosauro> dinosauri = new Hashtable<String,Dinosauro>();

	/**
	 * Contiene il punteggio corrente della specie.
	 * @uml.property name="punteggio"
	 */
	private int punteggio = 0;

	/**
	 * Definisce definitivamente i turni di vita massimi per una specie.
	 * uml.property name="turni_DI_VITA_MAX"
	 */
	private final int TURNI_DI_VITA_MAX = 120;
	
	/**
	 * Contiene i turni di vita della specie.
	 * uml.property name="turniDiVita"
	 */
	private int turniDiVita = 0;
	
	/**
	 * Dice se la razza si è già estinta o meno.
	 * @uml.property name="isEstinta"
	 */
	private boolean isEstinta = false;

	/**
	 * Helper per verificare se la specie è estinta o meno.
	 */
	public boolean isEstinta() {
		return isEstinta;
	}

	/**
	 * Restituisce il punteggio corrente della specie.
	 */
	public int getPunteggio() {
		return punteggio;
	}

	/**
	 * Aggiorna il punteggio della specie.
	 */
	private void aggiornaPunteggio() {
		Iterator<String> itDinosauri = getItIdDinosauri();
		while (itDinosauri.hasNext()) {
			punteggio = (punteggio + (1+dinosauri.get(itDinosauri.next()).getDimensione())); 
		}
	}

	/**
	 * Aggiunge un dinosauro alla lista di dinosauri della Specie.
	 */
	protected void aggiungiDinosauro(Dinosauro dinosauro, String nuovoIdDinosauro){
		if (nuovoIdDinosauro == null) {
			dinosauri.put(CommonUtils.getNewToken(), dinosauro);
		}
		else if (nuovoIdDinosauro != null) {
			dinosauri.put(nuovoIdDinosauro, dinosauro);
		}
	}

	private void laRazzaEEstinta() {
		isEstinta = true;
	}

	/**
	 * Uccide la razza di dinosauri.
	 * Imposta isEstinta a true e dealloca gli oggetti.
	 */
	private void uccidiRazza() {
		laRazzaEEstinta();
		dinosauri = null;
	}

	/**
	 * Rimuove il dinosauro con l'ID scelto dalla specie.
	 */
	public void rimuoviDinosauro(String idDinosauro) {
		dinosauri.remove(idDinosauro);
		if (dinosauri.size() == 0) {
			uccidiRazza();
		}
	}

	/**
	 * Restituisce il numero di dinosauri nella lista.
	 */
	public int getNumeroDinosauri(){
		return dinosauri.size();
	}

	/**
	 * Stub di un costruttore vuoto per garantire coerenza con la sottoclasse.
	 */
	protected Specie() { };

	/**
	 * Implementa la costruzione della classe Specie.
	 * Richiede il nuovo nome della razza e l'istanza del nuovo dinosauro.
	 */
	protected Specie(String nomeRazza, Dinosauro nuovodinosauro) {
		this.nomeRazza = nomeRazza;
		aggiungiDinosauro(nuovodinosauro, null);
	}

	/**
	 * Restituisce il dinosauro nella specie che ha un certo id.
	 * @param idDinosauroCercato
	 * @return
	 */
	protected Dinosauro getDinosauro(String idDinosauroCercato) {
		return dinosauri.get(idDinosauroCercato);
	}

	/**
	 * Restituisce un iteratore sui dinosauri presenti nella specie.
	 */
	public Iterator<Dinosauro> getItDinosauri() {
		return dinosauri.values().iterator();
	}

	/**
	 * Restituisce un iteratore sugli id dei dinosauri. Utile solo per inviare la lista dei dinosauri all'utente.
	 */
	public Iterator<String> getItIdDinosauri() {
		return dinosauri.keySet().iterator();
	}

	/**
	 * Helper booleano per verificare l'esistenza di un id (cioè l'esistenza di un dinosauro.
	 * @param idDaCercare
	 * @return
	 */
	protected boolean existsDinosauroWithId(String idDaCercare) {
		if (dinosauri.containsKey(idDaCercare)) return true;
		else return false;
	}

	/**
	 * Helper per restituire il tipo della razza del dinosauro.
	 */
	public String getTipoRazza() {
		Iterator<Dinosauro> itTemp = getItDinosauri();
		if (itTemp.hasNext()) {
			return itTemp.next().getTipoRazza();
		}
		return null;
	}
	
	/**
	 * Helper per invecchiare i dinosauri nella specie.
	 */
	private void invecchiaDinosauri() {
		Iterator<Dinosauro> itDinosauri = getItDinosauri();
		while (itDinosauri.hasNext()) {
			itDinosauri.next().invecchia();
		}
	}
	
	/**
	 * Helper per aggiornare la specie.
	 */
	protected void aggiornaSpecie() {
		if (turniDiVita >= TURNI_DI_VITA_MAX) uccidiRazza();
		aggiornaPunteggio();
		invecchiaDinosauri();
	}
}