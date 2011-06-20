package dinolib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
/**
 * @author fabio
 */
/**
 * Classe che gestisce l'elenco di giocatori.
 */
class PlayerManager implements List<Giocatore> {
	/**
	 * Istanzia il riferimento alla lista dei Giocatori.
	 * @uml.property name="listaGiocatori"
	 */
	private ArrayList<Giocatore> listaGiocatori = null;

	/**
	 * Inizializza una collezione vuota di giocatori.
	 */
	public PlayerManager() { listaGiocatori = new ArrayList<Giocatore>(); } // Testato
	
	/**
	 * Controlla se l'utente esiste nella lista.
	 * @param nomeGiocatore Il nome dell'utente che non so se esiste nella lista.
	 * @return True se il giocatore e' nella lista, false altrimenti.
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
	 * @param nomeGiocatore Il nome del giocatore richiesto.
	 * @return Il giocatore richiesto se e' nella lista, null altrimenti.
	 */
	protected Giocatore getPlayer(String nomeGiocatore) { // Testato
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
	 * Aggiorna tutti i giocatori nella lista sul cambio turno.
	 */
	protected void aggiorna() {
		Iterator<Giocatore> itGiocatori = this.iterator();
		while (itGiocatori.hasNext()) {
			itGiocatori.next().aggiorna();
		}
	}
	/* Metodi di interfaccia - implementati */
	/**
	 * Aggiunge un giocatore alla collezione.
	 * @param newGiocatore Il giocatore da aggiungere.
	 * @return True se la collezione e' stata modificata, false altrimenti.
	 */
	@Override // Testato
	public boolean add(Giocatore newGiocatore) { return listaGiocatori.add(newGiocatore); } // Testato
	/**
	 * Dice se la collezione contiene un giocatore.
	 * @param arg0 Il giocatore che non so se e' contenuto nella lista.
	 * @return True se il giocatore e' contenuto nella lista, false altrimenti.
	 */
	@Override
	public boolean contains(Object arg0) { return listaGiocatori.contains(arg0); } // Testato
	/**
	 * Restituisce un iteratore sui giocatori.
	 * @return Un iteratore sui giocatori.
	 */
	@Override
	public Iterator<Giocatore> iterator() { return listaGiocatori.iterator(); } // Testato
	
	/* Metodi di interfaccia - non implementati */
	/**
	 * Metodo non implementato
	 */
	@Override
	public void add(int arg0, Giocatore arg1) { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public boolean addAll(Collection<? extends Giocatore> arg0) { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public boolean addAll(int arg0, Collection<? extends Giocatore> arg1) { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public void clear() { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public boolean containsAll(Collection<?> arg0) { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public Giocatore get(int arg0) { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public int indexOf(Object arg0) { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public boolean isEmpty() { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public int lastIndexOf(Object arg0) { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public ListIterator<Giocatore> listIterator() { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public ListIterator<Giocatore> listIterator(int arg0) { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public boolean remove(Object arg0) { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public Giocatore remove(int arg0) { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public boolean removeAll(Collection<?> arg0) { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public boolean retainAll(Collection<?> arg0) { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public Giocatore set(int arg0, Giocatore arg1) { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public int size() { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public List<Giocatore> subList(int arg0, int arg1) { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */
	@Override
	public Object[] toArray() { throw new UnsupportedOperationException(); }
	/**
	 * Metodo non implementato
	 */@Override
	public <T> T[] toArray(T[] arg0) { throw new UnsupportedOperationException(); }
}