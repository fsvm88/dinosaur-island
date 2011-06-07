package dinolib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** 
 * Astrae la collezione dei dinosauri rispetto al giocatore.
 */
class Razza implements Set<Dinosauro> {
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
	/**
	 * Contiene il numero massimo di dinosauri per specie. È una costante di gioco.
	 * @uml.property name="numero_MAX_DINOSAURI"
	 */
	private final int numero_MAX_DINOSAURI = 5;
	
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
		this.add(nuovodinosauro);
	}
	
	/* Tutti i getter */
	public String getNome() { return nome; }
	public int getPunteggio() { return punteggio; }
	public String getTipoRazza() { return tipoRazza; }
	public boolean isEstinta() { return isEstinta; }
	protected Dinosauro getDinosauroById(String idDinosauroCercato) {
		if (existsDinosauroWithId(idDinosauroCercato)) {
			Iterator<Dinosauro> itDinosauri = this.iterator();
			Dinosauro tempDinosauro;
			while (itDinosauri.hasNext()) {
				tempDinosauro = itDinosauri.next();
				if (tempDinosauro.getIdDinosauro().equals(idDinosauroCercato)) return tempDinosauro;
			}
		}
		return null;
	}
	protected boolean hasNumeroMassimo() {
		if (this.size() >= numero_MAX_DINOSAURI) return true;
		else return false;
	}
	/**
	 * Helper booleano per verificare l'esistenza di un id (cioè l'esistenza di un dinosauro).
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
	/* Tutti i setter */
	private void estingui() { isEstinta = true; }
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
	 * Helper per invecchiare i dinosauri nella specie.
	 */
	private void invecchiaDinosauri() {
		Iterator<Dinosauro> itDinosauri = this.iterator();
		while (itDinosauri.hasNext()) {
			itDinosauri.next().invecchia();
		}
	}
	/**
	 * Helper per aggiornare la specie.
	 */
	protected void aggiornaSpecie() {
		invecchiaDinosauri();
		if (turniDiVita >= TURNI_DI_VITA_MAX) uccidiRazza();
		aggiornaPunteggio();
	}
	/**
	 * Rimuove un dinosauro usando l'id come parametro.
	 * @param idToRemove
	 */
	public void removeById(String idToRemove) {
		Iterator<Dinosauro> itDinosauri = this.iterator();
		Dinosauro tempDinosauro;
		while(itDinosauri.hasNext()) {
			tempDinosauro = itDinosauri.next();
			if (tempDinosauro.getIdDinosauro().equals(idToRemove)) {
				dinosauri.remove(tempDinosauro);
				return;
			}
		}
	}

	/* Tutti i metodi importati dall'interfaccia, questi sono quelli supportati */
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
	public Iterator<Dinosauro> iterator() {
		return dinosauri.iterator();
	}

	@Override
	public void clear() { dinosauri = new HashSet<Dinosauro>(); }

	@Override
	public boolean add(Dinosauro e) {
		Iterator<Dinosauro> itDinosauro = this.iterator();
		while (itDinosauro.hasNext()) {
			if (e.getIdDinosauro().equals(itDinosauro.next().getIdDinosauro())) return false;
		}
		dinosauri.add(e);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		if (o != null) {
			dinosauri.remove((Dinosauro) o);
			if (this.isEmpty()) uccidiRazza();
			return true;
		}
		else return false;
	}

	/* Tutti i metodi importati dall'interfaccia, quelli non supportati */
	@Override
	public boolean addAll(Collection<? extends Dinosauro> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	public Object[] toArray() { throw new UnsupportedOperationException(); }
	@Override
	public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}