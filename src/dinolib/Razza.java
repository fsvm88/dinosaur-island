package dinolib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Set;

import dinolib.CommonUtils;

/** 
 * Astrae la collezione dei dinosauri rispetto al giocatore.
 */
class Razza implements Set {
	/* Tutte le variabili statiche/definitive e non modificabili */
	/**
	 * Definisce definitivamente i turni di vita massimi per una specie.
	 * uml.property name="turni_DI_VITA_MAX"
	 */
	private final int TURNI_DI_VITA_MAX = 120;
	/**
	 * Variabile che definisce il nome della specie di Dinosauri.
	 * Viene impostata definitivamente dal costruttore.
	 * @uml.property  name="nomeRazza"
	 */
	private String nome = null;
	/**
	 * Variabile che definisce il tipo della razza di Dinosauri.
	 * Viene impostata definitivamente dal costruttore.
	 * @uml.property  name="tipoRazza"
	 */
	private String tipoRazza = null;
	
	/* Tutte le variabili istanziabili */
	/**
	 * ConcurrentHashMap con tutti i dinosauri presenti nella specie.
	 * @uml.property name="Dinosauri"
	 */
	private HashSet<Dinosauro> dinosauri = new HashSet<Dinosauro>();
	/**
	 * Contiene il punteggio corrente della specie.
	 * @uml.property name="punteggio"
	 */
	private int punteggio = 0;
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
	
	/* Costruttore */
	/**
	 * Implementa la costruzione della classe Specie.
	 * Richiede il nuovo nome della razza e l'istanza del nuovo dinosauro.
	 */
	protected Razza(String nomeRazza, Dinosauro nuovodinosauro) {
		this.nome = nomeRazza;
		this.tipoRazza = nuovodinosauro.getTipoRazza();
		aggiungiDinosauro(nuovodinosauro, null);
	}
	
	/* Tutti i getter */
	public String getNome() { return nome; }
	public int getPunteggio() { return punteggio; }
	protected Dinosauro getDinosauro(String idDinosauroCercato) { return dinosauri.get(idDinosauroCercato); }
	public String getTipoRazza() { return tipoRazza; }
	
	/**
	 * Estingui la razza.
	 */
	private void estingui() {
		isEstinta = true;
	}
	/**
	 * Helper per verificare se la specie è estinta o meno.
	 */
	public boolean isEstinta() {
		return isEstinta;
	}
	
	/**
	 * Helper booleano per verificare l'esistenza di un id (cioè l'esistenza di un dinosauro.
	 * @param idDaCercare
	 * @return
	 */
	protected boolean existsDinosauroWithId(String idDaCercare) {
		Iterator<Dinosauro> itDinosauri = this.iterator();
		while (itDinosauri.hasNext()) {
			if (itDinosauri.next().getIdDinosauro().equals(idDaCercare)) return true;
		}
		return false;
	}
	/**
	 * Aggiorna il punteggio della specie.
	 */
	private void aggiornaPunteggio() {
		Iterator<Dinosauro> itDinosauri = this.iterator();
		while (itDinosauri.hasNext()) {
			punteggio = (punteggio + (1+itDinosauri.next().getDimensione())); 
		}
	}
	/**
	 * Uccide la razza di dinosauri.
	 * Imposta isEstinta a true e dealloca gli oggetti.
	 */
	private void uccidiRazza() {
		estingui();
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

	/* Tutti i metodi importati dall'interfaccia */
	@Override
	public int size() {
		if (!dinosauri.isEmpty()) {
			return dinosauri.size();
		}
		else return 0;
	}

	@Override
	public boolean isEmpty() {
		if (dinosauri.isEmpty()) return true;
		else return false;
	}

	@Override
	public boolean contains(Object o) {
		if (dinosauri.contains(o)) return true;
		else return false;
	}

	@Override
	public Iterator iterator() {
		return dinosauri.iterator();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray(Object[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(Object e) {
		Iterator<Dinosauro> itDinosauro = this.iterator();
		while (itDinosauro.hasNext()) {
			if (((Dinosauro) e).equals(itDinosauro.next())) return false;
		}
		dinosauri.add((Dinosauro) e);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
}