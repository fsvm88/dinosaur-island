package dinolib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PlayerManager implements List<Giocatore> {
	/**
	 * Istanzia il riferimento alla lista dei Giocatori.
	 * @uml.property name="listaGiocatori"
	 */
	private ArrayList<Giocatore> listaGiocatori = null;

	/**
	 * Costruttore, inizializza l'ArrayList.
	 */
	public PlayerManager() { // Testato
		listaGiocatori = new ArrayList<Giocatore>();
	}
	
	/**i
	 * Controlla se l'utente esiste nella lista.
	 * @param nomeGiocatore
	 * @return
	 */
	protected boolean exists(String nomeGiocatore) { // Testato
		if (!listaGiocatori.isEmpty()) {
			Iterator<Giocatore> itGiocatori = iterator();
			while (itGiocatori.hasNext()) {
				if (itGiocatori.next().getNome().equals(nomeGiocatore)) return true;
			}
		}
		return false;
	}
	/**
	 * Restituisce il giocatore richiesto. 
	 * @param nomeGiocatore
	 * @return
	 */
	protected Giocatore getPlayer(String nomeGiocatore) {
		if (exists(nomeGiocatore)) {
			Iterator<Giocatore> itGiocatori = iterator();
			Giocatore tempGiocatore = null;
			while (itGiocatori.hasNext()) {
				tempGiocatore = itGiocatori.next();
				if (tempGiocatore.getNome().equals(nomeGiocatore)) break;
			}
			return tempGiocatore;
		}
		else return null;
	}
	/**
	 * Aggiorna tutti i giocatori sul cambio turno.
	 */
	protected void updateGiocatori() {
		Iterator<Giocatore> itGiocatori = this.iterator();
		while (itGiocatori.hasNext()) {
			itGiocatori.next().aggiorna();
		}
	}
	/* Metodi di interfaccia - implementati */
	@Override // Testato
	public boolean add(Giocatore newGiocatore) { return listaGiocatori.add(newGiocatore); }
	@Override
	public boolean contains(Object arg0) { return listaGiocatori.contains(arg0); }
	@Override
	public Iterator<Giocatore> iterator() { return listaGiocatori.iterator(); }
	
	/* Metodi di interfaccia - non implementati */
	@Override
	public void add(int arg0, Giocatore arg1) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(Collection<? extends Giocatore> arg0) { throw new UnsupportedOperationException(); }
	@Override
	public boolean addAll(int arg0, Collection<? extends Giocatore> arg1) { throw new UnsupportedOperationException(); }
	@Override
	public void clear() { throw new UnsupportedOperationException(); }
	@Override
	public boolean containsAll(Collection<?> arg0) { throw new UnsupportedOperationException(); }
	@Override
	public Giocatore get(int arg0) { throw new UnsupportedOperationException(); }
	@Override
	public int indexOf(Object arg0) { throw new UnsupportedOperationException(); }
	@Override
	public boolean isEmpty() { throw new UnsupportedOperationException(); }
	@Override
	public int lastIndexOf(Object arg0) { throw new UnsupportedOperationException(); }
	@Override
	public ListIterator<Giocatore> listIterator() { throw new UnsupportedOperationException(); }
	@Override
	public ListIterator<Giocatore> listIterator(int arg0) { throw new UnsupportedOperationException(); }
	@Override
	public boolean remove(Object arg0) { throw new UnsupportedOperationException(); }
	@Override
	public Giocatore remove(int arg0) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeAll(Collection<?> arg0) { throw new UnsupportedOperationException(); }
	@Override
	public boolean retainAll(Collection<?> arg0) { throw new UnsupportedOperationException(); }
	@Override
	public Giocatore set(int arg0, Giocatore arg1) { throw new UnsupportedOperationException(); }
	@Override
	public int size() { throw new UnsupportedOperationException(); }
	@Override
	public List<Giocatore> subList(int arg0, int arg1) { throw new UnsupportedOperationException(); }
	@Override
	public Object[] toArray() { throw new UnsupportedOperationException(); }
	@Override
	public <T> T[] toArray(T[] arg0) { throw new UnsupportedOperationException(); }
}