package dinolib.CommunicationObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import dinolib.GameObjects.Giocatore;

public class Classifica implements Serializable {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = 2813658587475977859L;
	/**
	 * Contiene le tuple dei punteggi della classifica.
	 * @uml.property name="myPunteggi"
	 */
	private ArrayList<TuplaPunteggio> myPunteggi = null;
	/**
	 * Crea un'istanza e riempie la classifica con le tuple.
	 * @param myLogica La Logica da cui prendere le informazioni.
	 */
	public Classifica(Iterator<Giocatore> itGiocatori) {
		myPunteggi = new ArrayList<TuplaPunteggio>();

		while (itGiocatori.hasNext()) {
			aggiungiUnaTupla(itGiocatori.next());
		}
	}
	/**
	 * Aggiunge alla lista una tupla.
	 * @param giocatore Il giocatore da cui estrarre i punteggi.
	 */
	private void aggiungiUnaTupla(Giocatore giocatore) {
		String curPlayingRazza = null;
		if (giocatore.hasRazza()) {
			curPlayingRazza = giocatore.getRazza().getNome();
		}
		Iterator<String> itNomeRazze = giocatore.getPunteggio().iterator();
		String curRazza = null;
		while (itNomeRazze.hasNext()) {
			curRazza = itNomeRazze.next();
			if (curPlayingRazza != null) {
				if ((curRazza.equals(curPlayingRazza))) {
					myPunteggi.add(new TuplaPunteggio(giocatore.getNome(), curRazza, giocatore.getPunteggio().getPunteggioDaNome(curRazza), 's'));
				}
				else {
					myPunteggi.add(new TuplaPunteggio(giocatore.getNome(), curRazza, giocatore.getPunteggio().getPunteggioDaNome(curRazza), 'n'));
				}
			}
		}
	}
	/**
	 * Restituisce un iteratore sulle tuple dei punteggi.
	 * @return L'iteratore sulle tuple dei punteggi.
	 */
	public Iterator<TuplaPunteggio> iterator() { return myPunteggi.iterator(); }
}
