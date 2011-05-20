package dinolib;

import java.util.Iterator;
import java.util.Hashtable;

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
	 * Aggiunge un dinosauro alla lista di dinosauri della Specie.
	 */
	public void aggiungiDinosauroAllaSpecie(Dinosauro dinosauro){
		dinosauri.put(getNewToken(), dinosauro);
	}
	
	/**
	 * Helper per la generazione di un nuovo token alfanumerico.
	 * @return
	 */
	private static String getNewToken() {
		return Long.toString(Double.doubleToLongBits(Math.random()));
	}


	/**
	 * Rimuove il dinosauro con l'ID scelto dalla specie.
	 */
	public void rimuoviDinosauroDallaSpecie(String idDinosauro) {
		dinosauri.remove(idDinosauro);
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
		aggiungiDinosauroAllaSpecie(nuovodinosauro);
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
}