package dinolib;

import java.util.ArrayList;
import java.util.Iterator;

public class PlayerManager {
	/**
	 * Istanzia il riferimento alla lista dei Giocatori.
	 * @uml.property name="listaGiocatori"
	 */
	private ArrayList<Giocatore> listaGiocatori = null;
	
	/**
	 * Costruttore, inizializza l'ArrayList.
	 */
	public PlayerManager() {
		listaGiocatori = new ArrayList<Giocatore>();
	}
	/**
	 * Aggiunge un nuovo giocatore alla lista dei giocatori.
	 * @param newGiocatore
	 * @return
	 */
	protected boolean aggiungi(Giocatore newGiocatore) {
		if (newGiocatore != null) {
			listaGiocatori.add(newGiocatore);
			return true;
		}
		else return false;
	}
	/**
	 * Dà un iteratore sulla lista dei giocatori.
	 * @return
	 */
	protected Iterator<Giocatore> getIteratorOnPlayers() {
		if (!listaGiocatori.isEmpty()) return listaGiocatori.iterator();
		else return null;
	}
	/**
	 * Controlla se l'utente esiste nella lista.
	 * @param nomeGiocatore
	 * @return
	 */
	protected boolean exists(String nomeGiocatore) {
		Iterator<Giocatore> itGiocatori = getIteratorOnPlayers();
		while (itGiocatori.hasNext()) {
			if (itGiocatori.next().getNome().equals(nomeGiocatore)) return true;
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
		Iterator<Giocatore> itGiocatori = getIteratorOnPlayers();
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
		Iterator<Giocatore> itGiocatori = getIteratorOnPlayers();
		while (itGiocatori.hasNext()) {
			itGiocatori.next().aggiornaGiocatoreSuTurno();
		}
	}
}