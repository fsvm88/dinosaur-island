package dinolib;

import java.util.Iterator;
import java.util.Hashtable;

import dinolib.CommonUtils;

/** 
 * Astrae la collezione dei dinosauri rispetto al giocatore.
 */
public class Specie {
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
	 * Getter of the property <tt>Dinosauri</tt>
	 * @return  Returns the dinosauri.
	 * @uml.property  name="Dinosauri"
	 */
	public Hashtable<String, Dinosauro> getDinosauri() {
		return dinosauri;
	}
	
	/**
	 * Contiene il punteggio corrente della specie.
	 * @uml.property name="punteggio"
	 */
	private int punteggio = 0;
	
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
	public void aggiornaPunteggio() {
		Iterator<String> itDinosauri = getItIdDinosauri();
		while (itDinosauri.hasNext()) {
			punteggio = (punteggio + (1+dinosauri.get(itDinosauri.next()).getDimensione())); 
		}
	}
	
	/**
	 * Aggiunge un dinosauro alla lista di dinosauri della Specie.
	 */
	public void aggiungiDinosauroAllaSpecie(Dinosauro dinosauro, String nuovoIdDinosauro){
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
	public void rimuoviDinosauroDallaSpecie(String idDinosauro) {
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
	public Specie(String nomeRazza, Dinosauro nuovodinosauro) {
		this.nomeRazza = nomeRazza;
		aggiungiDinosauroAllaSpecie(nuovodinosauro, null);
	}
	
	/**
	 * Restituisce il dinosauro nella specie che ha un certo id.
	 * @param idDinosauroCercato
	 * @return
	 */
	public Dinosauro getDinosauro(String idDinosauroCercato) {
		return dinosauri.get(idDinosauroCercato);
	}
	
	/**
	 * Restituisce un iteratore sui dinosauri presenti nella specie.
	 */
	public Iterator<Dinosauro> getIteratoreSuiDinosauri() {
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
	public boolean validateIdExistance(String idDaCercare) {
		if (dinosauri.containsKey(idDaCercare)) return true;
		else return false;
	}
	
	/**
	 * Helper per restituire il tipo della razza del dinosauro.
	 */
	public String getTipoRazza() {
		Iterator<Dinosauro> itTemp = getIteratoreSuiDinosauri();
		if (itTemp.hasNext()) {
			return itTemp.next().getTipoRazza();
		}
		return null;
	}
}