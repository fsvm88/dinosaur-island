package dinolib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Astrae la collezione dei dinosauri rispetto al giocatore.
 */
class PlayersCollection implements Set<Giocatore> {
	/* Tutte le variabili statiche/definitive e non modificabili */

	/* Tutte le variabili istanziabili */
	private HashSet<Giocatore> collection;

	/* Costruttore */
	/**
	 * Implementa la costruzione della classe Specie.
	 * Richiede il nuovo nome della razza e l'istanza del nuovo dinosauro.
	 */
	protected PlayersCollection() {
		collection = new HashSet<Giocatore>();
	}

	/* Tutti i getter */
	public boolean containsPlayerByName(String nome) {
		Iterator<Giocatore> itGiocatori = this.iterator(); 
		while (itGiocatori.hasNext()) {
			if (itGiocatori.next().getNome().equals(nome)) return true;
		}
		return false;
	}
	
	public Giocatore getPlayerByName(String nome) {
		Iterator<Giocatore> itGiocatori = this.iterator();
		Giocatore giocatoreToReturn = null; 
		while (itGiocatori.hasNext()) {
			giocatoreToReturn = itGiocatori.next();
			if (giocatoreToReturn.getNome().equals(nome)) return giocatoreToReturn;
		}
		return null;
	}
	
	public boolean removeByName(String nome) {
		Iterator<Giocatore> itGiocatori = this.iterator();
		Giocatore giocatoreToRemove = null; 
		while (itGiocatori.hasNext()) {
			giocatoreToRemove = itGiocatori.next();
			if (giocatoreToRemove.getNome().equals(nome)) break;
		}
		return this.remove(giocatoreToRemove);
	}
	/* Tutti i metodi importati dall'interfaccia, questi sono quelli supportati */
	@Override
	public int size() {
		return collection.size();
	}

	@Override
	public boolean isEmpty() {
		if (collection.isEmpty()) return true;
		else return false;
	}

	@Override
	public boolean contains(Object o) {
		if (collection.contains(o)) return true;
		else return false;
	}

	@Override
	public Iterator<Giocatore> iterator() {
		return collection.iterator();
	}

	@Override
	public void clear() { collection = new HashSet<Giocatore>(); }

	@Override
	public boolean add(Giocatore e) {
		if (!isEmpty()) {
		Iterator<Giocatore> itGiocatore = this.iterator();
		while (itGiocatore.hasNext()) {
			if (e.getNome().equals(itGiocatore.next().getNome())) return false;
		}
		collection.add(e);
		return true;
		}
		else {
			collection.add(e);
			return true;
		}
	}

	@Override
	public boolean remove(Object o) {
		if (o != null) {
			collection.remove((Giocatore) o);
			if (this.isEmpty()) collection.clear();
			return true;
		}
		else return false;
	}

	/* Tutti i metodi importati dall'interfaccia, quelli non supportati */
	@Override
	public boolean addAll(Collection<? extends Giocatore> c) { throw new UnsupportedOperationException(); }
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