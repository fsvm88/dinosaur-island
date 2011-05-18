package dinolib;

import java.util.ArrayList;
import java.util.Iterator;

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
	 * ArrayList con tutti i dinosauri presenti nella specie.
	 * @uml.property  name="Dinosauri"
	 */
	private ArrayList<Dinosauro> dinosauri;

	/**
	 * Getter of the property <tt>Dinosauri</tt>
	 * @return  Returns the dinosauri.
	 * @uml.property  name="Dinosauri"
	 */
	public ArrayList getDinosauri() {
		return dinosauri;
	}
	
	/**
	 * 
	 */
	public Iterator<Dinosauro> getIteratoreSuiDinosauri () {
		return dinosauri.iterator();
	}

	/**
	 * Aggiunge un dinosauro alla lista di dinosauri della Specie.
	 */
	public void aggiungiDinosauroAllaSpecie(Dinosauro dinosauro){
	}


	/**
	 * Rimuove il dinosauro con l'ID scelto dalla specie.
	 */
	public void rimuoviDinosauroDallaSpecie(String idDinosauro){
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
}
